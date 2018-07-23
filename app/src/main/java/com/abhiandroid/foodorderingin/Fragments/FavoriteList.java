package com.abhiandroid.foodorderingin.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


import com.abhiandroid.foodorderingin.Activities.AccountVerification;
import com.abhiandroid.foodorderingin.Adapter.MyWishListAdapter;
import com.abhiandroid.foodorderingin.Extras.Config;
import com.abhiandroid.foodorderingin.Activities.Login;
import com.abhiandroid.foodorderingin.MVP.Extra;
import com.abhiandroid.foodorderingin.MVP.Product;
import com.abhiandroid.foodorderingin.MVP.Variants;
import com.abhiandroid.foodorderingin.MVP.WishlistResponse;
import com.abhiandroid.foodorderingin.Activities.MainActivity;
import com.abhiandroid.foodorderingin.R;
import com.abhiandroid.foodorderingin.Retrofit.Api;
import com.abhiandroid.foodorderingin.Activities.SignUp;
import com.abhiandroid.foodorderingin.Volley.Volley_Request;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FavoriteList extends Fragment {

    View view;
public static RecyclerView productt;
public static LinearLayout wishlist,loginn,verify;
public static Button continuee;
   // @BindView(R.id.categoryRecyclerView)
    //static RecyclerView productsRecyclerView;
    public static List<Product> productsData = new ArrayList<>();
    //@BindView(R.id.emptyWishlistLayout)
    //LinearLayout emptyWishlistLayout;
    //@BindView(R.id.loginLayout)
    //LinearLayout loginLayout;
    //@BindView(R.id.verifyEmailLayout)
    //static LinearLayout verifyEmailLayout;
    //@BindView(R.id.continueShopping)
    //Button continueShopping;
    public static SweetAlertDialog pd;
public static MyWishListAdapter wishListAdapter;
public static Activity mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_wish_list, container, false);
        ButterKnife.bind(this, view);
        productt = (RecyclerView) getActivity().findViewById(R.id.categoriesRecyclerView);
        wishlist = (LinearLayout) getActivity().findViewById(R.id.emptyWishlistLayout);
        loginn = (LinearLayout) getActivity().findViewById(R.id.loginLayout);
        verify = (LinearLayout) getActivity().findViewById(R.id.verifyEmailLayout);
        continuee = (Button) getActivity().findViewById(R.id.continueShopping);
        MainActivity.title.setText("My Favorite");
        mContext = getActivity();
        if (!MainActivity.userId.equalsIgnoreCase("")) {
            getWishList();
        } else {
            loginn.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @OnClick({R.id.continueShopping, R.id.loginNow, R.id.txtSignUp, R.id.verfiyNow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continueShopping:
                Config.moveTo(getActivity(), MainActivity.class);
                getActivity().finish();
                break;
            case R.id.loginNow:
                Config.moveTo(getActivity(), Login.class);
                break;
            case R.id.txtSignUp:
                Config.moveTo(getActivity(), SignUp.class);
                break;
            case R.id.verfiyNow:
                Config.moveTo(getActivity(), AccountVerification.class);
                break;
        }
    }

    public void getWishList() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        pd= pDialog;
        String req = "{\"res_id\":\"res007\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(getActivity(), getResources().getString(R.string.mJSONURL_wishlist), "POST", "getWishList", req);
        /*
        Api.getClient().getWishList("res007", MainActivity.userId, new Callback<WishlistResponse>() {
            @Override
            public void success(WishlistResponse wishlistResponse, Response response) {
                pDialog.dismiss();
                try {
                    if (wishlistResponse.getSuccess().equalsIgnoreCase("true")) {

                        Log.d("cartId", wishlistResponse.getProducts().size() + "");
                        productsData.clear();
                        productsData = wishlistResponse.getProducts();
                        setProductsData();

                    } else {
                        verifyEmailLayout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Log.d("wishList", "Not available");
                    emptyWishlistLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                emptyWishlistLayout.setVisibility(View.VISIBLE);
                pDialog.dismiss();

            }
        });
        */
    }

    public static void getWishListResponse(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            List<Product> pList = new ArrayList<>();
            JSONArray PArr = jsonObject.getJSONArray("product");
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
                    variantList.add(new Variants(variant.getString("variantid"),variant.getString("variantname"),variant.getString("varprice")));
                }

                JSONArray extraL = jObj.getJSONArray("extra");
                List<Extra> extraList = new ArrayList<>();
                for(int k=0; k< extraL.length(); k++){
                    JSONObject extra = extraL.getJSONObject(k);
                    extraList.add(new Extra(extra.getString("extraid"),extra.getString("extraname"),extra.getString("extraprice")));
                }
                Product p = new Product(jObj.getString("productId"), jObj.getString("productName"), jObj.getString("status"), jObj.getString("primaryimage"), jObj.getString("description"),jObj.getString("plimit"), imageList, variantList, extraList);
                pList.add(p);
                Log.d("myTag", "added another : " + pList.get(i).getProductName());
            }
            pd.dismiss();
            if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                Log.d("cartId", pList.size() + "");
                productsData.clear();
                productsData = pList;
                setProductsData();

            } else {
                verify.setVisibility(View.VISIBLE);
            }
        }catch(Exception ex){
            Log.d("myTag", "error : " , ex);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        Config.getCartList(getActivity(), true);
    }

    private static void setProductsData() {
        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(mContext, 2);
        productt.setLayoutManager(gridLayoutManager);
        wishListAdapter = new MyWishListAdapter(mContext, productsData);
        productt.setAdapter(wishListAdapter);

    }
}
