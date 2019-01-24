package com.rsmapps.selfieall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.rsmapps.selfieall.R;

/**
 * Created by mnafian on 6/16/15.
 */
public class FilterAdapterFactory extends RecyclerView.Adapter<FilterAdapterFactory.FilterHolder> {

    private String itemData[] = {
            "No Effect",
            "Autofix",
            "BlackAndWhite",
            "Brightness",
            "Contrast",
            "CrossProcess",
            "Documentary",
            "Duotone",
            "Fillight",
            "FishEye",
            "Grain",
            "Grayscale",
            "Lomoish",
            "Negative",
            "Posterize",
            "Sepia",
            "Sharpen",
            "Temperature",
            "TintEffect",
            "Vignette"};

    public FilterAdapterFactory(Context mContext) {
        super();
    }

    @Override
    public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.imf_filter_item, parent, false);
        FilterHolder viewHolder = new FilterHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterHolder holder, int position) {
        String val = itemData[position];
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(2)
                .fontSize(28)
                .endConfig()
                .rect();

        TextDrawable drawable = builder.build(val, color1);
        holder.imFilter.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return itemData.length;
    }

    public class FilterHolder extends RecyclerView.ViewHolder {
        public ImageView imFilter;
        public FilterHolder(View itemView) {
            super(itemView);
            imFilter = (ImageView) itemView.findViewById(R.id.effectsviewimage_item);
        }
    }
}
