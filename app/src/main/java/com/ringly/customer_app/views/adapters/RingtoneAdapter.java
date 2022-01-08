package com.ringly.customer_app.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.ringly.customer_app.entities.AppUtils;
import com.ringly.customer_app.R;
import com.ringly.customer_app.entities.Constant;
import com.ringly.customer_app.entities.DatabaseReferences;
import com.ringly.customer_app.entities.Logger;
import com.ringly.customer_app.entities.MySharedPref;
import com.ringly.customer_app.models.RingtoneModel;
import com.ringly.customer_app.views.activities.RingtoneDetailActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class RingtoneAdapter extends RecyclerView.Adapter<RingtoneAdapter.RingtoneViewHolder> {
    private static final String TAG = "RecyclerAdapter";
    private  String categoryID;

    private Context context;
    private ArrayList<RingtoneModel> allSongs;
    private boolean isAudioPlaying = false;
    private MediaPlayer mediaPlayer1;
    private Button playingView;
    private Map<View, Integer> playPauseHashMap = new HashMap<>();
    private Map<View, Integer> seekbarHashMap = new HashMap<>();
    private Map<ImageView, Boolean> favCheckUncheck = new HashMap<>();
    private HashMap<String, Boolean> favHash= new HashMap<>();
    private Timer timer;
    private Boolean isFavFrag;
    private List<Integer> colorCodeList;
    private boolean isPrepared = true;
    private RingtoneViewHolder ringtoneViewHolderCurrentPlaying;
    private int playingPosition;


    public RingtoneAdapter(Context context, List<RingtoneModel> allSongs, Boolean isFavFrag) {
        this.context = context;
        mediaPlayer1 = new MediaPlayer();
        this.allSongs = (ArrayList<RingtoneModel>) allSongs;
        this.isFavFrag = isFavFrag;
        createRandomColorList(allSongs.size());
    }

    public RingtoneAdapter(Context context, Boolean isFavFrag, String categoryID) {
        this.context = context;
        mediaPlayer1 = new MediaPlayer();
        this.isFavFrag = isFavFrag;
        this.categoryID = categoryID;
    }

    private void createRandomColorList(int size) {
        int i=0;
        colorCodeList = new ArrayList<>();
        while(i<size){
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            colorCodeList.add(color);
            i++;
        }
    }

    @NonNull
    @Override
    public RingtoneViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder: Element view of recycler list");
        View v = LayoutInflater.from(context).inflate(R.layout.ringtone_item_layout,viewGroup,false);
        return new RingtoneViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final RingtoneAdapter.RingtoneViewHolder ringtoneViewHold, final int position) {
        final RingtoneModel ringtoneModel = allSongs.get(position);
        RingtoneViewHolder ringtoneViewHolder = ringtoneViewHold;
        ringtoneViewHolder.audio_description.setText(ringtoneModel.getRingtoneName());
        int color = AppUtils.getColorCode(context, position);
        ringtoneViewHolder.cardb.setBackgroundColor(color);

        int downloadCount=ringtoneModel.getRingtoneDownloadCount();
        int likesCount=ringtoneModel.getRingtoneUsedAsFavourite();
        String download="0";
        String likes="0";
        if (downloadCount>=1000){
            downloadCount = downloadCount/1000;
            download = downloadCount+"k";
        }else {
            download = String.valueOf(downloadCount);
        }
        ringtoneViewHold.tvDownloadCount.setText(download);

        /*if (likesCount>=1000){
            likesCount = likesCount/1000;
            likes = likesCount+"k";
        }else {
            likes = String.valueOf(likesCount);
        }*/
        /*ringtoneViewHold.tvLikesCount.setText(likes);*/

        if (ringtoneModel.getIsPlaying().equalsIgnoreCase("false")){
            ringtoneViewHold.seekBar.setVisibility(View.INVISIBLE);
            ringtoneViewHold.play_audio.setBackgroundResource(R.drawable.play_image);
        }else {
            ringtoneViewHold.seekBar.setVisibility(View.VISIBLE);
            ringtoneViewHold.play_audio.setBackgroundResource(R.drawable.pause_image);
        }

        /*Checking for the Fragment if come from FavouriteFragment icon wont show*/
        if(userSignedIn()) {
           /* if (isFavFrag) {
                ringtoneViewHolder.favourite.setVisibility(View.INVISIBLE);
                ringtoneViewHolder.favourite.setEnabled(false);
            } else {*/
                ringtoneViewHolder.favourite.setVisibility(View.VISIBLE);
                ringtoneViewHolder.favourite.setEnabled(true);
                checkForFavRingtone(ringtoneModel, ringtoneViewHolder);
            /*}*/
        }

        ringtoneViewHolder.play_audio.setOnClickListener(v -> {
            if (isPrepared) {
                playPauseRingtone(ringtoneModel.getRingtoneLink(), ringtoneViewHolder, position);
            }
        });

        mediaPlayer1.setOnCompletionListener(mediaPlayer -> {
            stopAudioAndTimer(ringtoneViewHolderCurrentPlaying);
            ringtoneViewHolder.seekBar.setVisibility(View.INVISIBLE);
            isAudioPlaying = false;
            allSongs.get(playingPosition).setIsPlaying("false");
            ringtoneViewHolder.play_audio.setBackgroundResource(R.drawable.play_image);
        });

        /*click event for making ringtone favourite*/
        ringtoneViewHolder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavFrag)
                    return;
                if (userSignedIn()) {  // if user is sign then it will make fav else login first
                    setFavMark(ringtoneModel, ringtoneViewHolder);
                }else {
                    AppUtils.moveToSignInActivity(context);
                }
            }
        });


        ringtoneViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RingtoneDetailActivity.class);
                /*intent.putParcelableArrayListExtra("Ringtone", allSongs);*/
                int position = (allSongs.size()-1)-ringtoneModel.getPosition();
                intent.putExtra("Position", position);
                intent.putExtra("CategoryID", categoryID);
                intent.putExtra("Scroll", true);
                context.startActivity(intent);
            }
        });



    }

    private void setFavMark(RingtoneModel ringtoneModel, RingtoneViewHolder ringtoneViewHolder) {
        if (favCheckUncheck.containsKey(ringtoneViewHolder.favourite)) {
            callApiForRemoveRingtoneFav(ringtoneModel.getRingtoneId());
            ringtoneViewHolder.favourite.setImageResource(R.drawable.ic_fav_unselected);
            favHash.remove(ringtoneModel.getRingtoneId());
            favCheckUncheck.remove(ringtoneViewHolder.favourite);
        } else {
            favCheckUncheck.put(ringtoneViewHolder.favourite, true);
            favHash.put(ringtoneModel.getRingtoneId(), true);
            callApiForMakingRingtoneFav(ringtoneModel);
            ringtoneViewHolder.favourite.setImageResource(R.drawable.ic_favorite_selected);
        }
    }

    private void checkForFavRingtone(RingtoneModel ringtoneModel, RingtoneViewHolder ringtoneViewHolder) {
        if (favHash.containsKey(ringtoneModel.getRingtoneId())) {
            ringtoneViewHolder.favourite.setImageResource(R.drawable.ic_favorite_selected);
            favCheckUncheck.put(ringtoneViewHolder.favourite,true);
        }else {
            ringtoneViewHolder.favourite.setImageResource(R.drawable.ic_fav_unselected);
            favCheckUncheck.remove(ringtoneViewHolder.favourite);
        }
    }

    private void playPauseRingtone(String ringtoneLink, final RingtoneViewHolder ringtoneViewHolder, int position) {

        if (isAudioPlaying){

            /*Stop Audio If Playing*/
            if(mediaPlayer1 != null) {
                stopAudioAndTimer(ringtoneViewHolderCurrentPlaying);
            }

            if (playPauseHashMap.containsKey(ringtoneViewHolder.play_audio)){
                ringtoneViewHolder.play_audio.setBackgroundResource(R.drawable.play_image);
                ringtoneViewHolder.seekBar.setVisibility(View.INVISIBLE);
                playPauseHashMap.remove(ringtoneViewHolder.play_audio);
                seekbarHashMap.remove(ringtoneViewHolder.seekBar);
            }else {

                try {
                    /**/
                    for (View view : playPauseHashMap.keySet()){
                        view.setVisibility(View.VISIBLE);
                        view.setBackgroundResource(R.drawable.play_image);

                    }

                    for (View view : seekbarHashMap.keySet()){
                        view.setVisibility(View.INVISIBLE);
                    }
                    ringtoneViewHolder.play_audio.setVisibility(View.INVISIBLE);
                    ringtoneViewHolder.progress.setVisibility(View.VISIBLE);
                    mediaPlayer1.reset();
                    mediaPlayer1.setDataSource(ringtoneLink);
                    mediaPlayer1.prepareAsync();
                    for (View view : playPauseHashMap.keySet()) {
                        playPauseHashMap.remove(view);
                    }
                    for (View view : seekbarHashMap.keySet()) {
                        seekbarHashMap.remove(view);
                    }
                    playPauseHashMap.put(ringtoneViewHolder.play_audio, 1);
                    seekbarHashMap.put(ringtoneViewHolder.seekBar, 1);
                    isPrepared = false;
                    mediaPlayer1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            ringtoneViewHolderCurrentPlaying = ringtoneViewHolder;
                            ringtoneViewHolder.play_audio.setBackgroundResource(R.drawable.play_image);
                            isAudioPlaying = true;
                            mp.start();
                            isPrepared = true;
                            playingPosition =position;
                            Logger.logD(TAG, "playing pos "+playingPosition);
                            allSongs.get(position).setIsPlaying("true");
                            ringtoneViewHolder.play_audio.setVisibility(View.VISIBLE);
                            ringtoneViewHolder.progress.setVisibility(View.GONE);
                            ringtoneViewHolder.play_audio.setBackgroundResource(R.drawable.pause_image);
                            ringtoneViewHolder.seekBar.setVisibility(View.VISIBLE);


                            timer = new Timer();
                            timer.scheduleAtFixedRate(new TimerTask() {
                                @Override
                                public void run() {
                                    ringtoneViewHolder.seekBar.setProgress(mediaPlayer1.getCurrentPosition());
                                    ringtoneViewHolder.seekBar.setMax(mediaPlayer1.getDuration());
                                }
                            }, 0, 100);
                        }
                    });
                } catch (Exception ex) {
                    Log.e(TAG, "" + ex.getMessage());
                }
            }
        }else {
            try {
                ringtoneViewHolder.play_audio.setVisibility(View.GONE);
                ringtoneViewHolder.progress.setVisibility(View.VISIBLE);
                mediaPlayer1.reset();
                mediaPlayer1.setDataSource(ringtoneLink);
                mediaPlayer1.prepareAsync();
                isPrepared = false;
                playPauseHashMap.put(ringtoneViewHolder.play_audio, 1);
                seekbarHashMap.put(ringtoneViewHolder.seekBar, 1);
                mediaPlayer1.setOnPreparedListener(mp -> {
                    mp.start();
                    isAudioPlaying = true;
                    isPrepared = true;
                    playingPosition =position;
                    Logger.logD(TAG, "playing pos "+playingPosition);
                    ringtoneViewHolderCurrentPlaying = ringtoneViewHolder;
                    ringtoneViewHolder.play_audio.setVisibility(View.VISIBLE);
                    ringtoneViewHolder.progress.setVisibility(View.GONE);
                    allSongs.get(position).setIsPlaying("true");
                    ringtoneViewHolder.play_audio.setBackgroundResource(R.drawable.pause_image);
                    ringtoneViewHolder.seekBar.setVisibility(View.VISIBLE);

                    timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            ringtoneViewHolder.seekBar.setProgress(mediaPlayer1.getCurrentPosition());
                            ringtoneViewHolder.seekBar.setMax(mediaPlayer1.getDuration());
                        }
                    }, 0, 100);
                });
            } catch (Exception ex) {
                Log.e(TAG, "" + ex.getMessage());
            }
        }
    }

    /**
     * Background Runnable thread
     * */


    public void stopAudioAndTimer(RingtoneViewHolder ringtoneViewHolder) {
        try {
            isAudioPlaying = false;
            if (mediaPlayer1!=null /*&& mediaPlayer1.isPlaying()*/) {
                mediaPlayer1.stop(); //error
                mediaPlayer1.reset();
            }
        } catch (Exception e) {
            Log.d("Nitif Activity", e.toString());
        }
        if(timer != null) {
            timer.cancel();
            timer = null;
        }

        if (ringtoneViewHolder!=null) {
            if (!isPrepared) {
                ringtoneViewHolder.play_audio.setVisibility(View.VISIBLE);
                ringtoneViewHolder.progress.setVisibility(View.GONE);
                isPrepared = true;
            }
                ringtoneViewHolder.seekBar.setVisibility(View.INVISIBLE);
                isAudioPlaying = false;
                allSongs.get(playingPosition).setIsPlaying("false");
                ringtoneViewHolder.play_audio.setBackgroundResource(R.drawable.play_image);
            }

    }

    private boolean userSignedIn() {
        return new MySharedPref(context).readBoolean(Constant.IS_USER_LOGIN,false);
    }

    private void callApiForMakingRingtoneFav(RingtoneModel ringtoneModel) {
        DatabaseReferences.makeRingtoneUserFavourite(ringtoneModel, new MySharedPref(context).readString(Constant.USER_ID,""));
    }

    private void callApiForRemoveRingtoneFav(String ringtoneId) {
        DatabaseReferences.removeRingtoneFromUserFavourite(ringtoneId);
    }

    public void playRingtone(String imgUrl, final RingtoneViewHolder ringtoneViewHolder) {

        if (!isAudioPlaying) {
            isAudioPlaying = true;
            try {
                mediaPlayer1.setDataSource(imgUrl);
                mediaPlayer1.prepareAsync();
                mediaPlayer1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        ringtoneViewHolder.seekBar.setVisibility(View.VISIBLE);
                    }
                });
                mediaPlayer1.prepareAsync();

            } catch (Exception e) {
                e.getMessage();
            }

        } else {
            mediaPlayer1.pause();
            ringtoneViewHolder.seekBar.setVisibility(View.INVISIBLE);
            mediaPlayer1.reset();
            isAudioPlaying = false;
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ringtoneViewHolder.seekBar.setProgress(mediaPlayer1.getCurrentPosition());
                ringtoneViewHolder.seekBar.setMax(mediaPlayer1.getDuration());
            }
        },0,100);
    }

    @Override
    public int getItemCount() {
        return allSongs==null|| allSongs.isEmpty()?0:allSongs.size();
    }

    public void setFavData(HashMap<String, Boolean> favHash) {
        this.favHash.putAll(favHash);
        notifyDataSetChanged();
    }

    

    public void setList(List<RingtoneModel> datalist) {
        this.allSongs = (ArrayList<RingtoneModel>) datalist;
        createRandomColorList(allSongs.size());
        notifyDataSetChanged();
    }

    public RingtoneViewHolder getCurrentView() {
        return ringtoneViewHolderCurrentPlaying;
    }

    public interface OnItemClickListener {
    }


    public class RingtoneViewHolder extends RecyclerView.ViewHolder {

        public TextView audio_description;
        public CardView card_view;
        public Button play_audio;
        public ImageView favourite;
        public LinearLayout cardb;
        public ProgressBar progress;
        public SeekBar seekBar;
        public TextView tvDownloadCount;
        public TextView tvLikesCount;

        public RingtoneViewHolder(@NonNull View itemView) {
            super(itemView);
            audio_description = itemView.findViewById(R.id.audio_description);
            card_view = itemView.findViewById(R.id.card_View);
            play_audio = itemView.findViewById(R.id.btnPlayPause);
            cardb = itemView.findViewById(R.id.cardback);
            seekBar = itemView.findViewById(R.id.seekBar);
            progress = itemView.findViewById(R.id.progress_play);
            tvDownloadCount = itemView.findViewById(R.id.tvDownloadCount);
            tvLikesCount = itemView.findViewById(R.id.tvLikesCount);
            favourite = itemView.findViewById(R.id.ivFav);



        }


    }
}



