"""
Updates the android app content with artwork from the artwork folder.
"""

from utils.utils import *
import zipfile


def do_icons():
    """
    copies the icons
    """

    icons = ('close', 'delete', 'navigate_before', 'photo_library', 'settings', 'share')

    # hdpi folder

    # remove all files
    clean_path(path.drawable_hdpi)

    # copy logo
    copy(os.path.join(path.logo, 'PNG', 'ic_launcher_hdpi.png'), os.path.join(path.drawable_hdpi, 'ic_launcher.png'))

    # extract icons from zips
    for icon in icons:
        zip = zipfile.ZipFile(os.path.join(path.icons, 'round-{}-android-white.zip'.format(icon))) # mode is 'r' by default
        content = zip.read('res/drawable-hdpi/round_{}_white_48.png'.format(icon))
        with open(os.path.join(path.drawable_hdpi, 'ic_{}_white_48dp.png'.format(icon)), 'wb') as f:
            f.write(content)
        zip.close()

    # xxhdpi folder

    # remove all files
    clean_path(path.drawable_xxhdpi)

    # copy logo
    copy(os.path.join(path.logo, 'PNG', 'ic_launcher_xxhdpi.png'), os.path.join(path.drawable_xxhdpi, 'ic_launcher.png'))

    # extract icons from zips
    for icon in icons:
        zip = zipfile.ZipFile(os.path.join(path.icons, 'round-{}-android-white.zip'.format(icon))) # mode is 'r' by default
        content = zip.read('res/drawable-xxhdpi/round_{}_white_48.png'.format(icon))
        with open(os.path.join(path.drawable_xxhdpi, 'ic_{}_white_48dp.png'.format(icon)), 'wb') as f:
            f.write(content)
        zip.close()


def get_temp_file(raw_path, name):
    """

    """
    return os.path.join(path.temp, os.path.relpath(raw_path, path.raw), name + '.adapted.cleaned.png')


def process_entry(output, entry):
    """

    """
    copy(entry[1], os.path.join(output, entry[0]))
    return entry[0]


def process_book(book):
    """

    """
    output = os.path.join(path.library, book['folder'])
    recreate_folder(output)

    # cover photo
    book['cover'] = process_entry(output, book['cover'])

    # all pages
    pages = book['pages']
    for page in pages:
        page['file'] = process_entry(output, page['file'])

    book['pages'] = pages


def create_library():
    """

    """

    # recreate library path in assets
    recreate_folder(path.library)

    # start with empty library definition
    library = []

    # define animals book
    book = {
        'name': 'Cartoon animals',
        'folder': 'book_cartoon_animals',
        'cover': ('cover.png', os.path.join(path.raw_openclipart, 'lemmling-Cartoon-owl.png')),
        'pages': [
            {
                'name': 'Bunny',
                'file': ('bunny.png', get_temp_file(path.raw_openclipart, 'bunny'))
            },
            {
                'name': 'Bee',
                'file': ('bee.png', get_temp_file(path.raw_openclipart, 'bee-forestgreen'))
            },
            {
                'name': 'Sheep',
                'file': ('sheep.png', get_temp_file(path.raw_openclipart, 'Eid-Sheep1'))
            },
            {
                'name': 'Owl',
                'file': ('owl.png', get_temp_file(path.raw_openclipart, 'owl-line-art-lemmling-Cartoon-owl'))
            },
            {
                'name': 'Triceratops',
                'file': ('triceratops.png', get_temp_file(path.raw_openclipart, 'StudioFibonacci-Cartoon-triceratops'))
            }

        ]
    }

    # process the animals book (copy the files at the right place) and append the definition
    process_book(book)
    library.append(book)

    # write the library definition
    text = json.dumps(library, indent=1)
    write_text(os.path.join(path.library, 'library.json'), text)


def do_misc():
    """
    book and page background
    """
    source_path = os.path.join(path.raw, 'book and page background')
    # book background
    copy(os.path.join(source_path, 'book_background.png'), path.drawable)
    # page background
    copy(os.path.join(source_path, 'page_background.png'), path.drawable)


if __name__ == '__main__':

    # copy icons
    #do_icons()

    # create library
    create_library()

    # all the remaining stuff
    #do_misc()
