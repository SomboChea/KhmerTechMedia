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
import com.sombochea.khmertechmedia.Adapter.SportsAdapter;
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
public class frm_sports_main extends Fragment {

    private RecyclerView rvSports;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    private View view;

    public frm_sports_main() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        view = inflater.inflate(R.layout.frm_sports, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh_sports);
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
        return view;
    }

    private void fetchData(){
        urlParse parse = new urlParse();
        final String url = parse.distUrl + "feed.sb?cat=sport";
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        initRecyclerView(response);
                        Toast.makeText(context.getApplicationContext(),"Data loaded!",Toast.LENGTH_LONG).show();

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
    }

    private void initRecyclerView(String response){
        ArrayList<DataItem> sportsList = new JsonConverter<DataItem>().toArrayList(response, DataItem.class);
        rvSports = (RecyclerView) view.findViewById(R.id.rv_sports);
        rvSports.setLayoutManager(new LinearLayoutManager(context));
        rvSports.setHasFixedSize(true);
        SportsAdapter adatper = new SportsAdapter(context, sportsList);
        rvSports.setAdapter(new ScaleInAnimationAdapter(adatper));
    }

}
