package com.ringly.customer_app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class RingtoneModel implements Parcelable {

    private String ringtoneId;

    private String userId;

    private String ringtoneName;

    private String ringtoneLink;

    private String ringtoneCategory;

    private String ringtoneCategoryId;

    private String ringtoneCreatedDate;

    private String ringtoneModifiedDate;

    private Integer ringtoneDownloadCount=0;

    private Integer ringtoneUsedCount=0;

    private Integer ringtoneUsedAsContactToneCount =0;

    private Integer ringtoneUsedAsAlarmToneCount =0;

    private Integer ringtoneUsedAsFavourite =0;

    private Integer position;

    private Long ringtoneDuration;

    private String isPlaying="false";

    public String getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(String isPlaying) {
        this.isPlaying = isPlaying;
    }

    public RingtoneModel() {
    }

    public Long getRingtoneDuration() {
        return ringtoneDuration;
    }

    public void setRingtoneDuration(Long ringtoneDuration) {
        this.ringtoneDuration = ringtoneDuration;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    protected RingtoneModel(Parcel in) {
        ringtoneId = in.readString();
        ringtoneName = in.readString();
        ringtoneLink = in.readString();
        ringtoneCategory = in.readString();
        ringtoneCategoryId = in.readString();
        ringtoneCreatedDate = in.readString();
        ringtoneModifiedDate = in.readString();
        if (in.readByte() == 0) {
            ringtoneDownloadCount = null;
        } else {
            ringtoneDownloadCount = in.readInt();
        }
        if (in.readByte() == 0) {
            ringtoneUsedCount = null;
        } else {
            ringtoneUsedCount = in.readInt();
        }
        if (in.readByte() == 0) {
            ringtoneUsedAsContactToneCount = null;
        } else {
            ringtoneUsedAsContactToneCount = in.readInt();
        }
        if (in.readByte() == 0) {
            ringtoneUsedAsAlarmToneCount = null;
        } else {
            ringtoneUsedAsAlarmToneCount = in.readInt();
        }
        if (in.readByte() == 0) {
            ringtoneUsedAsFavourite = null;
        } else {
            ringtoneUsedAsFavourite = in.readInt();
        }

        if (in.readByte() == 0) {
            position = null;
        } else {
            position = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ringtoneId);
        dest.writeString(ringtoneName);
        dest.writeString(ringtoneLink);
        dest.writeString(ringtoneCategory);
        dest.writeString(ringtoneCategoryId);
        dest.writeString(ringtoneCreatedDate);
        dest.writeString(ringtoneModifiedDate);
        if (ringtoneDownloadCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(ringtoneDownloadCount);
        }
        if (ringtoneUsedCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(ringtoneUsedCount);
        }
        if (ringtoneUsedAsContactToneCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(ringtoneUsedAsContactToneCount);
        }
        if (ringtoneUsedAsAlarmToneCount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(ringtoneUsedAsAlarmToneCount);
        }
        if (ringtoneUsedAsFavourite == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(ringtoneUsedAsFavourite);
        }
        if (position == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(position);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RingtoneModel> CREATOR = new Creator<RingtoneModel>() {
        @Override
        public RingtoneModel createFromParcel(Parcel in) {
            return new RingtoneModel(in);
        }

        @Override
        public RingtoneModel[] newArray(int size) {
            return new RingtoneModel[size];
        }
    };

    public String getRingtoneId() {
        return ringtoneId;
    }

    public void setRingtoneId(String ringtoneId) {
        this.ringtoneId = ringtoneId;
    }

    public String getRingtoneName() {
        return ringtoneName;
    }

    public void setRingtoneName(String ringtoneName) {
        this.ringtoneName = ringtoneName;
    }

    public String getRingtoneLink() {
        return ringtoneLink;
    }

    public void setRingtoneLink(String ringtoneLink) {
        this.ringtoneLink = ringtoneLink;
    }

    public String getRingtoneCategory() {
        return ringtoneCategory;
    }

    public void setRingtoneCategory(String ringtoneCategory) {
        this.ringtoneCategory = ringtoneCategory;
    }

    public String getRingtoneCategoryId() {
        return ringtoneCategoryId;
    }

    public void setRingtoneCategoryId(String ringtoneCategoryId) {
        this.ringtoneCategoryId = ringtoneCategoryId;
    }

    public String getRingtoneCreatedDate() {
        return ringtoneCreatedDate;
    }

    public void setRingtoneCreatedDate(String ringtoneCreatedDate) {
        this.ringtoneCreatedDate = ringtoneCreatedDate;
    }

    public String getRingtoneModifiedDate() {
        return ringtoneModifiedDate;
    }

    public void setRingtoneModifiedDate(String ringtoneModifiedDate) {
        this.ringtoneModifiedDate = ringtoneModifiedDate;
    }

    public Integer getRingtoneDownloadCount() {
        return ringtoneDownloadCount;
    }

    public void setRingtoneDownloadCount(Integer ringtoneDownloadCount) {
        this.ringtoneDownloadCount = ringtoneDownloadCount;
    }

    public Integer getRingtoneUsedCount() {
        return ringtoneUsedCount;
    }

    public void setRingtoneUsedCount(Integer ringtoneUsedCount) {
        this.ringtoneUsedCount = ringtoneUsedCount;
    }

    public Integer getRingtoneUsedAsContactToneCount() {
        return ringtoneUsedAsContactToneCount;
    }

    public void setRingtoneUsedAsContactToneCount(Integer ringtoneUsedAsContactToneCount) {
        this.ringtoneUsedAsContactToneCount = ringtoneUsedAsContactToneCount;
    }

    public Integer getRingtoneUsedAsAlarmToneCount() {
        return ringtoneUsedAsAlarmToneCount;
    }

    public void setRingtoneUsedAsAlarmToneCount(Integer ringtoneUsedAsAlarmToneCount) {
        this.ringtoneUsedAsAlarmToneCount = ringtoneUsedAsAlarmToneCount;
    }

    public Integer getRingtoneUsedAsFavourite() {
        return ringtoneUsedAsFavourite;
    }

    public void setRingtoneUsedAsFavourite(Integer ringtoneUsedAsFavourite) {
        this.ringtoneUsedAsFavourite = ringtoneUsedAsFavourite;
    }

}
