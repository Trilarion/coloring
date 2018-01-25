package de.frype.coloring.page_selection;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import de.frype.coloring.library.Library;
import de.frype.coloring.R;

/**
 * Created by Jan on 03.12.2015.
 */
public class PageSelectionAdapter extends BaseAdapter {

    private final Context context;
    private final int size;
    private final Library library = Library.getInstance();

    public PageSelectionAdapter(Context context, int size) {
        this.context = context;
        this.size = size;
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
        View view;

        if (convertView == null) {
            view = View.inflate(context, R.layout.element_page_selection, null);

            view.setLayoutParams(new GridView.LayoutParams(size, size));
        } else {
            view = convertView;
        }

        library.setCurrentPage(position);
        TextView categoryNameView = view.findViewById(R.id.pageNameTextView);
        categoryNameView.setText(library.getStringFromCurrentPage("name"));
        categoryNameView.setText("test");

        // TODO transparency after loading and scaling
        ImageView previewImageView = view.findViewById(R.id.pagePreviewImageView);
        Bitmap bitmap = library.loadCurrentPageBitmap();
        previewImageView.setImageBitmap(bitmap);

        return view;
    }
}
