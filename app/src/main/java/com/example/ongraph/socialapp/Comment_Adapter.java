package com.example.ongraph.socialapp;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.MyViewHolder> {

    private List<Comment> commentList;
    private Context context;

    Comment_Adapter(Context context, List<Comment> commentList) {
        this.commentList = commentList;

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView comment_name;
        ImageView comment_image;
        TextView comment;
        MyViewHolder(View view) {
            super(view);
            comment_name = (TextView) view.findViewById(R.id.comment_name);
            comment_image = (ImageView) view.findViewById(R.id.image_comment);
            comment = (TextView) view.findViewById(R.id.comment);
            context = view.getContext();


        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (commentList.get(position).getCommentname() != null)
            holder.comment_name.setText(commentList.get(position).getCommentname());
        if (commentList.get(position).getPic() != null)
            Picasso.with(context).load(commentList.get(position).getPic()).into(holder.comment_image);
        if (commentList.get(position).getComment() != null)
            holder.comment.setText(commentList.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
