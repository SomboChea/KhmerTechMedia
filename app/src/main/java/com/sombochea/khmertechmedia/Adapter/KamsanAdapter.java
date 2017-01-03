package com.sombochea.khmertechmedia.Adapter;import android.content.Context;import android.content.Intent;import android.support.v7.widget.CardView;import android.support.v7.widget.RecyclerView;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.ImageView;import android.widget.TextView;import com.bumptech.glide.Glide;import com.bumptech.glide.load.engine.DiskCacheStrategy;import com.sombochea.khmertechmedia.R;import com.sombochea.khmertechmedia.other.DataItem;import com.sombochea.khmertechmedia.other.activity.DetailActivity;import java.util.ArrayList;/** * Created by sombochea on 9/30/16. */public class KamsanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {    private ArrayList<DataItem> mKamsanList;    private Context mContext;    public KamsanAdapter(Context c, ArrayList<DataItem> kamsanList){        this.mContext = c;        this.mKamsanList = kamsanList;    }    @Override    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {        LayoutInflater inflater = LayoutInflater.from(parent.getContext());        View view = inflater.inflate(R.layout.cardview_kamsan, parent, false);        KamsanListVH kamsanListVH = new KamsanListVH(view);        return kamsanListVH;    }    @Override    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {        final DataItem dataItem = mKamsanList.get(position);        if (holder instanceof KamsanListVH) {            KamsanListVH kamsanLVH = (KamsanListVH) holder;            kamsanLVH.txt_title_thumb.setText(dataItem.post_title);            kamsanLVH.txt_desc_thumb.setText(dataItem.post_desc);            kamsanLVH.txt_cat_thumb.setText(dataItem.post_cat);            kamsanLVH.txt_date_thumb.setText(dataItem.post_date);            kamsanLVH.txt_author.setText(dataItem.post_author);//            urlParse parse = new urlParse();//            String url = parse.distUrl + "/khmertech/img/" + dataItem.post_feature_img;            Glide.with(mContext)                    .load(dataItem.post_feature_img)                    .diskCacheStrategy(DiskCacheStrategy.ALL)                    .placeholder(R.mipmap.logo)                    .centerCrop()                    .crossFade()                    .error(R.mipmap.error_holder)                    .into(kamsanLVH.ivItem_thumb);            ((KamsanListVH) holder).cv_kamsan.setOnClickListener(new View.OnClickListener() {                @Override                public void onClick(View v) {                    Intent in = new Intent(mContext, DetailActivity.class);                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);                    in.putExtra("items", dataItem);                    mContext.startActivity(in);                }            });        }    }    @Override    public int getItemCount() {        if (mKamsanList != null) {            return mKamsanList.size();        }        return 0;    }    public static class KamsanListVH extends RecyclerView.ViewHolder{        private CardView cv_kamsan;        private ImageView ivItem_thumb;        private TextView txt_title_thumb,txt_desc_thumb,txt_cat_thumb,txt_date_thumb,txt_author;        public KamsanListVH(View v) {            super(v);            cv_kamsan = (CardView) v.findViewById(R.id.cv_kamsan);            ivItem_thumb = (ImageView) v.findViewById(R.id.post_feature_img);            txt_title_thumb = (TextView) v.findViewById(R.id.post_title);            txt_desc_thumb = (TextView) v.findViewById(R.id.post_title);            txt_cat_thumb = (TextView) v.findViewById(R.id.post_cat);            txt_date_thumb = (TextView) v.findViewById(R.id.post_date);            txt_author = (TextView) v.findViewById(R.id.author);        }    }}