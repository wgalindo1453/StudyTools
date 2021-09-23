from git import Repo
import os
import glob
import xml.etree.ElementTree as ET
import requests
import shutil
import sys

# git url
git_url = sys.argv[1]
# local repo dir
repo_dir = os.getcwd() + "/curRepo"
#delete old repo if exist
shutil.rmtree(repo_dir, ignore_errors=True)
# request
r = requests.get(git_url)


#Step 1: clone the repo
#Check if we are able to access the repo
if str(r) != "<Response [200]>":
    exit()
try:
    print("cloning")
    #Attempt to clone. This will fail if the repo is private
    Repo.clone_from(git_url, repo_dir, env={"depth": "1"})
    print("cloned")
except:
    shutil.rmtree(repo_dir, ignore_errors=True)
    exit()
print("cloned directory.")
os.chmod(repo_dir, 0o777) #Change file access

#Step 2: Find all Androidmanifest.xml in the repo
manifests = []
for root, dirs, files in os.walk(repo_dir):
    for file in files:
        if file.endswith("AndroidManifest.xml"):
            manifests.append(os.path.join(root, file))
print("Discovered all manifests")

#Step 3: parse the XML and look for package
ids = []
for manifest in manifests:
    f = open(manifest, "r")
    manData = f.read()
    try:

        root = ET.parse(manifest).getroot()
        ids.append(root.attrib['package'])
    except: #Incorrectly formatted XML or doesn't contain a package? Skip
        pass
    f.close()
print("found all ids")
#Step 4: send http request to play.google.com to look for app
for id in ids:
    r = requests.get('https://play.google.com/store/apps/details?id=' + id)
    print(id)
    if str(r) == "<Response [200]>":
        print("good!")

#Step 5: delete current repo
shutil.rmtree(repo_dir, ignore_errors=True)
