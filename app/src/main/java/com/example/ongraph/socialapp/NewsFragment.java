package com.example.ongraph.socialapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import static android.content.ContentValues.TAG;

public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerViewAdapter adapter;
    private List<Status> status_list = new ArrayList<>();
    private EditText status_text;
    ParseFile file = null;
    Button post_but;
    TextView photo;
    String userChoosenTask, post;
    ImageView status_img, profile_img;
    AppSharedPref appSharedPref;
    Bitmap image = null;
    SwipeRefreshLayout swipeLayout;
    ArrayList<String> followingList=new ArrayList<>();
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appSharedPref = new AppSharedPref(getActivity());
        context=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_news_fragment, container, false);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);

        status_text = (EditText) view.findViewById(R.id.status_text);
        post_but = (Button) view.findViewById(R.id.post_button);
        status_img = (ImageView) view.findViewById(R.id.img_status);
        profile_img = (ImageView) view.findViewById(R.id.img);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new RecyclerViewAdapter(status_list, getActivity(), new AppListeners.OnClickListener() {
            @Override
            public void onClick(int position, int value) {

                if (value == 0) {
                    Toast.makeText(getActivity(), "LIKE", Toast.LENGTH_SHORT).show();
                    String id = status_list.get(position).getStatusId();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Updates");
                    query.whereEqualTo("objectId", id);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {

                            boolean stat = object.getBoolean("like_stat");
                            int count = object.getInt("count");
                            if (e == null) {
                                if (!stat) {
                                    count++;
                                    object.put("like_stat", true);
                                    object.put("like", count);
                                    object.saveInBackground();
                                }
                            } else
                                Log.e("result", e.getMessage());
                        }
                    });
                }
                else
                {
                    //AppdialogClass comment_class = new AppdialogClass();
                    AppdialogClass.CommentDialog(context,status_list.get(position));
                }
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        followinglist();

        post_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status_text.getText().toString().isEmpty() && file == null){
                    Toast.makeText(getActivity(), "Can't Upload Empty Status", Toast.LENGTH_SHORT).show();
                }
                else {
                    String status = status_text.getText().toString();
                    ParseObject object = new ParseObject("Updates");

                    object.put("userId", appSharedPref.getUsername());
                    object.put("Status", status);
                    object.put("Name", appSharedPref.getName());
                    object.put("datetime", getDateTime());
                    object.put("like", 0);
                    if (file != null) {
                        try {
                            file.save();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        object.put("Photo", file);
                    }

                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.e("result", "success");
                                getPostsFromServer(false);
                                Toast.makeText(getActivity(), "Status Uploaded", Toast.LENGTH_SHORT).show();
                            } else
                                Log.e("result", e.getMessage());
                        }
                    });
                    status_text.setText(" ");
                }
            }
        });
        photo = (TextView) view.findViewById(R.id.photo_status);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        post = appSharedPref.getImage();
        if (post!=null)
            Picasso.with(getActivity()).load(post).into(profile_img);

        return view;
    }

    public void followinglist() {
        followingList.clear();
        ParseQuery<ParseObject> foll = new ParseQuery<>("Following");
        appSharedPref = new AppSharedPref(getActivity());
        if(appSharedPref.getUsername()!=null){
            Log.e("Result", appSharedPref.getUsername());
            foll.whereEqualTo("userId", appSharedPref.getUsername());
        }
        else {
            Log.e("Result","no username");
        }
        followingList.add(appSharedPref.getUsername());
        foll.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects!=null) {
                        for (int j = 0; j < objects.size(); j++) {
                            ParseObject parseObject = objects.get(j);

                            followingList.add(parseObject.getString("followingId"));

                        }
                        getPostsFromServer(false);
                    }
                }
            }
        });

    }

    private void getPostsFromServer(final boolean isSwipeRefresh) {

        status_list.clear();
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Updates");
        query.orderByDescending("datetime");
        if(followingList==null)
            followingList.add(appSharedPref.getUsername());
        query.whereContainedIn("userId",followingList);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (isSwipeRefresh){
                        swipeLayout.setRefreshing(false);
                    }
                    for (int j = 0; j < objects.size(); j++) {
                        ParseObject parseObject = objects.get(j);

                        String statusId = parseObject.getObjectId();
                        String status = parseObject.getString("Status");
                        String name = parseObject.getString("Name");
                        Date date = parseObject.getDate("datetime");
                        int like = parseObject.getInt("like");
                        ParseFile url = parseObject.getParseFile("Photo");
                        String post = null;
                        if (url != null) {
                            post = url.getUrl();
                        }
                        Status update = new Status(name, status, date, statusId, post, like, false);

                        status_list.add(update);
                    }
                    adapter.notifyDataSetChanged();
                }
                else{
                    if (isSwipeRefresh){
                        swipeLayout.setRefreshing(false);
                    }
                    Log.e("result",e.getMessage());
            }
            }
        });
    }

    private Date getDateTime() {
        return new Date();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionForCamera.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = PermissionForCamera.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }

    }

    private void onCaptureImageResult(Intent data) {
        Bitmap image = (Bitmap) data.getExtras().get("data");
        /*File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");*/
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG, "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            if (image!=null)
                image.compress(Bitmap.CompressFormat.JPEG, 1, fos);
            fos.close();
            //destination.createNewFile();
            saveImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private  File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getActivity().getPackageName()
                + "/Files");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm", Locale.ENGLISH).format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    private void onSelectFromGalleryResult(Intent data) {

        if (data != null) {
            try {
                image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                saveImage(image);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void saveImage(Bitmap image1) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        image1.compress(Bitmap.CompressFormat.PNG, 1, stream);
        byte[] image = stream.toByteArray();
        file = new ParseFile("post.jpeg", image);
    }

    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(true);
        getPostsFromServer(true);
    }

}