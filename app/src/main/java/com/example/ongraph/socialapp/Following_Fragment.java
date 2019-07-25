package com.example.ongraph.socialapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.ArrayList;
import java.util.List;

public class Following_Fragment extends Fragment {

    public TextView followingname;
    RecyclerView recyclerView;
    public Following_Adapter followingAdapter;
    AppSharedPref appSharedPref;
    private List<Following> following_list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_following_fragment, container, false);

        followingname = (TextView) view.findViewById(R.id.follower_following_name);
        recyclerView = (RecyclerView) view.findViewById(R.id.following_recycler_view);
        followingAdapter = new Following_Adapter(following_list, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(followingAdapter);
        getFollowingsFromServer();
        return view;
    }

    private void getFollowingsFromServer() {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Following");
        appSharedPref = new AppSharedPref(getActivity());
        String id = appSharedPref.getUsername();
        query.whereEqualTo("userId", id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (int j = 0; j < objects.size(); j++) {
                            ParseObject parseObject = objects.get(j);
                            String name = parseObject.getString("followingName");

                            Following update = new Following(name);

                            following_list.add(update);
                        }
                        followingAdapter.notifyDataSetChanged();
                    }
                }
                else{
                    Log.e("result",e.getMessage());
                }
            }
        });
    }

}