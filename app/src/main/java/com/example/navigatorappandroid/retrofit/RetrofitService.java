package com.example.navigatorappandroid.retrofit;
import com.google.gson.Gson;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import lombok.Getter;

@Getter
public class RetrofitService {

    private Retrofit retrofit;

    public RetrofitService() {
        initializeRetrofit();
    }

    private void initializeRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.1.1.214:8080")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }
}
