package com.github.paulononaka.installapkinbackground.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.paulononaka.installapkinbackground.R;

import java.util.ArrayList;

/**
 * Created by Maks on 8/10/2017.
 */

public class ListItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ListItem> list = new ArrayList<>();

    public ListItemAdapter(Context context) {
        this.context = context;
    }

    public void addItem(Drawable icon, CharSequence appName, String packName) {
        list.add(new ListItem(icon, appName.toString(), packName));
    }

    public void clear() {
        list.clear();
    }

    public ArrayList<ListItem> getList() {
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);

            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.textView1 = (TextView) convertView.findViewById(R.id.textView1);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ListItem listData = list.get(position);
        if (!listData.isIconNull())
            viewHolder.imageView.setImageDrawable(listData.getIcon());
        viewHolder.textView1.setText(listData.getAppName());

        return convertView;
    }

    private class ViewHolder {
        public ImageView imageView;
        public TextView textView1;
    }
}
