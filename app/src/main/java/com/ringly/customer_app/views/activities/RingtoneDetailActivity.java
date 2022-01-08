package com.ringly.customer_app.views.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ringly.customer_app.R;
import com.ringly.customer_app.entities.AppUtils;
import com.ringly.customer_app.entities.AudioDownloadUtility;
import com.ringly.customer_app.entities.Constant;
import com.ringly.customer_app.entities.ContactFetchListener;
import com.ringly.customer_app.entities.DatabaseReferences;
import com.ringly.customer_app.entities.FetchContactAsyncTask;
import com.ringly.customer_app.entities.FileModel;
import com.ringly.customer_app.entities.Logger;
import com.ringly.customer_app.entities.MySharedPref;
import com.ringly.customer_app.entities.OnMediaDownloadListener;
import com.ringly.customer_app.entities.PermissionClass;
import com.ringly.customer_app.models.ContactModel;
import com.ringly.customer_app.models.RingtoneModel;
import com.ringly.customer_app.views.adapters.ContactAdapter;
import com.ringly.customer_app.views.adapters.RingtoneDetailAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RingtoneDetailActivity extends AppCompatActivity implements View.OnClickListener,
        RecyclerViewScrollListener, OnMediaDownloadListener, ContactFetchListener, RingtoneDetailAdapter.DurationListener {

    private static final String TAG = RingtoneDetailActivity.class.getSimpleName();
    final HashMap<String, Boolean> favHash = new HashMap<>();
    ArrayList<RingtoneModel> ringtoneModelList = new ArrayList<>();
    Integer position;
    private RecyclerView mRecyclerView;
    private RingtoneDetailAdapter adapter;
    private TextView tvTitle;
    private ImageView ivBack;
    private LinearLayoutManager manager;
    private ImageView ivOptions;
    private ImageView ivFavourite;
    private ImageView ivShare;
    private RelativeLayout rlMain;
    private RelativeLayout rlOptions;
    private boolean ringtoneFav;
    private RingtoneModel ringtoneModel;
    private File internalPath;
    private RelativeLayout rlParent;
    private KProgressHUD progressHUD;
    private boolean hasContactFetched;
    private List<ContactModel> contactModelList;
    private AlertDialog alertDialog;
    private RecyclerView recyclerView;
    private String ringtonePath;
    private boolean isToDownload = false;
    private boolean showNotifaction = false;
    private String categoryID = "";
    private boolean isScrollable = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringtone_detail);
        initiateContactDialog();
        getReference();
        inititeProgessHud();
        getIntentData();
        if (PermissionClass.checkPermission(this)) {
            new FetchContactAsyncTask(this).execute();
        } else {
            PermissionClass.requestPermission(this);
            isToDownload = false;
        }

    }

    private void setColorForBackground(RelativeLayout rlParent, int position) {
        int colorCode;
        if (categoryID.equalsIgnoreCase("Search")){
             colorCode = AppUtils.getColorCode(this, 0);
        }else {
             colorCode = AppUtils.getColorCode(this, position);
        }
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{colorCode, 0x000000});
        gd.setCornerRadius(0f);
        /*GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[] { colorCode, Color.rgb(255, 0, 0), Color.BLACK });
        g.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        g.setGradientRadius(140.0f);
        g.setGradientCenter(0.0f, 0.45f);*/
        rlParent.setBackgroundDrawable(gd);
    }


    private void inititeProgessHud() {
        progressHUD = KProgressHUD.create(RingtoneDetailActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

    }

    public void showProgressHud() {
        progressHUD.show();
    }

    public void hideProgressHud() {
        if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }
    }

    private void getReference() {
        ivOptions = findViewById(R.id.ivOptions);
        ivFavourite = findViewById(R.id.ivFavourite);
        ivShare = findViewById(R.id.ivShare);

        rlMain = findViewById(R.id.rlMain);
        rlOptions = findViewById(R.id.rlOptions);


        LinearLayout llSetAsContactRingtone = findViewById(R.id.llSetAsContactRingtone);
        LinearLayout llSetAsAlarmTone = findViewById(R.id.llSetAsAlarmTone);
        LinearLayout llSetAsRingtone = findViewById(R.id.llSetAsRingtone);
        LinearLayout llDownloadRingtone = findViewById(R.id.llDownloadRingtone);
        rlParent = findViewById(R.id.rlParent);

        mRecyclerView = findViewById(R.id.recycler_view);
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);

        ivOptions.setOnClickListener(this);
        ivOptions.setOnClickListener(this);
        ivFavourite.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        rlOptions.setOnClickListener(this);

        llSetAsAlarmTone.setOnClickListener(this);
        llSetAsContactRingtone.setOnClickListener(this);
        llSetAsRingtone.setOnClickListener(this);
        llDownloadRingtone.setOnClickListener(this);

        mRecyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        manager.setReverseLayout(true);
