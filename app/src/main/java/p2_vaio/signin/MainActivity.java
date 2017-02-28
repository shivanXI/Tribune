package p2_vaio.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static android.R.attr.password;
import static android.widget.Toast.LENGTH_SHORT;
import static p2_vaio.signin.R.id.email;
import static p2_vaio.signin.R.id.google_signin;
import static p2_vaio.signin.R.id.profile_name;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private Button btnregister;
    private EditText etemail, etpassword,name;
    private TextView tvsignin;
    private ProgressDialog mProgressDialog;




    private ProgressDialog progressdailog;

    private FirebaseAuth firebaseauth;
    private SignInButton google,google_signin;
    private FirebaseAuth.AuthStateListener mAuthStateListener ;
    public static final int RC_SIGN_IN =0;
    private static final String TAG="MAIN_ACTIVITY";
    private GoogleApiClient mGoogleApiClient;
    private TextView profilename;




/*    @Override
    protected void onStart() {


        super.onStart();
        firebaseauth.addAuthStateListener(mAuthStateListener);
    }*/


    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        //updateUI(false);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        // [END_EXCLUDE]
                    }
                });
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("LOADING");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener !=null)
            firebaseauth.removeAuthStateListener(mAuthStateListener);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

         google_signin = (SignInButton)findViewById(R.id.google_signin);

        profilename = (TextView)findViewById(R.id.profile_name);




        firebaseauth = FirebaseAuth.getInstance();
        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user =firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                }
            }
        };




        //findViewById(R.id.google).setOnClickListener(this);


        if(firebaseauth.getCurrentUser() !=null)
        {
            //start profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        progressdailog = new ProgressDialog(this);
       // google=(SignInButton)findViewById(R.id.google);
        btnregister=(Button)findViewById(R.id.btnregister);
        etemail=(EditText)findViewById(R.id.editTextemail);
        etpassword=(EditText)findViewById(R.id.editTextpassword);
        tvsignin=(TextView)findViewById(R.id.tvsignin);
        name=(EditText)findViewById(R.id.name);

        btnregister.setOnClickListener(this);
        tvsignin.setOnClickListener(this);
        google_signin.setOnClickListener(this);


    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Invalid Credential..!!", Toast.LENGTH_LONG).show();
    }

    private void registeruser() {
        String email = etemail.getText().toString().trim();
        String password = etpassword.getText().toString().trim();
        String n = name.getText().toString().trim();


        if (!validate()) {
            onSignupFailed();
        } else {

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter your Email", Toast.LENGTH_SHORT).show();
                return;
                //email is empty

            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter your Password", Toast.LENGTH_SHORT).show();

                return;
                //password is empty
            }
            //progressbar
            progressdailog.setMessage("Registering User..");
            progressdailog.show();
            firebaseauth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            } else {
                                progressdailog.dismiss();
                                Toast.makeText(MainActivity.this, "Failed. Try again!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }


    public boolean validate() {
        boolean valid = true;

        String n = name.getText().toString();
        String email = etemail.getText().toString();
        String password = etpassword.getText().toString();

        if (n.isEmpty() || name.length() < 3) {
            name.setError("at least 3 characters");
            valid = false;
        } else {
            name.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etemail.setError("enter a valid email address");
            valid = false;
        } else {
            etemail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            etpassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            etpassword.setError(null);
        }

        return valid;
    }
    @Override
    public void onClick(View view) {

        if(view==btnregister)
        {registeruser();}

        else if(view==tvsignin)
        {
            startActivity(new Intent(this, LoginActivity.class));
        }
        else if (view == google_signin)
        {
            signIn();
        }
/*        else if(view.getId()==R.id.google)
        {
            signIn();

        }*/
            //login activity

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            startActivity(new Intent(getApplicationContext(), MainProfile.class));
           // profilename.setText("signed in as "+ acct.getDisplayName());

            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
            Toast.makeText(this,"failed",Toast.LENGTH_LONG).show();
        }
    }




    /*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess())
            {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
            else
            {
                Log.d(TAG,"google login failed");
            }
        }

    }*/
    private void firebaseAuthWithGoogle(GoogleSignInAccount account)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseauth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Toast.makeText(MainActivity.this, "ho gaya!", Toast.LENGTH_SHORT).show();
                Log.d("AUTH","Sign in with credential:on complete: "+task.isSuccessful());
                finish();
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

            }
        });
    }

    private void signIn() {
        Intent signinintent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signinintent , RC_SIGN_IN);
    }

/*    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }*/

/*    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d(TAG,"connection failed");
    }*/
}
