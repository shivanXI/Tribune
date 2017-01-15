package project.com.tribune;

<<<<<<< HEAD
import android.os.Bundle;
=======
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
>>>>>>> 4327e12c9e333d204d8c7ab4df57b50adfdebf23
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
<<<<<<< HEAD
import com.google.android.gms.auth.api.Auth;
=======
import com.google.android.gms.tasks.OnSuccessListener;
>>>>>>> 4327e12c9e333d204d8c7ab4df57b50adfdebf23
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mPhotosDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private static final int RC_SIGN_IN=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mPhotosDatabaseReference = mFirebaseDatabase.getReference().child("photos");


<<<<<<< HEAD
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
=======
        mDatabaseReference=mFirebaseDatabase.getReference().child("data");

        mFirebaseAuth=FirebaseAuth.getInstance();

        mAuthStateListener=new FirebaseAuth.AuthStateListener()     //initialization of mAuthStateListener.
        {
>>>>>>> 4327e12c9e333d204d8c7ab4df57b50adfdebf23
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null)
                {

                }
                else
                {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
<<<<<<< HEAD
                                    .setProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()/*,
                                            new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()*/
                                    ))
=======
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER
                                            //AuthUI.FACEBOOK_PROVIDER
                                    )
>>>>>>> 4327e12c9e333d204d8c7ab4df57b50adfdebf23
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==RC_SIGN_IN)
            if(resultCode==RESULT_OK)
                Toast.makeText(getApplicationContext(),"signing in...",Toast.LENGTH_SHORT).show();
            else if(resultCode==RESULT_CANCELED)
            {
                Toast.makeText(getApplicationContext(),"exiting...",Toast.LENGTH_SHORT).show();
                finish();
            }
    }


}
