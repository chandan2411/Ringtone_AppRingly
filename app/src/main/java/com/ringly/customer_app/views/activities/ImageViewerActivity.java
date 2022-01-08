package com.ringly.customer_app.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import android.Manifest;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.ringly.customer_app.Api.ApiClient;
import com.ringly.customer_app.Api.UnsplashService;
import com.ringly.customer_app.R;
import com.ringly.customer_app.entities.AppUtils;
import com.ringly.customer_app.entities.Constant;
import com.ringly.customer_app.entities.DatabaseReferences;
import com.ringly.customer_app.entities.MySharedPref;
import com.ringly.customer_app.models.Links;
import com.ringly.customer_app.models.PhotoStats;
import com.ringly.customer_app.models.WallPaperModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pkmmte.view.CircularImageView;
import com.truizlop.fabreveallayout.FABRevealLayout;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageViewerActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DIR_NAME ="WallUp Wallpapers";
    private static final String LOG_TAG = ImageViewerActivity.class.getSimpleName() ;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE =2 ;
    ImageView detailImg,alertImg;
    CircularImageView profileImg;
    TextView user,location,viewsTxt,downloadTxt,likesTxt;
    Uri imageUri;
    DownloadManager downloadManager;
    LinearLayout downloadBtn,setBtn,shareBtn,favBtn;
    long downloadReference;
    Bitmap bmp;
    private String filename="Wallpaper.jpg";
    FABRevealLayout fabRevealLayout;
    CircularProgressDrawable circularProgressDrawable;
    ImageView xBtn;
    Boolean likeBtn= true;
    ImageView favWall;
    private String imageId;
    private String imageFullUrl;
    private WallPaperModel wallPaperModel;
    private MySharedPref sharedPref;
    final HashMap<String, Boolean> favHash = new HashMap<>();
    private boolean ringtoneFav=false;
    private KProgressHUD progressHUD;
    private ImageView ivBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        detailImg= findViewById(R.id.detailImg);
        user= findViewById(R.id.user);
        downloadBtn= findViewById(R.id.downloadBtn);
        setBtn= findViewById(R.id.setBtn);
        shareBtn= findViewById(R.id.shareBtn);
        favBtn= findViewById(R.id.favBtn);
        ivBack= findViewById(R.id.ivBack);
        location= findViewById(R.id.location);
        fabRevealLayout= findViewById(R.id.fablayout);
        xBtn= findViewById(R.id.xBtn);
        viewsTxt= findViewById(R.id.viewTxt);
        downloadTxt= findViewById(R.id.downloadTxt);
        likesTxt= findViewById(R.id.likeTxt);
        alertImg= findViewById(R.id.alertImg);
        circularProgressDrawable=new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        favWall= findViewById(R.id.favWall);

        sharedPref = new MySharedPref(this);


        Glide.with(this)
                .load(R.drawable.alert)
                .centerCrop()
                .into(alertImg);
        inititeProgessHud();
        getIntentData();
        getRecentData();
        getUserFavWallPaperList();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transition));
            detailImg.setTransitionName("sharedTransition");
        }

        imageUri=Uri.parse(imageFullUrl);

        downloadBtn.setOnClickListener(this);
        setBtn.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
        favBtn.setOnClickListener(this);
        xBtn.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                return;
            }
        }
    }

    private void getRecentData() {
        showProgressHud();
        //GET PHOTO STATS
        //RECENT PHOTOS FIRST FETCH
        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
        Call<PhotoStats> call=service.getPhotoStats(imageId, Constant.UNSPLASH_API_KEY);
        call.enqueue(new Callback<PhotoStats>() {
            @Override
            public void onResponse(Call<PhotoStats> call, Response<PhotoStats> response) {
                hideProgressHud();
                PhotoStats photoStats=response.body();
                if (photoStats!=null) {
                    viewsTxt.setText(String.valueOf(photoStats.getViewsModel().getTotal()));
                    downloadTxt.setText(String.valueOf(photoStats.getDownloadsModel().getTotal()));
                    likesTxt.setText(String.valueOf(photoStats.getLikesModel().getTotal()));
                }

            }

            @Override
            public void onFailure(Call<PhotoStats> call, Throwable t) {
                hideProgressHud();
            }
        });
    }

    private void getIntentData() {
        if (getIntent()!=null){
            imageId = getIntent().getStringExtra("id");
            imageFullUrl = getIntent().getStringExtra("Image");
            String uploaderName = getIntent().getStringExtra("user");
            String uploadLocation =getIntent().getStringExtra("location");

            if(uploadLocation == null){
                location.setText("unknown");
            }else
                location.setText(uploadLocation);


            user.setText(uploaderName);
            Glide.with(getApplicationContext())
                    .load(imageFullUrl)
                    .centerCrop()
                    .placeholder(circularProgressDrawable)
                    .into(detailImg);
            wallPaperModel = new WallPaperModel(imageId, imageFullUrl, uploaderName, uploadLocation);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    downloadBtn.setEnabled(true);


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    downloadBtn.setEnabled(false);
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

    private void settingWallpaper(Bitmap bmp,Uri imageUri){
        WallpaperManager wallManager = WallpaperManager.getInstance(getApplicationContext());
        try {

            wallManager.setBitmap(bmp);
            Toast.makeText(ImageViewerActivity.this, "Wallpaper Set Successfully!!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(ImageViewerActivity.this, "Setting WallPaper Failed!!", Toast.LENGTH_SHORT).show();
        }
    }

    private long DownloadData (Uri uri) {
        File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + DIR_NAME + "/");
        if (!direct.exists()) {
            direct.mkdir();
            Log.d(LOG_TAG, "dir created for first time");
        }
        // Create request for android download manager
        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        //Setting title of request
        request.setTitle("Wallpaper");
        //Setting description of request
        request.setDescription("Wallpapers downloading")
//                .setMimeType("image/jpeg")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                        File.separator + DIR_NAME + File.separator + filename);
        //Enqueue download and save into referenceId
        downloadReference = downloadManager.enqueue(request);
        return downloadReference;
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //check if the broadcast message is for our enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (referenceId ==downloadReference ) {

                Toast toast = Toast.makeText(ImageViewerActivity.this,
                        "Image Download Complete", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(downloadReceiver);
    }

    public void registerDownload(String id){
        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
        Call<Links> call=service.getDownloadEndpoint(id,Constant.UNSPLASH_API_KEY);
        call.enqueue(new Callback<Links>() {
            @Override
            public void onResponse(Call<Links> call, Response<Links> response) {
                Links res=response.body();
            }

            @Override
            public void onFailure(Call<Links> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int Id = view.getId();
        switch (Id){
            case R.id.setBtn:
                detailImg.buildDrawingCache();
                bmp=detailImg.getDrawingCache();
                fabRevealLayout.revealMainView();
                settingWallpaper(bmp,imageUri);
                break;

            case R.id.shareBtn:
                Intent shareintent = new Intent();
                shareintent.setAction(Intent.ACTION_SEND);
                shareintent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.devloper.ringtone_app" );
                shareintent.setType("text/plain");
                startActivity(Intent.createChooser(shareintent, "share via"));
                break;

            case R.id.favBtn:
                if (userSignedIn()){
                    if (ringtoneFav) {
                        showProgressHud();
                        callApiForRemoveWallpaperFav(wallPaperModel.getWallPaperId());
                    } else {
                        showProgressHud();
                        callApiForMakingWallpaperFav(wallPaperModel);
                    }
                }else {
                    AppUtils.moveToSignInActivity(this);
                }
                break;

            case R.id.downloadBtn:
                DownloadData(imageUri);
                fabRevealLayout.revealMainView();
                registerDownload(getIntent().getStringExtra("id"));
                break;
            case R.id.xBtn:
                fabRevealLayout.revealMainView();
                break;
            case R.id.ivBack:
                ImageViewerActivity.this.finish();
                break;
        }
    }

    private void callApiForMakingWallpaperFav(WallPaperModel wallPaperModel) {
        DatabaseReferences.makeWallpaperUserFavourite(wallPaperModel, new MySharedPref(this).readString(Constant.USER_ID, ""));
        favWall.setImageResource(R.drawable.ic_favorite_selected);
        hideProgressHud();
    }

    private void callApiForRemoveWallpaperFav(String wallPaperId) {
        DatabaseReferences.removeWallpaperFromUserFavourite(wallPaperId);
        favWall.setImageResource(R.drawable.ic_fav_unselected);
        hideProgressHud();
    }

    private void inititeProgessHud() {
        progressHUD = KProgressHUD.create(ImageViewerActivity.this)
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


    private boolean userSignedIn() {
        return new MySharedPref(this).readBoolean(Constant.IS_USER_LOGIN, false);
    }

    private void getUserFavWallPaperList() {
        showProgressHud();
        DatabaseReference userFavDbRef = DatabaseReferences.
                getUserFavoriteWallpaperReference(new MySharedPref(this).readString(Constant.USER_ID, ""));

        userFavDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideProgressHud();
                favHash.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String ringtoneId = postSnapshot.getValue(WallPaperModel.class).getWallPaperId();
                    favHash.put(ringtoneId, true);
                }
                setTitleAndFav(favHash);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgressHud();
                Toast.makeText(ImageViewerActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setTitleAndFav(HashMap<String, Boolean> favHash) {
        boolean isFav = favHash.containsKey(imageId);
        if (isFav) {
            ringtoneFav = true;
            favWall.setImageResource(R.drawable.ic_favorite_selected);
        } else {
            favWall.setImageResource(R.drawable.ic_fav_unselected);
            ringtoneFav = false;
        }
    }

}
