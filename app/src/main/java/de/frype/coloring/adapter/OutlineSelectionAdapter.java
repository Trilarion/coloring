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
 * Created by Jan on 03.12.2015.
 */
public class OutlineSelectionAdapter extends BaseAdapter {

    private Context context;
    private JSONObject category;

    public OutlineSelectionAdapter(Context context, JSONObject category) {
        this.context = context;
        this.category = category;

    }

    @Override
    public int getCount() {
        JSONArray jsonArray = null;
        try {
            jsonArray = category.getJSONArray("outlines");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray.length();
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

        try {
            JSONArray jsonArray = category.getJSONArray("outlines");

            TextView categoryNameView = (TextView) view.findViewById(R.id.outlineNameTextView);
            categoryNameView.setText(jsonArray.getString(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }
}
