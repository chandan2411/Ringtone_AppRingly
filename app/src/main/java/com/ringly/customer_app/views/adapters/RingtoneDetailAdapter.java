package com.ringly.customer_app.views.adapters;


import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.ringly.customer_app.R;
import com.ringly.customer_app.entities.AppUtils;
import com.ringly.customer_app.entities.Utilities;
import com.ringly.customer_app.models.RingtoneModel;
import com.ringly.customer_app.views.activities.RecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RingtoneDetailAdapter extends RecyclerView.Adapter<RingtoneDetailAdapter.RingtoneViewHolder> {

    private Context context;
    private ArrayList<RingtoneModel> allSongs;
    private boolean isAudioPlaying = false;
    private MediaPlayer mediaPlayer;
    private boolean isAudioInitialize = false;
    private Timer timer;
    private Map<View, Integer> playPauseHashMap = new HashMap<>();
    private List<Integer> colorCodeList;
    private Map<ImageView, Boolean> favCheckUncheck = new HashMap<>();
    private HashMap<String, Boolean> favHash = new HashMap<>();
    private boolean isOptionsShowing=false;
    private RecyclerViewScrollListener listener;
    private RingtoneDetailAdapter.RingtoneViewHolder viewHolder;
    private boolean isPrepared = true;
    private Handler mHandler = new Handler();;
    private long totalDuration =0L;
    private long audioDuration =0L;
    private DurationListener durationListener;


    public interface DurationListener{
        void onDuration(View view, Long duartion);
    }


    private void createRandomColorList(int size) {
        int i = 0;
        colorCodeList = new ArrayList<>();
        while (i < size) {
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            colorCodeList.add(color);
            i++;
        }
    }

    public List<Integer> getColorCodeList(){
        return colorCodeList;
    }

    private void checkForFavRingtone(RingtoneModel ringtoneModel) {
        if (favHash.containsKey(ringtoneModel.getRingtoneId())) {
            listener.OnScrollListener(ringtoneModel, true);
        }else{
            listener.OnScrollListener(ringtoneModel, false);
        }
    }




    public RingtoneDetailAdapter(Context context, ArrayList<RingtoneModel> ringtoneModelList, HashMap<String, Boolean> favHash) {
        allSongs = ringtoneModelList;
        this.context = context;
        mediaPlayer = new MediaPlayer();
        createRandomColorList(allSongs.size());
//        this.favHash.putAll(favHash);
        listener = (RecyclerViewScrollListener) context;
        durationListener = (DurationListener) context;

    }

    @NonNull
    @Override
    public RingtoneDetailAdapter.RingtoneViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.adapter_ringtone_detail, viewGroup, false);
        return new RingtoneDetailAdapter.RingtoneViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull final RingtoneDetailAdapter.RingtoneViewHolder ringtoneViewHolder, int position) {
        final RingtoneModel ringtoneModel = allSongs.get(position);
        int color = AppUtils.getColorCode(context, position);
        ringtoneViewHolder.cardView.setCardBackgroundColor(color);
        ringtoneViewHolder.audio_description.setText(ringtoneModel.getRingtoneName());
//        setAudioDuration(ringtoneViewHolder, ringtoneModel);
        int downloadCount=ringtoneModel.getRingtoneDownloadCount();
        int likesCount=ringtoneModel.getRingtoneUsedAsFavourite();
        String download="0";
        String likes="0";
        if(ringtoneModel.getRingtoneDuration()!=null){
            audioDuration = ringtoneModel.getRingtoneDuration();
            ringtoneViewHolder.tvAudioTimer.setText(audioDuration+"s");
        }

        if (downloadCount>=1000){
            downloadCount = downloadCount/1000;
            download = downloadCount+"k";
        }else {
            download = String.valueOf(downloadCount);
        }
        ringtoneViewHolder.tvDownloadCount.setText(download);

        if (likesCount>=1000){
            likesCount = likesCount/1000;
            likes = likesCount+"k";
        }else {
            likes = String.valueOf(likesCount);
        }
        ringtoneViewHolder.tvLikesCount.setText(likes);

        checkForFavRingtone(ringtoneModel);

        ringtoneViewHolder.play_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalDuration = audioDuration;
                viewHolder = ringtoneViewHolder;
                playPauseRingtone(ringtoneModel.getRingtoneLink(), ringtoneViewHolder);
            }
        });

        if (ringtoneModel.getIsPlaying().equalsIgnoreCase("false")){
            ringtoneViewHolder.seekBar.setVisibility(View.INVISIBLE);
            ringtoneViewHolder.play_audio.setBackgroundResource(R.drawable.play_image);
        }else {
            ringtoneViewHolder.seekBar.setVisibility(View.VISIBLE);
            ringtoneViewHolder.play_audio.setBackgroundResource(R.drawable.pause_image);
        }
    }

    public void setAudioDuartion(RingtoneViewHolder viewHolder, long audioTime){
        viewHolder.tvAudioTimer.setText(audioTime+"s");
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        Utilities utils = new Utilities();
        public void run() {
            long countdown=0;
            long currentDuration = mediaPlayer.getCurrentPosition()/1000;
            if (totalDuration==0){
                countdown = currentDuration;
            }else {
                countdown = totalDuration-currentDuration;
            }
            // Displaying Total Duration time
            // Displaying time completed playing
//            viewHolder.tvAudioTimer.setText(""+utils.milliSecondsToTimer(currentDuration));
            viewHolder.tvAudioTimer.setText(countdown+"s");
            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 1000);
        }
    };

    public RingtoneDetailAdapter.RingtoneViewHolder getViewHolder(){
        return viewHolder;
    }

    private void setAudioDuration(RingtoneViewHolder ringtoneViewHolder, RingtoneModel ringtoneModel) {
        MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
        String mediaPath = Uri.parse(ringtoneModel.getRingtoneLink()).getPath();

        Log.d("URI", Uri.parse(ringtoneModel.getRingtoneLink()).toString());
        mRetriever.setDataSource(ringtoneModel.getRingtoneLink(), new HashMap<String, String>());
        String s = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        totalDuration = Long.parseLong(s);
//        ringtoneViewHolder.tvAudioTimer.setText(new Utilities().milliSecondsToTimer(totalDuration));
        ringtoneViewHolder.tvAudioTimer.setText(totalDuration+"s");
        mRetriever.release();

    }

    public void stopAudioAndTimer() {
        try {
//            viewHolder.tvAudioTimer.setText(new Utilities().milliSecondsToTimer(totalDuration));
            viewHolder.tvAudioTimer.setText(totalDuration+"s");
            isAudioPlaying = false;
            mediaPlayer.stop(); //error
            mediaPlayer.reset();
        } catch (Exception e) {
            Log.d("Nitif Activity", e.toString());
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (viewHolder!=null) {
            if (!isPrepared) {
                viewHolder.play_audio.setVisibility(View.VISIBLE);
                viewHolder.progress.setVisibility(View.GONE);
                isPrepared = true;
            }
            viewHolder.seekBar.setVisibility(View.INVISIBLE);
            isAudioPlaying = false;
            viewHolder.play_audio.setBackgroundResource(R.drawable.play_image);
        }

    }

    public void playPauseRingtone(String ringtoneLink, RingtoneDetailAdapter.RingtoneViewHolder ringtoneViewHolder) {

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopAudioAndTimer();
                ringtoneViewHolder.seekBar.setVisibility(View.INVISIBLE);
                isAudioPlaying = false;
                ringtoneViewHolder.play_audio.setBackgroundResource(R.drawable.play_image);
            }
        });

        if (isAudioPlaying) {
            /*Stop Audio If Playing*/
            if (mediaPlayer != null) {
                stopAudioAndTimer();
            }
            ringtoneViewHolder.play_audio.setBackgroundResource(R.drawable.play_image);
//            ringtoneViewHolder.seekBar.setVisibility(View.INVISIBLE);
            if (playPauseHashMap.containsKey(ringtoneViewHolder.play_audio)) {
                ringtoneViewHolder.play_audio.setBackgroundResource(R.drawable.play_image);
                playPauseHashMap.remove(ringtoneViewHolder.play_audio);
            } else {
                playPauseHashMap.remove(playPauseHashMap.keySet());
                playPauseHashMap.put(ringtoneViewHolder.play_audio, 1);
                try {
                    ringtoneViewHolder.progress.setVisibility(View.VISIBLE);
                    ringtoneViewHolder.play_audio.setVisibility(View.GONE);
                    mediaPlayer.setDataSource(ringtoneLink);
                    mediaPlayer.prepareAsync();
                    isPrepared = false;
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            isPrepared = true;
                            isAudioPlaying = true;
                            ringtoneViewHolder.seekBar.setVisibility(View.VISIBLE);
                            mp.start();
                            // Updating progress bar
                            updateProgressBar();
                            ringtoneViewHolder.progress.setVisibility(View.GONE);
                            ringtoneViewHolder.play_audio.setVisibility(View.VISIBLE);
                            ringtoneViewHolder.play_audio.setBackgroundResource(R.drawable.pause_image);

                            timer = new Timer();
                            timer.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    ringtoneViewHolder.seekBar.setProgress(mediaPlayer.getCurrentPosition());
                                    ringtoneViewHolder.seekBar.setMax(mediaPlayer.getDuration());
                                }
                            }, 0, 100);
                        }
                    });
                } catch (Exception ex) {
                    Log.e("bv", "" + ex.getMessage());
                }
            }
        } else {
            ringtoneViewHolder.progress.setVisibility(View.VISIBLE);
            ringtoneViewHolder.play_audio.setVisibility(View.GONE);
            playPauseHashMap.put(ringtoneViewHolder.play_audio, 1);
            try {
                mediaPlayer.setDataSource(ringtoneLink);
                mediaPlayer.prepareAsync();
                isPrepared = false;
                mediaPlayer.setOnPreparedListener(mp -> {
                    ringtoneViewHolder.seekBar.setVisibility(View.VISIBLE);
                    isPrepared = true;
                    // Updating progress bar
                    updateProgressBar();
                    ringtoneViewHolder.play_audio.setBackgroundResource(R.drawable.pause_image);
                    mp.start();
                    ringtoneViewHolder.progress.setVisibility(View.GONE);
                    ringtoneViewHolder.play_audio.setVisibility(View.VISIBLE);
                    isAudioPlaying = true;
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            ringtoneViewHolder.seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            ringtoneViewHolder.seekBar.setMax(mediaPlayer.getDuration());
                        }
                    }, 0, 100);
                });
            } catch (Exception ex) {
                Log.e("k", "" + ex.getMessage());
            }
        }
    }

    private void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    public void playRingtone(String imgUrl) {

        if (!isAudioPlaying) {
            isAudioPlaying = true;
            try {
                //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
                if (!isAudioInitialize) {
                    isAudioInitialize = true;
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(imgUrl);
                    mediaPlayer.prepare();
                }
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            mediaPlayer.pause();
            isAudioPlaying = false;
            mediaPlayer.reset();
        }
    }

    @Override
    public int getItemCount() {
        return allSongs.size();
    }

    public Boolean getAudioPlaying() {
        return isAudioPlaying;
    }

    class RingtoneViewHolder extends RecyclerView.ViewHolder {
        TextView audio_description;
        TextView tvAudioTimer;
        TextView tvDownloadCount;
        TextView tvLikesCount;
        Button play_audio;
        CardView cardView;
        public ProgressBar progress;
        public SeekBar seekBar;


        RingtoneViewHolder(@NonNull View itemView) {
            super(itemView);
            audio_description = itemView.findViewById(R.id.audio_description);
            play_audio = itemView.findViewById(R.id.btnPlayPause);
            cardView = itemView.findViewById(R.id.card_View);
            tvAudioTimer = itemView.findViewById(R.id.tvAudioTimer);
            progress = itemView.findViewById(R.id.progress_play);
            tvDownloadCount = itemView.findViewById(R.id.tvDownloadCount);
            tvLikesCount = itemView.findViewById(R.id.tvLikesCount);
            seekBar = itemView.findViewById(R.id.seekBar);

        }
    }
}