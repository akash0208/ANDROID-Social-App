package com.example.ongraph.socialapp;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.MyViewHolder> {

    private List<Follower> follower_list;

    public FollowerAdapter(List<Follower> follower_list, FragmentActivity activity) {
        this.follower_list = follower_list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView follower_name;
        public MyViewHolder(View view) {
            super(view);
            follower_name = (TextView) view.findViewById(R.id.follower_following_name);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.following_follower_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (follower_list.get(position).getName() != null)
            holder.follower_name.setText(follower_list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return follower_list.size();
    }
}
