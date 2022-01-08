package com.ringly.customer_app.entities;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.ringly.customer_app.R;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.google.firebase.database.DatabaseReference;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AudioDownloadUtility extends AsyncTask<Void, String, String> {

    private final int fileType;
    private final String savePath;
    private final List<FileModel> fileModelList;
    private final boolean isToShowProgess;
    private final int downloadType;
    private HttpURLConnection connection;
    String TAG = AudioDownloadUtility.class.getSimpleName();
    private OnMediaDownloadListener mediaDownloadListener;
    private Context context;
    private int totalSize;
    private int downloadId;
    private NotificationManagerCompat notificationManager;
    private NotificationCompat.Builder builder;
    private String fileName;
    private String fileLink;
    private String fileID;
    private Integer downloadCount;
    private DatabaseReferences ringtoneRecordRef;

    public AudioDownloadUtility(Context context,int fileType, String savePath, List<FileModel> fileModelList, boolean isToShowProgess, int downloadType){
        this.fileType = fileType;
        this.savePath = savePath;
        this.fileModelList = fileModelList;
        this.isToShowProgess = isToShowProgess;
        this.context =context;
        this.downloadType =downloadType;
        mediaDownloadListener = ((OnMediaDownloadListener) context);
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        for (int i = 0; i < fileModelList.size(); i++) {
            startDownloading(fileModelList.get(i));
        }
        return null;
    }

    public void startDownloading(FileModel fileModel) {
        fileName = fileModel.getFileName()+".mp3";
        fileLink = fileModel.getFileUrl();
        fileID = fileModel.getFileId();
        downloadCount = fileModel.getDownloadCount();

        Logger.logD(TAG, "Downloading Image Link:"+fileLink);
        try {
            if ((checkConnection().contains("200"))) {
                checkFreeSpaceAndDownload();
            } else {
                Thread.sleep(200);
            }
        } catch (Exception e) {
            Logger.logE(TAG, "loopThroughPathsNDownload", e);
        }


    }

    private void checkFreeSpaceAndDownload() {
        String statusFOrDownloadSpace;
        try {
            File file = new File(savePath.concat("/" + fileName));
            boolean folderToSaveVideo;
            folderToSaveVideo = file.exists() || file.mkdirs();
            if (!folderToSaveVideo) {
                Logger.logD(TAG, context.getString(R.string.couldnot_create_file));
                statusFOrDownloadSpace = context.getString(R.string.couldnot_create_file);
//                Toast.makeText(context, statusFOrDownloadSpace, Toast.LENGTH_SHORT).show();
                return;
            }
            file = new File(file.getAbsolutePath());
            Logger.logD(TAG, " file path to download : " + file.getAbsolutePath());
            totalSize = connection.getContentLength();
            Logger.logV(TAG, "the  size of the url and file size  is........." + fileLink + "......" + totalSize);
            long megaBytes = AppUtils.getExternalFreeSpace();
            long bytesMemory = AppUtils.convertMegaBytesToBytes(megaBytes);
            if (bytesMemory > totalSize) {
                Logger.logV(TAG, " CONTENT length " + "the total size is......." + totalSize);
                if (totalSize == -1 || totalSize <= 0) {
                    Logger.logV(TAG, "the files are already downloaded" + "downloaded is completed.....");
                    Logger.logD(TAG, context.getString(R.string.content_size_invalid));
                }
                checkFileExits(file);
            } else {
//                Toast.makeText(context, context.getString(R.string.memory_is_not_there_to_download_the_videos), Toast.LENGTH_SHORT).show();
                Logger.logD(TAG, context.getString(R.string.insufficient_memory));
            }
        } catch (Exception e) {
            Logger.logE(TAG, fileName, e);
        }
    }

    private int calculatelengthofFileInMB(long file) {

        long inkB = file / 1024;
        return (int) inkB / 1024;


    }

    private void checkFileExits(File file) {
        try {
            if (file.exists()) {
                cleanUp(file.getAbsolutePath());
            }
            file.createNewFile();
            downloadVideo(fileLink, savePath, fileName);
            Log.v(TAG, "the size of the file is......" + file.getName());

        } catch (Exception e) {
            Logger.logE(TAG, " checkFileExits ", e);
        }
    }

    private void cleanUp(String path) throws IOException {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Path paths = Paths.get(path);
            Files.delete(paths);
        }
    }

    private String checkConnection() {
        String strRes;
        try {
            URL url = new URL(fileLink);
            Logger.logV(TAG, "path of the video is : " + fileLink);
            Logger.logV(TAG, "the video path is " + "the url is......." + url);
            connection = (HttpURLConnection) url.openConnection();
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            connection.connect();
            Logger.logV(TAG, "the response code is " + url + "\n " + connection.getResponseCode());
            strRes = "" + connection.getResponseCode() + " " + connection.getResponseMessage();
        } catch (Exception e) {
            Logger.logE(TAG, "the downloading video error is", e);
            strRes = context.getString(R.string.couldnot_connect_to_server);
        }
        return strRes + " \n " + fileName;
    }

    private void downloadVideo(final String videourl, String downloadPath, String fileName) {

        downloadId = PRDownloader.download(videourl, downloadPath, fileName)
                .build()
                .setOnStartOrResumeListener(() -> {
                    showProgressDialoG();
                    showNotification();
                })
                .setOnPauseListener(() -> Logger.logD(TAG + "on Pause VideoDownloading: ", fileName))
                .setOnCancelListener(() -> {
                    dismissDialog();
                    dismissNotification(false);
                })
                .setOnProgressListener(progress -> {
                    setProgressOnDialog(progress, fileName);
                    showNotificationProgress(progress);
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        updateDownload();
                        dismissDialog();
                        dismissNotification(true);
                        mediaDownloadListener.onMediaDownload(fileType,downloadPath+"/"+ fileName, fileName, downloadType);
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                        dismissDialog();
                        dismissNotification(false);
                    }

                });

    }

    private void updateDownload() {

        downloadCount = downloadCount+1;
        if (isToShowProgess){
            DatabaseReference reference = DatabaseReferences.getRingtoneReference().child(fileID);
            Map<String, Object> updates = new HashMap<>();
            updates.put(Constant.FB_RING_DownloadCount, downloadCount);
            reference.updateChildren(updates);
        }
    }

    private void dismissNotification(boolean isComplete) {
        if (!isToShowProgess)
            return;
        if (isComplete)
            builder.setContentText(context.getString(R.string.down_comp));
        builder.setProgress(0, 0, false);
        notificationManager.notify(1, builder.build());
    }

    private void showNotificationProgress(Progress progress) {
        if (!isToShowProgess)
            return;
        builder.setProgress(calculatelengthofFileInMB(progress.totalBytes), calculatelengthofFileInMB(progress.currentBytes), false);
        notificationManager.notify(1, builder.build());
    }

    private void showNotification() {
        if (!isToShowProgess)
            return;

        notificationManager = NotificationManagerCompat.from(context);
        builder = new NotificationCompat.Builder(context, Constant.CHANNEL_ID);
        builder.setContentTitle(context.getString(R.string.app_name) + ": " + fileName)
                .setContentText(context.getString(R.string.down_prog))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_LOW);
        notificationManager.notify(1, builder.build());
    }

    private void setProgressOnDialog(Progress progress, String videoName) {
        if (!isToShowProgess)  // we are not displaying progress dialog for audio downloading
            return;

        int downloadedPercentage = (int) ((progress.currentBytes * 100) / progress.totalBytes);
        tvPercentage.setText(""+downloadedPercentage+"%");
        tvContentName.setText(videoName);
        progressBar.setProgress(downloadedPercentage);
        btnCancel.setOnClickListener(view -> {
            dismissDialog();
            dismissNotification(false);
            dismissDownloading();
        });

        btnBackground.setOnClickListener(view -> dismissDialog());

    }

    private void dismissDialog() {
        if (dialog != null && context != null)
            dialog.dismiss();
    }

    private Dialog dialog;
    private ProgressBar progressBar;
    private Button btnCancel;
    private Button btnBackground;
    private TextView tvPercentage;
    private TextView tvContentName;

    private void showProgressDialoG() {
        if (!isToShowProgess)
            return;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_progress_dialog_view);
        progressBar = dialog.findViewById(R.id.progressBar);
        btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(view -> {

        });
        btnBackground = dialog.findViewById(R.id.btnBackground);
        tvPercentage = dialog.findViewById(R.id.tvPercentage);
        tvContentName = dialog.findViewById(R.id.tvContentName);
        dialog.show();
    }

    private void dismissDownloading() {
        if (!isToShowProgess)  // we are not displaying progress dialog for audio downloading
            return;
        PRDownloader.cancel(downloadId);
    }


}
