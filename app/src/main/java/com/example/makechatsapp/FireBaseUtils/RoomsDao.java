package com.example.makechatsapp.FireBaseUtils;

import com.example.makechatsapp.Model.Room;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

public class RoomsDao {

    public static void AddChatRoom(Room room,
                                   OnSuccessListener onSuccessListener,
                                   OnFailureListener onFailureListener){
       DatabaseReference roomNode= MyDataBase.getRoomsBranch()
                .push();
       room.setId(roomNode.getKey());
       roomNode.setValue(room)
               .addOnSuccessListener(onSuccessListener)
               .addOnFailureListener(onFailureListener);
    }
}
