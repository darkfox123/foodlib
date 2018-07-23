package com.abhiandroid.foodorderingin.Fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.abhiandroid.foodorderingin.Adapter.MyPagerAdapter;
import com.abhiandroid.foodorderingin.Adapter.ProductsByCategoryAdapter;
import com.abhiandroid.foodorderingin.Extras.Config;
import com.abhiandroid.foodorderingin.Extras.DetectConnection;
import com.abhiandroid.foodorderingin.MVP.CategoryListResponse;
import com.abhiandroid.foodorderingin.MVP.Extra;
import com.abhiandroid.foodorderingin.MVP.Product;
import com.abhiandroid.foodorderingin.MVP.RecommendedProductsResponse;
import com.abhiandroid.foodorderingin.MVP.RestaurantDetailResponse;
import com.abhiandroid.foodorderingin.Activities.MainActivity;
import com.abhiandroid.foodorderingin.MVP.Variants;
import com.abhiandroid.foodorderingin.R;
import com.abhiandroid.foodorderingin.Retrofit.Api;
import com.abhiandroid.foodorderingin.Activities.SplashScreen;
import com.abhiandroid.foodorderingin.Volley.Volley_Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindString;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainFragment extends Fragment {

    View view;
    public static Activity activity;
    private String TAG = "testing";
    @BindString(R.string.app_name)
    String app_name;
    public static SwipeRefreshLayout swipeRefreshLayout;
    public static ViewPager viewPager;
    public static TabLayout simpleTabLayout;
    ViewPager.OnPageChangeListener mOnPageChangeListener;
    public static LinearLayout tabLinearlayout;
    public static ArrayList<Integer> selectedPosList;
    public static LinkedHashMap<Integer, ArrayList<Integer>> selectedPosHashMap = new LinkedHashMap<>();
    public static int viewPagerCurrentPos = 0;
    public static ViewPagerAdapter adapter;
    public static List<ProductsByCategoryAdapter> instances = new ArrayList();
    public static MyPagerAdapter mAdapter;
    public static List<Extra> extraList;
    public static Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        MainActivity.title.setText(app_name);
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        simpleTabLayout = (TabLayout) view.findViewById(R.id.simpleTabLayout);
        tabLinearlayout = (LinearLayout) view.findViewById(R.id.tabLinearlayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        activity = (Activity) view.getContext();
        mContext = getActivity();
        MainActivity.cart.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_UNLOCKED);
        MainActivity.drawerLayout.closeDrawers();

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayout);

        // implement setOnRefreshListener event on SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(getActivity())) {
                    getCategoryList();

                } else {
                    Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("onStart", "called " + viewPagerCurrentPos);
        MainActivity.search.setVisibility(View.VISIBLE);
        selectedPosList = new ArrayList<>();
        if (viewPagerCurrentPos == 0 ) {
            for (int i = 0; i < SplashScreen.recommendedProductList.size(); i++) {
                selectedPosList.add(0);
            }
        } else {
            for (int i = 0; i < SplashScreen.categoryListResponseData.get(viewPagerCurrentPos - 1).getProducts().size(); i++) {
                selectedPosList.add(0);
            }
        }
        selectedPosHashMap.put(viewPagerCurrentPos, selectedPosList);
        instances = new ArrayList<>();
        createTabs(); // create custom tabs
        Config.getCartList(getActivity(), true);


    }

    @Override
    public void onStop() {
        super.onStop();
        MainActivity.search.setVisibility(View.GONE);
    }

    private static void createTabs() {
        setupViewPager(viewPager);
        simpleTabLayout.setupWithViewPager(viewPager);
    }

    public static List<ProductsByCategoryAdapter> getInstances() {
        return instances;
    }

    private static void setupViewPager(final ViewPager viewPager) {
        ProductsByCategoryAdapter.isFirstTime = true;

        adapter.addFragment(new Home(), "Home");
        for (int i = 0; i < SplashScreen.categoryListResponseData.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("pos", i);
            ProductsByCategory categoryWiseNewsList = new ProductsByCategory();
            categoryWiseNewsList.setArguments(bundle);
            adapter.addFragment(categoryWiseNewsList, SplashScreen.categoryListResponseData.get(i).getCategory_name());
        }
        viewPager.setOffscreenPageLimit(SplashScreen.categoryListResponseData.size() + 1);
        Log.d("viewPagerPosition", viewPagerCurrentPos + "");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(viewPagerCurrentPos);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("pagePosition", position + "");
                viewPagerCurrentPos = position;
                selectedPosList = new ArrayList<>();
                try {
                    Log.d("selectedHashMap", selectedPosHashMap.get(position).toString());
                    selectedPosList.addAll(selectedPosHashMap.get(position));
                } catch (Exception e) {
                    if (position != 0) {
                        for (int i = 0; i < SplashScreen.categoryListResponseData.get(position - 1).getProducts().size(); i++) {
                            selectedPosList.add(0);
                        }
                    } else {
                        for (int i = 0; i < SplashScreen.recommendedProductList.size(); i++) {
                            selectedPosList.add(0);
                        }
                    }
                    selectedPosHashMap.put(position, selectedPosList);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE);

            }
        });
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    private static void enableDisableSwipeRefresh(boolean enable) {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(enable);
        }
    }


    // API Methods

    public void getCategoryList() {
        // getting category list news data
        String req = "{\"res_id\":\"res007\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(getActivity(), getActivity().getResources().getString(R.string.mJSONURL_catlist), "POST", "getCatListMF", req);

       /*
        Api.getClient().getCategoryList("res007", new Callback<List<CategoryListResponse>>() {
            @Override
            public void success(List<CategoryListResponse> categoryListResponses, Response response) {

                try {
                    SplashScreen.categoryListResponseData = new ArrayList<>();
                    SplashScreen.categoryListResponseData.addAll(categoryListResponses);
                    Log.d("categoryData", categoryListResponses.get(0).getCategory_name());

                    getRestaurantDetail();
                } catch (Exception e) {
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        */
    }

    public static void getCategoryResponse(String response){
        try {
            JSONArray catList = new JSONArray(response);
            SplashScreen.categoryListResponseData = new ArrayList<CategoryListResponse>();
            for(int i = 0 ; i < catList.length(); i++){
                JSONArray productArr = catList.getJSONObject(i).getJSONArray("products");
                List<Product> productList = new ArrayList<>();
                for(int j=0; j<productArr.length(); j++){
                    Product pr = new Product();
                    productList.add(pr);
                }
                CategoryListResponse ctr = new CategoryListResponse(productList, null ,catList.getJSONObject(i).getString("cat_id"),catList.getJSONObject(i).getString("category_name"),catList.getJSONObject(i).getString("category_image"));
                SplashScreen.categoryListResponseData.add(ctr);
                ////Log.d("myTag", "parsed : " + categoryListResponseData.get(0).getCategory_name());
            }
            Log.d("myTag", SplashScreen.categoryListResponseData.get(0).getCat_id() + " : " + SplashScreen.categoryListResponseData.get(1).getCategory_name());
            getRestaurantDetail();
        }catch(Exception e){
            Log.d("myTag", "error in json : " , e);
        }
    }


    public static void getRestaurantDetail() {
        String req = "{\"res_id\":\"res007\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(mContext, mContext.getResources().getString(R.string.mJSONURL_restaurantdetail), "POST", "getRestaurantDetailMF", req);

        // getting slider list data
        /*
        Api.getClient().getRestaurantDetail("res007", new Callback<RestaurantDetailResponse>() {
            @Override
            public void success(RestaurantDetailResponse restaurantDetailResponse, Response response) {
                SplashScreen.restaurantDetailResponseData = restaurantDetailResponse;
                getRecommendedList();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                swipeRefreshLayout.setRefreshing(false);

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
            SplashScreen.restaurantDetailResponseData = new RestaurantDetailResponse(jObj.getString("name"),jObj.getString("address"), jObj.getString("description"), jObj.getString("phone"), jObj.getString("web"), jObj.getString("lat"), jObj.getString("lon"),jObj.getString("time"), jObj.getString("tax"), jObj.getString("currency"), jObj.getString("minorder"), imageList,deliveryList);
            Log.d("myTag", "added another : " + SplashScreen.restaurantDetailResponseData.getAddress());
            getRecommendedList();
        }catch(JSONException ex ){
            Log.d("myTag", "ex : " , ex);
        }
    }


    public static void getRecommendedList() {
        String req = "{\"res_id\":\"res007\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(mContext, mContext.getResources().getString(R.string.mJSONURL_recommproduct), "POST", "getRecommendedProductsMF", req);

        // getting slider list data
        /*
        Api.getClient().getRecommendedProducts("resId", new Callback<RecommendedProductsResponse>() {
            @Override
            public void success(RecommendedProductsResponse recommendedProductsResponse, Response response) {
                if (recommendedProductsResponse.getSuccess().equalsIgnoreCase("true")) {
                    SplashScreen.recommendedProductList = new ArrayList<>();
                    SplashScreen.recommendedProductList.addAll(recommendedProductsResponse.getProductList());
                } else {
                    SplashScreen.recommendedProductList = new ArrayList<>();
                }
                getAllProducts();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        */
    }

    public static void getRecommListResponse(String response){
        try {
            SplashScreen.recommendedProductList = new ArrayList<>();
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
                    variantList.add(new Variants(variant.getString("variantid"),variant.getString("variantname"),variant.getString("varprice")));
                }

                JSONArray extraL = jObj.getJSONArray("extra");
                List<Extra> extraList = new ArrayList<>();
                for(int k=0; k< extraL.length(); k++){
                    JSONObject extra = extraL.getJSONObject(k);
                    extraList.add(new Extra(extra.getString("extraid"),extra.getString("extraname"),extra.getString("extraprice")));
                }
                Product p = new Product(jObj.getString("productId"), jObj.getString("productName"), jObj.getString("status"), jObj.getString("primaryimage"), jObj.getString("description"),jObj.getString("plimit"), imageList, variantList, extraList);
                SplashScreen.recommendedProductList.add(p);
                Log.d("myTag", "added another : " + SplashScreen.recommendedProductList.get(i).getProductName());

            }
            getAllProducts();
        } catch(Exception ex){
            Log.d("myTag", "error : " , ex);
        }
    }


    public static void getAllProducts() {
        // getting news list data
        String req = "{\"res_id\":\"res007\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(mContext, mContext.getResources().getString(R.string.mJSONURL_product), "POST", "getAllProductsMF", req);

        /*
        Api.getClient().getAllProducts("res007", new Callback<List<Product>>() {
            @Override
            public void success(List<Product> allProducts, Response response) {

                swipeRefreshLayout.setRefreshing(false);

                try {
                    SplashScreen.allProductsData = new ArrayList<>();
                    SplashScreen.allProductsData.addAll(allProducts);
                } catch (Exception e) {

                }

                selectedPosList = new ArrayList<>();
                if (viewPagerCurrentPos == 0) {
                    for (int i = 0; i < SplashScreen.recommendedProductList.size(); i++) {
                        selectedPosList.add(0);
                    }
                } else {
                    for (int i = 0; i < SplashScreen.categoryListResponseData.get(viewPagerCurrentPos - 1).getProducts().size(); i++) {
                        selectedPosList.add(0);
                    }
                }
                selectedPosHashMap.put(viewPagerCurrentPos, selectedPosList);
                instances = new ArrayList<>();
                createTabs(); // create custom tabs


            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        */
    }

    public static void getProductResponse(String response){
        try {
            swipeRefreshLayout.setRefreshing(false);

            SplashScreen.allProductsData = new ArrayList<>();
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
                    variantList.add(new Variants(variant.getString("variantid"),variant.getString("variantname"),variant.getString("varprice")));
                }

                JSONArray extraL = jObj.getJSONArray("extra");
                List<Extra> extraList = new ArrayList<>();
                for(int k=0; k< extraL.length(); k++){
                    JSONObject extra = extraL.getJSONObject(k);
                    extraList.add(new Extra(extra.getString("extraid"),extra.getString("extraname"),extra.getString("extraprice")));
                }
                Product p = new Product(jObj.getString("productId"), jObj.getString("productName"), jObj.getString("status"), jObj.getString("primaryimage"), jObj.getString("description"),jObj.getString("plimit"), imageList, variantList, extraList);
                SplashScreen.allProductsData.add(p);
                Log.d("myTag", "added another : " + SplashScreen.allProductsData.get(i).getProductName());
               // moveNext();

            }

            selectedPosList = new ArrayList<>();
            if (viewPagerCurrentPos == 0) {
                for (int i = 0; i < SplashScreen.recommendedProductList.size(); i++) {
                    selectedPosList.add(0);
                }
            } else {
                for (int i = 0; i < SplashScreen.categoryListResponseData.get(viewPagerCurrentPos - 1).getProducts().size(); i++) {
                    selectedPosList.add(0);
                }
            }
            selectedPosHashMap.put(viewPagerCurrentPos, selectedPosList);
            instances = new ArrayList<>();
            createTabs(); // create custom tabs


        } catch(Exception ex){
            Log.d("myTag", "error : " , ex);
        }
    }

}
