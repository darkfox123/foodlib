package com.abhiandroid.foodorderingin.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abhiandroid.foodorderingin.Extras.Common;
import com.abhiandroid.foodorderingin.Extras.Config;
import com.abhiandroid.foodorderingin.Extras.DetectConnection;
import com.abhiandroid.foodorderingin.MVP.CategoryListResponse;
import com.abhiandroid.foodorderingin.MVP.Extra;
import com.abhiandroid.foodorderingin.MVP.Product;
import com.abhiandroid.foodorderingin.MVP.RecommendedProductsResponse;
import com.abhiandroid.foodorderingin.MVP.RestaurantDetailResponse;
import com.abhiandroid.foodorderingin.MVP.Variants;
import com.abhiandroid.foodorderingin.R;
import com.abhiandroid.foodorderingin.Retrofit.Api;
import com.abhiandroid.foodorderingin.Volley.Volley_Request;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.PATCH;

public class SplashScreen extends Activity {

    public static List<CategoryListResponse> categoryListResponseData;
    public static RestaurantDetailResponse restaurantDetailResponseData;
    public static List<Product> allProductsData,recommendedProductList;
    public static List<Product> productList;
    static String id = "";
    @BindView(R.id.errorText)
    TextView errorText;
    @BindView(R.id.internetNotAvailable)
    LinearLayout internetNotAvailable;
    @BindView(R.id.splashImage)
    ImageView splashImage;
    static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
mContext = SplashScreen.this;
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // check data from FCM
        try {
            Intent intent = getIntent();
            id = intent.getStringExtra("id");
            Log.d("notification Data", id);
        } catch (Exception e) {
            Log.d("error notification data", e.toString());
        }

        // Check the internet and get response from API's
        if (DetectConnection.checkInternetConnection(getApplicationContext())) {
            getCategoryList();
        } else {
            errorText.setText("Internet Connection Not Available");
            internetNotAvailable.setVisibility(View.VISIBLE);
            splashImage.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tryAgain)
    public void onClick() {
        if (DetectConnection.checkInternetConnection(getApplicationContext())) {
            internetNotAvailable.setVisibility(View.GONE);
            splashImage.setVisibility(View.VISIBLE);
            getCategoryList();
        } else {
            errorText.setText("Internet Connection Not Available");
            internetNotAvailable.setVisibility(View.VISIBLE);
            splashImage.setVisibility(View.GONE);
        }
    }

    public void getCategoryList() {
        // getting category list news data


        /*
        Api.getClient().getCategoryList("res007", new Callback<List<CategoryListResponse>>() {
            @Override
            public void success(List<CategoryListResponse> categoryListResponses, Response response) {
                try {
                    categoryListResponseData = categoryListResponses;
                    Log.d("categoryData", categoryListResponses.get(0).getCategory_name());

                    getRestaurantDetail();
                } catch (Exception e) {
                    errorText.setText("No Category Added In This Store!");
                    internetNotAvailable.setVisibility(View.VISIBLE);
                    splashImage.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                errorText.setText("Internet Connection Not Available");
                internetNotAvailable.setVisibility(View.VISIBLE);
                splashImage.setVisibility(View.GONE);
            }
        });
*/

        String req = "{\"res_id\":\"res007\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(SplashScreen.this, getResources().getString(R.string.mJSONURL_catlist), "POST", "getCatList", req);

    }

    public static void getCategoryResponse(String response){
        try {
            JSONArray catList = new JSONArray(response);
            categoryListResponseData = new ArrayList<CategoryListResponse>();
            for(int i = 0 ; i < catList.length(); i++){
                JSONArray productArr = catList.getJSONObject(i).getJSONArray("products");
                List<Product> productList = new ArrayList<>();
                for(int j=0; j<productArr.length(); j++){
                    Product pr = new Product();
                    productList.add(pr);
                }
                CategoryListResponse ctr = new CategoryListResponse(productList, null ,catList.getJSONObject(i).getString("cat_id"),catList.getJSONObject(i).getString("category_name"),catList.getJSONObject(i).getString("category_image"));
            categoryListResponseData.add(ctr);
            ////Log.d("myTag", "parsed : " + categoryListResponseData.get(0).getCategory_name());
            }
            Log.d("myTag", categoryListResponseData.get(0).getCat_id() + " : " + categoryListResponseData.get(1).getCategory_name());
            getRestaurantDetail();
        }catch(Exception e){
            Log.d("myTag", "error in json : " , e);
        }
    }



