"""
Useful routines. Will usually print some output.
"""

import os
import shutil
import json
import time
import utils.path as path


def copy(src, dst):
    """
    Calls shutil.copy2 with printing a message
    """
    print('  copy {} to {}'.format(os.path.relpath(src, path.root), os.path.relpath(dst, path.root)))
    shutil.copy2(src, dst)


def read_text(file):
    """
    Reads a whole text file (UTF-8 encoded).
    """
    with open(file, mode='r', encoding='utf-8') as f:
        text = f.read()
    return text


def write_text(file, text):
    """
    Writes a whole text file (UTF-8 encoded).
    """
    with open(file, mode='w', encoding='utf-8') as f:
        f.write(text)


def clean_path(path):
    """
    Removes all files in path.
    """
    print(' clean folder {}'.format(path))
    for f in os.listdir(path):
        os.remove(os.path.join(path, f))


def recreate_folder(folder):
    """

    """
    shutil.rmtree(folder, ignore_errors=True)
    for attempts in range(10):
        try:
            os.mkdir(folder)
        except PermissionError:
            time.sleep(0.1)
            continue
        else:
            break
    else:
        raise RuntimeError()
