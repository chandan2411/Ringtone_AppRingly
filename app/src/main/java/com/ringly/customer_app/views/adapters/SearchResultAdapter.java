package com.ringly.customer_app.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.ringly.customer_app.views.activities.ImageViewerActivity;
import com.ringly.customer_app.R;
import com.ringly.customer_app.models.ResultsArray;

import java.util.List;


public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

   Context context;
   private List<ResultsArray> resultsArrays;
   CircularProgressDrawable circularProgressDrawable;

    public SearchResultAdapter(Context context, List<ResultsArray> resultsArray) {
        this.context = context;
        this.resultsArrays = resultsArray;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_photos_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        ResultsArray searchResultlist=resultsArrays.get(position);
        circularProgressDrawable=new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        Glide.with(context)
                .load(searchResultlist.getUrls().getImage_regular())
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, ImageViewerActivity.class);
                i.putExtra("i",1);
                i.putExtra("id",resultsArrays.get(position).getId());
                i.putExtra("profileImage",resultsArrays.get(position).getUser().getProfileImage().getImage_large());
                i.putExtra("Image",resultsArrays.get(position).getUrls().getImage_regular());
                i.putExtra("user",resultsArrays.get(position).getUser().getName());
                i.putExtra("username",resultsArrays.get(position).getUser().getUsername());
                i.putExtra("location",resultsArrays.get(position).getUser().getLocation());
                i.putExtra("bio",resultsArrays.get(position).getUser().getBio());
                i.putExtra("instaName",resultsArrays.get(position).getUser().getInstaName());
                i.putExtra("total_photos",resultsArrays.get(position).getUser().getTotal_photos());
                holder.imageView.setTransitionName("sharedTransition");
                Pair<View,String> pair= Pair.create((View)holder.imageView,holder.imageView.getTransitionName());
                ActivityOptionsCompat optionsCompat= ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,pair);
                context.startActivity(i,optionsCompat.toBundle());
            }
        });
    }

    public void addPhotos(List<ResultsArray> newPhotos) {
        if(newPhotos!=null) {
            resultsArrays.addAll(newPhotos);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        return resultsArrays.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.recentImg);
        }
    }
}
