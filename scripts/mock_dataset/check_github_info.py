import requests
import re
import time
import datetime
import math
import sys
import getopt
import subprocess
import os
import signal
import argparse
import threading
import configparser
import json

github_username = ""
github_password = ""
repo_clone_timeout = 0
java_bin_full_path = ""
rm_bin_full_path = ""
git_bin_full_path = ""
analysis_jar_full_path = ""
repo_dest_full_path = ""
curr_proc = None
killed_curr_proc = False
FNULL = open(os.devnull, "w")

def read_config(config_file):
    global github_username
    global github_password
    global repo_clone_timeout
    global java_bin_full_path
    global analysis_jar_full_path
    global rm_bin_full_path
    global git_bin_full_path
    global repo_dest_full_path
    config = configparser.ConfigParser()
    config.read_file(open(config_file))
    github_username = config.get("General Configs", "github_username")
    github_password = config.get("General Configs", "github_password")
    repo_clone_timeout = int(config.get("General Configs", "repo_clone_timeout"))
    java_bin_full_path = config.get("General Configs", "java_bin_full_path")
    rm_bin_full_path = config.get("General Configs", "rm_bin_full_path")
    git_bin_full_path = config.get("General Configs", "git_bin_full_path")
    analysis_jar_full_path = config.get("General Configs", "analysis_jar_full_path")
    repo_dest_full_path = config.get("General Configs", "repo_dest_full_path")

def execute_github_query(query):
    request_wait_time = 3
    while True:
        time.sleep(request_wait_time)
        query_response = requests.get(query, auth=requests.auth.HTTPBasicAuth(github_username, github_password))
        try:
            query_results = query_response.json()
            if "documentation_url" in query_results and query_results["documentation_url"] == "https://developer.github.com/v3/#abuse-rate-limits":
                continue
        except ValueError:
            query_results = {}
            query_results["query_exception"] = "query_exception"
            return query_results
        return query_results

def printStarsForRepo(repo):
    repo_query = "https://api.github.com/repos/"+repo
    repo_query_results = execute_github_query(repo_query)
    print(repo_query_results["stargazers_count"])

def kill_curr_proc():
    global curr_proc
    global killed_curr_proc
    print("Killing process", flush=True)
    os.killpg(os.getpgid(curr_proc.pid), signal.SIGTERM)
    killed_curr_proc = True

def analyze(repo_full_path, repo_root_name, package):
    analysis_out = open("analysis_out.txt", "w")
    analysis_cmd = [java_bin_full_path, "-jar", analysis_jar_full_path, "pfps", repo_full_path, repo_root_name, package]
    analysis_proc = subprocess.Popen(analysis_cmd, stdout=analysis_out, stderr=FNULL)
    analysis_proc.wait()
    analysis_out.close()
    analysis_out = open("analysis_out.txt")
    analysis_lines = analysis_out.read().splitlines()
    analysis_out.close()
    rm_cmd = [rm_bin_full_path, "-f", "-r", "analysis_out.txt"]
    rm_proc = subprocess.Popen(rm_cmd, stdout=FNULL, stderr=FNULL)
    rm_proc.wait()
    #printing result
    for line in analysis_lines:
        print(line, flush=True)

def delete_folder(repo_local_name):
    #clone project
    rm_cmd = [rm_bin_full_path, "-f", "-r", repo_local_name]
    rm_proc = subprocess.Popen(rm_cmd, stdout=FNULL, stderr=FNULL)
    rm_proc.wait()

