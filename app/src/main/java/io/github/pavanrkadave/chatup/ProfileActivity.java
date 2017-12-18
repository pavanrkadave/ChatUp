package io.github.pavanrkadave.chatup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private TextView mDisplayId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mDisplayId = (TextView) findViewById(R.id.profile_diplayName);
        String id = getIntent().getStringExtra("user_id");
        mDisplayId.setText(id);
    }
}