    public static void getRestaurantDetail() {
        String req = "{\"res_id\":\"res007\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(mContext, mContext.getResources().getString(R.string.mJSONURL_restaurantdetail), "POST", "getRestaurantDetail", req);


        // getting slider list data
        /*
        Api.getClient().getRestaurantDetail("res007", new Callback<RestaurantDetailResponse>() {
            @Override
            public void success(RestaurantDetailResponse restaurantDetailResponse, Response response) {
                restaurantDetailResponseData = restaurantDetailResponse;
                getRecommendedList();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                errorText.setText("Internet Connection Not Available");
                internetNotAvailable.setVisibility(View.VISIBLE);
                splashImage.setVisibility(View.GONE);
            }
        });
        */
    }

    public static void resDetailResponse(String response){
        try {
            JSONObject jObj = new JSONObject(response);
            List<String> imageList = new ArrayList<>();
            /*
            if(jObj.getJSONObject("images")!= null) {
                JSONArray images = jObj.getJSONArray("images");
                for (int i = 0; i < images.length(); i++) {
                    imageList.add(images.get(i).toString());
                }
            }*/
            JSONArray deliveryL = jObj.getJSONArray("deliverycity");
            List<String> deliveryList = new ArrayList<>();
            for(int i=0; i< deliveryL.length(); i++){
                deliveryList.add(deliveryL.get(i).toString());
            }
            restaurantDetailResponseData = new RestaurantDetailResponse(jObj.getString("name"),jObj.getString("address"), jObj.getString("description"), jObj.getString("phone"), jObj.getString("web"), jObj.getString("lat"), jObj.getString("lon"),jObj.getString("time"), jObj.getString("tax"), jObj.getString("currency"), jObj.getString("minorder"), imageList,deliveryList);
        Log.d("myTag", "added another : " + restaurantDetailResponseData.getAddress());
        getRecommendedList();
        }catch(JSONException ex ){
            Log.d("myTag", "ex : " , ex);
        }
    }

    public static void getRecommendedList() {
        String req = "{\"res_id\":\"res007\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(mContext,  mContext.getResources().getString(R.string.mJSONURL_recommproduct), "POST", "getRecommendedProducts", req);

        // getting slider list data
        /*
        Api.getClient().getRecommendedProducts("resId", new Callback<RecommendedProductsResponse>() {
            @Override
            public void success(RecommendedProductsResponse recommendedProductsResponse, Response response) {
                if (recommendedProductsResponse.getSuccess().equalsIgnoreCase("true")) {
                    recommendedProductList = recommendedProductsResponse.getProductList();
                }else {
                    recommendedProductList=new ArrayList<>();
                }
                getAllProducts();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                errorText.setText("Internet Connection Not Available");
                internetNotAvailable.setVisibility(View.VISIBLE);
                splashImage.setVisibility(View.GONE);
            }
        });
        */
    }

    public static void getRecommListResponse(String response){
       try {
           recommendedProductList = new ArrayList<>();
           JSONObject jsonObject = new JSONObject(response);
           JSONArray PArr = jsonObject.getJSONArray("rcomitem");
           for(int i=0; i< PArr.length(); i++){
               JSONObject jObj = PArr.getJSONObject(i);
               List<String> imageList = new ArrayList<>();
                JSONArray images = jObj.getJSONArray("images");
                for (int j = 0; j < images.length(); j++) {
                    imageList.add(images.get(i).toString());
                }
               JSONArray variantL = jObj.getJSONArray("variants");
               List<Variants> variantList = new ArrayList<>();
               for(int k=0; k< variantL.length(); k++){
                   JSONObject variant = variantL.getJSONObject(k);
                   variantList.add(new Variants(variant.getString("varientid"),variant.getString("variantname"),variant.getString("varprice")));
               }

               JSONArray extraL = jObj.getJSONArray("extra");
               List<Extra> extraList = new ArrayList<>();
               for(int k=0; k< extraL.length(); k++){
                   JSONObject extra = extraL.getJSONObject(k);
                   extraList.add(new Extra(extra.getString("extraid"),extra.getString("extraname"),extra.getString("extraprice")));
               }
Product p = new Product(jObj.getString("productId"), jObj.getString("productName"), jObj.getString("status"), jObj.getString("primaryimage"), jObj.getString("description"),jObj.getString("plimit"), imageList, variantList, extraList);
                recommendedProductList.add(p);
               Log.d("myTag", "added another : " + recommendedProductList.get(i).getProductName());

           }
getAllProducts();
       } catch(Exception ex){
           Log.d("myTag", "error : " , ex);
       }
    }

