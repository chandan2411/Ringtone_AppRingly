package com.ringly.customer_app.Api;


import com.ringly.customer_app.models.Links;
import com.ringly.customer_app.models.PhotoModel;
import com.ringly.customer_app.models.PhotoStats;
import com.ringly.customer_app.models.SearchResultModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UnsplashService {

    @GET("/photos")
    Call<List<PhotoModel>> getRecentPhotos(@Query("client_id") String apiKey,
                                           @Query("page") int page,
                                           @Query("per_page") int pageLimit);



    /*@GET("collections/featured")
    Call<List<FeaturedCollectionModel>> getFeaturedCollections(@Query("client_id") String apiKey,
                                                               @Query("page") int page,
                                                               @Query("per_page") int per_page);

    @GET("collections/curated")
    Call<List<FeaturedCollectionModel>> getCuratedCollections(@Query("client_id") String apiKey,
                                                              @Query("page") int page,
                                                              @Query("per_page") int per_page);*/

    @GET("collections/{id}/photos")
    Call<List<PhotoModel>> getCollectionPhotos(@Path("id") int id,
                                               @Query("client_id") String apiKey,
                                               @Query("page") int page,
                                               @Query("per_page") int per_page);

    @GET("collections/curated/{id}/photos")
    Call<List<PhotoModel>> getCuratedCollectionPhotos(@Path("id") int id,
                                                      @Query("client_id") String apiKey,
                                                      @Query("page") int page,
                                                      @Query("per_page") int per_page);

    @GET("search/photos")
    Call<SearchResultModel> getSearchPhotos(@Query("client_id") String apiKey,
                                            @Query("query") String query,
                                            @Query("page") int page,
                                            @Query("per_page") int pageLimit);

    @GET("users/{username}/photos")
    Call<List<PhotoModel>> getUserProfile(@Path("username") String username,
                                          @Query("client_id") String apiKey,
                                          @Query("page") int page,
                                          @Query("per_page") int per_page);

    @GET("/photos/{id}/statistics")
    Call<PhotoStats> getPhotoStats(@Path("id") String id,
                                   @Query("client_id") String apiKey);

    @GET("/photos/{id}/download")
    Call<Links> getDownloadEndpoint(@Path("id") String id,
                                    @Query("client_id") String apiKey);

}
