package com.ringly.customer_app.views.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ringly.customer_app.Api.ApiClient;
import com.ringly.customer_app.Api.UnsplashService;
import com.ringly.customer_app.R;
import com.ringly.customer_app.entities.Constant;
import com.ringly.customer_app.models.PhotoModel;
import com.ringly.customer_app.models.WallPaperModel;
import com.ringly.customer_app.views.adapters.RecentPhotoAdapter;
import com.github.florent37.expectanim.ExpectAnim;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.github.florent37.expectanim.core.Expectations.alpha;
import static com.github.florent37.expectanim.core.Expectations.height;
import static com.github.florent37.expectanim.core.Expectations.leftOfParent;
import static com.github.florent37.expectanim.core.Expectations.topOfParent;

public class WallActivity extends AppCompatActivity implements MaterialSearchBar.OnSearchActionListener {

    public static String BACKGROUND_IMG_URL="https://images.unsplash.com/photo-1532288191429-2093e0783809?ixlib=rb-0.3.5&ixid=eyJhcHBfaWQiOjEyMDd9&s=6583d68060ff2f60484f7edaf88ee276&auto=format&fit=crop&w=3450&q=80";
    public int PAGE_LIMIT=30;
    RecentPhotoAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    RecyclerView recyclerView;
    MaterialSearchBar searchBar;
    ImageView backgroundImg,aboutIcon,noInternetImg;
    Button noInternetBtn;
    TextView retryTxt;
    FrameLayout mainFrame;


    @BindView(R.id.mainTxt)
    View mainTxt;

    @BindView(R.id.background)
    View backbground;

    @BindView(R.id.scrim)
    View scrim;

    @BindView(R.id.scrollview)
    NestedScrollView scrollView;

    @BindDimen(R.dimen.height)
    int height;

    private ExpectAnim expectAnimMove;
    private ImageView ivBack;
    private TextView tvTitle;
    private KProgressHUD progressHUD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
        ButterKnife.bind(this);

        backgroundImg= findViewById(R.id.background_img);
        recyclerView = findViewById(R.id.recentPhotos);
        aboutIcon= findViewById(R.id.aboutIcon);
        noInternetImg=findViewById(R.id.nointernetImg);
        noInternetBtn=findViewById(R.id.noInternetBtn);
        mainFrame=findViewById(R.id.mainFrame);
        retryTxt=findViewById(R.id.retryTxt);
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);

        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(gridLayoutManager);
        ViewCompat.setNestedScrollingEnabled(recyclerView,false);

        searchBar = findViewById(R.id.searchBar);
        searchBar.setOnSearchActionListener(this);

        ivBack.setOnClickListener(view -> {
            WallActivity.this.finish();
        });

        inititeProgessHud();



        if(!isNetworkAvailable()){
            scrollView.setVisibility(View.GONE);
            mainFrame.setVisibility(View.GONE);
            noInternetImg.setVisibility(View.VISIBLE);
            retryTxt.setVisibility(View.VISIBLE);
            Glide.with(this).load(R.drawable.nointernet).centerCrop().into(noInternetImg);
            noInternetBtn.setVisibility(View.VISIBLE);
            noInternetBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(isNetworkAvailable()){
                        scrollView.setVisibility(View.VISIBLE);
                        mainFrame.setVisibility(View.VISIBLE);
                        retryTxt.setVisibility(View.GONE);
                        noInternetImg.setVisibility(View.GONE);
                        noInternetBtn.setVisibility(View.GONE);
                        getWallpaperWS();
                    }else {
                        Toast.makeText(WallActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else {
            getWallpaperWS();
        }

        Glide.with(getApplicationContext())
                .load(BACKGROUND_IMG_URL)
                .centerCrop()
                .into(backgroundImg);

        this.expectAnimMove = new ExpectAnim()
                .expect(searchBar)
                .toBe(
                        topOfParent().withMarginDp(20),
                        leftOfParent().withMarginDp(10)

                )

                .expect(scrim)
                .toBe(
                        alpha(0f)
                )
                .expect(mainTxt)
                .toBe(
                        alpha(0f)
                )


                .expect(backgroundImg)
                .toBe(
                        alpha(0f)

                )

                .expect(aboutIcon)
                .toBe(
                        alpha(0f)
                )

                .expect(backbground)
                .toBe(
                        height(height).withGravity(Gravity.LEFT, Gravity.TOP)
                )

                .toAnimation();

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                final float percent = (scrollY * 1f) / v.getMaxScrollAmount();
                expectAnimMove.setPercent(percent);
            }
        });

    }

//    public void updatePhotos(int page){
//        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
//        Call<List<PhotoModel>> call=service.getRecentPhotos(Constant.UNSPLASH_API_KEY,page,PAGE_LIMIT);
//        call.enqueue(new Callback<List<PhotoModel>>() {
//            @Override
//            public void onResponse(Call<List<PhotoModel>> call, Response<List<PhotoModel>> response) {
//                List<PhotoModel> photoModelList=response.body();
//                adapter.addPhotos(photoModelList);
//            }
//
//            @Override
//            public void onFailure(Call<List<PhotoModel>> call, Throwable t) {
//
//            }
//        });
//    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchBar.clearSuggestions();
        Intent i=new Intent(WallActivity.this,InfiniteScrollerActivity.class);
        i.putExtra("history",5);
        i.putExtra("headerTxt","Results for : " +searchBar.getText());
        i.putExtra("query",searchBar.getText());
        startActivity(i);
    }

    @Override
    public void onButtonClicked(int buttonCode) {

        switch (buttonCode){
            case MaterialSearchBar.BUTTON_NAVIGATION:
                searchBar.disableSearch();
                break;
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void inititeProgessHud() {
        progressHUD = KProgressHUD.create(WallActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

    }

    public void showProgressHud() {
        progressHUD.show();
    }

    public void hideProgressHud() {
        if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }
    }

    public void getWallpaperWS(){
        showProgressHud();
        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
        Call<List<PhotoModel>> call=service.getRecentPhotos(Constant.UNSPLASH_API_KEY,1,PAGE_LIMIT);
        call.enqueue(new Callback<List<PhotoModel>>() {
            @Override
            public void onResponse(Call<List<PhotoModel>> call, Response<List<PhotoModel>> response) {
                hideProgressHud();
                if (response.body()!=null) {
                    List<PhotoModel> photoModelList = response.body();
                    List<WallPaperModel> wallPaperModelList = new ArrayList<>();
                    /*Change to model class*/
                    for(PhotoModel photoModel: photoModelList){
                        WallPaperModel model = new WallPaperModel();
                        model.setWallPaperId(photoModel.getId());
                        model.setWallPaperFullImageUrl(photoModel.getUrls().getImage_regular());
                        model.setUploaderName(photoModel.getUser().getName());
                        model.setUploadLocation(photoModel.getUser().getLocation());
                        wallPaperModelList.add(model);
                    }

                    adapter = new RecentPhotoAdapter(WallActivity.this, wallPaperModelList, true);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                    recyclerView.setVisibility(View.VISIBLE);
                }else {
                    showSnackBar(Constant.NO_INTERNET);
                }
            }

            @Override
            public void onFailure(Call<List<PhotoModel>> call, Throwable t) {
                showSnackBar(t.getMessage());
            }
        });
    }

    private void showSnackBar(String message) {
        Snackbar.make(findViewById(R.id.llParent), message, Snackbar.LENGTH_SHORT).show();
    }
}