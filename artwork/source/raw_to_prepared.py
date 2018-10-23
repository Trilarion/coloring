"""
Lots of manual tweaking of raw content to make it suitable.
"""

from utils.utils import *
import PyQt5.QtSvg as QtSvg
import PyQt5.QtGui as QtGui
import PyQt5.QtCore as QtCore
from PIL import Image
import numpy as np
from scipy import ndimage


def svg_to_png(svg_path, png_path):
    """
    inspired by https://stackoverflow.com/questions/8551690/how-to-render-a-scaled-svg-to-a-qimage
    """
    renderer = QtSvg.QSvgRenderer(svg_path)
    default_size = renderer.defaultSize()
    image = QtGui.QImage(default_size, QtGui.QImage.Format_ARGB32)
    image.fill(QtCore.Qt.white)
    painter = QtGui.QPainter(image)
    renderer.render(painter)
    image.save(png_path)
    painter.end()


def rasterize_adapted_svg():
    """
    Uses PyQt5 to rasterize SVG
    """

    # openclipart
    source_path = path.raw_openclipart

    # read info text file
    info_file = os.path.join(source_path, '_info.txt')
    info = read_text(info_file)

    # select useful information from text file and split
    info = [[y.strip() for y in x.split(',')] for x in info.splitlines() if x and not x.startswith('#')]

    # for each entry
    for file, name, url in info:
        svg_path = os.path.join(source_path, file[:-4] + '.adapted.svg')
        if not os.path.isfile(svg_path):
            # TODO a warning maybe
            continue
        png_path = os.path.join(path.temp, file[:-4] + '.adapted.png')
        svg_to_png(svg_path, png_path)


def clean_images():
    """
    Converts existing grayscale images to true black and white greatly reducing the size.
    """

    # get png files in root path
    input_files = [f for f in os.listdir(path.temp) if os.path.isfile(os.path.join(path.temp, f)) and f.endswith('.adapted.png')]

    # loop over input files
    for file in input_files:

        # load png
        im = Image.open(os.path.join(path.temp, file))
        print('Image {} with size {}, format {}, mode {}'.format(file, im.size, im.format, im.mode))
        im = im.convert('RGB')

        # clean (colors)
        a = np.array(im)
        m = np.sum(a, axis=2) > 128*3
        a[m, :] = 255
        a[~m, :] = 0

        # save again
        im_out = Image.fromarray(a, mode=im.mode)
        im_out.save(os.path.join(path.temp, file[:-4] + '.cleaned.png'), compress_level=9)

        # count segments
        segmented_image, number_segments = ndimage.measurements.label(m)
        print('segments {}'.format(number_segments))

        # randomly colorize the input for each segment
        for i in range(number_segments):
            color = np.random.randint(50, 200, (3,), np.uint8)
            m = segmented_image == i + 1
            a[m, :] = color

        # save again if needed
        im_out = Image.fromarray(a, mode='RGB')
        im_out.save(os.path.join(path.temp, file[:-4] + '.example.png'), compress_level=9)


def clean_temp():
    """

    """
    # if not existing, create
    if not os.path.isdir(path.temp):
        os.mkdir(path.temp)
    else:
        clean_path(path.temp)


if __name__ == '__main__':

    # clean temp directory
    clean_temp()

    # rasterize adapted svg to png
    rasterize_adapted_svg()

    # clean images
    clean_images()



