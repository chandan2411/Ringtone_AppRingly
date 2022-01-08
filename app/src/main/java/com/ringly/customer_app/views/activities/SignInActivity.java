package com.ringly.customer_app.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.ringly.customer_app.R;
import com.ringly.customer_app.entities.Constant;
import com.ringly.customer_app.entities.DatabaseReferences;
import com.ringly.customer_app.entities.MySharedPref;
import com.ringly.customer_app.models.UserModel;
import com.ringly.customer_app.views.adapters.MyPagerAdapter;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = SignInActivity.class.getSimpleName();
    private ViewPager viewPager;
    private LinearLayout layoutDot;
    private int[] layouts;
    private MyPagerAdapter pagerAdapter;
    private static final int RC_SIGN_IN = 0;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private Button btnSignOut;
    private Button btnSignIn;
    private TextView txtEmail;
    private TextView txtUser;
    private ImageView imgProfile;
    private FirebaseAuth firebaseAuth;
    private boolean cameFromOtherThanSplash = false;

    private String userId;
    private String userName;
    private String userEmail;
    private MySharedPref sharedPref;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build());
    private Uri userImageUrl;


    public void onButtonClick(View v) {
        Intent intent = new Intent(getBaseContext(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(intent);
        this.finish();
    }
    public void startSignIn(View v) {
        Intent myIntent = new Intent(getBaseContext(), RegisterActivity.class);
        startActivity(myIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);
        sharedPref = new MySharedPref(this);

        if (getIntent()!=null){
            cameFromOtherThanSplash = getIntent().getBooleanExtra(Constant.OTHER_THAN_SPLASH_SCREEN, false);
        }


        viewPager = findViewById(R.id.view_pager);

        layoutDot = findViewById(R.id.dotLayout);
        layouts = new int[]{R.layout.login_slider_1, R.layout.login_slider_2, R.layout.login_slider_3};
        pagerAdapter = new MyPagerAdapter(layouts, getApplicationContext());
        viewPager.setAdapter(pagerAdapter);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(), 15000, 15000);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        TextView mTextView = (TextView) findViewById(R.id.animtext);
        Animation marquee = AnimationUtils.loadAnimation(this, R.anim.myanim);
        mTextView.startAnimation(marquee);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                setDotStatus(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setDotStatus(0);

       /* if (user != null) {
            finish();
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
        }*/

        for (String provider : AuthUI.SUPPORTED_PROVIDERS) {
            Log.v(this.getClass().getName(), provider);
        }

        mAuth = FirebaseAuth.getInstance();
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignOut = (Button) findViewById(R.id.btnSignOut);

        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtUser = (TextView) findViewById(R.id.txtUser);

        mAuthListener = firebaseAuth -> updateUi();
    }

    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            SignInActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }


    private void updateUi() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            txtEmail.setVisibility(View.GONE);
            txtUser.setVisibility(View.GONE);
            imgProfile.setImageBitmap(null);
        } else {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            txtEmail.setVisibility(View.VISIBLE);
            txtUser.setVisibility(View.VISIBLE);
            userName = user.getDisplayName();
            userEmail = user.getEmail();
            userId = user.getUid();
            userImageUrl = user.getPhotoUrl();
            txtUser.setText(userName);

//            saveUserDetails();


            txtEmail.setText(userEmail);
            Picasso.get().load(user.getPhotoUrl()).into(imgProfile);
        }
        if (user != null) {
            if (cameFromOtherThanSplash){
                finish();
            }else {
                startActivity(new Intent(SignInActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        }
    }

    private void saveUserDetails() {
        /*saving to session manager User Id, Email, Name*/
        sharedPref.writeBoolean(Constant.IS_USER_LOGIN, true);
        sharedPref.writeString(Constant.SIGN_UP_MODE, Constant.GMAIL);
        sharedPref.writeString(Constant.USER_EMAIL, userEmail);
        sharedPref.writeString(Constant.USER_NAME, userName);
        sharedPref.writeString(Constant.USER_ID, userId);
        if (userImageUrl!=null)
            sharedPref.writeString(Constant.USER_IMAGE_URL, userImageUrl.toString());

        /*Create table for user in firebase realtime database*/
        registerUserInTable();
    }

    private void registerUserInTable() {
        DatabaseReferences.registerUser(new UserModel(userId, userName, userEmail));
    }


    public void signOut(View view) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SignInActivity.this, "signed out succesfully ... ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (response==null)
                return;
            if (resultCode == Activity.RESULT_OK) {
                Log.d(this.getClass().getName(), "This user signed in with " + response.getProviderType());
                updateUi();
            } else {
                updateUi();
            }
        }
    }

    public void deleteAccount(View view) {
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }


    public void signIn(View view) {


        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(providers)
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.mipmap.icon3)
                        .setTosAndPrivacyPolicyUrls("https://www.termsfeed.com/terms-service/a8b31f096521eff873c7bb0a0e2467cb",
                                "https://www.termsfeed.com/privacy-policy/dff7fdbc740dddf322840318de1b61b4")
                        .build(),
                RC_SIGN_IN);
    }


    public void setDotStatus(int page) {
        layoutDot.removeAllViews();
        TextView[] dotstv = new TextView[layouts.length];
        for (int i = 0; i < dotstv.length; i++) {
            dotstv[i] = new TextView(this);
            dotstv[i].setText(Html.fromHtml("&#8226;"));
            dotstv[i].setTextSize(30);
            dotstv[i].setTextColor(Color.parseColor("#ffffff"));
            layoutDot.addView(dotstv[i]);
        }
        //Set current dot active
        if (dotstv.length > 0) {
            dotstv[page].setTextColor(Color.parseColor("#00ACC1"));
        }
    }
}