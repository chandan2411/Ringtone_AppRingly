package com.ringly.customer_app.databases;

import com.ringly.customer_app.entities.AppUtils;
import com.ringly.customer_app.entities.DatabaseReferences;
import com.ringly.customer_app.models.RingtoneCategoryModel;
import com.ringly.customer_app.models.RingtoneModel;
import com.ringly.customer_app.views.activities.HomeActivity;
import com.google.firebase.database.DatabaseReference;

public class UploadDataToFirebaseDbClass {

    DatabaseReference ringtoneDbRef;
    DatabaseReference ringtoneCatDbRef;

    public UploadDataToFirebaseDbClass(HomeActivity homeActivity) {
        ringtoneDbRef = DatabaseReferences.getRingtoneReference();
        ringtoneCatDbRef = DatabaseReferences.getRingtoneCategoryRefence();
    }


    public void addCategory() {

        String id = ringtoneCatDbRef.push().getKey();
        RingtoneCategoryModel model = new RingtoneCategoryModel();
        model.setRingtoneCategoryId(id);
        model.setRingtoneCategory("Trending");
        model.setCategoryImageUrl("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/ringCategory%2F1574991826608.jpg?alt=media&token=6b7b3018-d201-4912-8a5b-418cd9372547");
        ringtoneCatDbRef.child(id).setValue(model);

        String id1 = ringtoneCatDbRef.push().getKey();
        RingtoneCategoryModel model1 = new RingtoneCategoryModel();
        model1.setRingtoneCategoryId(id1);
        model1.setRingtoneCategory("Classic");
        model1.setCategoryImageUrl("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/ringCategory%2F1574991826608.jpg?alt=media&token=6b7b3018-d201-4912-8a5b-418cd9372547");
        ringtoneCatDbRef.child(id1).setValue(model1);


        String id2 = ringtoneCatDbRef.push().getKey();
        RingtoneCategoryModel model2 = new RingtoneCategoryModel();
        model2.setRingtoneCategoryId(id2);
        model2.setRingtoneCategory("Bollywood");
        model2.setCategoryImageUrl("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/ringCategory%2F1574994598423.jpg?alt=media&token=d2622d8b-7ddf-46ab-bf31-a2f17b33854b");
        ringtoneCatDbRef.child(id2).setValue(model2);


        String id3 = ringtoneCatDbRef.push().getKey();
        RingtoneCategoryModel model3 = new RingtoneCategoryModel();
        model3.setRingtoneCategoryId(id3);
        model3.setRingtoneCategory("Unplugged");
        model3.setCategoryImageUrl("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/ringCategory%2F1574994630050.jpg?alt=media&token=0f5fac1c-ab01-4aab-b96a-25c892ea075c");
        ringtoneCatDbRef.child(id3).setValue(model3);


        String id4 = ringtoneCatDbRef.push().getKey();
        RingtoneCategoryModel model4 = new RingtoneCategoryModel();
        model4.setRingtoneCategoryId(id4);
        model4.setRingtoneCategory("Oldies");
        model4.setCategoryImageUrl("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/ringCategory%2F1574994649446.jpg?alt=media&token=b2ae4297-d322-434d-87a8-f299d98fab50");
        ringtoneCatDbRef.child(id4).setValue(model4);


        String id5 = ringtoneCatDbRef.push().getKey();
        RingtoneCategoryModel model5 = new RingtoneCategoryModel();
        model5.setRingtoneCategoryId(id5);
        model5.setRingtoneCategory("Instrumental");
        model5.setCategoryImageUrl("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/ringCategory%2F1574994866724.jpg?alt=media&token=7180e70e-a939-41f1-9955-f58f8327d8cf");
        ringtoneCatDbRef.child(id5).setValue(model5);


        String id6 = ringtoneCatDbRef.push().getKey();
        RingtoneCategoryModel model6 = new RingtoneCategoryModel();
        model6.setRingtoneCategoryId(id6);
        model6.setRingtoneCategory("Love");
        model6.setCategoryImageUrl("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/ringCategory%2F1574994882638.jpg?alt=media&token=9f3c1030-49c1-46cc-9c96-b0e9fdbd091c");
        ringtoneCatDbRef.child(id6).setValue(model6);


        String id7 = ringtoneCatDbRef.push().getKey();
        RingtoneCategoryModel model7 = new RingtoneCategoryModel();
        model7.setRingtoneCategoryId(id7);
        model7.setRingtoneCategory("Music");
        model7.setCategoryImageUrl("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/ringCategory%2F1574994902921.jpg?alt=media&token=d5335d5a-8bff-4b8c-93a4-d6fa08946cba");
        ringtoneCatDbRef.child(id7).setValue(model7);


        String id8 = ringtoneCatDbRef.push().getKey();
        RingtoneCategoryModel model8 = new RingtoneCategoryModel();
        model8.setRingtoneCategoryId(id8);
        model8.setRingtoneCategory("Romantic");
        model8.setCategoryImageUrl("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/ringCategory%2F1574994923906.jpg?alt=media&token=2a10cd25-df37-407e-880a-1dcb42293a38");
        ringtoneCatDbRef.child(id8).setValue(model8);


        String id9 = ringtoneCatDbRef.push().getKey();
        RingtoneCategoryModel model9 = new RingtoneCategoryModel();
        model9.setRingtoneCategoryId(id9);
        model9.setRingtoneCategory("Reels");
        model9.setCategoryImageUrl("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/ringCategory%2F1574994943113.jpg?alt=media&token=fa8c749e-0ee5-4aba-81f0-9fc6b04b5b0c");
        ringtoneCatDbRef.child(id9).setValue(model9);

        String id11 = ringtoneCatDbRef.push().getKey();
        RingtoneCategoryModel model11 = new RingtoneCategoryModel();
        model11.setRingtoneCategoryId(id11);
        model11.setRingtoneCategory("Popular");
        model11.setCategoryImageUrl("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/ringCategory%2F1574994970577.jpg?alt=media&token=76e29e9d-5676-4b51-9a82-d7d3eed81624");
        ringtoneCatDbRef.child(id11).setValue(model11);

        String id12 = ringtoneCatDbRef.push().getKey();
        RingtoneCategoryModel model12 = new RingtoneCategoryModel();
        model12.setRingtoneCategoryId(id12);
        model12.setRingtoneCategory("Animal");
        model12.setCategoryImageUrl("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/ringCategory%2F1574995018271.jpg?alt=media&token=b06ed9b0-8393-4354-b7b5-4d32d12efd6d");
        ringtoneCatDbRef.child(id12).setValue(model12);
    }

