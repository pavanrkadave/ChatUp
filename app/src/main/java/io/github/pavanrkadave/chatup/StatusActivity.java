package io.github.pavanrkadave.chatup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private Toolbar mStatusToolbar;

    private EditText mStatus;
    private Button mSavebtn;

    //Progress Dialog
    private ProgressDialog mStatusProgress;

    //Firebase Database Setup
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mStatusToolbar = (Toolbar) findViewById(R.id.status_appbar);
        setSupportActionBar(mStatusToolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String status_value = getIntent().getStringExtra("status_value");
        Log.v("Current Status", " " + status_value);

        mStatus = (EditText) findViewById(R.id.status_input);
        mStatus.setText(status_value);
        mSavebtn = (Button) findViewById(R.id.status_save_btn);

        mSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mStatusProgress = new ProgressDialog(StatusActivity.this);
                mStatusProgress.setTitle("Saving Changes!");
                mStatusProgress.setMessage("Please Wait While the Status is Saved!");
                mStatusProgress.show();

                String status = mStatus.getText().toString().trim();
                mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mStatusProgress.dismiss();
                            Toast.makeText(StatusActivity.this, "Status Changed Successfully!", Toast.LENGTH_SHORT).show();
                            Intent settingsIntent = new Intent(StatusActivity.this, SettingsActivity.class);
                            startActivity(settingsIntent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "There Was Some Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}
