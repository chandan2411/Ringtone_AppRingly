package com.ringly.customer_app.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ringly.customer_app.R;
import com.ringly.customer_app.models.ContactModel;
import com.ringly.customer_app.views.activities.RingtoneDetailActivity;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactView> {

    Context context;
    List<ContactModel> contactModelList;

    public ContactAdapter(Context context, List<ContactModel> contactModelList) {
        this.context = context;
        this.contactModelList = contactModelList;
    }

    @NonNull
    @Override
    public ContactView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_list_item, parent, false);
        return new ContactView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactView holder, int position) {
        ContactModel contactModel = contactModelList.get(position);
        /*holder.ivProfileImage.setImageBitmap(contactModel.getImageBitmap());*/
        holder.tvContact.setText(contactModel.getContactNumber());
        holder.tvName.setText(contactModel.getContactName());
        holder.llMain.setOnClickListener(view -> ((RingtoneDetailActivity)context).clickedContact(contactModel));
    }

    @Override
    public int getItemCount() {
        return contactModelList==null||contactModelList.isEmpty()?0:contactModelList.size();
    }

    public class ContactView extends RecyclerView.ViewHolder {
        ImageView ivProfileImage;
        LinearLayout llMain;
        TextView tvName;
        TextView tvContact;
        public ContactView(@NonNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvContact = itemView.findViewById(R.id.tvContact);
            llMain = itemView.findViewById(R.id.llMain);
        }
    }
}
