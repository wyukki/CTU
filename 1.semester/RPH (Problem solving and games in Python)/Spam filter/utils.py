import os


def read_classification_from_file(directory):
    dictionary = {}
    with open(directory, 'rt', encoding='utf-8') as file:
        for lines in file:
            line = lines.split()
            dictionary[line[0]] = line[1]
        return dictionary


if __name__ == '__main__':
    cwd = os.getcwd()
    truth_dir = cwd + '/corpus/training/!truth.txt'
    print(read_classification_from_file(truth_dir))
