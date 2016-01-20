import os
from PIL import Image

input_file = os.path.join('..', 'pngs', 'balloons.png')

# load png
im = Image.open(input_file)
im.show()
