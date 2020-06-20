package com.example.makechatsapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.makechatsapp.Base.BaseActivity;
import com.example.makechatsapp.FireBaseUtils.UsersDao;
import com.example.makechatsapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Splash extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String email=getStringValue("email");
        final String password=getStringValue("password");

        if(email==null||password==null){
            gotoLoginActivity();
        }else {
            UsersDao.getUsersByEmail(email)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    User user = data.getValue(User.class);
                                    if (password.equals(user.getPassword())) {
                                        DataHolder.currentUser=user;
                                        gotoHomeActivity();
                                        return;
                                    }
                                }
                                gotoLoginActivity();

                            } else {
                                gotoLoginActivity();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            gotoLoginActivity();
                        }
                    });

        }

    }

    public void gotoHomeActivity(){
        startActivity(new Intent(activity,HomeActivity.class));

    }
    public void gotoLoginActivity(){
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(activity,Login.class));
                    }
                },2000);

    }
}


