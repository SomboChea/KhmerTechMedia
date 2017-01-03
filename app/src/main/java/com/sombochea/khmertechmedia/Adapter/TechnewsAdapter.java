package com.sombochea.khmertechmedia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sombochea.khmertechmedia.R;
import com.sombochea.khmertechmedia.other.DataItem;
import com.sombochea.khmertechmedia.other.activity.DetailActivity;

import java.util.ArrayList;

/**
 * Created by sombochea on 9/30/16.
 */

public class TechnewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<DataItem> mMainList;
    private Context mContext;

    public TechnewsAdapter(Context context, ArrayList<DataItem> mainList){
        this.mContext = context;
        this.mMainList = mainList;
    }

    public static class TechnewsVH extends RecyclerView.ViewHolder{
        CardView cv_technews;
        ImageView ivPhoto;
        TextView post_title,post_desc,post_cat,post_date,txt_author;

        public TechnewsVH(View v) {
            super(v);
            cv_technews = (CardView) v.findViewById(R.id.cv_technews);
            ivPhoto = (ImageView) v.findViewById(R.id.post_feature_img);
            post_title = (TextView) v.findViewById(R.id.post_title);
            post_desc = (TextView) v.findViewById(R.id.post_desc);
            post_cat = (TextView) v.findViewById(R.id.post_cat);
            post_date = (TextView) v.findViewById(R.id.post_date);
            txt_author = (TextView) v.findViewById(R.id.author);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cardview_technews, parent, false);

        TechnewsVH technewsVH = new TechnewsVH(view);
        return technewsVH;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DataItem dataItem = mMainList.get(position);

        if (holder instanceof TechnewsVH) {
            TechnewsVH technewsVH = (TechnewsVH) holder;
            technewsVH.post_title.setText(dataItem.post_title);
            technewsVH.post_desc.setText(dataItem.post_desc);
            technewsVH.post_cat.setText(dataItem.post_cat);
            technewsVH.post_date.setText(dataItem.post_date);
            technewsVH.txt_author.setText(dataItem.post_author);

            Glide.with(mContext)
                    .load(dataItem.post_feature_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.logo)
                    .centerCrop()
                    .crossFade()
                    .error(R.mipmap.error_holder)
                    .into(technewsVH.ivPhoto);
            ((TechnewsVH) holder).cv_technews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(mContext, DetailActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("items", dataItem);
                    mContext.startActivity(in);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mMainList != null) {
            return mMainList.size();
        }
        return 0;
    }

}
