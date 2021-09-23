import os
import subprocess
import sys
import configparser

git_bin_full_path = ""
rm_bin_full_path = ""
FNULL = open(os.devnull, "w")

def read_config(config_file):
    global git_bin_full_path
    global rm_bin_full_path
    config = configparser.ConfigParser()
    config.read_file(open(config_file))
    git_bin_full_path = config.get("General Configs", "git_bin_full_path")
    rm_bin_full_path = config.get("General Configs", "rm_bin_full_path")

def main():
    read_config(sys.argv[1])
    repo_folder = sys.argv[2]
    os.chdir(repo_folder)
    out = subprocess.check_output([git_bin_full_path, '--no-pager', 'log', '--pretty=format:"%ad   %H"','--date=iso'], stderr=FNULL)
    analysis_out = out.decode('utf-8')
    analysis_lines = analysis_out.split('\n')
    for line in analysis_lines:
        line = line.strip('"')
        if line == "":
            continue
        print(line, flush=True)

if __name__ == "__main__":
    main()
