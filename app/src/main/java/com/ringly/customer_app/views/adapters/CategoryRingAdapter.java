package com.ringly.customer_app.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ringly.customer_app.R;
import com.ringly.customer_app.models.RingtoneCategoryModel;
import com.ringly.customer_app.views.activities.HomeActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryRingAdapter extends RecyclerView.Adapter<CategoryRingAdapter.ImageViewHolder> {
    private Context mContext;
    private List<RingtoneCategoryModel> categoryList;

    public  CategoryRingAdapter(Context context, List<RingtoneCategoryModel> categoryList) {
        this.mContext = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(mContext).inflate(R.layout.image_item, viewGroup,false);
        return  new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        final RingtoneCategoryModel ringtoneCategoryModel = categoryList.get(position);
        holder.img_description.setText(ringtoneCategoryModel.getRingtoneCategory());
        Picasso.get()
                .load(ringtoneCategoryModel.getCategoryImageUrl())
                /*.placeholder(R.drawable.imagepreview)*/
                .fit()
                .centerCrop()
                .into(holder.image_view);

        holder.catCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)mContext).moveToCateActivity(ringtoneCategoryModel.getRingtoneCategoryId(), ringtoneCategoryModel.getRingtoneCategory());
            }
        });
    }



    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder  {
        public TextView img_description;
        public ImageView image_view;
        public RelativeLayout catCard;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            img_description = itemView.findViewById(R.id.img_description);
            image_view = itemView.findViewById(R.id.image_view);
            catCard = itemView.findViewById(R.id.cat_card);


        }


    }
}