package com.example.makechatsapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.makechatsapp.Base.BaseActivity;
import com.example.makechatsapp.FireBaseUtils.MessagesDao;
import com.example.makechatsapp.FireBaseUtils.MyDataBase;
import com.example.makechatsapp.FireBaseUtils.UsersDao;
import com.example.makechatsapp.Model.Message;
import com.example.makechatsapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration extends BaseActivity implements View.OnClickListener {

    protected TextInputLayout userName;
    protected TextInputLayout email;
    protected TextInputLayout password;
    protected Button register;
    protected TextView login;
    protected CircleImageView imageView;
    public Uri profileIMG;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_registration);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register) {
            String sUsername = userName.getEditText().getText().toString();
            if (sUsername.trim().equals("")) {
                userName.setError(getString(R.string.required));
                return;
            }
            userName.setError(null);
            String sPassword = password.getEditText().getText().toString();
            if (sPassword.trim().equals("")) {
                password.setError(getString(R.string.required));
                return;

            } else if (sPassword.length() < 6) {
                password.setError(getString(R.string.min_6_chars));
                return;

            }
            password.setError(null);

            String sEmail = email.getEditText().getText().toString();
            if (!isEmailValid(sEmail)) {
                email.setError(getString(R.string.not_valid));
                return;

            }
            email.setError(null);

            User user = new User(sUsername, sPassword, sEmail,profileIMG.toString());
            uploadFile(user);

        } else if (view.getId() == R.id.login) {
            startActivity(new Intent(activity,Login.class));
            finish();
        }else if (view.getId() == R.id.profileIMG){
            selectImage();
        }
    }

    public void registerUser(final User user) {
        DataHolder.currentUser = user;
        showProgressBar(R.string.loading, R.string.please_wait);
        UsersDao.getUsersByEmail(user.getEmail())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        hideProgressBar();
                        if (dataSnapshot.hasChildren()) {
                            showMessage(R.string.error, R.string.email_registered_before);
                        } else {
                            showProgressBar(R.string.loading, R.string.please_wait);
                            UsersDao.AddUser(user, onUserAdded, onUserAddFail);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        hideProgressBar();
                        showMessage(getString(R.string.error), databaseError.getMessage());

                    }
                });


    }

    boolean isEmailValid(String email) {
        if (!TextUtils.isEmpty(email) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true;
        return false;

    }

    OnSuccessListener onUserAdded = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            hideProgressBar();
            saveStringValue("email",DataHolder.currentUser.getEmail());
            saveStringValue("password",DataHolder.currentUser.getPassword());


            showConfirmationMessage(R.string.success, R.string.user_added, R.string.ok,
                    new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            startActivity(new Intent(activity, HomeActivity.class));
                            finish();

                        }
                    });
        }
    };
    OnFailureListener onUserAddFail = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            hideProgressBar();
            showMessage(getString(R.string.fail), e.getLocalizedMessage());

        }
    };


    public void selectImage() {
        final CharSequence[] options = {"from Camera", "from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Choose photo ?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("from Camera")) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, 0);
                } else if (options[item].equals("from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            //camera
            if (requestCode == 0) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                profileIMG = getImageUri(activity, photo);
                imageView.setImageURI(profileIMG);
                Log.e("TAG", profileIMG.toString());
                //gallery
            } else if (requestCode == 2) {
                profileIMG = data.getData();
                imageView.setImageURI(profileIMG);
                Log.e("TAG", profileIMG.toString());
            }
        }
    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext
                .getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }




    private void initView() {
        userName = (TextInputLayout) findViewById(R.id.user_name);
        email = (TextInputLayout) findViewById(R.id.email);
        password = (TextInputLayout) findViewById(R.id.password);
        imageView = findViewById(R.id.profileIMG);
        imageView.setOnClickListener(Registration.this);
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(Registration.this);
        login = (TextView) findViewById(R.id.login);
        login.setOnClickListener(Registration.this);
    }

    private void uploadFile(final User user) {

        if (user.getProfileIMG() != null) {
            StorageReference fileReference = MyDataBase.getUserBranchsIMG().child(user.getUserName());
            mUploadTask = fileReference.putFile(Uri.parse(user.getProfileIMG()))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>(){

                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    Log.e("TAG:", "the url is: " + url);
                                    user.setProfileIMG(url);

                                    registerUser(user);

                                }
                            });


                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });



        }
        else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}
