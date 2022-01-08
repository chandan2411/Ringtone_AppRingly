package com.ringly.customer_app.entities;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.ringly.customer_app.models.ContactModel;
import com.ringly.customer_app.views.activities.RingtoneDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class FetchContactAsyncTask extends AsyncTask<Void,Void ,List<ContactModel>> {

    private Context context;
    List<ContactModel> contactModelList = new ArrayList<>();
    ContactFetchListener listener;
    public static final String TAG = FetchContactAsyncTask.class.getSimpleName();


    public FetchContactAsyncTask(Context context) {
        this.context = context;
        listener = (RingtoneDetailActivity) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<ContactModel> doInBackground(Void... voids) {
        return GetContactsIntoArrayList();
    }

    @Override
    protected void onPostExecute(List<ContactModel> contactModelList) {
        super.onPostExecute(contactModelList);
        listener.onContactFetch(contactModelList, true);
    }

    public List<ContactModel> GetContactsIntoArrayList(){

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC";

        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, sortOrder);

        if (cursor!=null) {
            while (cursor.moveToNext()) {

                String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                /*Bitmap photo = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.black_avatar);*/

                /*try {
                    *//*InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(contactId)));
*//*
                    *//*if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream);
                    }*//*

                    if(inputStream != null)
                        inputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                String phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));


                contactModelList.add(new ContactModel(name, phonenumber, null, contactId));
                Logger.logD(TAG, "Name :"+name+" Phone: "+phonenumber);
            }

            cursor.close();
        }
        return contactModelList;
    }

}
