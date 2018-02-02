package de.frype.coloring.page_selection;

import android.content.Context;
import android.graphics.*;
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
 * Page selection adapter providing the coloring pages from the current coloring book in a grid view.
 */
public class PageSelectionAdapter extends BaseAdapter {

    private final Context context;
    private final int size; // it's the same same for all elements
    private final Library library = Library.getInstance(); // just convenience

    public PageSelectionAdapter(Context context) {
        this.context = context;
        this.size = context.getResources().getDimensionPixelSize(R.dimen.page_preview_width);
    }

    @Override
    public int getCount() {
        return library.getNumberPagesFromCurrentBook();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // returns a single page preview

        // update page in library
        library.setCurrentPage(position);

        View view;
        if (convertView == null) {
            view = View.inflate(context, R.layout.element_page_selection, null);
            view.setLayoutParams(new GridView.LayoutParams(size, size));
        } else {
            view = convertView;
        }

        // customize page view
        TextView categoryNameView = view.findViewById(R.id.pageNameTextView);
        categoryNameView.setText(library.getStringFromCurrentPage("name") + "\nEasy");

        ImageView previewImageView = view.findViewById(R.id.pagePreviewImageView);
        Bitmap bitmap = library.loadCurrentPageBitmapDownscaled(size, size);
        bitmap = Utils.replaceColorInBitmap(bitmap, Color.WHITE, Color.TRANSPARENT);
        previewImageView.setImageBitmap(bitmap);

        return view;
    }
}
