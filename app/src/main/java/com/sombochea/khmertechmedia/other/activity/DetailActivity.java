package com.sombochea.khmertechmedia.other.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sombochea.khmertechmedia.R;
import com.sombochea.khmertechmedia.other.DataItem;
import com.sombochea.khmertechmedia.other.MySingleton;
import com.sombochea.khmertechmedia.other.urlParse;

import java.io.UnsupportedEncodingException;

import static android.content.ContentValues.TAG;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView imageView = (ImageView) findViewById(R.id.ivPhoto_detail);
        TextView cat_label, posted_date, desc_full, txt_title;
        txt_title = (TextView) findViewById(R.id.title_full);
        desc_full = (TextView) findViewById(R.id.decs_full);
        cat_label = (TextView) findViewById(R.id.cat_label_detail);
        posted_date = (TextView) findViewById(R.id.date_posted_detail);


        final WebView wb = (WebView) findViewById(R.id.wbAds);
        final WebView wbView = (WebView) findViewById(R.id.wbView);

        //Get Items From other
        if (getIntent().getSerializableExtra("items") != null){
            DataItem dataItem = (DataItem) getIntent().getSerializableExtra("items");
//            urlParse parse = new urlParse();
//            String url = parse.distUrl + "/khmertech/img/" + dataItem.post_feature_img;
            Glide.with(this)
                    .load(dataItem.post_feature_img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.mipmap.logo)
                    .centerCrop()
                    .crossFade()
                    .error(R.mipmap.error_holder)
                    .into(imageView);
            cat_label.setText(dataItem.post_cat);
            posted_date.setText(dataItem.post_date);
            txt_title.setText(dataItem.post_title);
            desc_full.setText(dataItem.post_desc);

            String headHTML = "<!DOCTYPE HTML><html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" /><link rel=\"stylesheet\" type=\"text/css\" href=\"https://mobile.app.khmer-tech.com/css/materialize.min.css\" media=\"screen, projection\" /><link href=\"https://fonts.googleapis.com/icon?family=Material+Icons\" rel=\"stylesheet\"><script type=\"text/javascript\" src=\"https://mobile.app.khmer-tech.com/js/jquery-2.1.1.min.js\"></script><script type=\"text/javascript\" src=\"https://mobile.app.khmer-tech.com/js/materialize.min.js\"></script></head><body>";
            String footHTML = "</body></html>";
            String mainDataHTML = headHTML + dataItem.post_content + footHTML;

            wbView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });
            wbView.getSettings().setJavaScriptEnabled(true);
            wbView.getSettings().setUseWideViewPort(true);
            wbView.getSettings().setAllowContentAccess(true);
            wbView.getSettings().setAllowUniversalAccessFromFileURLs(false);
            wbView.getSettings().setDomStorageEnabled(true);
            wbView.loadDataWithBaseURL("", mainDataHTML,"text/html", "text/javascript", "utf-8");
//            Toast.makeText(getApplicationContext(),mainDataHTML,Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(),R.string.not_reveiced,Toast.LENGTH_LONG).show();
        }
        wb.getSettings().setAllowContentAccess(true);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setAppCacheEnabled(true);
        wb.getSettings().setAllowFileAccess(true);
        wb.getSettings().setAllowFileAccessFromFileURLs(true);
        wb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        urlParse parse = new urlParse();
        final String ad_250 = parse.distUrlAds + "250";

        StringRequest wbRequest = new StringRequest(ad_250, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                wb.loadDataWithBaseURL("", response,"text/html", "text/javascript", "utf-8");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//              wb.loadDataWithBaseURL("",htmlData,"text/html","utf-8","");
                Cache cache = MySingleton.getInstance(getApplicationContext()).getRequestQueue().getCache();
                Cache.Entry entry = cache.get(ad_250);

                //MySingleton.getInstance(context).getRequestQueue().getCache().clear();
                if(entry != null){
                    try {
                        //Load Data Cached
                        String data = new String(entry.data, "UTF-8");
                        wb.loadDataWithBaseURL("",data,"text/html","utf-8","");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }else {
                    Log.d(TAG, "onErrorResponse: Cache not available!");
                }
            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(wbRequest);
    }

}
