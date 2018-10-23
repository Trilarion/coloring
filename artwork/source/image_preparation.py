"""
Collection of scripts for cleaning up images for coloring.
"""

import os
import numpy as np
from scipy import ndimage
from PIL import Image


def clean_images():
    """
    Converts existing grayscale images to true black and white greatly reducing the size.
    """

    # get png files in root path
    input_files = [f for f in os.listdir(root_path) if os.path.isfile(os.path.join(root_path, f)) and f.endswith('.png')]
    output_path = os.path.join(root_path, 'cleaned')

    # loop over input files
    for file in input_files:

        # load png
        im = Image.open(os.path.join(root_path, file))
        print('Image {} with size {}, format {}, mode {}'.format(file, im.size, im.format, im.mode))

        # clean (colors)
        a = np.array(im)
        m = a > 50
        a[m] = 0
        a[~m] = 1

        # TODO smooth edges (morphological closing, dilate followed by erode)
        #a = ndimage.binary_dilation(a, np.ones((5, 5), dtype=np.uint8)).astype(a.dtype)
        #a = ndimage.binary_erosion(a, np.ones((7, 7), dtype=np.uint8)).astype(a.dtype)
        a = (1 - a) * 255

        # save again
        im_out = Image.fromarray(a, mode=im.mode)
        im_out.save(os.path.join(output_path, file), compress_level=9)


def segment_check():
    """
    Prints how many segments there are per image using a scipy function.
    """

    # get input files
    input_path = os.path.join(root_path, 'cleaned')
    output_path = os.path.join(root_path, 'segment_check')
    input_files = [f for f in os.listdir(input_path) if os.path.isfile(os.path.join(input_path, f)) and f.endswith('.png')]

    # loop over input files
    for file in input_files:
        # load png
        im = Image.open(os.path.join(input_path, file))
        print('Image {} with size {}, format {}, mode {}'.format(file, im.size, im.format, im.mode))

        # count segments
        a = np.array(im)
        m = a > 50
        segmented_image, number_segments = ndimage.measurements.label(m)
        print('segments {}'.format(number_segments))

        # randomly colorize the input for each segment
        b = np.zeros(a.shape + (3,), dtype=np.uint8)
        b[:, :, 0] = a
        b[:, :, 1] = a
        b[:, :, 2] = a
        for i in range(number_segments):
            color = np.random.randint(50, 200, (3,), np.uint8)
            m = segmented_image == i + 1
            b[m, :] = color


        # save again if needed
        im_out = Image.fromarray(b, mode='RGB')
        im_out.save(os.path.join(output_path, file), compress_level=9)


def set_background_transparent():
    """
    Not used anymore, we do the transparency directly in the app, if needed.
    """
    # get input files
    input_path = os.path.join(root_path, 'cleaned')
    input_files = [f for f in os.listdir(input_path) if
                   os.path.isfile(os.path.join(input_path, f)) and f.endswith('.png')]
    output_path = os.path.join(root_path, 'transparent')

    # loop over input files
    for file in input_files:
        # load png
        im = Image.open(os.path.join(input_path, file))
        print('Image {} with size {}, format {}, mode {}'.format(file, im.size, im.format, im.mode))

        # convert structure to transparency
        a = np.array(im)
        b = np.zeros(a.shape + (2,), dtype=np.uint8)
        b[:, :, 0] = a
        b[:, :, 1] = (a != 255) * 255

        # save again
        im_out = Image.fromarray(b, mode='LA')
        im_out.save(os.path.join(output_path, file), compress_level=9)


if __name__ == '__main__':

    # set root path
    root_path = os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir, 'pngs'))

    # clean images
    clean_images()

    # segment info
    segment_check()

    # transparency
    # set_background_transparent()





