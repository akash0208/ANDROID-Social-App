package com.example.ongraph.socialapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private List<Search> search_List;
    private AppListeners.OnClickListener onClickListener;
    Context context;

    public SearchAdapter(List<Search> search_List, Context context, AppListeners.OnClickListener onClickListener) {
        this.search_List = search_List;
        this.onClickListener = onClickListener;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView search_name;
        TextView addfriend;
        public MyViewHolder(View view) {
            super(view);
            addfriend = (TextView) view.findViewById(R.id.add_friend);
            search_name = (TextView) view.findViewById(R.id.search_name);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (search_List.get(position).getName() != null)
            holder.search_name.setText(search_List.get(position).getName());

        holder.addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(position, 0);

            }
        });
    }

    @Override
    public int getItemCount() {
        return search_List.size();
    }
}
