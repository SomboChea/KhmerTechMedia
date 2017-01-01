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

public class SportsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<DataItem> mSportsList;
    private Context mContext;

    public SportsAdapter(Context c, ArrayList<DataItem> sportsList){
        this.mContext = c;
        this.mSportsList = sportsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cardview_sports, parent, false);

        SportsListVH sportsListVH = new SportsListVH(view);
        return sportsListVH;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final DataItem dataItem = mSportsList.get(position);

        if (holder instanceof SportsListVH) {
            SportsListVH sportsListVH = (SportsListVH) holder;
            sportsListVH.txt_title.setText(dataItem.post_title);
            sportsListVH.txt_desc.setText(dataItem.post_desc);
            sportsListVH.txt_cat.setText(dataItem.post_cat);
            sportsListVH.txt_date.setText(dataItem.post_date);

//            urlParse parse = new urlParse();
//            String url = parse.distUrl + "/khmertech/img/" + dataItem.post_feature_img;
            Glide.with(mContext)
                    .load(dataItem.post_feature_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.logo)
                    .centerCrop()
                    .crossFade()
                    .error(R.mipmap.error_holder)
                    .into(sportsListVH.ivItem);

            ((SportsListVH) holder).cv_sports.setOnClickListener(new View.OnClickListener() {
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
        if (mSportsList != null) {
            return mSportsList.size();
        }
        return 0;
    }

    public static class SportsListVH extends RecyclerView.ViewHolder {
        CardView cv_sports;
        private ImageView ivItem;
        private TextView txt_title,txt_desc,txt_info,txt_cat,txt_date;

        public SportsListVH(View v) {
            super(v);
            cv_sports = (CardView) v.findViewById(R.id.cv_sports);
            ivItem = (ImageView) v.findViewById(R.id.post_feature_img);
            txt_title = (TextView) v.findViewById(R.id.post_title);
            txt_desc = (TextView) v.findViewById(R.id.post_desc);
            txt_cat = (TextView) v.findViewById(R.id.post_cat);
            txt_date = (TextView) v.findViewById(R.id.post_date);
        }
    }
}
