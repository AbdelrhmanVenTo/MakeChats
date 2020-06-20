package com.example.makechatsapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.makechatsapp.Model.Room;
import com.example.makechatsapp.R;

import java.util.List;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.ViewHolder> {
    List<Room> rooms;

    public RoomsAdapter(List<Room> rooms) {
        this.rooms = rooms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_room,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int pos) {


        final Room room =rooms.get(pos);
        viewHolder.name.setText(room.getName());
        viewHolder.desc.setText(room.getDetails());
        if(onItemClickListener!=null)
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(pos,room);
                }
            });

    }

  public void  changeData(List<Room> rooms){
        this.rooms=rooms;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(rooms==null)return 0;
        return rooms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            desc=itemView.findViewById(R.id.details);
        }
    }
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int pos, Room room);
    }
}
