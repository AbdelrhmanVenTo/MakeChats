package com.example.makechatsapp;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.makechatsapp.Base.BaseActivity;
import com.example.makechatsapp.FireBaseUtils.RoomsDao;
import com.example.makechatsapp.Model.Room;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddChatRoom extends BaseActivity implements View.OnClickListener {

    protected TextInputLayout name;
    protected TextInputLayout desc;
    protected Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_add_chat_room);
        initView();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add) {
            String sname=name.getEditText().getText().toString();
            String sDesc= desc.getEditText().getText().toString();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            String currentTimeStamp=simpleDateFormat.format(new Date());
            Room room = new Room(sname,sDesc,currentTimeStamp);

            showProgressBar(R.string.loading,R.string.please_wait);
            RoomsDao.AddChatRoom(room,onRoomAdded,onRoomAddFail);

        }

    }

    OnSuccessListener onRoomAdded=new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            hideProgressBar();

            showConfirmationMessage(R.string.success, R.string.roomAdded, R.string.ok,
                    new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            //   startActivity(new Intent(activity,HomeActivity.class));
                            finish();

                        }
                    });
        }
    };
    OnFailureListener onRoomAddFail= new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            hideProgressBar();
            showMessage(getString(R.string.fail),e.getLocalizedMessage());

        }
    };
    private void initView() {
        name = (TextInputLayout) findViewById(R.id.name);
        desc = (TextInputLayout) findViewById(R.id.desc);
        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(AddChatRoom.this);
    }
}

