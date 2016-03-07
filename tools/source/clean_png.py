'''
    True black and white.
'''

import os
import numpy as np
from PIL import Image

# get input files
input_path = os.path.join('..', 'pngs')
input_files = [f for f in os.listdir(input_path) if os.path.isfile(os.path.join(input_path, f)) and f.endswith('.png')]
output_path = os.path.join('..', 'pngs', 'cleaned')

# loop over input files
for file in input_files:

    # load png
    im = Image.open(os.path.join(input_path, file))
    print('Image {} with size {}, format {}, mode {}'.format(file, im.size, im.format, im.mode))

    # clean (colors)
    a = np.array(im)
    a[a > 50] = 255
    a[a < 50] = 0

    # smooth edges (morphological closing, dilate followed by erode)

    # save again
    im_out = Image.fromarray(a, mode=im.mode)
    im_out.save(os.path.join(output_path, file), compress_level=9)



