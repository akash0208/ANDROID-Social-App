package com.example.ongraph.socialapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.util.List;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;

    private List<Status> status_list;
    private AppListeners.OnClickListener onClickListener;

    RecyclerViewAdapter(List<Status> status_list, Context context, AppListeners.OnClickListener onClickListener) {
        this.status_list = status_list;
        this.context = context;
        this.onClickListener=onClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, status, like_but, comment;
        ImageView image;

        MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.uploader_name);
            date = (TextView) view.findViewById(R.id.upload_datetime);
            status = (TextView) view.findViewById(R.id.status);
            like_but = (TextView) view.findViewById(R.id.like_button);
            image = (ImageView) view.findViewById(R.id.img_status);
            comment = (TextView) view.findViewById(R.id.comment);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (status_list.get(position).getName() != null)
            holder.name.setText(status_list.get(position).getName());

        DateFormat df = DateFormat.getDateInstance();
        if (status_list.get(position).getName() != null)
            holder.date.setText(df.format(status_list.get(position).getDate()));

        if (status_list.get(position).getName() != null)
            holder.status.setText(status_list.get(position).getStatus());

        if (status_list.get(position).getImg() != null) {
            holder.image.setVisibility(View.VISIBLE);
            Picasso.with(context).load(status_list.get(position).getImg()).resize(250,250).into(holder.image);
        }
        else{
            holder.image.setVisibility(View.GONE);
        }

        holder.like_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(position, 0);
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(position, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return status_list.size();
    }

}