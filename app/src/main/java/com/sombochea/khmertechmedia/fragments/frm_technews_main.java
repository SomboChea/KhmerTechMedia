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
import com.sombochea.khmertechmedia.Adapter.TechnewsAdapter;
import com.sombochea.khmertechmedia.other.DataItem;
import com.sombochea.khmertechmedia.other.MySingleton;
import com.sombochea.khmertechmedia.R;
import com.sombochea.khmertechmedia.other.urlParse;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class frm_technews_main extends Fragment {

    private RecyclerView rvTechnews;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    private View view;
    private TechnewsAdapter adatper;

    public frm_technews_main() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        view = inflater.inflate(R.layout.frm_technews, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh_technews);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3
        );

        fetchData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        return view;
    }

    //Fetching Data from Database
    private void fetchData(){
        urlParse parse = new urlParse();
        final String url = parse.distUrl + "feed.sb?cat=technews";
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        initRecyclerView(response);
                        Toast.makeText(context.getApplicationContext(),"Data was loaded!",Toast.LENGTH_LONG).show();

                        if (swipeRefreshLayout.isRefreshing()){
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getContext(), "Error!", Toast.LENGTH_LONG).show();
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
                                            Toast.makeText(context.getApplicationContext(), "You used cache data!", Toast.LENGTH_LONG).show();
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
                            Log.d(TAG, "onErrorResponse: Error while loading!");
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

    } //End get data from database

    //Initialization Recycler View
    private void initRecyclerView(String response){
        ArrayList<DataItem> mainList = new JsonConverter<DataItem>().toArrayList(response, DataItem.class);
        rvTechnews = (RecyclerView) view.findViewById(R.id.rv_technews);
        rvTechnews.setLayoutManager(new LinearLayoutManager(context));
        rvTechnews.setHasFixedSize(true);
        adatper = new TechnewsAdapter(context, mainList);
        rvTechnews.setAdapter(new SlideInBottomAnimationAdapter(adatper));
        //rvTechnews.setItemAnimator(new SlideInUpAnimator());
    }//End Recycler View
}
