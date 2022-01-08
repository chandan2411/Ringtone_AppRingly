package com.ringly.customer_app.views.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ringly.customer_app.R;
import com.ringly.customer_app.entities.AppUtils;
import com.ringly.customer_app.entities.Constant;
import com.ringly.customer_app.entities.MySharedPref;
import com.ringly.customer_app.entities.ViewDialog;
import com.ringly.customer_app.models.RingtoneModel;
import com.ringly.customer_app.views.adapters.RingtoneUploadActivity;
import com.ringly.customer_app.views.fragments.FavouritesFragment;
import com.ringly.customer_app.views.fragments.InfoFragment;
import com.ringly.customer_app.views.fragments.RingtoneFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;


public class HomeActivity extends AppCompatActivity implements RecyclerViewScrollListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();

    public Fragment fragment = null;
    private MySharedPref sharedPref;
    private ImageView ivProfileImage;
    private boolean isHomeFragment=true;
    private DrawerLayout drawer;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        /*new UploadDataToFirebaseDbClass(this).addRingtone();*/

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedPref = new MySharedPref(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();

        if (!sharedPref.readBoolean(Constant.IS_USER_LOGIN,false)){
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
            nav_Menu.findItem(R.id.nav_login).setVisible(true);
        }else {
            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
            nav_Menu.findItem(R.id.nav_login).setVisible(false);
        }

        View header = navigationView.inflateHeaderView(R.layout.nav_header_nav);
        TextView tvUserName = header.findViewById(R.id.tvUserName);
        TextView tvProfileEmail = header.findViewById(R.id.tvUserEmail);
        ivProfileImage = header.findViewById(R.id.ivProfileImage);
        String loginMode = sharedPref.readString(Constant.SIGN_UP_MODE,"");
        if (loginMode.equals(Constant.GMAIL)){
            tvUserName.setText(sharedPref.readString(Constant.USER_NAME, ""));
            tvProfileEmail.setText(sharedPref.readString(Constant.USER_EMAIL, ""));
            /*setUserProfilePhoto(ivProfileImage);*/
        } else if (loginMode.equals(Constant.PHONE)) {
            tvUserName.setText(sharedPref.readString(Constant.MOBILE_NUMBER, ""));
            tvProfileEmail.setVisibility(View.GONE);
        }
        replaceFragment(new RingtoneFragment());

        navigationView.setNavigationItemSelectedListener(this);
    }


    private void setUserProfilePhoto(ImageView ivProfileImage) {
        String userImageUrl = sharedPref.readString(Constant.USER_IMAGE_URL, "");
        Uri imageUri = null;
        if (!userImageUrl.isEmpty()) {
            imageUri = Uri.parse(userImageUrl);
        }
        Picasso.get()
                .load(imageUri)
                .placeholder(R.drawable.avatar)
                .fit()
                .centerCrop()
                .into(ivProfileImage);
    }



    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container_wrapper, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
    }


   /* public void setFragment(String trendCategoryId) {
        sharedPref.writeBoolean(Constant.IS_HOME_FRAG, false);
        fragment = new FeatureRingtoneFragment(trendCategoryId);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container_wrapper, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }*/

    public void moveToCateActivity(String ringtoneCategoryId, String ringtoneCategory) {
        Intent intent = new Intent(HomeActivity.this, CategorizesActivity.class);
        intent.putExtra("CAT_NAME", ringtoneCategory);
        intent.putExtra("CAT_ID", ringtoneCategoryId);
        startActivity(intent);
    }

    @Override
    public void OnScrollListener(RingtoneModel ringtoneModel, boolean isFav) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent myIntent;

        switch (id){
            case R.id.nav_ring:
                isHomeFragment = true;
                toolbar.setTitle("Ringtone");
                sharedPref.writeBoolean(Constant.IS_HOME_FRAG, true);
                fragment = new RingtoneFragment();
                replaceFragment(fragment);
                break;
            case R.id.nav_wall:
                drawer.closeDrawer(GravityCompat.START);
                sharedPref.writeBoolean(Constant.IS_HOME_FRAG, false);
                myIntent = new Intent(getBaseContext(), WallActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nav_video:
                sharedPref.writeBoolean(Constant.IS_HOME_FRAG, false);
                myIntent = new Intent(getBaseContext(), RingtoneUploadActivity.class);
                startActivity(myIntent);
                break;
            case R.id.nav_fav:
                if (sharedPref.readBoolean(Constant.IS_USER_LOGIN, false)){
                    isHomeFragment = false;
                    toolbar.setTitle("Favourite List");
                    sharedPref.writeBoolean(Constant.IS_HOME_FRAG, false);
                    fragment = new FavouritesFragment();
                    replaceFragment(fragment);
                }else {
                    drawer.closeDrawer(GravityCompat.START);
                    AppUtils.moveToSignInActivity(this);
                }
                break;
            case R.id.nav_share:
                drawer.closeDrawer(GravityCompat.START);
                sharedPref.writeBoolean(Constant.IS_HOME_FRAG, false);
                Intent shareintent = new Intent();
                shareintent.setAction(Intent.ACTION_SEND);
                shareintent.putExtra(Intent.EXTRA_TEXT, "Download Best Reels, Ringtones and HD Wallpapers     https://play.google.com/store/apps/details?id=com.ringly.customer_app");
                shareintent.setType("text/plain");
                startActivity(Intent.createChooser(shareintent, "fabShare via"));
                break;
            case R.id.nav_info:
                isHomeFragment = false;
                sharedPref.writeBoolean(Constant.IS_HOME_FRAG, false);
                fragment = new InfoFragment();
                replaceFragment(fragment);
                break;
            case R.id.nav_logout:
                drawer.closeDrawer(GravityCompat.START);
                sharedPref.writeBoolean(Constant.IS_HOME_FRAG, false);
                ViewDialog.showDialog(HomeActivity.this, Constant.SignOutMessage, Constant.SignOutTitle);
                break;
            case R.id.nav_login:
                drawer.closeDrawer(GravityCompat.START);
                FirebaseAuth.getInstance().signOut();
                new MySharedPref(this).deleteAllData();
                Intent i = new Intent(this, SignInActivity.class);
                i.putExtra(Constant.OTHER_THAN_SPLASH_SCREEN, true);
                startActivity(i);
                break;
            case R.id.nav_rateUs:
                drawer.closeDrawer(GravityCompat.START);
                sharedPref.writeBoolean(Constant.IS_HOME_FRAG, false);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.devloper.ringtone_app")));
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (isHomeFragment) {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                super.onBackPressed();
            } else {
                isHomeFragment=true;
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                replaceFragment(new RingtoneFragment());
            }
        }
    }
}
