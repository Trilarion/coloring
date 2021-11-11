"""
Some service functions
"""

from utils.utils import *


def inspect_raw_folders():
    """

    """

    # openclipart
    source_path = path.raw_openclipart
    print('openclipart')

    # read info text file
    info_file = os.path.join(source_path, '_info.txt')
    info = read_text(info_file)

    # select useful information from text file and split
    info = [[y.strip() for y in x.split(',')] for x in info.splitlines() if x and not x.startswith('#')]

    # check integrity of info.txt

    # exactly three entries always
    for entry in info:
        if len(entry) != 3:
            print('entry with <> 3 parts {}'.format(' '.join(entry)))

    for file, name, url in info:
        # name should not be empty
        if not name:
            print('empty name {} {}'.format(file, url))

    # check that files are existing for each entry of info
    for file, _, _ in info:
        file_path = os.path.join(source_path, file)
        if not os.path.isfile(file_path):
            print('warning: file {} not existing'.format(file))
        file_path = os.path.join(source_path, file[:-4] + '.adapted.svg')
        if not os.path.isfile(file_path):
            print('warning: file {} not existing'.format(file))

    # check for files that are not part of info.txt

if __name__ == '__main__':

    # look for non-adapted files
    inspect_raw_folders()