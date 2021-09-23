import os
import subprocess
import sys
import configparser

cloc_bin_full_path = ""
rm_bin_full_path = ""
FNULL = open(os.devnull, "w")

def read_config(config_file):
    global cloc_bin_full_path
    global rm_bin_full_path
    config = configparser.ConfigParser()
    config.read_file(open(config_file))
    cloc_bin_full_path = config.get("General Configs", "cloc_bin_full_path")
    rm_bin_full_path = config.get("General Configs", "rm_bin_full_path")

def main():
    read_config(sys.argv[1])
    file_to_analyze = sys.argv[2]
    out = subprocess.check_output([cloc_bin_full_path, file_to_analyze], stderr=FNULL)
    analysis_out = out.decode('utf-8')
    analysis_lines = analysis_out.split('\n')
    for line in analysis_lines:
        if line == "":
            continue
        print(line, flush=True)

if __name__ == "__main__":
    main()
