import xml.etree.ElementTree as ET
import sys

# inputs
app_xml_file = sys.argv[1]

# process file
root = ET.parse(app_xml_file).getroot()

count = 0
for type_tag in root.findall('application'):
    count = count + 1

print(count)