//        manager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(manager);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.scrollToPosition(0);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    ivFavourite.setVisibility(View.INVISIBLE);
                    ivOptions.setVisibility(View.INVISIBLE);
                    ivShare.setVisibility(View.INVISIBLE);
                    tvTitle.setVisibility(View.INVISIBLE);
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    position = manager.findFirstVisibleItemPosition();
                    Logger.logD(TAG, "Recycler position " + position);
                    ivFavourite.setVisibility(View.VISIBLE);
                    ivOptions.setVisibility(View.VISIBLE);
                    ivShare.setVisibility(View.VISIBLE);
                    tvTitle.setVisibility(View.VISIBLE);
                    setTitleAndFav(position);
                    Boolean isAudioPlaying = adapter.getAudioPlaying();
//                    adapter.setAudioDuartion(adapter.getViewHolder(), ringtoneModelList.get(position).getRingtoneDuration());
                    if (isAudioPlaying) {
                        adapter.playPauseRingtone("", adapter.getViewHolder());
                    }

                    setColorForBackground(rlMain, position);
                }
            }
        });


        /*mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    ivFavourite.setVisibility(View.INVISIBLE);
                    ivOptions.setVisibility(View.INVISIBLE);
                    ivShare.setVisibility(View.INVISIBLE);
                    //Dragging
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    ivFavourite.setVisibility(View.INVISIBLE);
                    ivOptions.setVisibility(View.INVISIBLE);
                    ivShare.setVisibility(View.INVISIBLE);
                    position = manager.findFirstVisibleItemPosition();
                    String title = ringtoneModelList.get(position).getRingtoneName();
                    tvTitle.setText(title);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });*/
    }

    private void setTitleAndFav(int position) {
        if (ringtoneModelList.isEmpty())
            return;
        if (categoryID.equalsIgnoreCase("Search"))
            ringtoneModel = ringtoneModelList.get(0);
        else
            ringtoneModel = ringtoneModelList.get(position);
        String songTitle = ringtoneModel.getRingtoneName();
        String fileId = ringtoneModel.getRingtoneId();
        tvTitle.setText(songTitle);
        boolean isFav = favHash.containsKey(fileId);
        if (isFav) {
            ringtoneFav = true;
            ivFavourite.setImageResource(R.drawable.ic_favorite_selected);
        } else {
            ivFavourite.setImageResource(R.drawable.ic_fav_unselected);
            ringtoneFav = false;
        }
    }


    private void getIntentData() {
        position = getIntent().getIntExtra("Position", 0);
        categoryID = getIntent().getStringExtra("CategoryID");
        isScrollable = getIntent().getBooleanExtra("Scroll", true);

        if (categoryID != null && !categoryID.isEmpty()) {
            if (categoryID.equalsIgnoreCase("FAV")) {
                getUserFavRingtoneList(true);
            } else if (categoryID.equalsIgnoreCase("Popular")) {
                getPopularRingtone();
            } else if (categoryID.equalsIgnoreCase("Search")){
                String searchText = getIntent().getStringExtra("RingtoneTitle");
                getSearchedRingtone(searchText);
            }else{
                getCategorizeRingtone();

            }
        } else {
            getRingtoneFromServer();
        }

        if (isScrollable) {
            recyclerView.setNestedScrollingEnabled(true);
        } else {
            recyclerView.setNestedScrollingEnabled(false);
        }


    }

    private void getSearchedRingtone(String searchText) {
        showProgressHud();
        DatabaseReference mDatabaseRef = DatabaseReferences.getRingtoneReference();
        Query query = mDatabaseRef.orderByChild("ringtoneName").equalTo(searchText);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ringtoneModelList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    RingtoneModel ringtoneModel = postSnapshot.getValue(RingtoneModel.class);
//                    addDuartion(ringtoneModel);

                    ringtoneModelList.add(ringtoneModel);
                }
                if (!ringtoneModelList.isEmpty()) {
                    String title = ringtoneModelList.get(0).getRingtoneName();
                    tvTitle.setText(title);
                    getUserFavRingtoneList(false);
                }
                hideProgressHud();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RingtoneDetailActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                hideProgressHud();
            }
        });
    }

    private void getPopularRingtone() {
        showProgressHud();
        DatabaseReference mDatabaseRef = DatabaseReferences.getRingtoneReference();
        Query query = mDatabaseRef.orderByChild(Constant.FB_RING_DOWNLOAD);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ringtoneModelList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    RingtoneModel ringtoneModel = postSnapshot.getValue(RingtoneModel.class);
//                    addDuartion(ringtoneModel);

                    ringtoneModelList.add(ringtoneModel);
                }
                if (!ringtoneModelList.isEmpty()) {
                    String title = ringtoneModelList.get(position).getRingtoneName();
//                    tvTitle.setText(title);
                    getUserFavRingtoneList(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RingtoneDetailActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                hideProgressHud();
            }
        });
    }

    private void callApiForMakingRingtoneFav(RingtoneModel ringtoneModel) {
        DatabaseReferences.makeRingtoneUserFavourite(ringtoneModel, new MySharedPref(this).readString(Constant.USER_ID, ""));
        ivFavourite.setImageResource(R.drawable.ic_favorite_selected);
        updateLikesCount(false);
        hideProgressHud();
    }


    private void callApiForRemoveRingtoneFav(String ringtoneId) {
        DatabaseReferences.removeRingtoneFromUserFavourite(ringtoneId);
        ivFavourite.setImageResource(R.drawable.ic_fav_unselected);
        updateLikesCount(true);
        hideProgressHud();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adapter != null)
            adapter.stopAudioAndTimer();
    }

    private void getCategorizeRingtone() {
        showProgressHud();
        DatabaseReference mDatabaseRef = DatabaseReferences.getRingtoneReference();
        Query query = mDatabaseRef.orderByChild("ringtoneCategoryId").equalTo(categoryID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ringtoneModelList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    RingtoneModel ringtoneModel = postSnapshot.getValue(RingtoneModel.class);
//                    addDuartion(ringtoneModel);

                    ringtoneModelList.add(ringtoneModel);
                }
                if (!ringtoneModelList.isEmpty()) {
                    String title = ringtoneModelList.get(position).getRingtoneName();
//                    tvTitle.setText(title);
                    getUserFavRingtoneList(false);
                }
                hideProgressHud();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RingtoneDetailActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                hideProgressHud();
            }
        });
    }


    private void getRingtoneFromServer() {
        showProgressHud();
        DatabaseReference ringtoneDatabaseReference = DatabaseReferences.getRingtoneReference();
        ringtoneDatabaseReference.orderByChild("ringtoneCreatedDate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ringtoneModelList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    RingtoneModel ringtoneModel = postSnapshot.getValue(RingtoneModel.class);
//                    addDuartion(ringtoneModel);
                    ringtoneModelList.add(ringtoneModel);
                }
//                String title = ringtoneModelList.get(position).getRingtoneName();
//                tvTitle.setText(title);
                getUserFavRingtoneList(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RingtoneDetailActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                hideProgressHud();
            }
        });

    }

    private void addDuartion(RingtoneModel ringtoneModel) {
        MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
        String mediaPath = Uri.parse(ringtoneModel.getRingtoneLink()).getPath();

        Log.d("URI", Uri.parse(ringtoneModel.getRingtoneLink()).toString());
        mRetriever.setDataSource(ringtoneModel.getRingtoneLink(), new HashMap<String, String>());
        String s = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        ringtoneModel.setRingtoneDuration(Long.parseLong(s));
        mRetriever.release();
    }


    private void getUserFavRingtoneList(boolean type) {
        DatabaseReference userFavDbRef = DatabaseReferences.
                getUserFavoriteRingtoneReference(new MySharedPref(this).readString(Constant.USER_ID, ""));

        userFavDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favHash.clear();
                if (type)
                    ringtoneModelList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String ringtoneId = postSnapshot.getValue(RingtoneModel.class).getRingtoneId();
                    favHash.put(ringtoneId, true);
                    if (type) {
                        RingtoneModel ringtoneModel = postSnapshot.getValue(RingtoneModel.class);
//                        addDuartion(ringtoneModel);
                        ringtoneModelList.add(ringtoneModel);
                    }
                }
                Collections.reverse(ringtoneModelList);
                adapter = new RingtoneDetailAdapter(RingtoneDetailActivity.this, ringtoneModelList, null);
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.getLayoutManager().scrollToPosition(position);
                setTitleAndFav(position);
                setColorForBackground(rlMain, position);

                hideProgressHud();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RingtoneDetailActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                hideProgressHud();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewID = view.getId();
        switch (viewID) {
            case R.id.ivBack:
                this.finish();
                break;
            case R.id.ivOptions:
                /*if (userSignedIn()) {*/
                showHideOptions(true);
                /*} else {
                    AppUtils.moveToSignInActivity(this);
                }*/
                break;
            case R.id.ivFavourite:
                if (userSignedIn()) {
                    if (ringtoneFav) {
                        showProgressHud();
                        callApiForRemoveRingtoneFav(ringtoneModel.getRingtoneId());
                    } else {
                        showProgressHud();
                        callApiForMakingRingtoneFav(ringtoneModel);
                    }
                } else {
                    AppUtils.moveToSignInActivity(this);
                }
                break;
            case R.id.ivShare:
                /* if (userSignedIn()) {*/
                shareRingtone();
               /* } else {
                    AppUtils.moveToSignInActivity(this);
                }*/
                break;

            case R.id.llSetAsContactRingtone:
                showNotifaction = false;
                if (PermissionClass.writePermission(this)) {
                    /*if (userSignedIn()) {*/
                    if (PermissionClass.checkPermission(this)) {
                        showProgressHud();
                        checkAndSetRingtone(Constant.SET_CONTACT_RINGTONE);
                    } else {
                        PermissionClass.requestPermission(this);
                        isToDownload = true;
                    }
                    /*} else {
                        AppUtils.moveToSignInActivity(this);
                    }*/
                } else {
                    permissionForWriteSetting();
                }
                showHideOptions(false);
                break;

            case R.id.llSetAsAlarmTone:
                showNotifaction = false;
                if (PermissionClass.writePermission(this)) {
                    /*if (userSignedIn()) {*/
                    showProgressHud();
                    checkAndSetRingtone(Constant.SET_ALARM_RINGTONE);
                    /*} else {
                        AppUtils.moveToSignInActivity(this);
                    }*/
                } else {
                    permissionForWriteSetting();
                }
                showHideOptions(false);
                break;

            case R.id.llSetAsRingtone:
                showNotifaction = false;
                if (PermissionClass.writePermission(this)) {
                    /*if (userSignedIn()) {*/
                    showProgressHud();
                    checkAndSetRingtone(Constant.SET_RINGTONE);
                   /* } else {
                        AppUtils.moveToSignInActivity(this);
                    }*/
                } else {
                    permissionForWriteSetting();
                }
                showHideOptions(false);
                break;

            case R.id.llDownloadRingtone:
                showNotifaction = true;
                if (PermissionClass.checkPermission(this)) {
                    /*if (userSignedIn()) {*/
                    File externalPath = AppUtils.completePathInSDCard(Constant.AUDIO);
                    if (!fileExist(externalPath)) {
                        showProgressHud();
                        downloadRingtone(Constant.SET_RINGTONE, externalPath, showNotifaction);
                    }else {
                        Snackbar.make(rlParent, ringtoneModel.getRingtoneName() + " has downloaded", Snackbar.LENGTH_SHORT).show();
                    }
                   /* } else {
                        AppUtils.moveToSignInActivity(this);
                    }*/
                } else {
                    PermissionClass.requestPermission(this);
                    isToDownload = true;
                }
                showHideOptions(false);
                break;

            case R.id.rlOptions:
                showHideOptions(false);
                break;
        }
    }

    private boolean fileExist(File externalPath) {
        return new File(externalPath, ringtoneModel.getRingtoneName()+".mp3").exists();
    }


    private void permissionForWriteSetting() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, 2);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_SETTINGS}, 2);
        }
    }

    private void checkAndSetRingtone(int ringtoneType) {
        internalPath = AppUtils.completePathInSDCard(Constant.AUDIO_HIDDEN);
        File filePath = new File(internalPath, ringtoneModel.getRingtoneName() + ".mp3");
        if (checkFileAvailable(filePath)) {
            setRingtone(filePath.getAbsolutePath(), ringtoneType);
        } else {
            downloadRingtone(ringtoneType, internalPath, showNotifaction);
        }
    }

    private boolean userSignedIn() {
        return new MySharedPref(this).readBoolean(Constant.IS_USER_LOGIN, false);
    }

    private void downloadRingtone(int setContactRingtone, File internalPath, boolean showNotification) {
        FileModel fileModel = new FileModel(ringtoneModel.getRingtoneName(),
                ringtoneModel.getRingtoneId(), ringtoneModel.getRingtoneLink(), ringtoneModel.getRingtoneDownloadCount());
        List<FileModel> fileModelList = new ArrayList<>();
        fileModelList.add(fileModel);
        new AudioDownloadUtility(this, Constant.AUDIO, internalPath.getAbsolutePath(),
                fileModelList, showNotification, setContactRingtone).execute();
    }

    private boolean checkFileAvailable(File filePath) {
        boolean isFileAvailable = false;
        if (filePath.exists()) {
            isFileAvailable = true;
        }
        return isFileAvailable;
    }

    private void setRingtone(String savedPath, int ringtoneType) {
        if (ringtoneType == Constant.SET_CONTACT_RINGTONE) {
            hideProgressHud();
            showHideOptions(false);
            showContactDialog(savedPath);
        } else {
            setFileAsRingtone(savedPath, ringtoneType);
        }
    }

    public void hideContactDialog() {
        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();
    }

    public void showContactDialog(String savedPath) {
        if (!hasContactFetched)
            showProgressHud();

        ringtonePath = savedPath;

        if (alertDialog != null)
            alertDialog.show();
    }

    private void initiateContactDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.contact_layout, viewGroup, false);
        ImageView ivBack = dialogView.findViewById(R.id.ivBack);
        recyclerView = dialogView.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();

    }


    private void setFileAsRingtone(String savedPath, int ringtoneType) {
        File file = new File(savedPath); // path is a file to /sdcard/media/ringtone
        int fileSize = Integer.parseInt(String.valueOf(file.length() / 1024));

        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        values.put(MediaStore.MediaColumns.TITLE, ringtoneModel.getRingtoneName());
        values.put(MediaStore.MediaColumns.SIZE, fileSize);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.Audio.Media.ARTIST, "");
        String message = "";
        /*values.put(MediaStore.Audio.Media.DURATION, 0)*/
        ;

        switch (ringtoneType) {
            case Constant.SET_ALARM_RINGTONE:
                values.put(MediaStore.Audio.Media.IS_RINGTONE, false);
                values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
                values.put(MediaStore.Audio.Media.IS_ALARM, true);
                values.put(MediaStore.Audio.Media.IS_MUSIC, false);
                try {
                    Uri uri = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
//                    getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + file.getAbsolutePath() + "\"", null);
                    Uri newUri = getContentResolver().insert(uri, values);
                    RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM, newUri);
                    updateSetAsAlarmRingtoneCount();
                    message = "Alarm tone has been updated successfully";
                } catch (Exception ex) {
                    Logger.logE(TAG, ex.getMessage(), ex);
                }

                break;
            case Constant.SET_RINGTONE:
                values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
                values.put(MediaStore.Audio.Media.IS_ALARM, false);
                values.put(MediaStore.Audio.Media.IS_MUSIC, false);
                try {
                    Uri uri1 = MediaStore.Audio.Media.getContentUriForPath(file.getAbsolutePath());
                    Uri newUri1 = this.getContentResolver().insert(uri1, values);
                    RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, newUri1);
                    updateSetAsRingtoneCount();
                    message = "Ringtone has been updated successfully";
                } catch (Exception ex) {
                    Logger.logE(TAG, ex.getMessage(), ex);
                }

                break;
        }

        Snackbar snackbar = Snackbar.make(rlParent, message, Snackbar.LENGTH_LONG);
        snackbar.show();
        hideProgressHud();


    }


    private void shareRingtone() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Ringy");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store?hl=en";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    private void showHideOptions(boolean haveToShowOptions) {
        if (haveToShowOptions) {
            rlOptions.setVisibility(View.VISIBLE);
            disableEnableControls(false, rlMain);
        } else {
            rlOptions.setVisibility(View.GONE);
            disableEnableControls(true, rlMain);
        }
    }

    private void disableEnableControls(boolean enable, ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup) {
                disableEnableControls(enable, (ViewGroup) child);
            }
        }
    }


    @Override
    public void OnScrollListener(RingtoneModel ringtoneModel, boolean isFav) {
       /* this.ringtoneModel = ringtoneModel;
        if (isFav) {
            ringtoneFav = true;
            ivFavourite.setImageResource(R.drawable.ic_favorite_selected);
        } else {
            ivFavourite.setImageResource(R.drawable.ic_fav_unselected);
            ringtoneFav = false;
        }*/
    }

    @Override
    public void onMediaDownload(int type, String savedPath, String name, int downloadType) {
        hideProgressHud();
        if (downloadType != Constant.DOWNLOAD_RINGTONE)
            setRingtone(savedPath, downloadType);
        else {
            Snackbar.make(rlParent, name + " has downloaded", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (userSignedIn()) {
                    File externalPath = AppUtils.completePathInSDCard(Constant.AUDIO);
                    new FetchContactAsyncTask(this).execute();
                    if (isToDownload)
                        downloadRingtone(Constant.SET_RINGTONE, externalPath, showNotifaction);
                } else {
                    AppUtils.moveToSignInActivity(this);
                }
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(RingtoneDetailActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (requestCode == 2 && Settings.System.canWrite(this)) {
                Log.d("TAG", "MainActivity.CODE_WRITE_SETTINGS_PERMISSION success");
                //do your code
            }
        }
    }


    @Override
    public void onContactFetch(List<ContactModel> contactModelList, boolean hasContactFetched) {
        this.hasContactFetched = hasContactFetched;
        this.contactModelList = contactModelList;
        ContactAdapter ad = new ContactAdapter(this, contactModelList);
        recyclerView.setAdapter(ad);
        if (hasContactFetched)
            hideProgressHud();

    }

    public void clickedContact(ContactModel contactModel) {
        hideContactDialog();
        setAsContactRingtone(contactModel);
    }

    private void setAsContactRingtone(ContactModel contactModel) {

        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactModel.getId()));
        ContentValues values = new ContentValues();
        values.put(ContactsContract.Contacts.CUSTOM_RINGTONE, ringtonePath);
//        getContentResolver().update(uri, values, ContactsContract.CommonDataKinds.Phone._ID+"=?", new String[] {String.valueOf(contactModel.getId())});
        getContentResolver().update(uri, values, null, null);
        updateSetAsContactRingtoneCount();
        Snackbar.make(rlParent, "Ringtone for " + contactModel.getContactName() + " has updated", Snackbar.LENGTH_SHORT).show();
    }

    private void updateSetAsRingtoneCount() {
        int contactCount = ringtoneModel.getRingtoneUsedAsContactToneCount() + 1;
        DatabaseReference reference = DatabaseReferences.getRingtoneReference().child(ringtoneModel.getRingtoneId());
        Map<String, Object> updates = new HashMap<>();
        updates.put(Constant.FB_RING_UsedCount, contactCount);
        reference.updateChildren(updates);
    }

    private void updateSetAsAlarmRingtoneCount() {
        int contactCount = ringtoneModel.getRingtoneUsedAsAlarmToneCount() + 1;
        DatabaseReference reference = DatabaseReferences.getRingtoneReference().child(ringtoneModel.getRingtoneId());
        Map<String, Object> updates = new HashMap<>();
        updates.put(Constant.FB_RING_UsedAsAlarmToneCount, contactCount);
        reference.updateChildren(updates);
    }

    private void updateLikesCount(boolean isToRemove) {
        int contactCount;
        if(!isToRemove)
            contactCount = ringtoneModel.getRingtoneUsedAsFavourite() + 1;
        else
            contactCount = ringtoneModel.getRingtoneUsedAsFavourite() - 1;
        DatabaseReference reference = DatabaseReferences.getRingtoneReference().child(ringtoneModel.getRingtoneId());
        Map<String, Object> updates = new HashMap<>();
        updates.put(Constant.FB_RING_UsedAsFavourite, contactCount);
        reference.updateChildren(updates);
    }

    private void updateSetAsContactRingtoneCount() {
        int contactCount = ringtoneModel.getRingtoneUsedAsFavourite() + 1;
        DatabaseReference reference = DatabaseReferences.getRingtoneReference().child(ringtoneModel.getRingtoneId());
        Map<String, Object> updates = new HashMap<>();
        updates.put(Constant.FB_RING_UsedAsContactToneCount, contactCount);
        reference.updateChildren(updates);
    }


    @Override
    public void onDuration(View view, Long duartion) {

    }
}
