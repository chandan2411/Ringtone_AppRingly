package com.ringly.customer_app.entities;


import com.ringly.customer_app.models.RingtoneModel;
import com.ringly.customer_app.models.UserModel;
import com.ringly.customer_app.models.WallPaperModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class DatabaseReferences {

    private static DatabaseReference ringtoneCategoryRef;
    private static DatabaseReference userRef;
    private static DatabaseReference ringtoneRef;
    private static DatabaseReference userRingtoneFavRef;
    private static DatabaseReference userWallpaperFavRef;

    private static StorageReference ringtoneStorageRef;

    private DatabaseReferences() {
    }

    public static DatabaseReference getUserReference(String userId) {
        if (userRef == null) {
            userRef = FirebaseDatabase
                    .getInstance()
                    .getReference(Constant.FB_USER)
                    .child(userId);
        }
        return userRef;
    }

    public static DatabaseReference getUserFavoriteRingtoneReference(String userId) {
        if (userRingtoneFavRef == null) {
            userRingtoneFavRef = FirebaseDatabase
                    .getInstance()
                    .getReference(Constant.FB_FAV_RINGTONE)
                    .child(userId);
        }
        return userRingtoneFavRef;
    }

    public static DatabaseReference getUserFavoriteWallpaperReference(String userId) {
        if (userWallpaperFavRef == null) {
            userWallpaperFavRef = FirebaseDatabase
                    .getInstance()
                    .getReference(Constant.FB_FAV_WALLPAPER)
                    .child(userId);
        }
        return userWallpaperFavRef;
    }



    public static DatabaseReference getRingtoneReference() {
        if (ringtoneRef == null) {
            ringtoneRef = FirebaseDatabase
                    .getInstance()
                    .getReference(Constant.FB_RINGTONE);
        }
        return ringtoneRef;
    }

    public static void makeRingtoneUserFavourite(RingtoneModel ringtoneModel, String userId) {
        getUserFavoriteRingtoneReference(userId).child(ringtoneModel.getRingtoneId()).setValue(ringtoneModel);
    }

    public static void makeWallpaperUserFavourite(WallPaperModel ringtoneModel, String userId) {
        getUserFavoriteWallpaperReference(userId).child(ringtoneModel.getWallPaperId()).setValue(ringtoneModel);
    }

    public static void removeRingtoneFromUserFavourite(String ringtoneId) {
        if (userRingtoneFavRef != null)
            userRingtoneFavRef.child(ringtoneId).removeValue();
    }

    public static void removeWallpaperFromUserFavourite(String ringtoneId) {
        if (userWallpaperFavRef != null)
            userWallpaperFavRef.child(ringtoneId).removeValue();
    }

    public static void registerUser(UserModel model) {
        DatabaseReference drForUSer = FirebaseDatabase.getInstance().getReference(Constant.FB_USER).child(model.getUserId());
        drForUSer.setValue(model);
    }

    public static StorageReference getRingtoneStorageRef() {
        if (ringtoneStorageRef == null) {
            ringtoneStorageRef = FirebaseStorage.getInstance().getReference(Constant.FB_RINGTONE);
        }
        return ringtoneStorageRef;
    }

    public static DatabaseReference getRingtoneCategoryRefence() {
        if (ringtoneCategoryRef == null) {
            ringtoneCategoryRef = FirebaseDatabase.getInstance().getReference(Constant.FB_RINGTONE_CATEGORY);
        }
        return ringtoneCategoryRef;
    }
}
