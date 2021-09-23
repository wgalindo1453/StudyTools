import sys
import os
import xlrd
import csv
import random
import shutil

#command
#python3 Code/extract_random_reviews_based_on_already_sampled.py IRR/random_indexes.txt HumanAspectsReviewsSamples HumanAspectsReviews IRR/labeled_reviews Codebook/random_sample_for_codebook\ -\ random_sample.csv negotiated_samples_90_10/reviews_file_names.txt negotiated_samples_90_10/sample_sizes.txt negotiated_samples_90_10/samples

mocks_folder = sys.argv[1]
sampled_mocks_folder = sys.argv[2]

repos = ["repository-1152","repository-222","repository-339","repository-971","repository-1140","repository-80","repository-612","repository-1086","repository-53","repository-944","repository-71","repository-665","repository-680","repository-1113","repository-500","repository-238","repository-667","repository-1037","repository-721","repository-1124","repository-589","repository-377","repository-1067","repository-360","repository-12","repository-79"]
sizes = ["302","149","160","143","141","117","103","76","64","63","51","38","33","31","30","26","16","25","23","23","19","20","18","16","16","15"]

def create_samples():
	#remove samples folder
	if os.path.isdir(sampled_mocks_folder):
		shutil.rmtree(sampled_mocks_folder)
	#create dir
	os.mkdir(sampled_mocks_folder)
	for index in range(len(repos)):
		for root, directories, files in os.walk(mocks_folder, topdown=False):
			for name in files:
				if repos[index] in name and "mockito_mocks" in name:
					print(name)
					mocks_file_for_repo = os.path.join(root, name)
					mocks_for_repo = []
					mocks_for_repo_count = 0
					mocks_header_row = None
					with open(mocks_file_for_repo) as csv_file:
						csv_reader = csv.reader(csv_file, delimiter=',')
						mocks_for_repo_count = 0
						for row in csv_reader:
							mocks_for_repo_count = mocks_for_repo_count + 1
							if mocks_for_repo_count == 1:
								mocks_header_row = row
								continue
							mocks_for_repo.append(row)
					#remove count of header
					mocks_for_repo_count = mocks_for_repo_count - 1
					if len(mocks_for_repo) != mocks_for_repo_count:
						print("mocks count does not match")
						exit()

					#get the sample of mocks
					mocks_to_sample_count = int(sizes[index])
					sampled_mocks = []
					sampled_mocks_indexes = []
					while mocks_to_sample_count > 0:
						random_index = random.randint(1, mocks_for_repo_count)
						if random_index in sampled_mocks_indexes:
							continue

						sampled_mocks.append(mocks_for_repo[random_index-1])
						sampled_mocks_indexes.append(random_index)
						mocks_to_sample_count = mocks_to_sample_count -1

					#create sampled file

					sample_name = name.replace(".csv", "_sample.csv")
					with open(os.path.join(sampled_mocks_folder,sample_name), mode='w') as random_sample_file:
						random_sample_file_csv = csv.writer(random_sample_file, delimiter=',', quotechar='"', quoting=csv.QUOTE_ALL)
						random_sample_file_csv.writerow(mocks_header_row)
						for mock_row in sampled_mocks:
							random_sample_file_csv.writerow(mock_row)

create_samples()



