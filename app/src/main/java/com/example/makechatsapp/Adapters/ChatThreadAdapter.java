package com.example.makechatsapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.makechatsapp.Adapters.ViewHolders.TextMessageViewHolder;
import com.example.makechatsapp.DataHolder;
import com.example.makechatsapp.Model.Message;
import com.example.makechatsapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed Nabil Mohamed (Nobel) on 2/26/2019.
 * byte code SA
 * m.nabil.fci2015@gmail.com
 */
public class ChatThreadAdapter extends RecyclerView.Adapter<TextMessageViewHolder> {

    List<Message> messages;
    Context context;

    public ChatThreadAdapter(List<Message> messages , Context context)
    {
        this.messages = messages;
        this.context = context;
    }

    private final static int INCOMING=1;
    private final static int OUTGOING=2;
    private final static int OUTGOINGIMG=3;
    private final static int INGOINGIMG=4;

    @Override
    public int getItemViewType(int position) {
        Message message=messages.get(position);
        if(message.getSenderId().equals(DataHolder.currentUser.getId())){
            if (!message.getImgMSG().equals("")){
                return OUTGOINGIMG;
            }else {
                return OUTGOING;
            }
        }else if (!message.getSenderId().equals(DataHolder.currentUser.getId())){
            if (!message.getImgMSG().equals("")){
                return INGOINGIMG;
            }
        }
        return INCOMING;
    }

    @NonNull
    @Override
    public TextMessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view=null;
        if(viewType==INCOMING){
            view=LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_item_message_incoming,viewGroup,false);
        }else if(viewType==OUTGOING){
            view=LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_item_message_outgoing,viewGroup,false);

        }else if(viewType==OUTGOINGIMG){
            view=LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_item_message_outgoing_img,viewGroup,false);

        }else if(viewType==INGOINGIMG){
            view=LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_item_message_incoming_img,viewGroup,false);

        }
        return new TextMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextMessageViewHolder textMessageViewHolder, int pos) {
        Message message=messages.get(pos);
        int viewType=getItemViewType(pos);

        textMessageViewHolder.time.setText(message.getTimestamp());

        if (viewType == INGOINGIMG){
            textMessageViewHolder.senderName.setText(message.getSenderName());
            Glide
                    .with(context)
                    .load(message.getSenderIMG())
                    .into(textMessageViewHolder.profile);
            textMessageViewHolder.msgIMG.setVisibility(View.VISIBLE);
            Glide
                    .with(context)
                    .load(message.getImgMSG())
                    .into(textMessageViewHolder.msgIMG);
        }


        if(viewType==INCOMING) {
            textMessageViewHolder.senderName.setText(message.getSenderName());
            textMessageViewHolder.content.setText(message.getContent());
            Glide
                    .with(context)
                    .load(message.getSenderIMG())
                    .into(textMessageViewHolder.profile);
        }



        if (viewType == OUTGOINGIMG){
            Glide
                    .with(context)
                    .load(message.getSenderIMG())
                    .into(textMessageViewHolder.profile);

            textMessageViewHolder.msgIMG.setVisibility(View.VISIBLE);

            Glide
                    .with(context)
                    .load(message.getImgMSG())
                    .into(textMessageViewHolder.msgIMG);
        }

        if (viewType == OUTGOING){
            textMessageViewHolder.content.setText(message.getContent());
            Glide
                    .with(context)
                    .load(message.getSenderIMG())
                    .into(textMessageViewHolder.profile);
        }



        }

    public void addMessage(Message message){
        if(messages==null)messages=new ArrayList<>();

        messages.add(message);
        notifyItemInserted(messages.size()-1);
    }

    @Override
    public int getItemCount() {
        if(messages==null)
            return 0;

        return messages.size() ;
    }

}
