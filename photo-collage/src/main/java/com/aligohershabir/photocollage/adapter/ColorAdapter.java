package com.aligohershabir.photocollage.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aligohershabir.photocollage.model.Color;
import com.aligohershabir.photocollage.R;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder>{

    private Activity context;
    private ArrayList<Color> colors;
    private OnItemClickedListener onItemClickedListener;

    public ColorAdapter(Activity context, ArrayList<Color> colors, OnItemClickedListener onItemClickedListener) {
        this.context = context;
        this.colors = colors;
        this.onItemClickedListener = onItemClickedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item_insta_pic_color_chooser,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final String hex = colors.get(holder.getAdapterPosition()).getHex();

        if(colors.get(holder.getAdapterPosition()).getName().equals(Color.COLOR_TRANS))
            holder.vColor.setBackgroundResource(R.drawable.ic_text_no_color);
        else
            holder.vColor.setBackgroundColor(android.graphics.Color.parseColor(hex));

        holder.cvColorItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickedListener.onColorClicked(holder.getAdapterPosition(), hex);
            }
        });
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View vColor;
        private CardView cvColorItem;

        public ViewHolder(View itemView) {
            super(itemView);

            vColor = itemView.findViewById(R.id.vColor);
            cvColorItem = itemView.findViewById(R.id.cvColorItem);
        }
    }

    public interface OnItemClickedListener{
        void onColorClicked(int pos, String hex);
    }
}
