package com.example.makechatsapp.FireBaseUtils;

import com.example.makechatsapp.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class UsersDao {

    public static void AddUser(User user,
                               OnSuccessListener onSuccessListener,
                               OnFailureListener onFailureListener){
       DatabaseReference userNode= MyDataBase.getUsersBranch()
                .push();
       user.setId(userNode.getKey());
       userNode.setValue(user)
               .addOnSuccessListener(onSuccessListener)
               .addOnFailureListener(onFailureListener);

    }

    public static Query getUsersByEmail(String email){
        return  MyDataBase.getUsersBranch()
                .orderByChild("email")
                .equalTo(email);
    }
}
