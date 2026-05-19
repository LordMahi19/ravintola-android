package com.example.ravintolaapp.network;

import com.example.ravintolaapp.models.DietLabel;
import com.example.ravintolaapp.models.Ingredient;
import com.example.ravintolaapp.models.LoginRequest;
import com.example.ravintolaapp.models.LoginResponse;
import com.example.ravintolaapp.models.MenuCategory;
import com.example.ravintolaapp.models.MenuItem;
import com.example.ravintolaapp.models.Order;
import com.example.ravintolaapp.models.OrderRequest;
import com.example.ravintolaapp.models.RegisterRequest;
import com.example.ravintolaapp.models.RegisterResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("api/users/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/users/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @GET("api/menu")
    Call<List<MenuItem>> getMenuItems(
        @Query("category") String category,
        @Query("diet") String diet
    );

    @GET("api/menu-categories")
    Call<List<MenuCategory>> getMenuCategories();

    @GET("api/diet-labels")
    Call<List<DietLabel>> getDietLabels();

    @GET("api/ingredients")
    Call<List<Ingredient>> getIngredients();

    @POST("api/orders")
    Call<Order> placeOrder(@Body OrderRequest request);

    @GET("api/orders/mine")
    Call<List<Order>> getMyOrders();

    @GET("api/orders/{id}")
    Call<Order> getOrderById(@Path("id") int orderId);
}
