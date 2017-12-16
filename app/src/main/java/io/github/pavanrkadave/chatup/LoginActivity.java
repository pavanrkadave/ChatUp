package io.github.pavanrkadave.chatup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Toolbar mLoginToolbar;

    private EditText mLoginEmail;
    private EditText mLoginPassword;
    private Button mLoginButton;

    private ProgressDialog mLoginProgress;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mLoginToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(mLoginToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");

        mLoginEmail = (EditText) findViewById(R.id.login_email);
        mLoginPassword = (EditText) findViewById(R.id.login_password);
        mLoginButton = (Button) findViewById(R.id.login_button);

        mLoginProgress = new ProgressDialog(this);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login_email = mLoginEmail.getText().toString().trim();
                String login_password = mLoginPassword.getText().toString().trim();

                if (!TextUtils.isEmpty(login_email) || !TextUtils.isEmpty(login_password)) {

                    mLoginProgress.setTitle("Loging In..");
                    mLoginProgress.setMessage("Please Wait While We Check Your Credentials!");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();
                    login_user(login_email, login_password);
                }
            }
        });
    }

    private void login_user(String login_email, String login_password) {

        mAuth.signInWithEmailAndPassword(login_email, login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    mLoginProgress.dismiss();
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    mLoginProgress.hide();
                    Toast.makeText(LoginActivity.this, "Error Logging In.. " + String.valueOf(task),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
