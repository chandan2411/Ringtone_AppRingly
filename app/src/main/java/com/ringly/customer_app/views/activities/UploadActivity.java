package com.ringly.customer_app.views.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ringly.customer_app.R;
import com.ringly.customer_app.entities.AppUtils;
import com.ringly.customer_app.entities.CheckNetwork;
import com.ringly.customer_app.entities.Constant;
import com.ringly.customer_app.entities.DatabaseReferences;
import com.ringly.customer_app.entities.Logger;
import com.ringly.customer_app.entities.MySharedPref;
import com.ringly.customer_app.entities.SpinAdapter;
import com.ringly.customer_app.models.RingtoneCategoryModel;
import com.ringly.customer_app.models.RingtoneUploadModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CHOOSE_AUDIO = 1;
    private static final String TAG = UploadActivity.class.getSimpleName();

    String ringtoneTitle;
    String ringtoneCategory = "";
    private Uri mp3Url;
    private EditText etRingtoneTitle;
    private Spinner spinnerRingtoneCategory;
    private RelativeLayout rlSelectFile;
    private TextView tvFileStatus;
    private ImageView ivFileStatus;
    private LinearLayout rlParent;
    private StorageTask mUploadTask;
    private KProgressHUD progressHUDDeterminate;
    private KProgressHUD progressHUD;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseCatRef;
    private String ringtoneCategoryId = "";
    private ArrayList<RingtoneCategoryModel> mCatObject;
    private SpinAdapter adapter;
    private RelativeLayout rlCategory;
    private TextView tvRingtoneCategory;
    private int second=0;
    private long fileSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        findReference();
        inititeProgessHudDeterminate();
        inititeProgessHud();
        if (CheckNetwork.checkNet(this)) {
            getDataFromFirebaseDb();
        } else {
            Snackbar.make(this.findViewById(R.id.rlParent), Constant.NO_INTERNET, Snackbar.LENGTH_SHORT).show();
        }

    }

    private void getDataFromFirebaseDb() {
        showProgressHud();
        mDatabaseCatRef = DatabaseReferences.getRingtoneCategoryRefence();
        mDatabaseCatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mCatObject = new ArrayList<>();
                mCatObject.add(new RingtoneCategoryModel("0", "", "Select Category"));
                Log.i("Ringtone", mCatObject.toString());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    RingtoneCategoryModel model = postSnapshot.getValue(RingtoneCategoryModel.class);
                    mCatObject.add(model);
                }
                adapter = new SpinAdapter(UploadActivity.this,
                        android.R.layout.simple_spinner_item,
                        mCatObject);
                spinnerRingtoneCategory.setAdapter(adapter);
                hideProgressHud();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UploadActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                hideProgressHud();
            }
        });
    }


    private void inititeProgessHud() {
        progressHUD = KProgressHUD.create(this)
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

    private void findReference() {
        ImageView ivBack = findViewById(R.id.ivBack);
        etRingtoneTitle = findViewById(R.id.etRingtoneTitle);
        spinnerRingtoneCategory = findViewById(R.id.spinnerRingtoneCategory);
        rlSelectFile = findViewById(R.id.rlSelectFile);
        tvFileStatus = findViewById(R.id.tvFileStatus);
        ivFileStatus = findViewById(R.id.ivFileStatus);
        rlParent = findViewById(R.id.rlParent);
        rlCategory = findViewById(R.id.rlCategory);
        tvRingtoneCategory = findViewById(R.id.tvRingtoneCategory);

        ivFileStatus.setImageResource(R.drawable.ic_file);
        tvFileStatus.setText("Select File");

        rlCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerRingtoneCategory.performClick();
            }
        });


        Button btnUpload = findViewById(R.id.btnUpload);

        spinnerRingtoneCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    ringtoneCategory = mCatObject.get(i).getRingtoneCategory();
                    ringtoneCategoryId = mCatObject.get(i).getRingtoneCategoryId();
                    tvRingtoneCategory.setText(ringtoneCategory);
                } else {
                    ringtoneCategory = "";
                    ringtoneCategoryId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ringtoneCategory = "";
                ringtoneCategoryId = "";
            }
        });

        ivBack.setOnClickListener(this);
        rlSelectFile.setOnClickListener(this);
        btnUpload.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        int ID = view.getId();
        switch (ID) {
            case R.id.ivBack:
                UploadActivity.this.finish();
                break;
            case R.id.rlSelectFile:
                showFileChoose();
                break;
            case R.id.btnUpload:
                if (CheckNetwork.checkNet(this)) {
                    validateAndUpdate();
                } else {
                    showSnackBar(Constant.NO_INTERNET);
                }
                break;
        }
    }

    private void showSnackBar(String message) {
        Snackbar.make(rlParent, message, Snackbar.LENGTH_SHORT).show();
    }

    private void showFileChoose() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_AUDIO);
    }

    private void validateAndUpdate() {
        String userId = new MySharedPref(this).readString(Constant.USER_ID, "");
        ringtoneTitle = etRingtoneTitle.getText().toString().trim();
        if (TextUtils.isEmpty(ringtoneTitle)) {
            showSnackBar(Constant.PLEASE_ENTER_RINGTONE_TITLE);
            return;
        }

        if (TextUtils.isEmpty(ringtoneCategory)) {
            showSnackBar(Constant.PLEASE_SELECT_CATEGORY);
            return;
        }

        if (mp3Url == null) {
            showSnackBar(Constant.PLEASE_SELECT_FILE_TO_UPLOAD);
            return;
        }

        if (fileSize>1024*1024){
            showSnackBar("File size exceeding the upload limit..Please select file size of 1 MB or less");
            return;
        }

        /*Getting Audio File Duration*/
        MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
        mRetriever.setDataSource(this, mp3Url);
        String s = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        second = Integer.parseInt(s)/1000;
        mRetriever.release();


        if (mUploadTask != null && mUploadTask.isInProgress()) {
            showSnackBar("Upload in progress");
        } else {
            uploadAudio(ringtoneTitle, ringtoneCategory, mp3Url, userId, ringtoneCategoryId);
        }

    }

    private void inititeProgessHudDeterminate() {

        progressHUDDeterminate = KProgressHUD.create(UploadActivity.this)
                .setStyle(KProgressHUD.Style.PIE_DETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("File is uploading...")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setMaxProgress(100);

    }

    private void simulateProgressUpdate(int progress) {
        if (progress == 80) {
            progressHUDDeterminate.setLabel("Almost finish...");
        }
        progressHUDDeterminate.setProgress(progress);

    }

    public void showProgressHudDeterminate() {
        progressHUDDeterminate.show();
    }

    public void hideProgressHudDeterminate() {
        if (progressHUDDeterminate != null && progressHUDDeterminate.isShowing()) {
            progressHUDDeterminate.dismiss();
        }
    }


    private void uploadAudio(String ringtoneTitle, String ringtoneCategory, Uri mp3Url, String userId, String ringtoneCategoryId) {

        showProgressHudDeterminate();
        String childName = "ringy_" + ringtoneTitle + " " + System.currentTimeMillis() + "." + getFileExtension(mp3Url);
        Logger.logD(TAG, "file name : " + childName);

        final StorageReference fileReference = DatabaseReferences.getRingtoneStorageRef().child(childName);
        mUploadTask = fileReference.putFile(mp3Url)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String dateTime = AppUtils.getDateTime();
                            String downloadUrl = uri.toString();
                            mDatabaseRef = DatabaseReferences.getRingtoneReference();
                            String uploadID = mDatabaseRef.push().getKey();
                            RingtoneUploadModel ringtoneModel = new RingtoneUploadModel();
                            ringtoneModel.setRingtoneCategory(ringtoneCategory);
                            ringtoneModel.setRingtoneId(uploadID);
                            ringtoneModel.setRingtoneCategoryId(ringtoneCategoryId);
                            ringtoneModel.setRingtoneLink(downloadUrl);
                            ringtoneModel.setRingtoneName(ringtoneTitle);
                            ringtoneModel.setRingtoneCreatedDate(dateTime);
                            ringtoneModel.setRingtoneModifiedDate(dateTime);
                            ringtoneModel.setRingtoneUsedAsFavourite(0);
                            ringtoneModel.setRingtoneUsedAsAlarmToneCount(0);
                            ringtoneModel.setRingtoneUsedAsContactToneCount(0);
                            ringtoneModel.setRingtoneDuration(second);
                            ringtoneModel.setUserId(userId);
                            ringtoneModel.setRingtoneUsedCount(0);

                            Logger.logD(TAG, "RingtoneId : " + uploadID + " RingtoneCategory " + ringtoneCategoryId
                                    + "RingtoneLink " + downloadUrl + " CategoryId" + ringtoneCategoryId + " Ringtone Name" + ringtoneTitle);

                            mDatabaseRef.child(uploadID).setValue(ringtoneModel);
                            hideProgressHudDeterminate();
                            showSnackBar("File Uploaded Successfully");
                            resetData();

                            Intent data = new Intent();
                            setResult(Activity.RESULT_OK, data);
                        }
                    });

                }))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideProgressHudDeterminate();
                        showSnackBar(e.getMessage());
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        simulateProgressUpdate((int) progress);
                    }
                });
    }

    private void resetData() {
        etRingtoneTitle.getText().clear();
        ringtoneTitle = "";
        ringtoneCategory = "";
        ringtoneCategoryId = "";
        tvFileStatus.setText("Select File");
        ivFileStatus.setImageResource(R.drawable.ic_file);
        tvRingtoneCategory.setText("Select Category");

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_AUDIO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mp3Url = data.getData();
            fileSize=0L;
            if (mp3Url.getPath()!=null) {
                AssetFileDescriptor afd = null;
                try {
                    afd = getContentResolver().openAssetFileDescriptor(mp3Url,"r");
                    fileSize = afd.getLength();
                    afd.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            tvFileStatus.setText(getResources().getString(R.string.file_selected));
            ivFileStatus.setImageResource(R.drawable.ic_tick);
        }

    }
}
