package com.delivery.app.data.remote.api;

import com.delivery.app.data.remote.model.ApiResponse;
import com.delivery.app.data.remote.model.DashboardStats;
import com.delivery.app.data.remote.model.Livraison;
import com.delivery.app.data.remote.model.LoginRequest;
import com.delivery.app.data.remote.model.Message;
import com.delivery.app.data.remote.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/auth/login")
    Call<ApiResponse<User>> login(@Body LoginRequest request);

    @GET("api/livraisons/today")
    Call<ApiResponse<List<Livraison>>> getTodayDeliveries();

    @GET("api/livraisons")
    Call<ApiResponse<List<Livraison>>> getDeliveries(
            @Query("date") String date,
            @Query("livreur") String livreur,
            @Query("etat") String etat,
            @Query("client") String client
    );

    @PUT("api/livraisons/{id}")
    Call<ApiResponse<Livraison>> updateLivraison(
            @Path("id") String id,
            @Body Livraison livraison
    );

    @POST("api/livraisons/sync")
    Call<ApiResponse<List<Livraison>>> syncLivraisons(@Body List<Livraison> livraisons);

    @GET("api/dashboard/stats")
    Call<ApiResponse<DashboardStats>> getDashboardStats(
            @Query("date") String date
    );

    @POST("api/messages")
    Call<ApiResponse<Message>> sendMessage(@Body Message message);

    @GET("api/messages")
    Call<ApiResponse<List<Message>>> getMessages(
            @Query("toUser") String toUser
    );
}