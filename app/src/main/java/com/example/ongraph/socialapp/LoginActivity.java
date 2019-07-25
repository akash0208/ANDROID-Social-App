package com.example.ongraph.socialapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.ImageRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener , View.OnClickListener{

    Button loginButton;
    TextView forget;
    ProgressDialog progressDialog;
    EditText username,password;
    TextView signup;
    Intent intent;
    ParseFile file;
    String profile_picUrl;
    private AlertDialog waitDialog;
    final List<String> permissions = Arrays.asList("public_profile", "email");

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginactivity);

        init();
        googleinit();
        fblogin();
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.facebookButton).setOnClickListener(this);
        findViewById(R.id.login_button).setOnClickListener(this);
        signup();
        forget();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.sign_in_button:
                showWaitDialog("Please wait");
                signIn();
                break;
            case R.id.facebookButton:
                    showWaitDialog("Please wait!!!");
                    ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, permissions, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            hideWaitDialog();
                            alertDisplayer("Login Failed", err.getMessage()+" Please Try Again");
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                            getUserDetailFromFB();
                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                            getUserDetailFromParse();
                        }
                    }
                });
                break;
            case R.id.login_button:
                    ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if (parseUser != null) {
                                progressDialog.dismiss();
                                AppSharedPref appSharedPref = new AppSharedPref(LoginActivity.this);
                                appSharedPref.setAppIsLogin(true);
                                startActivity(intent);

                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                if (e != null)
                                    alertDisplayer("Login Failed", e.getMessage() + " Please Try Again");
                            }
                        }
                    });
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setTitle("Logging in");
                    progressDialog.show();

                }
    }

    public void init() {
        loginButton = (Button) findViewById(R.id.login_button);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signup = (TextView) findViewById(R.id.signup);
        forget = (TextView) findViewById(R.id.forget);

        progressDialog = new ProgressDialog(LoginActivity.this);
        intent = new Intent(LoginActivity.this,MainContent.class);
    }

    public void googleinit() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this  /*FragmentActivity*/ , this  /*OnConnectionFailedListener*/ )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public void fblogin() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(LoginActivity.this);
    }

    void getUserDetailFromFB(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback(){
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try{
                    //URL imgurl="https://graph.facebook.com/"+object.getId()+"/picture";

                    String profileImageUrl = ImageRequest.getProfilePictureUri(object.getString("id"), 500, 500).toString();
                    Log.i("result", profileImageUrl);

                    String name=object.getString("name");
                    String email=object.getString("email");
                    String id=object.getString("id");
                    String url1 = "https://graph.facebook.com/"+id+"/picture";
//                    String picture=object.getString("picture");
                    //System.out.print("Profile picture" + picture);
                    saveNewUser(name,email,id, url1);
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields","name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    void saveNewUser(String name,String email,String id, String pic){
        ParseUser user = ParseUser.getCurrentUser();
        user.setUsername(id);
        user.setEmail(email);
        user.put("profile_image", pic);
        user.put("full_name",name);

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                else
                    Log.e("Result: ", e.getMessage());
            }
        });
        hideWaitDialog();
        launchActivity();
    }

    void getUserDetailFromParse(){

        ParseUser user = ParseUser.getCurrentUser();
        String username = user.getUsername();
        String email = user.getEmail();
        user.saveInBackground();
        hideWaitDialog();
        launchActivity();
    }

    private void launchActivity() {
        Intent i = new Intent(LoginActivity.this , MainContent.class);
        startActivity(i);
        hideWaitDialog();
    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
        }
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();

            assert acct != null;
            final String personName = acct.getDisplayName();
            final String personEmail = acct.getEmail();
            final String personId = acct.getId();
            Uri bit = acct.getPhotoUrl();
            if (bit != null) {
                profile_picUrl = bit.toString();

                Picasso.with(this).load(bit).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        saveImage(bitmap);
                        googleLogIn(personName, personEmail, personId);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        System.out.print("Failed");

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        System.out.print("result");
                    }
                });
            }
            else
                googleLogIn(personName,personEmail,personId);
        }
    }

    void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        hideWaitDialog();
        alertDisplayer("Connection error", "Check Internet Connection!!!");
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    public void signup() {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this , Signup_Activity.class);
                startActivity(intent);
            }
        });
    }

    public void forget() {

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


        Intent intent = new Intent(LoginActivity.this, ForgetPassword.class);
        startActivity(intent);
            }
        });
    }

    public void saveImage(Bitmap image1) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image1.compress(Bitmap.CompressFormat.PNG, 1, stream);
        byte[] image = stream.toByteArray();
        file = new ParseFile("post.png", image);
        try {
            file.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void saveAppSharedData(String id, String Email, String url, String name) {
        AppSharedPref appSharedPref = new AppSharedPref(LoginActivity.this);
        appSharedPref.setAppIsLogin(true);
        appSharedPref.setUsername(id);
        appSharedPref.setEmail(Email);
        appSharedPref.setImage(url);
        appSharedPref.setName(name);
        Log.e("username",appSharedPref.getUsername()+" "+appSharedPref.getEmail()+" "+appSharedPref.getImage());
    }

    public void googleLogIn(final String personName, final String personEmail, final String personId){
        ParseUser user = new ParseUser();
        if (file != null)
            user.put("profile_image",file);
        user.setUsername(personId);
        user.setEmail(personEmail);
        user.setPassword(personId);
        user.put("full_name",personName);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    saveAppSharedData(personId, personEmail, profile_picUrl, personName);
                    launchActivity();
                } else {
                    if (e.getMessage().equalsIgnoreCase("Account already exists for this username.")) {
                        saveAppSharedData(personId, personEmail, profile_picUrl, personName);
                        launchActivity();
                    }
                    else
                        hideWaitDialog();
                        alertDisplayer("Register Failed" , e.getMessage()+" Please Try Again");
                }
            }
        });
    }

    private void showWaitDialog(String message) {
        if (waitDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.wait_dialog_inflater, null);
            TextView txtMessage = (TextView) view.findViewById(R.id.txt_wait_dialog);
            txtMessage.setText(message);
            builder.setView(view);
            builder.setCancelable(false);
            waitDialog = builder.create();
        }
        if (!this.isFinishing()) {
            waitDialog.show();
        }
    }
    private void hideWaitDialog() {
        if (waitDialog != null) {
            waitDialog.dismiss();
        }
    }

}