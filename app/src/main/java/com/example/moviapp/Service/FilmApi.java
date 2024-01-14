package com.example.moviapp.Service;

import com.example.moviapp.Model.FilmModel;
import com.example.moviapp.Model.PeopleModel;
import com.example.moviapp.Response.CastApiResponse;
import com.example.moviapp.Response.CategoriApiResponse;
import com.example.moviapp.Response.CreditResponse;
import com.example.moviapp.Response.FilmApiResponse;
import com.example.moviapp.Response.PeopleApiResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public  interface FilmApi {

    @GET("movie/popular")
    Call<FilmApiResponse> getPopularFilms(
            @Query("api_key") String key,
            @Query("page") int page
    );
    @GET("person/popular")
    Call<PeopleApiResponse> getPopularPeople(
            @Query("api_key") String key,
            @Query("page") int page
    );

    @GET("genre/movie/list")
    Call<CategoriApiResponse> getCategori(
            @Query("api_key") String key
    );

    @GET("movie/upcoming")
    Call<FilmApiResponse> getUpcomingFilms(
            @Query("api_key") String key,
            @Query("page") int page
    );

    @GET("movie/top_rated")
    Call<FilmApiResponse> getTopRatedFilms(
            @Query("api_key") String key,
            @Query("page") int page
    );

    @GET("search/movie")
    Call<FilmApiResponse> getSearchFilm(
            @Query("api_key") String key,
            @Query("query") String query
    );

    @GET("search/person")
    Call<PeopleApiResponse> getSearchPeople(
            @Query("api_key") String key,
            @Query("query") String query
    );
    @GET("movie/{id}")
    Call<FilmModel> getFilmDetail(
            @Path("id") int movieId,
            @Query("api_key") String apiKey,
            @Query("append_to_response") String appendToResponse
    );
    @GET("person/{id}")
    Call<PeopleModel> getPersonDetail(
            @Path("id") int personId,
            @Query("api_key") String apiKey
    );


    @GET("discover/movie")
    Call<FilmApiResponse> getDiscover(
            @Query("with_genres") int kategoriId,
            @Query("api_key") String  apiKey,
            @Query("vote_average.gte") int puan
    );
    @GET("discover/movie")
    Call<FilmApiResponse> getSuggestion(
            @Query("with_genres") List<Integer> kategoriId,
            @Query("api_key") String  apiKey,
            @Query("vote_average.gte") int puan
    );
    //person/3223/movie_credits
    @GET("person/{id}/movie_credits")
    Call<CastApiResponse> getPeopleMovie(
            @Path("id") int personId,
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}/credits")
    Call<CreditResponse> getMovieCredits(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );
    @GET("movie/{movie_id}/recommendations")
    Call<FilmApiResponse> getSuggestion2(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey
    );
}
