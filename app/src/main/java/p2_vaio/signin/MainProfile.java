package p2_vaio.signin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import static p2_vaio.signin.MainActivity.RC_SIGN_IN;

public class MainProfile extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "";
    private FirebaseAuth firebaseauth;

    private GoogleApiClient mGoogleApiClient;
    private TextView name;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profile);

        name = (TextView)findViewById(R.id.profile_name) ;



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();








    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            showname(result);
        }
    }

    private void showname(GoogleSignInResult result) {

        GoogleSignInAccount acct = result.getSignInAccount();
        name.setText(getString(R.string.signed_in_fmt,acct.getDisplayName()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.profile) {

            startActivity(new Intent(getApplicationContext(), MainProfile.class));
            //do something
            return true;
        }
        if (id == R.id.chat) {

            //do something
            return true;
        }

        if (id == R.id.sign_out_menu) {


            signOut();

           // startActivity(new Intent(MainProfile.this,MainActivity.class));


/*            firebaseauth.signOut();
            finish();
            startActivity(new Intent(this , LoginActivity.class));*/

         /* Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // [START_EXCLUDE]
                            startActivity(new Intent(MainProfile.this,LoginActivity.class));
                            // [END_EXCLUDE]
                        }
                    });

            return true;*/
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        startActivity(new Intent(MainProfile.this,LoginActivity.class));
                        // [END_EXCLUDE]
                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}