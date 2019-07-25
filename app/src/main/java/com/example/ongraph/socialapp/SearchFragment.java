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
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    SearchAdapter adapter;
    RecyclerView recyclerView;
    AppSharedPref appSharedPref;
    private List<Search> search_list = new ArrayList<>();
    ArrayList<String> followingList=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.search_recycler_view);
        adapter = new SearchAdapter(search_list, getActivity(), new AppListeners.OnClickListener(){
            @Override
            public void onClick(final int position, int value) {

                final String name = search_list.get(position).getName();
                final String followingId = search_list.get(position).getId();
                final String userId = appSharedPref.getUsername();

                ParseObject parseObject = new ParseObject("Following");
                parseObject.put("userId", userId);
                parseObject.put("followingId", followingId);
                parseObject.put("followingName", name);

                followingList.add(followingId);
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            search_list.get(position).setFriend(true);
                            search_list.remove(position);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "You Followed " + name, Toast.LENGTH_SHORT).show();
                            ParseObject followerObject = new ParseObject("Followers");
                            followerObject.put("followerId", userId);
                            followerObject.put("userId", followingId);
                            followerObject.put("followerName", appSharedPref.getName());
                            followerObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null)
                                        System.out.print("Result true");
                                    else
                                        Log.e("result", e.getMessage());
                                }
                            });
                        }
                        else
                            Log.e("result", e.getMessage());
                    }
                });
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        followinglist();



        return view;
    }

    public void followinglist() {
        followingList.clear();
        ParseQuery<ParseObject> foll = new ParseQuery<>("Following");
        appSharedPref = new AppSharedPref(getActivity());
        if(appSharedPref.getUsername()!=null){
            foll.whereEqualTo("userId", appSharedPref.getUsername());
            Log.e("Result", appSharedPref.getUsername());
        }
        else
            Log.e("Result", appSharedPref.getUsername());
        foll.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects!=null) {
                        for (int j = 0; j < objects.size(); j++) {
                            ParseObject parseObject = objects.get(j);

                            followingList.add(parseObject.getString("followingId"));

                        }
                        getUserList();
                    }
                }
            }
        });

    }

    public void getUserList(){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        if (followingList == null || followingList.isEmpty())
            Log.e("Result","Found");
        else
            query.whereNotContainedIn("username",followingList);
        appSharedPref = new AppSharedPref(getActivity());
        final String user = appSharedPref.getUsername();

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (int j = 0; j < objects.size(); j++) {
                            ParseObject parseObject = objects.get(j);
                            followingList.add(parseObject.getString("userName"));

                            if (user.equals(parseObject.getString("username")))
                                Log.e("result", parseObject.getObjectId());
                            else {
                                String name = parseObject.getString("full_name");
                                String id = parseObject.getString("username");
                                Search update = new Search(name, id, false);
                                search_list.add(update);
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                else{
                    Log.e("result",e.getMessage());
                }
            }
        });


        /*query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (int j = 0; j < objects.size(); j++) {
                            ParseObject parseObject = objects.get(j);
                            followingList.add(parseObject.getString("userName"));

                            if (user.equals(parseObject.getString("username")))
                                Log.e("result", parseObject.getObjectId());
                            else {
                                String name = parseObject.getString("full_name");
                                String id = parseObject.getString("username");
                                Search update = new Search(name, id, false);
                                search_list.add(update);
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                else{
                    Log.e("result",e.getMessage());
                }
            }
        });*/
    }

}