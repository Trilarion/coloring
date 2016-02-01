import os
import numpy as np
from PIL import Image

# get input files
input_path = os.path.join('..', 'pngs', 'cleaned')
input_files = [f for f in os.listdir(input_path) if os.path.isfile(os.path.join(input_path, f)) and f.endswith('.png')]

# loop over input files
for file in input_files:

    # load png
    im = Image.open(os.path.join(input_path, file))
    print('Image {} with size {}, format {}, mode {}'.format(file, im.size, im.format, im.mode))

    # count segments
    a = np.array(im)
