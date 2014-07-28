package com.desmond.fullslideinnavigationmenudemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by desmond on 28/7/14.
 */
public class NavigationSectionListAdapter extends BaseExpandableListAdapter {

    private Context mCtx;
    private List<Section> mSectionList;

    public NavigationSectionListAdapter() {
        super();
    }

    public NavigationSectionListAdapter(Context context, List<Section> sections) {
        mSectionList = sections;
        mCtx = context;
    }

    @Override
    public int getGroupCount() {
        return mSectionList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mSectionList.get(groupPosition).getSectionItems().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mSectionList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mSectionList.get(groupPosition).getSectionItems().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mSectionList.get(groupPosition).getSectionItems().get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.slidingmenu_sectionview, parent, false);
        }

        TextView textView = (TextView) convertView
                .findViewById(R.id.slidingmenu_section_title);
        textView.setText(((Section) getGroup(groupPosition)).getTitle());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.slidingmenu_sectionitem, parent, false);
        }

        SectionItem sectionItem = mSectionList.get(groupPosition).getSectionItems().get(childPosition);

        TextView textView = (TextView) convertView
                .findViewById(R.id.slidingmenu_sectionitem_label);
        textView.setText(sectionItem.getTitle());

        final ImageView itemIcon = (ImageView) convertView
                .findViewById(R.id.slidingmenu_sectionitem_icon);
//        itemIcon.setImageDrawable(getDrawableByName(
//                oSectionItem.getIcon(), this.context))

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
