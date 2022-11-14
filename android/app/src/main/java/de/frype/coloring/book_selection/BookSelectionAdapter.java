package de.frype.coloring.book_selection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import de.frype.coloring.library.Library;
import de.frype.coloring.R;
import de.frype.util.Utils;

/**
 * Book selection adapter providing the book images in the coloring book selection grid view.
 */
public class BookSelectionAdapter extends BaseAdapter {

    private final Context context;
    private final int size; // it's the same for all elements
    private final Library library = Library.getInstance(); // just for convenience

    /**
     *
     * @param context
     */
    public BookSelectionAdapter(Context context) {
        this.context = context;
        this.size = context.getResources().getDimensionPixelSize(R.dimen.book_preview_width);
    }

    /**
     *
     * @return
     */
    @Override
    public int getCount() {
        return library.getNumberBooks();
    }

    /**
     *
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     *
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // returns a single book preview

        // update book in library
        library.setCurrentBook(position);

        View view;
        if (convertView == null) {
            view = View.inflate(context, R.layout.element_book_selection, null);
            view.setLayoutParams(new GridView.LayoutParams(size, size));
        } else {
            view = convertView;
        }

        // customize book view
        TextView categoryNameView = view.findViewById(R.id.bookNameTextView);
        categoryNameView.setText(library.getStringFromCurrentBook("name"));

        ImageView previewImageView = view.findViewById(R.id.bookPreviewImageView);
        Bitmap bitmap = library.loadCurrentBookCoverBitmapDownscaled(size, size);
        bitmap = Utils.replaceColorInBitmap(bitmap, Color.WHITE, Color.TRANSPARENT);
        previewImageView.setImageBitmap(bitmap);

        return view;
    }
}
