package com.huffingtonpost.ssreader.huffingtonpostrssreader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huffingtonpost.ssreader.huffingtonpostrssreader.R;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.modules.RssItem;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Zhisheng on 2015/6/29.
 */
public class CustomListAdapter extends BaseAdapter {

    private static final SimpleDateFormat SIMPLEDATE = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
    private Context mContext;
    private List<RssItem> items;
    private ViewHolder holder;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CustomListAdapter(Context context, List<RssItem> items) {
        this.mContext = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.custom_listview_item, null);
        holder = new ViewHolder();
        holder.mTitle = (TextView) convertView.findViewById(R.id.list_view_title);
        //holder.mDate = (TextView) convertView.findViewById(R.id.list_view_date);
        //holder.mImage = (ImageView) convertView.findViewById(R.id.list_view_image);

        holder.mTitle.setText(items.get(position).getTitle());
        //holder.mDate.setText(SIMPLEDATE.format(items.get(position).getPubDate()));
        //viewHolder.mImage.setImageURI(items.get(position).getLink());
        convertView.setTag(holder);
        return convertView;
    }

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    static class ViewHolder {
        // each data item is just a string in this case
        TextView mTitle;
        //TextView mDate;
        //ImageView mImage;
    }
}
