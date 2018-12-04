"""
Rasterizing scalable vector graphics and cleaning resulting images and test colorings.
"""

from utils.utils import *
import PyQt5.QtSvg as QtSvg
import PyQt5.QtGui as QtGui
import PyQt5.QtCore as QtCore
from PIL import Image
import numpy as np
from scipy import ndimage

MINIMAL_RASTER_SIZE = 500


def svg_to_png(svg_path, png_path):
    """
    inspired by https://stackoverflow.com/questions/8551690/how-to-render-a-scaled-svg-to-a-qimage
    """
    renderer = QtSvg.QSvgRenderer(svg_path)
    desired_size = renderer.defaultSize()
    if desired_size.width() < MINIMAL_RASTER_SIZE:
        desired_size *= MINIMAL_RASTER_SIZE / desired_size.width()
    if desired_size.height() < MINIMAL_RASTER_SIZE:
        desired_size *= MINIMAL_RASTER_SIZE / desired_size.height()
    print(desired_size)
    image = QtGui.QImage(desired_size, QtGui.QImage.Format_ARGB32)
    image.fill(QtCore.Qt.white)
    painter = QtGui.QPainter(image)
    renderer.render(painter)
    image.save(png_path)
    painter.end()


def rasterize_adapted_svg():
    """
    Uses PyQt5 to rasterize SVG. Stores the rasterized works in the temp directory.
    """

    # openclipart
    source_path = path.raw_openclipart
    destination_path = os.path.join(path.temp, os.path.relpath(source_path, path.raw))
    os.mkdir(destination_path)

    # read info text file
    info_file = os.path.join(source_path, '_info.txt')
    info = read_text(info_file)

    # select useful information from text file and split
    info = [[y.strip() for y in x.split(',')] for x in info.splitlines() if x and not x.startswith('#')]

    # for each entry
    for file, name, url in info:
        svg_path = os.path.join(source_path, file[:-4] + '.adapted.svg')
        if not os.path.isfile(svg_path):
            continue
        png_path = os.path.join(destination_path, file[:-4] + '.adapted.png')
        print(png_path)
        svg_to_png(svg_path, png_path)


def clean_images():
    """
    Converts existing grayscale images to true black and white greatly reducing the size.
    """

    for dirpath, dirnames, filenames in os.walk(path.temp):
        for f in filenames:
            if f.endswith('.adapted.png'):
                clean_image(os.path.join(dirpath, f))


def clean_image(file):
    """
    Makes a single rasterized image black and white.
    """
    # load png
    im = Image.open(file)
    print('Image {} with size {}, format {}, mode {}'.format(file, im.size, im.format, im.mode))
    im = im.convert('RGB')

    # clean (colors)
    a = np.array(im)
    m = np.sum(a, axis=2) > 128*3
    a[m, :] = 255
    a[~m, :] = 0

    # save again
    im_out = Image.fromarray(a, mode=im.mode)
    im_out.save(file[:-4] + '.cleaned.png', compress_level=9)

    # count segments to give an example
    segmented_image, number_segments = ndimage.measurements.label(m)
    print('segments {}'.format(number_segments))

    # randomly colorize the input for each segment
    for i in range(number_segments):
        color = np.random.randint(50, 200, (3,), np.uint8)
        m = segmented_image == i + 1
        a[m, :] = color

    # save again if needed
    im_out = Image.fromarray(a, mode='RGB')
    im_out.save(file[:-4] + '.example.png', compress_level=9)


if __name__ == '__main__':

    # clean temp directory
    recreate_folder(path.temp)

    # rasterize adapted svg to png
    rasterize_adapted_svg()

    # clean images
    clean_images()



