package com.example.makechatsapp.FireBaseUtils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyDataBase {

   private static FirebaseDatabase firebaseDatabase;

   static FirebaseDatabase getInstance(){
       if(firebaseDatabase==null)
           firebaseDatabase= FirebaseDatabase.getInstance();

       return firebaseDatabase;
   }

   private static final String Users="users";
   public static DatabaseReference getUsersBranch(){
       return getInstance().getReference(Users);
   }

   private static final String Rooms="rooms";
   public static DatabaseReference getRoomsBranch(){
       return getInstance().getReference(Rooms);
   }
   static final String Messages="messages";
   public static DatabaseReference getMessagesBranch(){
       return getInstance().getReference(Messages);
   }

    public static StorageReference getMessagesBranchsST() {
        return FirebaseStorage.getInstance().getReference(Messages);
    }
    public static StorageReference getUserBranchsIMG() {
        return FirebaseStorage.getInstance().getReference(Users);
    }
}
