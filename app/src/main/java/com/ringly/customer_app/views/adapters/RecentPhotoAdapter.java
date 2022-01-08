package com.ringly.customer_app.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ringly.customer_app.models.WallPaperModel;
import com.ringly.customer_app.views.activities.ImageViewerActivity;
import com.ringly.customer_app.R;
import com.ringly.customer_app.views.activities.InfiniteScrollerActivity;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;


public class RecentPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
//    private List<PhotoModel> recentPhotos=new ArrayList<>();
    private List<WallPaperModel> recentPhotos=new ArrayList<>();
    private boolean clickToNext=false;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;
    public String link="https://images.unsplash.com/photo-1428908728789-d2de25dbd4e2?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=d00f965d7f845968dd35c3590094149f&auto=format&fit=crop&w=2250&q=80";
    CircularProgressDrawable circularProgressDrawable;

    /*public RecentPhotoAdapter(Context context, List<PhotoModel> recentPhotos, boolean clickToNext) {
        this.context = context;
        this.recentPhotos = recentPhotos;
        this.clickToNext = clickToNext;
    }*/

    public RecentPhotoAdapter(Context context, List<WallPaperModel> recentPhotos, boolean clickToNext) {
        this.context = context;
        this.recentPhotos = recentPhotos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_photos_item, parent, false);
            return new Recent_photo_holder(itemView);
        }else if(viewType==TYPE_FOOTER){
            View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_phoyos_footer_item,parent,false);
            return new FooterViewHolder(itemView);
        }else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        circularProgressDrawable=new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        if (holder instanceof FooterViewHolder){
            FooterViewHolder footerViewHolder=(FooterViewHolder)holder;

            Glide.with(context)
                    .load(link)
                    .centerCrop()
                    .into(footerViewHolder.backImg);


            footerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, InfiniteScrollerActivity.class);
                    intent.putExtra("history",1);
                    intent.putExtra("headerTxt","Recent Photos");
                    context.startActivity(intent);

                }
            });

        }else {

            KProgressHUD progressHUD = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(true)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f).show();
            final Recent_photo_holder recent_photo_holder=(Recent_photo_holder) holder;
//            PhotoModel photo=recentPhotos.get(position);
            WallPaperModel photo=recentPhotos.get(position);
            Glide.with(context)
//                    .load(photo.getUrls().getImage_regular())
                    .load(photo.getWallPaperFullImageUrl())
                    .centerCrop()
                    /*.placeholder(circularProgressDrawable)*/
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            if (progressHUD != null && progressHUD.isShowing()) {
                                progressHUD.dismiss();
                                clickToNext =false;
                            }
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (progressHUD != null && progressHUD.isShowing()) {
                                progressHUD.dismiss();
                            }
                            clickToNext = true;
                            return false;
                        }
                    })
                    .into(((Recent_photo_holder) holder).recentImg);

            recent_photo_holder.recentImg.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View view) {
                    if (!clickToNext)
                        return;
                    Intent intent=new Intent(context, ImageViewerActivity.class);
                    intent.putExtra("i",1);
                    intent.putExtra("id",photo.getWallPaperId());
                    intent.putExtra("Image",photo.getWallPaperFullImageUrl());
                    intent.putExtra("user",photo.getUploaderName());
                    intent.putExtra("location",photo.getUploadLocation());

                    recent_photo_holder.recentImg.setTransitionName("sharedTransition");
                    Pair<View,String> pair= Pair.create((View)recent_photo_holder.recentImg,recent_photo_holder.recentImg.getTransitionName());
                    ActivityOptionsCompat optionsCompat= ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,pair);
                    context.startActivity(intent,optionsCompat.toBundle());
                }
            });
        }
    }

//    @Override
//    public void onBindViewHolder(@NonNull RecentPhotoAdapter.Recent_photo_holder holder, int position) {
//        PhotoModel photo=recentPhotos.get(position);
//        Glide.with(context)
//                .load(photo.getUrls().getImage_regular())
//                .centerCrop()
//                .into(holder.recentImg);
//    }


    /*public void addPhotos(List<PhotoModel> newPhotos) {
        if(newPhotos!=null) {
            recentPhotos.addAll(newPhotos);
            notifyDataSetChanged();
        }
    }*/

    @Override
    public int getItemViewType(int position) {
        if (clickToNext) {
            if (position == recentPhotos.size() - 1) {
                return TYPE_FOOTER;
            }
            return TYPE_ITEM;
        }else {
            return TYPE_ITEM;
        }

    }

    @Override
    public int getItemCount() {
        return recentPhotos.size();
    }

    public class Recent_photo_holder extends RecyclerView.ViewHolder{
        ImageView recentImg;
        public Recent_photo_holder(View itemView) {
            super(itemView);
            recentImg=(itemView.findViewById(R.id.recentImg));
        }


    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        ImageView footerText,backImg;

        public FooterViewHolder(View view) {
            super(view);
            backImg=view.findViewById(R.id.backImg);
            footerText = view.findViewById(R.id.recentFooter);
        }
    }
}
