"""
Updates the android app content with artwork from the artwork folder.
"""

from utils.utils import *


def copy_res_drawable():
    """
    book and page background
    """
    source_path = os.path.join(path.raw, 'book and page background')
    # book background
    copy(os.path.join(source_path, 'book_background.png'), path.drawable)
    # page background
    copy(os.path.join(source_path, 'page_background.png'), path.drawable)


if __name__ == '__main__':

    # do all the copying
    copy_res_drawable()