package de.frype.coloring.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import de.frype.coloring.Library;
import de.frype.coloring.R;

/**
 * Created by Jan on 03.12.2015.
 */
public class PageSelectionAdapter extends BaseAdapter {

    private Context context;
    private Library repo = Library.getInstance();

    public PageSelectionAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return repo.getNumberPagesFromCurrentBook();
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
        } else {
            view = convertView;
        }

        repo.setCurrentPage(position);
        TextView categoryNameView = (TextView) view.findViewById(R.id.pageNameTextView);
        categoryNameView.setText(repo.getStringFromCurrentPage("name"));

        return view;
    }
}
