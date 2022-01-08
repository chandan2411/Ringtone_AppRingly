package com.ringly.customer_app.views.adapters;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ringly.customer_app.R;
import com.ringly.customer_app.entities.DatabaseReferences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class RingtoneUploadActivity extends AppCompatActivity {
    private static final int CHOOSE_AUDIO = 1;

    private Button chooseAudio, btnUploadAudio;
    private EditText audioDescription;
    private ProgressBar uploadProgress;

    private Uri mp3Url;

    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ring_upload);
        uploadProgress = findViewById(R.id.uploadProgress);
        chooseAudio = findViewById(R.id.chooseAudio);
        btnUploadAudio = findViewById(R.id.btnUploadAudio);

        audioDescription = findViewById(R.id.audioDescription);

        mStorageRef = DatabaseReferences.getRingtoneStorageRef();
        mDatabaseRef = DatabaseReferences.getRingtoneReference();


        btnUploadAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(RingtoneUploadActivity.this, "Upload in progress", Toast.LENGTH_LONG).show();
                } else {
                    uploadAudio();
                }
            }
        });


        chooseAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChoose();
            }
        });
    }

    private void showFileChoose() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_AUDIO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_AUDIO && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mp3Url = data.getData();

        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadAudio() {
        if (mp3Url != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mp3Url));

            mUploadTask = fileReference.putFile(mp3Url)
                    .addOnSuccessListener(  new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    uploadProgress.setProgress(0);
                                }
                            }, 500);
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String uploadID = mDatabaseRef.push().getKey();
                                    Upload upload = new Upload(uploadID,audioDescription.getText().toString().trim(), uri.toString());
                                    mDatabaseRef.child(uploadID).setValue(upload);
                                    Toast.makeText(RingtoneUploadActivity.this, "Upload successfully", Toast.LENGTH_LONG).show();
                                    audioDescription.setText("");
                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            uploadProgress.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(RingtoneUploadActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}
