package com.ringly.customer_app.views.adapters;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ringly.customer_app.R;

import java.util.List;
import java.util.Random;

public class PopularRingAdapter extends RecyclerView.Adapter<PopularRingAdapter.RingtoneViewHolder> {
    private static final String TAG = "RecyclerAdapter";

    private Context context;
    private List<RingtoneObject> allSongs;
    private boolean isAudioPlaying = false;
    private boolean playPauseBtn = true;
    private boolean progressbar = false;
    MediaPlayer mediaPlayer1;
    private Button playingView;




    public PopularRingAdapter(Context context, List<RingtoneObject> allSongs) {
        this.context = context;
        mediaPlayer1 = new MediaPlayer();
        this.allSongs = allSongs;

    }


    @NonNull
    @Override
    public PopularRingAdapter.RingtoneViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: Element view of recycler list");
        View v = LayoutInflater.from(context).inflate(R.layout.ringtone_item_layout,viewGroup,false);
        return new PopularRingAdapter.RingtoneViewHolder(v);

    }


    @Override
    public void onBindViewHolder(@NonNull final PopularRingAdapter.RingtoneViewHolder ringtoneViewHolder, final int i) {
        final RingtoneObject uploadCur = allSongs.get(i);
        if (uploadCur.isFavourite) {
            ringtoneViewHolder.checkBoxFav.setChecked(true);
        }
        ringtoneViewHolder.audio_description.setText(uploadCur.imgName);
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        ringtoneViewHolder.cardb.setBackgroundColor(color);
        ringtoneViewHolder.play_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playPauseBtn) {
                    playingView = ringtoneViewHolder.play_audio;
                    if (mediaPlayer1.isPlaying()) {
                        mediaPlayer1.stop();
                        mediaPlayer1.prepareAsync();
                        ringtoneViewHolder.play_audio.setBackgroundResource(R.drawable.play_image);


                    } else {
                        mediaPlayer1.start();
                        ringtoneViewHolder.play_audio.setBackgroundResource(R.drawable.pause_image);
                    }
                    playPauseBtn = false;
                } else {
                    if (mediaPlayer1.isPlaying()) {
                        mediaPlayer1.stop();
                        mediaPlayer1.prepareAsync();
                        ringtoneViewHolder.play_audio.setBackgroundResource(R.drawable.play_image);

                    } else {
                        mediaPlayer1.start();
                        ringtoneViewHolder.play_audio.setBackgroundResource(R.drawable.pause_image);
                    }
                    playingView.setBackgroundResource(R.drawable.play_image);
                    playPauseBtn = true;
                }
                playRingtone(uploadCur.imgUrl, ringtoneViewHolder);

            }
        });
        ringtoneViewHolder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringtoneViewHolder.favourite.setBackgroundResource(R.drawable.ic_favorite_selected);
            }
        });


    }

    public void playRingtone(String imgUrl, final RingtoneViewHolder ringtoneViewHolder) {

        if (!isAudioPlaying) {
            isAudioPlaying = true;
            try {
                mediaPlayer1.stop();
                mediaPlayer1.setDataSource(imgUrl);
                mediaPlayer1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                mediaPlayer1.prepareAsync();

            } catch (Exception e) {
                e.getMessage();
            }

        } else {
            mediaPlayer1.pause();
            mediaPlayer1.reset();
            isAudioPlaying = false;
        }
    }

    @Override
    public int getItemCount() {
        return allSongs.size();
    }



    public class RingtoneViewHolder extends RecyclerView.ViewHolder {
        public TextView audio_description;
        public CardView card_view;
        public Button play_audio;
        public ImageView favourite;
        public RelativeLayout cardb;
        public ProgressBar progress;
        public SeekBar seekBar;
        CheckBox checkBoxFav;

        public RingtoneViewHolder(@NonNull View itemView) {
            super(itemView);
            audio_description = itemView.findViewById(R.id.audio_description);
            card_view = itemView.findViewById(R.id.card_View);
            play_audio = itemView.findViewById(R.id.btnPlayPause);
            cardb = itemView.findViewById(R.id.cardback);
            seekBar = itemView.findViewById(R.id.seekBar);
            progress = itemView.findViewById(R.id.progress_play);
//            favourite = itemView.findViewById(R.id.isFav);

        }





    }
}