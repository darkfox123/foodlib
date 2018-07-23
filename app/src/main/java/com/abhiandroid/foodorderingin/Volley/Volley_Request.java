package com.abhiandroid.foodorderingin.Volley;

import android.content.Context;
import android.util.Log;

import com.abhiandroid.foodorderingin.Activities.EditCart;
import com.abhiandroid.foodorderingin.Activities.ForgotPassword;
import com.abhiandroid.foodorderingin.Activities.ProductExtra;
import com.abhiandroid.foodorderingin.Activities.SignUp;
import com.abhiandroid.foodorderingin.Activities.SplashScreen;
import com.abhiandroid.foodorderingin.Adapter.CartListAdapter;
import com.abhiandroid.foodorderingin.Extras.Config;
import com.abhiandroid.foodorderingin.Fragments.ChoosePaymentMethod;
import com.abhiandroid.foodorderingin.Fragments.FAQ;
import com.abhiandroid.foodorderingin.Fragments.FavoriteList;
import com.abhiandroid.foodorderingin.Fragments.MainFragment;
import com.abhiandroid.foodorderingin.Fragments.MyCartList;
import com.abhiandroid.foodorderingin.Fragments.MyOrders;
import com.abhiandroid.foodorderingin.Fragments.MyProfile;
import com.abhiandroid.foodorderingin.Fragments.ProductDetail;
import com.abhiandroid.foodorderingin.Fragments.TermsAndConditions;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Asus on 10/25/2017.
 */

public class Volley_Request {

    static String responseString = "";

    public static void createRequest(Context mContext, String requestURI, String type, String returnPath, final String requestBody) {

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        if (type.equals("GET")) {
            Log.d("myTag", "inside get req");
            // Initialize a new JsonObjectRequest instance
            /*
            final String returnPathCopy = returnPath;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    requestURI,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Do something with response

                            for (int i = 0; i < 10; i++) {
                                //Log.d("myTag", "onResponse len : " + response.length());
                                if (response != null) {
                                    responseString = response.toString();
                                    Log.d("myTag", "responsestring != 1 : " + responseString);

                                } else {
                                    try {
                                        Thread.sleep(2000);
                                    } catch (Exception e) {
                                        Log.d("myTag", "error in sleep", e);
                                    }
                                }
                            }


                            //Log.d("myTag", "response : " + response.toString());
//responseString = response.toString();
                            // Log.d("myTag", "response assigned : " + responseString.toString());

                            //intent for Create Notification Page
                            // Intent i = new Intent(LandingActivity.this, SignupActivity.class);

                            // i.putExtra("input",response.toString());
                            //  i.putExtra("schoolId", "pihu007");
                            // startActivity(i);
                            // citySpinnerLoader(response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Do something when error occurred
                            Log.d("myTag", "error aaya : " + error.toString());
                        }
                    }
            );
            jsonObjectRequest.setTag("GET");
            requestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    Log.d("DEBUG", "request running: " + request.getTag().toString());
                    return true;
                }
            });
            requestQueue.add(jsonObjectRequest);
            */
        } else if (type.equals("POST")) {

            Log.d("myTag", "requesturi : " + requestURI + " : " + requestBody);
            //Log.d("myTag", "requesturi : " + requestURI);
            final String returnPathCopy = returnPath;
            JSONObject requestObject = null;
            try {
                requestObject = new JSONObject(requestBody);
                //requestArray = new JSONObject(requestBody);

            } catch (Exception e) {
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, requestURI, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("myTag", "got response : " + response);
                    /*
                    else if (returnPathCopy == "addcart")
                        EditCart.getaddToCart(responseString);
                    else if (returnPathCopy == "deletecart")
                        CartListAdapter.deleteCartResponse(responseString);
                    else if (returnPathCopy == "wishcheck")
                        ProductDetail.wishcheckResponse(responseString);
                    else if (returnPathCopy == "getWishList")
                        FavoriteList.getWishListResponse(responseString);
                    else if (returnPathCopy == "getCartList")
                        Config.getCartListResponse(responseString);
                    else if (returnPathCopy == "viewCart")
                        MyCartList.viewCartResponse(responseString);
                        else if (returnPathCopy == "userprofileprofile")
                        MyProfile.getUserProfileResponse(responseString);
                        else if (returnPathCopy == "userprofileprofile")
                        MyProfile.getUpdateProfileResponse(responseString);
                        else if (returnPathCopy == "resentemail")
                        AccountVerification.getResentEmailResponse(responseString);
                        else if (returnPathCopy == "login")
                        Login.getLoginResponse(responseString);
                        else if (returnPathCopy == "addOrder")
                        Config.addOrderResponse(responseString);
                        else if (returnPathCopy == "forgotPwd")
                        ForgotPassword.forgotPwdResponse(responseString);
                        else if (returnPathCopy == "registration")
                        SignUp.registrationResponse(responseString);
                        else
                        */
                    if (returnPathCopy == "getCatList")
                        SplashScreen.getCategoryResponse(response);
                    else if (returnPathCopy == "getRestaurantDetail")
                        SplashScreen.resDetailResponse(response);
                    else if (returnPathCopy == "getRecommendedProducts")
                        SplashScreen.getRecommListResponse(response);
                    else if (returnPathCopy == "getAllProducts")
                        SplashScreen.getProductResponse(response);
                    else if (returnPathCopy == "getCatListMF")
                        MainFragment.getCategoryResponse(response);
                    else if (returnPathCopy == "getRestaurantDetailMF")
                        MainFragment.resDetailResponse(response);
                    else if (returnPathCopy == "getRecommendedProductsMF")
                        MainFragment.getRecommListResponse(response);
                    else if (returnPathCopy == "getAllProductsMF")
                        MainFragment.getProductResponse(response);
                    else if (returnPathCopy == "getuserprofilepayment")
                        ChoosePaymentMethod.getProfileResponse(response);
                    else if (returnPathCopy == "getFAQ")
                        FAQ.getFAQResponse(response);
                    else if (returnPathCopy == "getTerms")
                        TermsAndConditions.getTCResponse(response);
                    else if (returnPathCopy == "getWishList")
                        FavoriteList.getWishListResponse(response);
                    else if (returnPathCopy == "getMyOrders")
                        MyOrders.myOrdersResponse(response);
                    else if (returnPathCopy == "viewCart")
                        MyCartList.viewCartResponse(response);
                    else if (returnPathCopy == "userprofileprofile")
                        MyProfile.getUpdateProfileResponse(response);
                    else if (returnPathCopy == "userprofileprofile")
                        MyProfile.getUserProfileResponse(response);
                    else if (returnPathCopy == "addwishlist")
                        ProductDetail.addToWishListResponse(response);
                    else if (returnPathCopy == "wishcheck")
                        ProductDetail.wishCheckResponse(response);
                    else if (returnPathCopy == "addcart")
                        EditCart.addToCartResponse(response);
                    else if (returnPathCopy == "forgotPwd")
                        ForgotPassword.forgotPasswordResponse(response);
                    else if (returnPathCopy == "addcart")
                        ProductExtra.addCartResponse(response);
                    else if (returnPathCopy == "registration")
                        SignUp.registrationResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        Log.d("myTag","responsse body : " + new String(response.data));
                        responseString = String.valueOf(new String(response.data));
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        }
    }
}



