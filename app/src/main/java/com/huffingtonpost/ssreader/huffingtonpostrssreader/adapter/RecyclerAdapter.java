package com.huffingtonpost.ssreader.huffingtonpostrssreader.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huffingtonpost.ssreader.huffingtonpostrssreader.R;
import com.huffingtonpost.ssreader.huffingtonpostrssreader.modules.Item;

import java.util.List;

/**
 * Created by Zhisheng on 2015/6/29.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private String[] mDataset;
    private Context context;
    private List<Item> items;
    private ViewHolder holder;

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_recyclerview_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        holder = new ViewHolder(v);
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Item item = items.get(position);
        holder.mTitle.setText(item.getTitle());
        holder.mDate.setText(item.getPubDate());
        if (item.getImageUrls().size() > 0) {
            Uri uri = Uri.parse(item.getImageUrls().get(0));
            holder.mImage.setImageURI(uri);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mTitle;
        TextView mDate;
        ImageView mImage;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mTitle = (TextView) itemView.findViewById(R.id.recycler_view_title);
            this.mDate = (TextView) itemView.findViewById(R.id.recycler_view_date);
            this.mImage = (ImageView) itemView.findViewById(R.id.recycler_view_image);
        }
    }
}
