package com.example.makechatsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.makechatsapp.Adapters.RoomsAdapter;
import com.example.makechatsapp.Base.BaseActivity;
import com.example.makechatsapp.FireBaseUtils.MyDataBase;
import com.example.makechatsapp.Model.Room;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    protected RecyclerView recyclerView;
    RoomsAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        adapter=new RoomsAdapter(null);
        layoutManager=new LinearLayoutManager(activity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setOnItemClickListener(new RoomsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, Room room) {
                ChatThread.currentRoom=room;
                startActivity(new Intent(activity,ChatThread.class));
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, AddChatRoom.class));
            }
        });
        MyDataBase.getRoomsBranch()
                .addValueEventListener(roomsValueEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyDataBase.getRoomsBranch().removeEventListener(roomsValueEventListener);

    }

    ValueEventListener roomsValueEventListener= new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<Room> rooms=new ArrayList<>();
            for(DataSnapshot itemData :dataSnapshot.getChildren()){
                Room room = itemData.getValue(Room.class);
                rooms.add(room);

            }
            adapter.changeData(rooms);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.logout){
            logoutUser();
            startActivity(new Intent(activity,Login.class));

        }
        return super.onOptionsItemSelected(item);
    }


    public void logoutUser () {
        getStringValue("email");
        getStringValue("password");
        clearStringValue("email");
        clearStringValue("password");

    }


}