    public void addRingtone() {

        String dateTime = AppUtils.getDateTime();


        String id = ringtoneDbRef.push().getKey();

        RingtoneModel model = new RingtoneModel();
        model.setRingtoneId(id);
        model.setRingtoneName("ringy_(1)");
        model.setRingtoneLink("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/Ringtone%2Fringy_%20(11).mpeg?alt=media&token=e4bceff2-7a77-42af-96e7-49dabc4ac73c");
        model.setRingtoneCategory("Trending");
        model.setRingtoneCategoryId("-LxgZn2gx_PfD6QDqvnG");
        model.setRingtoneCreatedDate(dateTime);
        model.setRingtoneModifiedDate(dateTime);
        model.setRingtoneDownloadCount(0);
        model.setRingtoneUsedCount(0);
        model.setRingtoneUsedAsContactToneCount(0);
        model.setRingtoneUsedAsAlarmToneCount(0);
        model.setRingtoneUsedAsFavourite(0);
        ringtoneDbRef.child(id).setValue(model);

        //////

        String id1 = ringtoneDbRef.push().getKey();

        RingtoneModel model1 = new RingtoneModel();
        model1.setRingtoneId(id1);
        model1.setRingtoneName("ringy_ (2)");
        model1.setRingtoneLink("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/Ringtone%2Fringy_%20(2).mpeg?alt=media&token=592c8d3d-5858-467d-b157-f91b531019a7");
        model1.setRingtoneCategory("Classic");
        model1.setRingtoneCategoryId("-LxgZn4FaZB4-7ucDuiO");
        model1.setRingtoneCreatedDate(dateTime);
        model1.setRingtoneModifiedDate(dateTime);
        model1.setRingtoneDownloadCount(0);
        model1.setRingtoneUsedCount(0);
        model1.setRingtoneUsedAsContactToneCount(0);
        model1.setRingtoneUsedAsAlarmToneCount(0);
        model1.setRingtoneUsedAsFavourite(0);
        ringtoneDbRef.child(id1).setValue(model1);

        //////
        String id2 = ringtoneDbRef.push().getKey();

        RingtoneModel model2 = new RingtoneModel();
        model2.setRingtoneId(id2);
        model2.setRingtoneName("ringy_ (3)");
        model2.setRingtoneLink("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/Ringtone%2Fringy_%20(3).mpeg?alt=media&token=f085c622-7ad3-4045-bd0b-3241bdc8e3ee");
        model2.setRingtoneCategory("Bollywood");
        model2.setRingtoneCategoryId("-LxgZn4bjoRQAGr2OLne");
        model2.setRingtoneCreatedDate(dateTime);
        model2.setRingtoneModifiedDate(dateTime);
        model2.setRingtoneDownloadCount(0);
        model2.setRingtoneUsedCount(0);
        model2.setRingtoneUsedAsContactToneCount(0);
        model2.setRingtoneUsedAsAlarmToneCount(0);
        model2.setRingtoneUsedAsFavourite(0);
        ringtoneDbRef.child(id2).setValue(model2);

        //////
        String id3 = ringtoneDbRef.push().getKey();

        RingtoneModel model3 = new RingtoneModel();
        model3.setRingtoneId(id3);
        model3.setRingtoneName("ringy_ (4)");
        model3.setRingtoneLink("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/Ringtone%2Fringy_%20(4).mpeg?alt=media&token=4cd787d1-97e2-4c66-a667-79fe5f520278");
        model3.setRingtoneCategory("Unplugged");
        model3.setRingtoneCategoryId("-LxgZn4xO8HpsnphVZrn");
        model3.setRingtoneCreatedDate(dateTime);
        model3.setRingtoneModifiedDate(dateTime);
        model2.setRingtoneDownloadCount(0);
        model2.setRingtoneUsedCount(0);
        model2.setRingtoneUsedAsContactToneCount(0);
        model2.setRingtoneUsedAsAlarmToneCount(0);
        model2.setRingtoneUsedAsFavourite(0);
        ringtoneDbRef.child(id3).setValue(model3);

        //////
        String id4 = ringtoneDbRef.push().getKey();

        RingtoneModel model4 = new RingtoneModel();
        model4.setRingtoneId(id4);
        model4.setRingtoneName("ringy_ (5)");
        model4.setRingtoneLink("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/Ringtone%2Fringy_%20(5).mpeg?alt=media&token=04b5b27a-301d-4a13-8049-f8a220619e19");
        model4.setRingtoneCategory("Oldies");
        model4.setRingtoneCategoryId("-LxgZn5FoBGaFtohXJSD");
        model4.setRingtoneCreatedDate(dateTime);
        model4.setRingtoneModifiedDate(dateTime);
        model2.setRingtoneDownloadCount(0);
        model2.setRingtoneUsedCount(0);
        model2.setRingtoneUsedAsContactToneCount(0);
        model2.setRingtoneUsedAsAlarmToneCount(0);
        model2.setRingtoneUsedAsFavourite(0);
        ringtoneDbRef.child(id4).setValue(model4);

        //////
        String id5 = ringtoneDbRef.push().getKey();
        RingtoneModel model5 = new RingtoneModel();
        model5.setRingtoneId(id5);
        model5.setRingtoneName("ringy_(6)");
        model5.setRingtoneLink("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/Ringtone%2Fringy_%20(6).mpeg?alt=media&token=7a76cf02-a392-4213-9856-3e54f57969d5");
        model5.setRingtoneCategory("Trending");
        model5.setRingtoneCategoryId("-LvWD_BPSqfP7raI-P2H");
        model5.setRingtoneCreatedDate(dateTime);
        model5.setRingtoneModifiedDate(dateTime);
        model2.setRingtoneDownloadCount(0);
        model2.setRingtoneUsedCount(0);
        model2.setRingtoneUsedAsContactToneCount(0);
        model2.setRingtoneUsedAsAlarmToneCount(0);
        model2.setRingtoneUsedAsFavourite(0);
        ringtoneDbRef.child(id5).setValue(model5);

        String id6 = ringtoneDbRef.push().getKey();
        RingtoneModel model6 = new RingtoneModel();
        model6.setRingtoneId(id6);
        model6.setRingtoneName("ringy_(7)");
        model6.setRingtoneLink("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/Ringtone%2Fringy_%20(7).mpeg?alt=media&token=fffc69ad-0e73-4cc4-9eb8-4b117f2dee25");
        model6.setRingtoneCategory("Trending");
        model6.setRingtoneCategoryId("-LvWD_BPSqfP7raI-P2H");
        model6.setRingtoneCreatedDate(dateTime);
        model6.setRingtoneModifiedDate(dateTime);
        model6.setRingtoneDownloadCount(0);
        model6.setRingtoneUsedCount(0);
        model6.setRingtoneUsedAsContactToneCount(0);
        model6.setRingtoneUsedAsAlarmToneCount(0);
        model6.setRingtoneUsedAsFavourite(0);
        ringtoneDbRef.child(id6).setValue(model6);

        String id7 = ringtoneDbRef.push().getKey();
        RingtoneModel model7 = new RingtoneModel();
        model7.setRingtoneId(id7);
        model7.setRingtoneName("ringy_(8)");
        model7.setRingtoneLink("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/Ringtone%2Fringy_%20(8).mpeg?alt=media&token=603e3ffb-2a65-4363-b3f3-1dc810b19cf1");
        model7.setRingtoneCategory("Trending");
        model7.setRingtoneCategoryId("-LvWD_BPSqfP7raI-P2H");
        model7.setRingtoneCreatedDate(dateTime);
        model7.setRingtoneModifiedDate(dateTime);
        model7.setRingtoneDownloadCount(0);
        model7.setRingtoneUsedCount(0);
        model7.setRingtoneUsedAsContactToneCount(0);
        model7.setRingtoneUsedAsAlarmToneCount(0);
        model7.setRingtoneUsedAsFavourite(0);
        ringtoneDbRef.child(id7).setValue(model7);

        String id8 = ringtoneDbRef.push().getKey();
        RingtoneModel model8 = new RingtoneModel();
        model8.setRingtoneId(id8);
        model8.setRingtoneName("ringy_(9)");
        model8.setRingtoneLink("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/Ringtone%2Fringy_%20(9).mpeg?alt=media&token=1e9b7b1d-4e9c-40fa-9fe8-8a307a1fe41a");
        model8.setRingtoneCategory("Trending");
        model8.setRingtoneCategoryId("-LvWD_BPSqfP7raI-P2H");
        model8.setRingtoneCreatedDate(dateTime);
        model8.setRingtoneModifiedDate(dateTime);
        model8.setRingtoneDownloadCount(0);
        model8.setRingtoneUsedCount(0);
        model8.setRingtoneUsedAsContactToneCount(0);
        model8.setRingtoneUsedAsAlarmToneCount(0);
        model8.setRingtoneUsedAsFavourite(0);
        ringtoneDbRef.child(id8).setValue(model8);

        String id9 = ringtoneDbRef.push().getKey();
        RingtoneModel model9 = new RingtoneModel();
        model9.setRingtoneId(id9);
        model9.setRingtoneName("ringy_(10)");
        model9.setRingtoneLink("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/Ringtone%2Fringy_%20(10).mpeg?alt=media&token=66efc301-c240-467b-aa42-122b7fe2a803");
        model9.setRingtoneCategory("Trending");
        model9.setRingtoneCategoryId("-LvWD_BPSqfP7raI-P2H");
        model9.setRingtoneCreatedDate(dateTime);
        model9.setRingtoneModifiedDate(dateTime);
        model9.setRingtoneDownloadCount(0);
        model9.setRingtoneUsedCount(0);
        model9.setRingtoneUsedAsContactToneCount(0);
        model9.setRingtoneUsedAsAlarmToneCount(0);
        model9.setRingtoneUsedAsFavourite(0);
        ringtoneDbRef.child(id9).setValue(model9);

        String id10 = ringtoneDbRef.push().getKey();
        RingtoneModel model10 = new RingtoneModel();
        model10.setRingtoneId(id10);
        model10.setRingtoneName("ringy_(11)");
        model10.setRingtoneLink("https://firebasestorage.googleapis.com/v0/b/ringtoneapp-4fcae1.appspot.com/o/Ringtone%2Fringy_%20(11).mpeg?alt=media&token=e4bceff2-7a77-42af-96e7-49dabc4ac73c");
        model10.setRingtoneCategory("Trending");
        model10.setRingtoneCategoryId("-LvWD_BPSqfP7raI-P2H");
        model10.setRingtoneCreatedDate(dateTime);
        model10.setRingtoneModifiedDate(dateTime);
        model2.setRingtoneDownloadCount(0);
        model2.setRingtoneUsedCount(0);
        model2.setRingtoneUsedAsContactToneCount(0);
        model2.setRingtoneUsedAsAlarmToneCount(0);
        model2.setRingtoneUsedAsFavourite(0);
        ringtoneDbRef.child(id10).setValue(model10);


    }

}
