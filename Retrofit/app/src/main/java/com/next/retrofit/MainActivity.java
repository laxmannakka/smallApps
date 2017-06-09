package com.next.retrofit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{
    List<Country> countries;
    RecyclerView mrecyclerview;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mrecyclerview = (RecyclerView) findViewById(R.id.recycerview);
       button = (Button) findViewById(R.id.button);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<WorldPopulationModel> call = apiService.doGetListCountries();

        call.enqueue(new Callback<WorldPopulationModel>()
        {
            @Override
            public void onResponse(Call<WorldPopulationModel> call, Response<WorldPopulationModel> response)
            {
                countries = response.body().getWorldpopulation();
                System.out.println("Response : " + countries.size());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                mrecyclerview.setLayoutManager(linearLayoutManager);

                RecyclerViewAdapter adapter = new RecyclerViewAdapter(MainActivity.this,countries);
                mrecyclerview.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<WorldPopulationModel> call, Throwable t)
            {

            }
        });

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(MainActivity.this,PostActivity.class);
                startActivity(intent);
            }
        });


    }
}
