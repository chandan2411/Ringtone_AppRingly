package com.ringly.customer_app.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.ringly.customer_app.R;
import com.ringly.customer_app.models.FeaturedCollectionModel;
import com.ringly.customer_app.views.activities.InfiniteScrollerActivity;

import java.util.ArrayList;
import java.util.List;

public class FeaturedCollectionAdapter extends RecyclerView.Adapter<FeaturedCollectionAdapter.ViewHolder> {

    Context context;
    private List<FeaturedCollectionModel> featuredCollectionModels=new ArrayList<>();
    private CircularProgressDrawable circularProgressDrawable;

    public FeaturedCollectionAdapter(Context context, List<FeaturedCollectionModel> featuredCollectionModels) {
        this.context = context;
        this.featuredCollectionModels = featuredCollectionModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_collection_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        circularProgressDrawable=new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        final FeaturedCollectionModel collectionModel=featuredCollectionModels.get(position);
        Glide.with(context)
                .load(collectionModel.getCoverPhoto().getUrls().getImage_regular())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(holder.FeaturedcollectionImg);

        String text= String.valueOf(featuredCollectionModels.get(position).getTotal_photos()) + " photos";
        holder.collectionName.setText(featuredCollectionModels.get(position).getTitle());
        holder.numTxt.setText(text);

        holder.FeaturedcollectionImg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, InfiniteScrollerActivity.class);
                i.putExtra("headerTxt",featuredCollectionModels.get(position).getTitle());
                i.putExtra("collectionId",featuredCollectionModels.get(position).getId());
                i.putExtra("history",2);
                i.putExtra("coverImg",featuredCollectionModels.get(position).getCoverPhoto().getUrls().getImage_regular());
                i.putExtra("curated",featuredCollectionModels.get(position).isCurated());
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void addCollections(List<FeaturedCollectionModel> newCollections) {
        if(newCollections!=null) {
            featuredCollectionModels.addAll(newCollections);
            notifyDataSetChanged();
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView FeaturedcollectionImg;
        TextView collectionName,numTxt;
        public ViewHolder(View itemView) {
            super(itemView);
            FeaturedcollectionImg=itemView.findViewById(R.id.featured_collection_img);
            collectionName=itemView.findViewById(R.id.collectionName);
            numTxt=itemView.findViewById(R.id.numTxt);
        }
    }
}
