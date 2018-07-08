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

import java.util.ArrayList;

public class ProblemsAdapter extends RecyclerView.Adapter<ProblemsAdapter.ViewHolder>{
    ArrayList<Problem> list;

    public static interface ProblemsClickListener{
        void onClick(Problem problem);
    }

    ProblemsClickListener listener;

    public ProblemsClickListener getListener() {
        return listener;
    }

    public void setListener(ProblemsClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView mCardView;
        ViewHolder(CardView c){
            super(c);
            mCardView = c;
        }
    }

    public ProblemsAdapter(ArrayList<Problem> problems) {
        super();
        list = problems;
    }

    @Override
    public ProblemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView c;
        c = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.problems_card,parent,false);
        return new ViewHolder(c);
    }

    @Override
    public void onBindViewHolder(ProblemsAdapter.ViewHolder holder, final int position) {
                    CardView cardView = holder.mCardView;
                    ImageView image = cardView.findViewById(R.id.info_image);
                    if(list.get(position).getType() == MainActivity.TRASH) {
                        image.setImageResource(R.drawable.ic_trash_icon);
                    }else
                        image.setImageResource(R.drawable.ic_grass_icon);
                    TextView textView = cardView.findViewById(R.id.info_text_title);
                    textView.setText(list.get(position).getDescription());
                    textView = cardView.findViewById(R.id.info_text_subtitle);
                    if(list.get(position).hasStreet())
                        textView.setText(list.get(position).getStreet());
                    else
                        textView.setText("СТАВРОПОЛЬЯ");
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
