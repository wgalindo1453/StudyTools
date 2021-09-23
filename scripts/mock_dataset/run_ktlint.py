import os
import subprocess
import sys
import configparser

ktlint_bin_full_path = ""
ktlint_rules_jar_full_path = ""
rm_bin_full_path = ""
FNULL = open(os.devnull, "w")

def read_config(config_file):
    global ktlint_bin_full_path
    global ktlint_rules_jar_full_path
    global rm_bin_full_path
    config = configparser.ConfigParser()
    config.read_file(open(config_file))
    ktlint_bin_full_path = config.get("General Configs", "ktlint_bin_full_path")
    ktlint_rules_jar_full_path = config.get("General Configs", "ktlint_rules_jar_full_path")
    rm_bin_full_path = config.get("General Configs", "rm_bin_full_path")

def main():
    read_config(sys.argv[1])
    file_to_analyze = sys.argv[2]
    out = None
    try:
        out = subprocess.check_output([ktlint_bin_full_path, '--disabled_rules=indent,no-semi,no-unused-imports,no-consecutive-blank-lines,no-blank-line-before-rbrace,'+
        'no-trailing-spaces,no-unit-return,no-empty-class-body,no-wildcard-imports,chain-wrapping,no-line-break-before-assignment,string-template,modifier-order,colon-spacing,'+
        'comma-spacing,curly-spacing,dot-spacing,double-colon-spacing,keyword-spacing,op-spacing,paren-spacing,range-spacing,final-newline,import-ordering',
        '-R', ktlint_rules_jar_full_path, file_to_analyze], stderr=FNULL)
    except subprocess.CalledProcessError as cpe:
        out = cpe.output
    analysis_out = out.decode('utf-8')
    analysis_lines = analysis_out.split('\n')
    for line in analysis_lines:
        if line == "":
            continue
        print(line, flush=True)

if __name__ == "__main__":
    main()
