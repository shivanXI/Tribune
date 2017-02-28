package p2_vaio.signin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn;
    private EditText email, password;
    private TextView tv;
    private ProgressDialog progressdailog;
    private FirebaseAuth firebaseauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("SIGN IN");
        firebaseauth = FirebaseAuth.getInstance();
        progressdailog = new ProgressDialog(this);
        if (firebaseauth.getCurrentUser() != null) {
            //start profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        btn = (Button) findViewById(R.id.btnregister1);
        email = (EditText) findViewById(R.id.editTextemail1);
        password = (EditText) findViewById(R.id.editTextpassword1);
        tv = (TextView) findViewById(R.id.tvsignin1);

        btn.setOnClickListener(this);
        tv.setOnClickListener(this);
    }

    private void userlogin() {

        String email1 = email.getText().toString().trim();
        String password1 = password.getText().toString().trim();

        if (TextUtils.isEmpty(email1)) {
            Toast.makeText(this, "Enter your Email", Toast.LENGTH_SHORT).show();
            return;
            //email is empty
        }
        if (TextUtils.isEmpty(password1)) {
            Toast.makeText(this, "Enter your Password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressdailog.setMessage("Logging In..");
        progressdailog.show();

        firebaseauth.signInWithEmailAndPassword(email1,password1).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressdailog.dismiss();
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Failed. Try again!", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public void onClick(View view) {
        if (view == btn) {
            userlogin();
        } else if (view == tv) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

}
