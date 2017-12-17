package io.github.pavanrkadave.chatup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //Firebase Auth variable
    private FirebaseAuth mAuth;

    //Toolbar for the activity
    private Toolbar mToolbar;

    //Viewpager to display the fragments
    private ViewPager mViewPager;
    //ViewPagerAdapter Class
    private SectionsPagerAdapter mSectionsPagerAdapter;

    //tablayout to display the title of the fragment.
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instance of the firebase auth variable
        mAuth = FirebaseAuth.getInstance();

        //Setting up toolbar and tabLayout.
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Chat Up");
        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);


        //Tabs for the screen
        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
        //Setting adapter to the viewpager.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        //Setting up View pager and attaching the tablayout to it.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            //If the user is not logged in then send to start page.
            sendToStart();
        }
    }

    //method to send intent to the start page.
    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

    /**
     * @param menu is the menu.
     * @returns a menu with 3 items.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    //Identifies the click on menu item and sends intents accordingly.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            //Login Intent
            case R.id.main_logut_button:
                FirebaseAuth.getInstance().signOut();
                sendToStart();
                break;
            //intent to settings activity.
            case R.id.main_settings_button:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;

            //Intent to all users activity.
            case R.id.main_all_button:
                Intent userIntent = new Intent(MainActivity.this, UsersActivity.class);
                startActivity(userIntent);
                break;
        }
        return true;
    }
}
