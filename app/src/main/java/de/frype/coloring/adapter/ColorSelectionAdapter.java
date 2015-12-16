package de.frype.coloring.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import de.frype.coloring.R;
import de.frype.util.Utils;

/**
 * Created by Jan on 14.12.2015.
 */
public class ColorSelectionAdapter  extends BaseAdapter {

    private Context context;
    private int columns;
    private int rows;

    public ColorSelectionAdapter(Context context, int columns, int rows) {
        this.context = context;
        this.columns = columns;
        this.rows = rows;
    }

    @Override
    public int getCount() {
        return columns * rows;
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
            view = new View(context);
            int size = (int)context.getResources().getDimension(R.dimen.color_button_size);
            view.setLayoutParams(new ViewGroup.LayoutParams(size, size));
        } else {
            view = convertView;
        }

        Drawable drawable = Utils.getDrawable(context.getResources(), R.drawable.color_selection_cell_background);
        Utils.setBackground(view, drawable);

        return view;

    }
}
