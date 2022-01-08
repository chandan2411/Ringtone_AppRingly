package com.ringly.customer_app.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ringly.customer_app.Api.ApiClient;
import com.ringly.customer_app.Api.UnsplashService;
import com.ringly.customer_app.R;
import com.ringly.customer_app.entities.Constant;
import com.ringly.customer_app.entities.InfiniteScrollListener;
import com.ringly.customer_app.models.PhotoModel;
import com.ringly.customer_app.models.ResultsArray;
import com.ringly.customer_app.models.SearchResultModel;
import com.ringly.customer_app.views.adapters.InfinitePhotoAdapter;
import com.ringly.customer_app.views.adapters.SearchResultAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfiniteScrollerActivity extends AppCompatActivity {

    public int PAGE_LIMIT=30;

    private GridLayoutManager gridLayoutManager;
    private InfiniteScrollListener infiniteScrollListener;
    RecyclerView infiniteRv;
    InfinitePhotoAdapter adapter;
    SearchResultAdapter searchResultAdapter;
    boolean isCurated;
    int i,id;
    TextView headerTxt;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infinite_scroller);
        infiniteRv=findViewById(R.id.infiniteRv);
        headerTxt=findViewById(R.id.tvTitle);
        progressBar=findViewById(R.id.progressBarInfy);
        headerTxt.setText(getIntent().getStringExtra("headerTxt"));
        i=getIntent().getExtras().getInt("history");
        id=getIntent().getExtras().getInt("collectionId");
        if(i==1||i==2||i==5) {
            gridLayoutManager = new GridLayoutManager(this, 2);
        }else
            gridLayoutManager = new GridLayoutManager(this, 1);
        infiniteRv.hasFixedSize();
        infiniteRv.setLayoutManager(gridLayoutManager);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        infiniteScrollListener = new InfiniteScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(i==1)
                    updateRecentPhotos(page);
                else if(i==2) {
                    isCurated = getIntent().getExtras().getBoolean("curated");
                    updateCollectionPhotos(id, page,isCurated);
                }

                else {
                    String query=getIntent().getStringExtra("query");
                    updateSearchLists(query,page);
                }
            }
        };

        infiniteRv.addOnScrollListener(infiniteScrollListener);

        if(i==1){
            getFirstRecentPhotos();
        }else if(i==2) {
            isCurated = getIntent().getExtras().getBoolean("curated");
            getFirstCollectionPhotos(id, isCurated);

        }
        else{
            String query=getIntent().getStringExtra("query");
            getFirstSearchPhotos(query);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transition));
            headerTxt.setTransitionName("sharedTransition");
        }

    }

    private void updateSearchLists(String query, int page) {
        UnsplashService searchService= ApiClient.getClient().create(UnsplashService.class);
        Call<SearchResultModel> callSearchItems=searchService.getSearchPhotos(Constant.UNSPLASH_API_KEY,query,page,PAGE_LIMIT);
        callSearchItems.enqueue(new Callback<SearchResultModel>() {
            @Override
            public void onResponse(Call<SearchResultModel> call, Response<SearchResultModel> response) {
                List<ResultsArray> resultsArrays=response.body().getResults();
                searchResultAdapter.addPhotos(resultsArrays);
            }

            @Override
            public void onFailure(Call<SearchResultModel> call, Throwable t) {

            }
        });
    }

    private void getFirstSearchPhotos(String query) {
        UnsplashService searchService= ApiClient.getClient().create(UnsplashService.class);
        Call<SearchResultModel> callSearchItems=searchService.getSearchPhotos(Constant.UNSPLASH_API_KEY,query,1,PAGE_LIMIT);
        callSearchItems.enqueue(new Callback<SearchResultModel>() {
            @Override
            public void onResponse(Call<SearchResultModel> call, Response<SearchResultModel> response) {
                List<ResultsArray> resultsArray=response.body().getResults();
                searchResultAdapter = new SearchResultAdapter(InfiniteScrollerActivity.this, resultsArray);
                searchResultAdapter.notifyDataSetChanged();
                infiniteRv.setAdapter(searchResultAdapter);
                progressBar.setVisibility(View.GONE);
                infiniteRv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<SearchResultModel> call, Throwable t) {

            }
        });
    }





    private void getFirstRecentPhotos(){
        //RECENT PHOTOS FIRST FETCH
        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
        Call<List<PhotoModel>> call=service.getRecentPhotos(Constant.UNSPLASH_API_KEY,2,PAGE_LIMIT);
        call.enqueue(new Callback<List<PhotoModel>>() {
            @Override
            public void onResponse(Call<List<PhotoModel>> call, Response<List<PhotoModel>> response) {
                List<PhotoModel> photoModelList=response.body();
                adapter = new InfinitePhotoAdapter(InfiniteScrollerActivity.this, photoModelList);
                adapter.notifyDataSetChanged();
                infiniteRv.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                infiniteRv.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<List<PhotoModel>> call, Throwable t) {

            }
        });
    }


    private void getFirstCollectionPhotos(int id,boolean isCurated) {
        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
        Call<List<PhotoModel>> call;
        if(!isCurated) {
            call = service.getCollectionPhotos(id, Constant.UNSPLASH_API_KEY, 1, PAGE_LIMIT);
        }else {
            call = service.getCuratedCollectionPhotos(id, Constant.UNSPLASH_API_KEY, 1, PAGE_LIMIT);
        }
        call.enqueue(new Callback<List<PhotoModel>>() {
            @Override
            public void onResponse(Call<List<PhotoModel>> call, Response<List<PhotoModel>> response) {
                List<PhotoModel> photoModelList=response.body();
                adapter = new InfinitePhotoAdapter(InfiniteScrollerActivity.this, photoModelList);
                adapter.notifyDataSetChanged();
                infiniteRv.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                infiniteRv.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<List<PhotoModel>> call, Throwable t) {

            }
        });
    }

    public void updateRecentPhotos(int page){
        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
        Call<List<PhotoModel>> call=service.getRecentPhotos(Constant.UNSPLASH_API_KEY,page,PAGE_LIMIT);
        call.enqueue(new Callback<List<PhotoModel>>() {
            @Override
            public void onResponse(Call<List<PhotoModel>> call, Response<List<PhotoModel>> response) {
                List<PhotoModel> photoModelList=response.body();
                adapter.addPhotos(photoModelList);
            }

            @Override
            public void onFailure(Call<List<PhotoModel>> call, Throwable t) {

            }
        });
    }

    private void updateCollectionPhotos(int id, int page,boolean isCurated) {
        UnsplashService service= ApiClient.getClient().create(UnsplashService.class);
        Call<List<PhotoModel>> call;
        if(!isCurated) {
            call = service.getCollectionPhotos(id, Constant.UNSPLASH_API_KEY, page, PAGE_LIMIT);
        }else {
            call = service.getCuratedCollectionPhotos(id, Constant.UNSPLASH_API_KEY, page, PAGE_LIMIT);
        }        call.enqueue(new Callback<List<PhotoModel>>() {
            @Override
            public void onResponse(Call<List<PhotoModel>> call, Response<List<PhotoModel>> response) {
                List<PhotoModel> photoModelList=response.body();
                adapter.addPhotos(photoModelList);
            }

            @Override
            public void onFailure(Call<List<PhotoModel>> call, Throwable t) {

            }
        });

    }
}
