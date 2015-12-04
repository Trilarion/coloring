package de.frype.coloring.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.frype.coloring.OutlineRepository;
import de.frype.coloring.R;

/**
 * Created by Jan on 03.12.2015.
 */
public class OutlineSelectionAdapter extends BaseAdapter {

    private Context context;
    private OutlineRepository repo = OutlineRepository.getInstance();

    public OutlineSelectionAdapter(Context context) {
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
            view = View.inflate(context, R.layout.element_outline_selection, null);
        } else {
            view = convertView;
        }

        repo.setCurrentPage(position);
        TextView categoryNameView = (TextView) view.findViewById(R.id.outlineNameTextView);
        categoryNameView.setText(repo.getStringFromCurrentPage("name"));

        return view;
    }
}
