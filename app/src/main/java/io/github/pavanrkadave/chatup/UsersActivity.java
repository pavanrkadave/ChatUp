package io.github.pavanrkadave.chatup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class UsersActivity extends AppCompatActivity {

    //Toolbar
    private Toolbar mUsersToolbar;

    //recyclerView
    private RecyclerView mUsersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mUsersToolbar = (Toolbar) findViewById(R.id.users_appbar);
        setSupportActionBar(mUsersToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsersList = (RecyclerView) findViewById(R.id.users_list);


    }
}
