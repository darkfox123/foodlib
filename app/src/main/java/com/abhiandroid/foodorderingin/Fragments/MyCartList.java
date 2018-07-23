package com.abhiandroid.foodorderingin.Fragments;

import android.content.Context;
import android.content.Intent;
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
import com.abhiandroid.foodorderingin.Adapter.CartListAdapter;
import com.abhiandroid.foodorderingin.Extras.Config;
import com.abhiandroid.foodorderingin.Activities.Login;
import com.abhiandroid.foodorderingin.MVP.CartProducts;
import com.abhiandroid.foodorderingin.MVP.CartistResponse;
import com.abhiandroid.foodorderingin.Activities.MainActivity;
import com.abhiandroid.foodorderingin.MVP.Extra;
import com.abhiandroid.foodorderingin.MVP.Product;
import com.abhiandroid.foodorderingin.MVP.Variants;
import com.abhiandroid.foodorderingin.R;
import com.abhiandroid.foodorderingin.Retrofit.Api;
import com.abhiandroid.foodorderingin.Activities.SignUp;
import com.abhiandroid.foodorderingin.Activities.SplashScreen;
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

import static com.abhiandroid.foodorderingin.Fragments.FavoriteList.verify;

public class MyCartList extends Fragment {

    View view;
public static RecyclerView category;
public static Button proceed,conttinue;
public static LinearLayout cartlayyout,emaill,llogin;

   // @BindView(R.id.categoryRecyclerView)
    //static RecyclerView productsRecyclerView;
    public static List<CartProducts> productsData = new ArrayList<>();
    public static CartistResponse cartistResponseData;
    //@BindView(R.id.proceedToPayment)
    //static Button proceedToPayment;
    public static Context context;
   // @BindView(R.id.emptyCartLayout)
    //static LinearLayout emptyCartLayout;
    //@BindView(R.id.loginLayout)
    //LinearLayout loginLayout;
    //@BindView(R.id.continueShopping)
    //Button continueShopping;
public static Context mContext;
    //@BindView(R.id.verifyEmailLayout)
    //static LinearLayout verifyEmailLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart_list, container, false);
        ButterKnife.bind(this, view);
        category = (RecyclerView) getActivity().findViewById(R.id.categoriesRecyclerView);
        proceed = (Button) getActivity().findViewById(R.id.proceedToPayment);
        conttinue = (Button) getActivity().findViewById(R.id.continueShopping);
        cartlayyout = (LinearLayout) getActivity().findViewById(R.id.emptyCartLayout);
        emaill = (LinearLayout) getActivity().findViewById(R.id.verifyEmailLayout);
        llogin = (LinearLayout) getActivity().findViewById(R.id.loginLayout);

