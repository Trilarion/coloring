"""
Useful predefined paths
"""

import os

# the root dir is the location of this file and three times up
root = os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir, os.pardir, os.pardir))

# artwork paths
artwork = os.path.join(root, 'artwork')
raw = os.path.join(artwork, 'raw')
raw_openclipart = os.path.join(raw, 'org.openclipart')
logo = os.path.join(artwork, 'logo')
icons = os.path.join(artwork, 'icons')
temp = os.path.join(artwork, 'temp')

# app paths
app = os.path.join(root, 'android', 'app', 'src', 'main')
assets = os.path.join(app, 'assets')
library = os.path.join(assets, 'library')
res = os.path.join(app, 'res')
drawable = os.path.join(res, 'drawable')
drawable_hdpi = os.path.join(res, 'drawable-hdpi')
drawable_xxhdpi = os.path.join(res, 'drawable-xxhdpi')