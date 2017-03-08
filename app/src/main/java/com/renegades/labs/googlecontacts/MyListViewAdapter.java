package com.renegades.labs.googlecontacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Виталик on 07.03.2017.
 */

public class MyListViewAdapter extends BaseAdapter {
    Context mContext;
    List<String> contactsList;

    public MyListViewAdapter(Context mContext, List<String> contactsList) {
        this.mContext = mContext;
        this.contactsList = contactsList;
    }

    static class ViewHolder {
        TextView textItem;
    }

    @Override
    public int getCount() {
        return contactsList.size();
    }

    @Override
    public Object getItem(int i) {
        return contactsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.textItem = (TextView) view.findViewById(R.id.listText);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        String rowText = contactsList.get(i);
        viewHolder.textItem.setText(rowText);

        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

}
