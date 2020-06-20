package com.example.makechatsapp.Adapters.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.makechatsapp.R;

/**
 * Created by Mohamed Nabil Mohamed (Nobel) on 2/26/2019.
 * byte code SA
 * m.nabil.fci2015@gmail.com
 */
public class TextMessageViewHolder extends RecyclerView.ViewHolder {

    public TextView senderName;
    public TextView content;
    public ImageView profile;
    public TextView time;
    public ImageView msgIMG;

    public TextMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        senderName =itemView.findViewById(R.id.sender_name);
        content=itemView.findViewById(R.id.content);
        time=itemView.findViewById(R.id.time);
        profile = itemView.findViewById(R.id.profile);
        msgIMG = itemView.findViewById(R.id.imgMsg);

    }
}
