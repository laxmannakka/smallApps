package com.next.retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by next on 25/5/17.
 */
public interface ApiInterface
{
    @GET("tutorial/jsonparsetutorial.txt")
    Call<WorldPopulationModel> doGetListCountries();

    @POST("/posts")
    @FormUrlEncoded
    Call<Post> savePost(@Field("title") String title,
                        @Field("body") String body,
                        @Field("userId") long userId);

    @POST("/posts")
    Call<Post> ssavePost(@Body Post post);
}
