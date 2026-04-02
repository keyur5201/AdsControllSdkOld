package com.adsv2.ads.control.sdk.recycler;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.adsv2.ads.control.sdk.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.VH> {

    private Activity activity;
    private final ArrayList<ItemModel> imagesList;

    public MyAdapter(Activity activity, ArrayList<ItemModel> imagesList) {
        this.activity = activity;
        this.imagesList = imagesList;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new VH(view);
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        ItemModel model = (ItemModel) imagesList.get(position);
        String baseUrl = "https://raw.githubusercontent.com/keyur5201/colorcaller/main/ImageTheme/";
        Glide.with(activity).load(baseUrl + model.getPath()).into(holder.image);
    }

    static class VH extends RecyclerView.ViewHolder {
        private ImageView image;

        VH(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}