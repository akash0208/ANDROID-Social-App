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

public class Follower_Fragment extends Fragment {

    public TextView followername;
    RecyclerView recyclerView;
    public FollowerAdapter followerAdapter;
    private List<Follower> follower_list = new ArrayList<>();
    AppSharedPref appSharedPref;
    String objectId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_follower_fragment, container, false);

        followername = (TextView) view.findViewById(R.id.follower_following_name);
        recyclerView = (RecyclerView) view.findViewById(R.id.follower_recycler_view);
        followerAdapter = new FollowerAdapter(follower_list, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(followerAdapter);
        getFollowersFromServer();
        return view;
    }

    private void getFollowersFromServer() {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Followers");
        appSharedPref = new AppSharedPref(getActivity());
        objectId = appSharedPref.getUsername();
        query.whereEqualTo("userId", objectId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (int j = 0; j < objects.size(); j++) {
                            ParseObject parseObject = objects.get(j);
                            String name = parseObject.getString("followerName");

                            Follower update = new Follower(name);

                            follower_list.add(update);
                        }
                        followerAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    Log.e("result",e.getMessage());
                }
            }
        });
    }

}