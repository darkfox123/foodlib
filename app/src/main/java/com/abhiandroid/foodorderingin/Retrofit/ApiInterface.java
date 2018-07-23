package com.abhiandroid.foodorderingin.Retrofit;


import com.abhiandroid.foodorderingin.MVP.AddToWishlistResponse;
import com.abhiandroid.foodorderingin.MVP.CartistResponse;
import com.abhiandroid.foodorderingin.MVP.CategoryListResponse;
import com.abhiandroid.foodorderingin.MVP.FAQResponse;
import com.abhiandroid.foodorderingin.MVP.MyOrdersResponse;
import com.abhiandroid.foodorderingin.MVP.Product;
import com.abhiandroid.foodorderingin.MVP.RecommendedProductsResponse;
import com.abhiandroid.foodorderingin.MVP.RegistrationResponse;
import com.abhiandroid.foodorderingin.MVP.RestaurantDetailResponse;
import com.abhiandroid.foodorderingin.MVP.SignUpResponse;
import com.abhiandroid.foodorderingin.MVP.StripeResponse;
import com.abhiandroid.foodorderingin.MVP.TermsResponse;
import com.abhiandroid.foodorderingin.MVP.UserProfileResponse;
import com.abhiandroid.foodorderingin.MVP.WishlistResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public interface ApiInterface {

    // API's endpoints

    @FormUrlEncoded
    @POST("/JSON/allitem.php")
    public void getAllProducts(@Field("res007") String resId,
            Callback<List<Product>> callback);

    @FormUrlEncoded
    @POST("/JSON/recom.php")
    public void getRecommendedProducts(@Field("res007") String resId,
            Callback<RecommendedProductsResponse> callback);
   //@FormUrlEncoded
    //@POST("/JSON/pbyc.php")
   // public void getCategoryList(@Field("res_id") String res_id, Callback<List<CategoryListResponse>> callback);
     @GET("/demo/food/JSON/pbyc.php")
     public void getCategoryList(Callback<List<CategoryListResponse>> callback);

    @FormUrlEncoded
    @POST("/JSON/resdetails.php")
    public void getRestaurantDetail(@Field("res007") String resId, Callback<RestaurantDetailResponse> callback);

    @FormUrlEncoded
    @POST("/JSON/faq.php")
    public void getFAQ(@Field("res007") String resId,Callback<FAQResponse> callback);

    @FormUrlEncoded
    @POST("/JSON/terms.php")
    public void getTerms(@Field("res007") String resId, Callback<TermsResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/pushadd.php")
    public void sendAccessToken(@Field("res007") String resId, @Field("accesstoken") String accesstoken, Callback<RegistrationResponse> callback);

    @FormUrlEncoded
    @POST("/JSON/addwishlist.php")
    public void addToWishList(@Field("res007") String resId, @Field("product_id") String product_id, @Field("user_id") String user_id, Callback<AddToWishlistResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/add-cart.php")
    public void addToCart(@Field("res007") String resId, @Field("product_id") String product_id, @Field("userid") String user_id,
                          @Field("varient_id") String varient_id, @Field("varient_quantity") String varient_quantity,
                          @Field("json_param") String json_param, @Field("varient_name") String varient_name,
                          @Field("varient_price") String varient_price, @Field("product_name") String product_name,
                          Callback<AddToWishlistResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/deletecart.php")
    public void deleteCartItem(@Field("res007") String resId, @Field("user_id") String user_id,
                               @Field("varient_id") String varient_id,
                               @Field("product_id") String product_id, Callback<AddToWishlistResponse> callback);

    @FormUrlEncoded
    @POST("/JSON/wishcheck.php")
    public void checkWishList(@Field("res007") String resId, @Field("product_id") String product_id, @Field("user_id") String user_id, Callback<AddToWishlistResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/wishlist.php")
    public void getWishList(@Field("res007") String resId, @Field("user_id") String user_id, Callback<WishlistResponse> callback);

    @FormUrlEncoded
    @POST("/JSON/viewordersdetails.php")
    public void getMyOrders(@Field("res007") String resId, @Field("user_id") String user_id, Callback<MyOrdersResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/viewcart.php")
    public void getCartList(@Field("res007") String resId, @Field("user_id") String user_id, Callback<CartistResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/userprofile.php")
    public void getUserProfile(@Field("res007") String resId, @Field("user_id") String user_id, Callback<UserProfileResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/updateprofile.php")
    public void updateProfile(@Field("res007") String resId,
                              @Field("user_id") String user_id,
                              @Field("name") String name,
                              @Field("city") String city,
                              @Field("state") String state,
                              @Field("pincode") String pincode,
                              @Field("local") String local,
                              @Field("flat") String flat,
                              @Field("gender") String gender,
                              @Field("phone") String phone,
                              @Field("landmark") String landmark,
                              Callback<SignUpResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/resentmail.php")
    public void resentEmail(@Field("res007") String resId, @Field("email") String email, Callback<SignUpResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/login.php")
    public void login(@Field("res007") String resId, @Field("email") String email, @Field("password") String password, @Field("logintype") String logintype, Callback<SignUpResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/paystripe.php")
    public void stripePayment(@Field("res007") String resId, @Field("stripeToken") String stripeToken,
                              @Field("total") String total,
                              @Field("user_id") String user_id,
                              @Field("cart_id") String cart_id,
                              @Field("address") String address,
                              @Field("phone") String phone,
                              Callback<StripeResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/addorders.php")
    public void addOrder(@Field("res007") String resId,
                         @Field("user_id") String user_id,
                         @Field("cart_id") String cart_id,
                         @Field("address") String address,
                         @Field("phone") String phone,
                         @Field("paymentref") String paymentref,
                         @Field("paystatus") String paystatus,
                         @Field("total") String total,
                         @Field("paymentmode") String paymentmode,
                         Callback<SignUpResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/forgot.php")
    public void forgotPassword(@Field("res007") String resId, @Field("email") String email, Callback<SignUpResponse> callback);


    @FormUrlEncoded
    @POST("/JSON/register.php")
    public void registration(@Field("res007") String resId, @Field("name") String name, @Field("email") String email, @Field("password") String password, @Field("logintype") String logintype, Callback<SignUpResponse> callback);


}
