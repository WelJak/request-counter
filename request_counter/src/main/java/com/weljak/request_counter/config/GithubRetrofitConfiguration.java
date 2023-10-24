package com.weljak.request_counter.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.weljak.request_counter.service.external.github.GithubApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Configuration
public class GithubRetrofitConfiguration {
    @Value("${weljak.com.github.base.url}")
    private String githubBaseUrl;

    @Bean
    Retrofit retrofit() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(
                        ZonedDateTime.class,
                        (JsonDeserializer<ZonedDateTime>) (json, typeOfT, context) -> ZonedDateTime.parse(json.getAsString())
                ).registerTypeAdapter(
                        LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, typeOfT, context) -> LocalDateTime.parse(json.getAsString()))
                .create();

        return new Retrofit.Builder()
                .baseUrl(githubBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Bean
    @Primary
    GithubApiService githubApiService() {
        return retrofit().create(GithubApiService.class);
    }
}
