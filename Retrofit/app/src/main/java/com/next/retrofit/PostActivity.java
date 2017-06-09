package com.next.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity extends AppCompatActivity
{
TextView mTextview,mTextView2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mTextview = (TextView) findViewById(R.id.textview);
        mTextView2 = (TextView) findViewById(R.id.textview2);
        String BASE_URL= "http://jsonplaceholder.typicode.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiService = retrofit.create(ApiInterface.class);
        Call<Post> call = apiService.savePost("laxmna","msg",1234);
         call.enqueue(new Callback<Post>()
        {
        @Override
        public void onResponse(Call<Post> call, Response<Post> response)
        {
            if(response.isSuccessful())
            {
                String responce = response.body().toString();
            mTextview.setText(responce);
            }
        }

        @Override
        public void onFailure(Call<Post> call, Throwable t)
        {

        }
    });



        Call<Post> call2=apiService.ssavePost(new Post("laxan","is good ",1,124));
        call2.enqueue(new Callback<Post>()
        {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response)
            {
                if(response.isSuccessful())
                {
                    String responce = response.body().toString();
                    mTextView2.setText(responce);
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t)
            {

            }
        });


    }


}
