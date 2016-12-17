package com.sombochea.khmertechmedia.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;
import com.sombochea.khmertechmedia.Adapter.MainAdapter;
import com.sombochea.khmertechmedia.R;
import com.sombochea.khmertechmedia.other.DataItem;
import com.sombochea.khmertechmedia.other.MySingleton;
import com.sombochea.khmertechmedia.other.urlParse;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class frm_home_main extends Fragment {

    RecyclerView rvHome;
    SwipeRefreshLayout swipeRefreshLayout;
    Context context;
    View view;

    public frm_home_main() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        view = inflater.inflate(R.layout.frm_home, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });
        fetchData();

        final WebView wb = (WebView) view.findViewById(R.id.wbAds);
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

//        String Url = "file:///android_asset/www/xhtml/index.html";
//        wb.getSettings().setJavaScriptEnabled(true);
//        wb.loadUrl(Url);

        urlParse parse = new urlParse();
        final String urlBase = parse.uLink + "/xhtml/index.html";
        StringRequest wbRequest = new StringRequest(urlBase, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                wb.loadDataWithBaseURL("", response,"text/html", "text/javascript", "utf-8");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                String htmlData = "Error!";
//                wb.loadDataWithBaseURL("",htmlData,"text/html","utf-8","");
                Cache cache = MySingleton.getInstance(context).getRequestQueue().getCache();
                Cache.Entry entry = cache.get(urlBase);

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
                    Log.d(TAG, "onErrorResponse: RRRRRRRRRR");
                }
            }
        });
        MySingleton.getInstance(context).addToRequestQueue(wbRequest);

        // Inflate the layout for this fragment
        return view;
    }

    private void fetchData(){
        urlParse parse = new urlParse();
        final String url = parse.uLink + "/khmertech/feed.php?cat=home";
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        initRecyclerView(response);

                        if (swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Define Cache for use offline
                        Cache cache = MySingleton.getInstance(context).getRequestQueue().getCache();
                        Cache.Entry entry = cache.get(url);

                        //MySingleton.getInstance(context).getRequestQueue().getCache().clear();
                        if(entry != null){
                            new MaterialDialog.Builder(context)
                                    .title("Somrthing error!")
                                    .content("Please make sure, your connection is available and working correctly.")
                                    .theme(Theme.DARK)
                                    .positiveText("Continue")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            Toast.makeText(context.getApplicationContext(), "You used cache date!", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .negativeText("Exit")
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            System.exit(0);
                                        }
                                    })
                                    .neutralText("Retry")
                                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            fetchData();
                                        }
                                    })
                                    .cancelable(false)
                                    .show();
                            try {
                                String data = new String(entry.data, "UTF-8");
                                // handle data, like converting it to xml, json, bitmap etc.,
                                //Log.d(TAG, "Cached Data: "+data);
                                initRecyclerView(data);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Log.d(TAG, "onErrorResponse: RRRRRRRRRR");
                            new MaterialDialog.Builder(context)
                                    .title("Somrthing error!")
                                    .content("Please make sure, your connection is available and working correctly.")
                                    .theme(Theme.DARK)
                                    .negativeText("Exit")
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            System.exit(0);
                                        }
                                    })
                                    .neutralText("Retry")
                                    .onNeutral(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            fetchData();
                                        }
                                    })
                                    .cancelable(false)
                                    .show();
                        }

                        if (swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }

    private void initRecyclerView(String response){
        ArrayList<DataItem> mainList = new JsonConverter<DataItem>().toArrayList(response, DataItem.class);
        rvHome = (RecyclerView) view.findViewById(R.id.rv_main);
        rvHome.setLayoutManager(new LinearLayoutManager(context));
        rvHome.setHasFixedSize(true);
        MainAdapter adatper = new MainAdapter(context, mainList);
        rvHome.setAdapter(new ScaleInAnimationAdapter(adatper));
    }


}
