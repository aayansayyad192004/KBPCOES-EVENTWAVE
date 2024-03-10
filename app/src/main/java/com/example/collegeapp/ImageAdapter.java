package com.example.collegeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{
    Context context;
    ArrayList<Integer> arrayList;
    OnItemClicklistener onItemClicklistener;

    public ImageAdapter(Context context, ArrayList<Integer> arrayList){
        this.context=context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.image_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(arrayList.get(position)).into(holder.imageView);
        holder.itemView.setOnClickListener(view -> onItemClicklistener.onClick(holder.imageView, arrayList.get(position)));
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.list_item_image);
        }
    }

    public void setOnItemClicklistener(OnItemClicklistener onItemClicklistener) {
        this.onItemClicklistener = onItemClicklistener;
    }

    public  interface OnItemClicklistener{
        void onClick(ImageView imageView, Integer url);
    }
}