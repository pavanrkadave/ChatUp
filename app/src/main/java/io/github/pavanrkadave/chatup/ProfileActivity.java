package io.github.pavanrkadave.chatup;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private ImageView mProfileImg;

    private TextView mProfileName, mProfileStatus, mProfileFriendsCount;
    private Button mProfileSendRequest, mProfleDeclineBtn;

    private ProgressDialog mProgressDialog;

    private DatabaseReference mUsersDatabase;
    private DatabaseReference mFriendReqDatabase;
    private FirebaseUser mCurrentUser;

    private String mCurrent_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mProgressDialog = new ProgressDialog(ProfileActivity.this);
        mProgressDialog.setTitle("Loading User Data");
        mProgressDialog.setMessage("Please Wait While We Load User Data");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        final String user_id = getIntent().getStringExtra("user_id");

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        mProfileImg = (ImageView) findViewById(R.id.profile_image);
        mProfileName = (TextView) findViewById(R.id.profile_displayName);
        mProfileStatus = (TextView) findViewById(R.id.profile_status);
        mProfileFriendsCount = (TextView) findViewById(R.id.profile_totalFriends);
        mProfileSendRequest = (Button) findViewById(R.id.profile_send_request_btn);

        mCurrent_state = "not_friends";

        mProfileSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProfileSendRequest.setEnabled(false);


                //--------------------Not Friends State---------------------------------------------

                if (mCurrent_state.equals("not_friends")) {

                    mFriendReqDatabase.child(mCurrentUser.getUid()).child(user_id).child("request_type")
                            .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                mFriendReqDatabase.child(user_id).child(mCurrentUser.getUid()).child("request_type")
                                        .setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(ProfileActivity.this, "Successfully Sent Request!",
                                                Toast.LENGTH_SHORT).show();
                                        mProfileSendRequest.setEnabled(true);
                                        mCurrent_state = "request_sent";
                                        mProfileSendRequest.setText("Cancel Request");
                                    }
                                });

                            } else {
                                Toast.makeText(ProfileActivity.this, "Failed Sending Request",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                //---------------------------------Cancel Request State-----------------------

                if (mCurrent_state.equals("request_sent")) {
                    mFriendReqDatabase.child(mCurrentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFriendReqDatabase.child(user_id).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    mProfileSendRequest.setEnabled(true);
                                    mCurrent_state = "not_friends";
                                    mProfileSendRequest.setText("Send Friend Request");
                                }
                            });
                        }
                    });
                }
            }
        });

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String displayName = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();

                mProfileName.setText(displayName);
                mProfileStatus.setText(status);
                mProfileFriendsCount.setText("Total Friends : 0  |  Mutual Friends : 0 ");
                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.account).into(mProfileImg);


                //-------Friends List/Request Feature---------
                mFriendReqDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild(user_id)) {
                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();

                            if (req_type.equals("received")) {

                                mProfileSendRequest.setEnabled(true);
                                mCurrent_state = "req_received";
                                mProfileSendRequest.setText("Accept Friend Request");

                            } else if (req_type.equals("sent")) {

                                mCurrent_state = "req_sent";
                                mProfileSendRequest.setText("Cancel Friend Request");

                            }
                        }
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
