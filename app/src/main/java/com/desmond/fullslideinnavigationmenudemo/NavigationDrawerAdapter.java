package com.desmond.fullslideinnavigationmenudemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by desmond on 27/7/14.
 */
public class NavigationDrawerAdapter extends BaseAdapter {

    private Context mCtx;

    public NavigationDrawerAdapter(Context ctx) {
        mCtx = ctx;
    }

    @Override
    public int getCount() {
        return 10;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.navigation_drawer_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.navigation_item);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.mTextView.setText("text " + position);

        return convertView;
    }

    private static class ViewHolder{
        TextView mTextView;
    }
}
