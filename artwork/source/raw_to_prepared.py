"""
Lots of manual tweaking of raw content to make it suitable.
"""

from utils.utils import *
from PyQt5.QtSvg import QSvgRenderer
from PyQt5.QtGui import QImage, QPainter


def svg_to_png(svg_path, png_path):
    """
    inspired by https://stackoverflow.com/questions/8551690/how-to-render-a-scaled-svg-to-a-qimage
    """
    renderer = QSvgRenderer(svg_path)
    print(renderer)
    print(renderer.viewBoxF())
    print(renderer.defaultSize())
    image = QImage(width, height, QImage.Format_ARGB32)
    painter = QPainter(image)
    renderer.render(painter)
    image.save(png_path)
    painter.end()


def rasterize_adapted_svg():
    """
    Uses PyQt5 to rasterize SVG
    """
    source_path = path.raw_openclipart
    info_file = os.path.join(source_path, '_info.txt')
    info = read_text(info_file)
    # select useful information and split
    info = [[y.strip() for y in x.split(',')] for x in info.splitlines() if x and not x.startswith('#')]
    for file, name, url in info:
        svg_path = os.path.join(source_path, file[:-4] + '.adapted.svg')
        if not os.path.isfile(svg_path):
            # TODO a warning maybe
            continue
        png_path = svg_path[:-4] + '.png'
        svg_to_png(svg_path, png_path)


if __name__ == '__main__':

    # rasterize adapted svg automatically
    rasterize_adapted_svg()



