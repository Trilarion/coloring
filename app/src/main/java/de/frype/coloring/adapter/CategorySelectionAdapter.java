package de.frype.coloring.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.frype.coloring.R;

/**
 * Created by Jan on 22.11.2015.
 */
public class CategorySelectionAdapter extends BaseAdapter {

    private Context context;
    private JSONArray categories;

    public CategorySelectionAdapter(Context context, JSONArray categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return categories.getJSONObject(position);
        } catch (JSONException e) {
            // it should be a JSONObject, if not we cannot repair it
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;

        if (convertView == null) {
            view = View.inflate(context, R.layout.element_category_selection, null);
        } else {
            view = convertView;
        }

        try {
            JSONObject jsonObject = categories.getJSONObject(position);

            TextView categoryNameView = (TextView) view.findViewById(R.id.categoryNameTextView);
            categoryNameView.setText(jsonObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;

    }
}
