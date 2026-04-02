package com.adsv2.ads.control.sdk.recycler;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adsv2.ads.control.sdk.R;
import com.adssdkmanager.rv_native.RvBaseAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerAdapter extends RvBaseAdapter {

    private Activity activity;
    private final ArrayList<Object> imagesList;

    public RecyclerAdapter(Activity activity, ArrayList<Object> imagesList) {
        super(activity);
        this.activity = activity;
        this.imagesList = imagesList;
    }

    @Override
    public int setItemLayout() {
        return R.layout.recycler_item;
    }

    @Override
    public void itemBind(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecyclerViewHolder viewHolder = (RecyclerViewHolder) holder;
        ItemModel model = (ItemModel) imagesList.get(position);
        String baseUrl = "https://raw.githubusercontent.com/keyur5201/colorcaller/main/ImageTheme/";
        Glide.with(activity).load(baseUrl + model.getPath()).into(viewHolder.image);
    }

    @Override
    public ArrayList<Object> arrayList() {
        return imagesList;
    }

    @Override
    public RecyclerView.ViewHolder viewHolder(View view) {
        return new RecyclerViewHolder(view);
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        public RecyclerViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }
}