def clone_and_analyze_github_repo(repo_name, package_name, repo_id):
    global curr_proc
    global killed_curr_proc
    #change directory to repository directory
    os.chdir(repo_dest_full_path)
    #get starting dir
    root_dir = os.getcwd()
    #clone project
    git_clone_cmd = [git_bin_full_path, "clone", repo_name, "repository-"+repo_id]
    curr_proc = subprocess.Popen(git_clone_cmd, stdout=FNULL, stderr=FNULL, preexec_fn=os.setsid)
    timer = threading.Timer(repo_clone_timeout, kill_curr_proc)
    timer.start()
    curr_proc.wait()
    timer.cancel()
    if killed_curr_proc == True:
        curr_proc = None
        killed_curr_proc = False
        delete_folder(root_dir+"/repository-"+repo_id)
        return False
    #run test and mock counter
    analyze(root_dir+"/repository-"+repo_id, "repository-"+repo_id, package_name)

    if killed_curr_proc == True:
        curr_proc = None
        killed_curr_proc = False
        delete_folder(root_dir+"/repository-"+repo_id)
        return False

    #delete_folder(root_dir+"/repository-"+repo_id)
    return True

def clone_repos(repos_json_file_name):
    global curr_proc
    global killed_curr_proc
    #change directory to repository directory
    print(repo_clone_timeout)
    print(repo_dest_full_path)
    os.chdir(repo_dest_full_path)
    #get starting dir
    root_dir = os.getcwd()

    repos = None
    with open(repos_json_file_name) as repos_file:
      repos = json.load(repos_file)
    repos_with_jvm_tests_count = 0
    repos_with_device_tests_count = 0
    repos_with_tests_count = 0
    repos_to_process_name = set()
    for repo_name in repos.keys():
        repo = repos[repo_name]
        if repo["num_tests"] > 0:
            repos_with_jvm_tests_count = repos_with_jvm_tests_count + 1
        if repo["num_androidTests"] > 0:
            repos_with_device_tests_count = repos_with_device_tests_count + 1
        if repo["num_tests"] > 0 or repo["num_androidTests"] > 0:
            repos_with_tests_count = repos_with_tests_count + 1
            repos_to_process_name.add(repo_name)
    print(str(repos_with_jvm_tests_count))
    print(str(repos_with_device_tests_count))
    print(str(repos_with_tests_count))

    mappings = dict()
    repos_count  = 10000
    for repo_name in repos_to_process_name:
        print("CLONING:"+repo_name)
        repos_count = repos_count + 1
        github_repo_name = "https://github.com/"+repo_name+".git"
        git_clone_cmd = [git_bin_full_path, "clone", github_repo_name, "repository-"+str(repos_count)]
        curr_proc = subprocess.Popen(git_clone_cmd, stdout=FNULL, stderr=FNULL, preexec_fn=os.setsid)
        timer = threading.Timer(repo_clone_timeout, kill_curr_proc)
        timer.start()
        curr_proc.wait()
        timer.cancel()
        if killed_curr_proc == True:
            curr_proc = None
            killed_curr_proc = False
            delete_folder(root_dir+"/repository-"+repo_id)
            print("NOT CLONED:"+repo_name+" in "+"repository-"+str(repos_count))
        else:
          print("CLONED:"+repo_name+" in "+"repository-"+str(repos_count))
          mappings[repo_name]="repository-"+str(repos_count)

    with open('mappings.json', 'w') as mappings_file:
        json.dump(mappings, mappings_file)


### main function ###
def main():
    flag = sys.argv[1]
    if flag == "stars":
        read_config(sys.argv[2])
        #find github stars based on project name
        ##parameters
        ##repo name -> "ankidroid/Anki-Android"
        #printStarsForRepo(sys.argv[2])
        printStarsForRepo(sys.argv[3])
    elif flag == "analyze":
        read_config(sys.argv[2])
        #compute statistics for project
        ##parameters
        ##repo -> "https://github.com/ankidroid/Anki-Android"
        ##app package name -> "com/ichi2/anki"
        clone_and_analyze_github_repo(sys.argv[3],sys.argv[4], sys.argv[5])
    elif flag == "download":
        read_config(sys.argv[2])
        #clone repo
        ##parameters
        ##repos json file -> repos.json
        clone_repos(sys.argv[3])

if __name__ == "__main__":
    main()

#get default branch name
#git rev-parse --abbrev-ref HEAD

#get current commit id for default branch
#git rev-parse HEAD