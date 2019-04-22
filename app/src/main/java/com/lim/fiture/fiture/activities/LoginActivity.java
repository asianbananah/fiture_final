package com.lim.fiture.fiture.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lim.fiture.fiture.R;
import com.lim.fiture.fiture.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    //facebook
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    //firebase
    private FirebaseAuth mAuth;

    //in app
    private static final String TAG = "LoginActivityTag";
    private String mEmail;
    private String mPhotoUrl;
    private String mId;
    private String mName;
    private User mUser;

    private Button fbLoginBtn, adminLoginBtn;

    private final String ADMIN_PASSWORD = "fitureAdmin321!";
    private DatabaseReference databaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        databaseUser = FirebaseDatabase.getInstance().getReference("Users");

        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);

        fbLoginBtn = findViewById(R.id.fbLoginBtn);
        fbLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });

        adminLoginBtn = findViewById(R.id.adminLoginBtn);
        adminLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputPasswordDialog();
            }
        });

        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                handleFacebookAccessToken(loginResult.getAccessToken());
                getUserDetailsFromFb(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login was cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, exception.getStackTrace().toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            for (UserInfo userInfo : currentUser.getProviderData()) {
                if (userInfo.getProviderId().equals("facebook.com")) {
                    startActivity(new Intent(LoginActivity.this, UserMainActivity.class).putExtra("userId",userInfo.getUid()));
                } else {
                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getUserDetailsFromFb(AccessToken accessToken) {

        GraphRequest req = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(final JSONObject object, GraphResponse response) {
                try {
                    Log.d("response101", String.valueOf(response));
                    if (object.has("email")) {
                        mEmail = object.getString("email");
                    } else {
                        mEmail = "";
                    }
                    mPhotoUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                    mId = object.getString("id");
                    mName = object.getString("name");

                    mUser = new User();
                    mUser.setiD(mId.toString());
                    mUser.setFirstName(mName.replace(mName.substring(mName.lastIndexOf(" ")), ""));
                    mUser.setLastName(mName.substring(mName.lastIndexOf(" ")).replace(" ", ""));
                    mUser.setEmail(mEmail);
                    mUser.setPhotoUrl(mPhotoUrl);

                    AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
                    mAuth.signInWithCredential(credential)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithCredential:success");
                                        //check if user already exists; if yes, proceed to userMainActivity
                                        databaseUser.child(mUser.getiD()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){
                                                    //we don't need the whole user object just the userId
                                                    Log.d(TAG,"iddd: " + mUser.getiD());
                                                    startActivity(new Intent(LoginActivity.this, UserMainActivity.class).putExtra("userId", mUser.getiD()));
                                                } else {
                                                    startActivity(new Intent(LoginActivity.this, EditProfileActivity.class).putExtra("user", mUser));
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "graph request error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large)");
        req.setParameters(parameters);
        req.executeAsync();
    }

    public void showInputPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Admin Password");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(input.getText().toString().equals(ADMIN_PASSWORD)){
                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                }else {
                    Toast.makeText(LoginActivity.this, "Invalid password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
