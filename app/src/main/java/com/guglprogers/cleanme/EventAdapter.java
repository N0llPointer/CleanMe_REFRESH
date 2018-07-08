package com.guglprogers.cleanme;

import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder>{
    ArrayList<Problem> list;

    public static interface EventClickListener{
        void onClick(Problem problem);
    }

    EventClickListener listener;

    public EventClickListener getListener() {
        return listener;
    }

    public void setListener(EventClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView mCardView;
        ViewHolder(CardView c){
            super(c);
            mCardView = c;
        }
    }

    public EventAdapter(ArrayList<Problem> problems) {
        super();
        list = problems;
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c;
        c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card,parent,false);
        return new ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, final int position) {
        CardView cardView = holder.mCardView;
        ImageView image = cardView.findViewById(R.id.info_image_event);
        if(list.get(position).getType() == MainActivity.TRASH) {
            image.setImageResource(R.drawable.ic_trash_icon);
        }else
            image.setImageResource(R.drawable.ic_grass_icon);
        TextView textView = cardView.findViewById(R.id.info_text_title_event);
        textView.setText(list.get(position).getDescription());
        textView = cardView.findViewById(R.id.info_text_subtitle_event);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        textView.setText(sdf.format(list.get(0).getDate()));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
