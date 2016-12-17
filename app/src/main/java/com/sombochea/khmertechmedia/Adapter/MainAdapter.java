package com.sombochea.khmertechmedia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<DataItem> mMainList;
    private Context mContext;

    public MainAdapter(Context context, ArrayList<DataItem> mainList){
        this.mContext = context;
        this.mMainList = mainList;
    }

    public static class MainItemVH extends RecyclerView.ViewHolder {
        CardView cv_home_main;
        ImageView ivItem;
        TextView txt_title,txt_desc,txt_cat,txt_date;
        Button readmore, share;

        public MainItemVH(View v) {
            super(v);
            cv_home_main = (CardView) v.findViewById(R.id.cv_home);
            ivItem = (ImageView) v.findViewById(R.id.post_feature_img);
            txt_title = (TextView) v.findViewById(R.id.post_title);
            txt_desc = (TextView) v.findViewById(R.id.post_desc);
            txt_cat = (TextView) v.findViewById(R.id.post_cat);
            txt_date = (TextView) v.findViewById(R.id.post_date);
            readmore = (Button) v.findViewById(R.id.read_more);
            share = (Button) v.findViewById(R.id.share);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.home_cardview, parent, false);

        MainItemVH mainItemVH = new MainItemVH(view);
        return mainItemVH;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DataItem dataItem = mMainList.get(position);

        if (holder instanceof MainItemVH) {
            MainItemVH mainItemVH = (MainItemVH) holder;
            mainItemVH.txt_title.setText(dataItem.post_title);
            mainItemVH.txt_desc.setText(dataItem.post_desc);
            mainItemVH.txt_cat.setText(dataItem.post_cat);
            mainItemVH.txt_date.setText(dataItem.post_date);

//            urlParse parse = new urlParse();
//            String url = parse.uLink + "/khmertech/img/" + dataItem.post_feature_img;
            Glide.with(mContext)
                    .load(dataItem.post_feature_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.logo)
                    .centerCrop()
                    .crossFade()
                    .error(R.mipmap.error_holder)
                    .into(mainItemVH.ivItem);

            mainItemVH.readmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(mContext, DetailActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("items", dataItem);
                    mContext.startActivity(in);
                }
            });

            ((MainItemVH) holder).cv_home_main.setOnClickListener(new View.OnClickListener() {
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
