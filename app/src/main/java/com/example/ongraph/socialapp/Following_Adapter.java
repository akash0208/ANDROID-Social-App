package com.example.ongraph.socialapp;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class Following_Adapter extends RecyclerView.Adapter<Following_Adapter.MyViewHolder> {

    private List<Following> following_list;

    public Following_Adapter(List<Following> following_list, FragmentActivity activity) {
        this.following_list = following_list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView following_name;
        public MyViewHolder(View view) {
            super(view);
            following_name = (TextView) view.findViewById(R.id.follower_following_name);

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
        if (following_list.get(position).getName() != null)
            holder.following_name.setText(following_list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return following_list.size();
    }
}
