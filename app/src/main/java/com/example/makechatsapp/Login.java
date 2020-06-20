package com.example.makechatsapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.makechatsapp.Base.BaseActivity;
import com.example.makechatsapp.FireBaseUtils.UsersDao;
import com.example.makechatsapp.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class Login extends BaseActivity implements View.OnClickListener {

    protected TextInputLayout email;
    protected TextInputLayout password;
    protected Button login;
    protected TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_login);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.login) {

            final String sEmail = email.getEditText().getText().toString();
            if (!isEmailValid(sEmail)) {
                email.setError(getString(R.string.not_valid));
                return;

            }
            email.setError(null);
            final String sPassword = password.getEditText().getText().toString();
            if (sPassword.trim().equals("")) {
                password.setError(getString(R.string.required));
                return;

            } else if (sPassword.length() < 6) {
                password.setError(getString(R.string.min_6_chars));
                return;

            }
            password.setError(null);

            showProgressBar(R.string.please_wait,R.string.loading);
            UsersDao.getUsersByEmail(sEmail)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            hideProgressBar();
                            if (dataSnapshot.hasChildren()) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    User user = data.getValue(User.class);
                                    if (sPassword.equals(user.getPassword())) {
                                        DataHolder.currentUser=user;
                                        saveStringValue("email",sEmail);
                                        saveStringValue("password",sPassword);
                                        startActivity(new Intent(activity, HomeActivity.class));
                                        finish();
                                        return;
                                    }
                                }
                                showMessage(R.string.error, R.string.email_or_password_not_valid);


                            } else {
                                showMessage(R.string.error, R.string.email_or_password_not_valid);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            hideProgressBar();
                            showMessage(getString(R.string.error), databaseError.getMessage());

                        }
                    });

        } else if (view.getId() == R.id.register) {
            startActivity(new Intent(activity,Registration.class));
            finish();
        }


    }


    boolean isEmailValid(String email) {
        if (!TextUtils.isEmpty(email) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true;
        return false;

    }

    private void initView() {
        email = (TextInputLayout) findViewById(R.id.email);
        password = (TextInputLayout) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(Login.this);
        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(Login.this);
    }


}