        context = getActivity();
        MainActivity.title.setText("My Cart");
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((int)Double.parseDouble(CartListAdapter.totalAmountPayable) >= Integer.parseInt(SplashScreen.restaurantDetailResponseData.getMinorder()))
                    ((MainActivity) getActivity()).loadFragment(new ChoosePaymentMethod(), true);
                else
                    Config.showCustomAlertDialog(getActivity(),
                            "",
                            "Minimum order value must be atleast " + SplashScreen.restaurantDetailResponseData.getMinorder(),
                            SweetAlertDialog.WARNING_TYPE);

            }
        });

        return view;
    }

    @OnClick({R.id.continueShopping, R.id.loginNow, R.id.txtSignUp, R.id.verfiyNow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continueShopping:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finishAffinity();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.cart.setVisibility(View.VISIBLE);
    }

    public void getCartList() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        String req = "{\"res_id\":\"res007\",\"user_id\":\""+ MainActivity.userId +"\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(getActivity(), getResources().getString(R.string.mJSONURL_viewcart), "POST", "viewCart", req);

        /*
        Api.getClient().getCartList("res007", MainActivity.userId, new Callback<CartistResponse>() {
            @Override
            public void success(CartistResponse cartistResponse, Response response) {

                cartistResponseData = cartistResponse;
                pDialog.dismiss();
                productsData = new ArrayList<>();
                productsData = cartistResponse.getProducts();
                if (cartistResponse.getSuccess().equalsIgnoreCase("false")) {
                    verifyEmailLayout.setVisibility(View.VISIBLE);
                    proceedToPayment.setVisibility(View.GONE);
                } else {
                    try {
                        Log.d("cartId", cartistResponse.getCartid());
                        cartistResponse.getProducts().size();
                        proceedToPayment.setVisibility(View.VISIBLE);
                        setProductsData();
                    } catch (Exception e) {
                        proceedToPayment.setVisibility(View.GONE);
                        emptyCartLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("errorInCartList", error.toString());

                pDialog.dismiss();

            }
        });
        */
    }

    public static void viewCartResponse(String response){
        try {
            JSONObject cartObj = new JSONObject(response);
            JSONArray PArr = cartObj.getJSONArray("cartProducts");
            for(int i=0; i< PArr.length(); i++) {
                JSONObject jObj = PArr.getJSONObject(i);
                List<String> imageList = new ArrayList<>();
                JSONArray images = jObj.getJSONArray("images");
                for (int j = 0; j < images.length(); j++) {
                    imageList.add(images.get(i).toString());
                }
               /*
                JSONArray variantL = jObj.getJSONArray("variants");
                List<Variants> variantList = new ArrayList<>();
                for (int k = 0; k < variantL.length(); k++) {
                    JSONObject variant = variantL.getJSONObject(k);
                    variantList.add(new Variants(variant.getString("variantid"), variant.getString("variantname"), variant.getString("varprice")));
                }
*/
                JSONArray extraL = jObj.getJSONArray("extra");
                List<Extra> extraList = new ArrayList<>();
                for (int k = 0; k < extraL.length(); k++) {
                    JSONObject extra = extraL.getJSONObject(k);
                    extraList.add(new Extra(extra.getString("extraid"), extra.getString("extraname"), extra.getString("extraprice")));
                }
                CartProducts p = new CartProducts(jObj.getString("productId"), jObj.getString("iteam_id"), jObj.getString("plimit"), jObj.getString("orderstatus"), jObj.getString("productName"), jObj.getString("mrp"),jObj.getString("sellprice"), jObj.getString("status"), jObj.getString("primaryimage"), jObj.getString("currency"), jObj.getString("quantity"), jObj.getString("stock"),jObj.getString("description"),null, imageList);
                productsData = new ArrayList<>();
                productsData.add(p);
                Log.d("myTag", "added another : " + productsData.get(i).getProductName());

            }
            cartistResponseData = new CartistResponse(cartObj.getString("cartid"),cartObj.getString("userid"),cartObj.getString("useremail"),cartObj.getString("tax"),cartObj.getString("delivery"),productsData );
            if (cartObj.getString("success").equalsIgnoreCase("false")) {
                verify.setVisibility(View.VISIBLE);
                proceed.setVisibility(View.GONE);
            } else {
                try {
                    Log.d("cartId", cartistResponseData.getCartid());
                    cartistResponseData.getProducts().size();
                    proceed.setVisibility(View.VISIBLE);
                    setProductsData();
                } catch (Exception e) {
                    proceed.setVisibility(View.GONE);
                    cartlayyout.setVisibility(View.VISIBLE);
                }
            }

        } catch(Exception ex){
            Log.d("myTag", "error : " , ex);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainActivity.cart.setVisibility(View.GONE);
        MainActivity.cartCount.setVisibility(View.GONE);
        Config.getCartList(getActivity(), false);
        if (!MainActivity.userId.equalsIgnoreCase("")) {
            getCartList();
        } else {
            proceed.setVisibility(View.GONE);
            llogin.setVisibility(View.VISIBLE);
        }
    }

    private static void setProductsData() {
        CartListAdapter wishListAdapter;
        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(context, 1);
        category.setLayoutManager(gridLayoutManager);
        wishListAdapter = new CartListAdapter(context, productsData);
        category.setAdapter(wishListAdapter);

    }
}
