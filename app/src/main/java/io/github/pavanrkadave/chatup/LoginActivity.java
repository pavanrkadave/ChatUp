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

    //Toolbar for the app
    private Toolbar mLoginToolbar;

    //required Edittext Fields and Buttons
    private EditText mLoginEmail;
    private EditText mLoginPassword;
    private Button mLoginButton;

    //Progress Dialog for showing the loading screen
    private ProgressDialog mLoginProgress;

    //Firebase auth variable.
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Instance of firebase auth.
        mAuth = FirebaseAuth.getInstance();

        //ToolBar For the Login Activity
        mLoginToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(mLoginToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");

        //Setting Up the layout
        mLoginEmail = (EditText) findViewById(R.id.login_email);
        mLoginPassword = (EditText) findViewById(R.id.login_password);
        mLoginButton = (Button) findViewById(R.id.login_button);

        //Progress Dialog
        mLoginProgress = new ProgressDialog(this);

        //ClickListeners
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login_email = mLoginEmail.getText().toString().trim();
                String login_password = mLoginPassword.getText().toString().trim();

                if (!TextUtils.isEmpty(login_email) || !TextUtils.isEmpty(login_password)) {

                    //Showing the progress dialog when logging in
                    mLoginProgress.setTitle("Loging In..");
                    mLoginProgress.setMessage("Please Wait While We Check Your Credentials!");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    //Logging in user using firebase login with email and password
                    login_user(login_email, login_password);
                }
            }
        });
    }

    //method to log in user using email and password
    private void login_user(String login_email, String login_password) {

        mAuth.signInWithEmailAndPassword(login_email, login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    //If the login is successful dismiss the progress dialog.
                    mLoginProgress.dismiss();
                    //Send the intent to mainActivity if the user is successfully logged in.
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    //Kill the login activity after successful login.
                    finish();
                } else {
                    //If the login fails then hide the progress dialog with a toast message.
                    mLoginProgress.hide();
                    Toast.makeText(LoginActivity.this, "Error Logging In.. " + String.valueOf(task),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
