package com.example.makechatsapp;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.makechatsapp.Adapters.ChatThreadAdapter;
import com.example.makechatsapp.Base.BaseActivity;
import com.example.makechatsapp.FireBaseUtils.MessagesDao;
import com.example.makechatsapp.FireBaseUtils.MyDataBase;
import com.example.makechatsapp.Model.Message;
import com.example.makechatsapp.Model.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ChatThread extends BaseActivity implements View.OnClickListener {

    protected RecyclerView recyclerView;
    protected ImageButton send;
    protected EditText message;
    protected ImageButton sendIMG;
    ChatThreadAdapter adapter;
    LinearLayoutManager layoutManager;

    public String msgIMG = "";
    static Room currentRoom;
    Query messagesQuery;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_chat_thread);
        getSupportActionBar().setTitle(currentRoom.getName());
        initView();
        layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        adapter=new ChatThreadAdapter(null ,activity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        messagesQuery=MessagesDao.getMessagesByRoomId(currentRoom.getId());
        messagesQuery.addChildEventListener(messagesListener);

    }


    ChildEventListener messagesListener=new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot,
                                 @Nullable String s) {
            Message message=dataSnapshot.getValue(Message.class);
            //Log.e("message",message.getContent());
            adapter.addMessage(message);

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send) {
            String content=message.getText().toString();

            Message message = new Message();
            message.setContent(content);
            message.setSenderId(DataHolder.currentUser.getId());
            message.setSenderName(DataHolder.currentUser.getUserName());
            message.setRoomId(currentRoom.getId());
            message.setImgMSG("");
            message.setSenderIMG(DataHolder.currentUser.getProfileIMG());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            String currentTimeStamp=simpleDateFormat.format(new Date());
            message.setTimestamp(currentTimeStamp);
            MessagesDao
                    .sendMessage(message,onSuccessListener,onFailureListener);

        }else if (view.getId()==R.id.sendIMG){
            selectImage();
        }
    }

    OnSuccessListener onSuccessListener=new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            message.setText("");

        }
    };
    OnFailureListener onFailureListener=new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(activity, R.string.try_again, Toast.LENGTH_SHORT).show();

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
                Uri profileCamera = getImageUri(activity, photo);
                //msgIMG = getRealPathFromURICamera(profileCamera);
                uploadFile(profileCamera);
                Log.e("TAG", profileCamera.toString());
                //gallery
            } else if (requestCode == 2) {
                Uri profileGallery = data.getData();
                //msgIMG = getRealPathFromURI(profileGallery);
                uploadFile(profileGallery);
                Log.e("TAG", profileGallery.toString());
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
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        send = (ImageButton) findViewById(R.id.send);
        send.setOnClickListener(ChatThread.this);
        message = (EditText) findViewById(R.id.message);
        sendIMG = findViewById(R.id.sendIMG);
        sendIMG.setOnClickListener(ChatThread.this);
    }



    private void uploadFile(final Uri mImageUri) {

        if (mImageUri != null) {
            StorageReference fileReference = MyDataBase.getMessagesBranchsST().child(currentRoom.getName()+System.currentTimeMillis());
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>(){

                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    Log.e("TAG:", "the url is: " + url);
                                    Message message = new Message();
                                    message.setImgMSG(url);
                                    message.setSenderIMG(DataHolder.currentUser.getProfileIMG());
                                    message.setSenderId(DataHolder.currentUser.getId());
                                    message.setSenderName(DataHolder.currentUser.getUserName());
                                    message.setRoomId(currentRoom.getId());
                                    message.setContent("");
                                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
                                    String currentTimeStamp=simpleDateFormat.format(new Date());
                                    message.setTimestamp(currentTimeStamp);
                                    MessagesDao
                                            .sendMessage(message,onSuccessListener,onFailureListener);
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