    public static void getAllProducts() {
        String req = "{\"res_id\":\"res007\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(mContext, mContext.getResources().getString(R.string.mJSONURL_product), "POST", "getAllProducts", req);
        // getting news list data
        /*
        Api.getClient().getAllProducts("res007", new Callback<List<Product>>() {
            @Override
            public void success(List<Product> allProducts, Response response) {
                try {
                    allProductsData = allProducts;
                    Log.d("allProductsData", allProducts.get(0).getProductName());
                    moveNext();
                } catch (Exception e) {
                    errorText.setText("No Product Added In This Store!");
                    internetNotAvailable.setVisibility(View.VISIBLE);
                    splashImage.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                errorText.setText("Internet Connection Not Available");
                internetNotAvailable.setVisibility(View.VISIBLE);
                splashImage.setVisibility(View.GONE);
            }
        });
        */
    }

    public static void getProductResponse(String response){
        try {
            allProductsData = new ArrayList<>();
            JSONArray PArr = new JSONArray(response);
            for(int i=0; i< PArr.length(); i++){
                JSONObject jObj = PArr.getJSONObject(i);
                List<String> imageList = new ArrayList<>();
                JSONArray images = jObj.getJSONArray("images");
                for (int j = 0; j < images.length(); j++) {
                    imageList.add(images.get(i).toString());
                }
                JSONArray variantL = jObj.getJSONArray("variants");
                List<Variants> variantList = new ArrayList<>();
                for(int k=0; k< variantL.length(); k++){
                    JSONObject variant = variantL.getJSONObject(k);
                    variantList.add(new Variants(variant.getString("varientid"),variant.getString("variantname"),variant.getString("varprice")));
                }

                JSONArray extraL = jObj.getJSONArray("extra");
                List<Extra> extraList = new ArrayList<>();
                for(int k=0; k< extraL.length(); k++){
                    JSONObject extra = extraL.getJSONObject(k);
                    extraList.add(new Extra(extra.getString("extraid"),extra.getString("extraname"),extra.getString("extraprice")));
                }
                Product p = new Product(jObj.getString("productId"), jObj.getString("productName"), jObj.getString("status"), jObj.getString("primaryimage"), jObj.getString("description"),jObj.getString("plimit"), imageList, variantList, extraList);
                allProductsData.add(p);
                Log.d("myTag", "added another : " + allProductsData.get(i).getProductName());
                moveNext();
            }

        } catch(Exception ex){
            Log.d("myTag", "error : " , ex);
        }
    }

    private static void moveNext() {
// redirect to next page after getting data from server

        boolean isFromNotification;
        try {
            productList = new ArrayList<>();
            if (id.length() > 0) {
                for (int j = 0; j < allProductsData.size(); j++) {
                    if (allProductsData.get(j).getProductId().trim().equalsIgnoreCase(id)) {
                        productList.add(allProductsData.get(j));
                    }
                }

                isFromNotification = true;
            } else {
                isFromNotification = false;
            }
        } catch (Exception e) {
            Log.d("error notification data", e.toString());
            isFromNotification = false;
        }
        if (isFromNotification)
        {
            Config.moveTo(mContext, MainActivity.class);
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra("isFromNotification", isFromNotification);
            mContext.startActivity(intent);
            //finishAffinity();
        } else if (Common.getSavedUserData(mContext, "firstTimeLogin").equalsIgnoreCase("")) {
            Config.moveTo(mContext, Login.class);
            //finishAffinity();
        } else {
            Config.moveTo(mContext, MainActivity.class);
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.putExtra("isFromNotification", isFromNotification);
            mContext.startActivity(intent);
            //finishAffinity();
        }

    }

}
