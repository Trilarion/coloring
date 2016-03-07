'''
    Not used anymore, we do the transparency directly in the app, if needed.
'''

import os
import numpy as np
from PIL import Image

# get input files
input_path = os.path.join('..', 'pngs', 'cleaned')
input_files = [f for f in os.listdir(input_path) if os.path.isfile(os.path.join(input_path, f)) and f.endswith('.png')]
output_path = os.path.join('..', 'pngs', 'transparent')

# loop over input files
for file in input_files:

    # load png
    im = Image.open(os.path.join(input_path, file))
    print('Image {} with size {}, format {}, mode {}'.format(file, im.size, im.format, im.mode))

    # convert structure to transparency
    a = np.array(im)
    b = np.zeros(a.shape + (2,), dtype=np.uint8)
    b[:,:,0] = a
    b[:,:,1] = (a != 255) * 255

    # save again
    im_out = Image.fromarray(b, mode='LA')
    im_out.save(os.path.join(output_path, file), compress_level=9)



