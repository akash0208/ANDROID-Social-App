package com.example.ongraph.socialapp;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

class AppdialogClass {

    private static List<Comment> comment_List = new ArrayList<>();
    private static Comment_Adapter adapter;
    static int num;

    public static void CommentDialog(final Context context, final Status status)
    {
        final AppSharedPref appSharedPref = new AppSharedPref(context);
        adapter = new Comment_Adapter(context, comment_List);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        final View convertView = LayoutInflater.from(context).inflate(R.layout.popup_layout, null);
        alertDialog.setView(convertView);

        ImageView comm =(ImageView) convertView.findViewById(R.id.comment_img);
        String pic = appSharedPref.getImage();
        Picasso.with(context).load(pic).into(comm);

        RecyclerView rv = (RecyclerView) convertView.findViewById(R.id.commentRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        alertDialog.show();

        ImageView comment_upload = (ImageView) convertView.findViewById(R.id.comment_upload);
        comment_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText commentText = (EditText) convertView.findViewById(R.id.writeComment);

                getCommentsFromServer(status.getStatusId());
                if (commentText.getText().toString().isEmpty()) {
                    Toast.makeText(context, "Can't Post Empty Comment", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ParseObject parseObject = new ParseObject("Comments");
                    parseObject.put("name", appSharedPref.getName());
                    parseObject.put("userId", appSharedPref.getUsername());
                    parseObject.put("comment", commentText.getText().toString());
                    parseObject.put("statusId", status.getStatusId());
                    parseObject.put("pic", appSharedPref.getImage());
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                commentText.setText("");
                                Toast.makeText(context, "Comment Uploaded", Toast.LENGTH_SHORT).show();
                            } else
                                Log.e("Result", e.getMessage());
                        }
                    });
                }
            }
        });
        getCommentsFromServer(status.getStatusId());
    }
    private static void getCommentsFromServer(String statusId) {

        comment_List.clear();
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Comments");
        query.whereEqualTo("statusId", statusId);
        query.orderByDescending("datetime");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (int j = 0; j < objects.size(); j++) {
                        ParseObject parseObject = objects.get(j);

                        String commentId = parseObject.getObjectId();
                        String comment = parseObject.getString("comment");
                        String name = parseObject.getString("name");
                        String pic = parseObject.getString("pic");

                        Comment comm = new Comment(name, comment, commentId, pic);

                        comment_List.add(comm);
                    }
                    num=objects.size();
                    adapter.notifyDataSetChanged();
                }
                else{
                    Log.e("result",e.getMessage());
                }
            }
        });

    }
}
