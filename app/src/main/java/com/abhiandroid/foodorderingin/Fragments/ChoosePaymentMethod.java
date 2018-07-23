package com.abhiandroid.foodorderingin.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.abhiandroid.foodorderingin.Extras.Config;
import com.abhiandroid.foodorderingin.MVP.UserProfileResponse;
import com.abhiandroid.foodorderingin.Activities.MainActivity;
import com.abhiandroid.foodorderingin.PaymentIntegrationMethods.RazorPayIntegration;
import com.abhiandroid.foodorderingin.R;
import com.abhiandroid.foodorderingin.Retrofit.Api;
import com.abhiandroid.foodorderingin.Activities.SplashScreen;
import com.abhiandroid.foodorderingin.Volley.Volley_Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ChoosePaymentMethod extends Fragment {

    View view;
    public static LinearLayout addresss,chooose;
    public static CheckBox check;
    public static TextView addd,filll;
    public static RadioGroup payment;
    public static ProgressBar pg;
    public static Button bt;

    //@BindView(R.id.addNewAddressLayout)
    //LinearLayout addNewAddressLayout;
    //@BindView(R.id.addressCheckBox)
    //static CheckBox addressCheckBox;
    //@BindView(R.id.addNewAddress)
    //TextView addNewAddress;
    //@BindView(R.id.fillAddress)
    //static TextView fillAddress;
    //@BindView(R.id.paymentMethodsGroup)
    //RadioGroup paymentMethodsGroup;
    //@BindView(R.id.makePayment)
    //static Button makePayment;
    String paymentMethod;
    //@BindView(R.id.progressBar)
    //static ProgressBar progressBar;
    //@BindView(R.id.choosePaymentLayout)
    //LinearLayout choosePaymentLayout;
    @BindViews({R.id.fullNameEdt, R.id.mobEditText, R.id.cityEditText, R.id.areaEditText, R.id.buildingEditText, R.id.pincodeEditText, R.id.stateEditText, R.id.landmarkEditText,})
    List<EditText> editTexts;
    public static String address, mobileNo,userEmail,profilePinCode;
    Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int layout = R.layout.fragment_choose_payment_method;
        view = inflater.inflate(layout, container, false);
        ButterKnife.bind(this, view);
        bt = (Button) getActivity().findViewById(R.id.makePayment);

        addresss = (LinearLayout) getActivity().findViewById(R.id.addNewAddressLayout);
        chooose = (LinearLayout) getActivity().findViewById(R.id.choosePaymentLayout);
        check = (CheckBox) getActivity().findViewById(R.id.addressCheckBox);
        addd = (TextView) getActivity().findViewById(R.id.addNewAddress);
filll = (TextView) getActivity().findViewById(R.id.fillAddress);
pg = (ProgressBar) getActivity() .findViewById(R.id.progressBar);
        payment = (RadioGroup) getActivity() .findViewById(R.id.paymentMethodsGroup);

        MainActivity.title.setText("Choose Payment Method");
        MainActivity.cart.setVisibility(View.GONE);
        MainActivity.cartCount.setVisibility(View.GONE);
        getUserProfileData();
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    addresss.setVisibility(View.GONE);
                    addd.setText("Add New Address");

                }
            }
        });
        chooose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);

            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.cart.setVisibility(View.VISIBLE);
        MainActivity.cartCount.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.addNewAddress, R.id.makePayment, R.id.fillAddress})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addNewAddress:
                addresss.setVisibility(View.VISIBLE);
                check.setChecked(false);
                addd.setText("Use This Address");
                break;
            case R.id.makePayment:
                if (!check.isChecked()) {
                    if (addresss.getVisibility() == View.VISIBLE) {
                        if (validate(editTexts.get(0))
                                && validate(editTexts.get(1))
                                && validate(editTexts.get(2))
                                && validate(editTexts.get(3))
                                && validate(editTexts.get(4))
                                && validatePinCode(editTexts.get(5))
                                && validate(editTexts.get(6))) {
                            String s = "";
                            if (editTexts.get(6).getText().toString().trim().length() > 0) {
                                s = ", " + editTexts.get(6).getText().toString().trim();
                            }
                            address = editTexts.get(0).getText().toString().trim()
                                    + ", "
                                    + editTexts.get(4).getText().toString().trim()
                                    + s
                                    + ", " + editTexts.get(3).getText().toString().trim()
                                    + ", " + editTexts.get(2).getText().toString().trim()
                                    + ", " + editTexts.get(6).getText().toString().trim()
                                    + ", " + editTexts.get(5).getText().toString().trim()
                                    + "\n" + editTexts.get(1).getText().toString().trim();
                            mobileNo = editTexts.get(1).getText().toString().trim();
                            moveNext();
                        }
                    } else {
                        Config.showCustomAlertDialog(getActivity(),
                                "Please choose your saved address or add new to make payment",
                                "",
                                SweetAlertDialog.ERROR_TYPE);
                    }
                } else {
                    if (SplashScreen.restaurantDetailResponseData.getDeliverycity().contains(profilePinCode.trim()))
                        moveNext();
                    else {
                        Config.showPincodeCustomAlertDialog1(getActivity(),
                                "Not Available",
                                "We have stopped food delivery in your area. Please change your pincode.",
                                SweetAlertDialog.WARNING_TYPE);

                    }
                }

                break;
            case R.id.fillAddress:
                ((MainActivity) getActivity()).loadFragment(new MyProfile(), true);
                break;
        }

    }
    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void moveNext() {
        switch (payment.getCheckedRadioButtonId()) {

            case R.id.razorPay:
                paymentMethod = "razorPay";
                intent = new Intent(getActivity(), RazorPayIntegration.class);
                startActivity(intent);
                break;
            case R.id.cod:
                paymentMethod = "cod";
                Config.addOrder(getActivity(),
                        "COD",
                        "COD");
                break;

            default:
                paymentMethod = "";
                Config.showCustomAlertDialog(getActivity(),
                        "Payment Method",
                        "Select your payment method to make payment",
                        SweetAlertDialog.NORMAL_TYPE);
                break;


        }

        Log.d("paymentMethod", paymentMethod);
    }

    private boolean validate(EditText editText) {
        if (editText.getText().toString().trim().length() > 0) {
            return true;
        }
        editText.setError("Please Fill This");
        editText.requestFocus();
        return false;
    }

    private boolean validatePinCode(EditText editText) {
        if (editText.getText().toString().trim().length() > 0) {
            if (SplashScreen.restaurantDetailResponseData.getDeliverycity().contains(editText.getText().toString().trim()))
                return true;
            else {
                Config.showPincodeCustomAlertDialog(getActivity(),
                        "Not Available",
                        "We currently don't deliver in your area.",
                        SweetAlertDialog.WARNING_TYPE);
                editText.setError("Not available");
                editText.requestFocus();
                return false;
            }
        }
        editText.setError("Please Fill This");
        editText.requestFocus();
        return false;
    }

    public void getUserProfileData() {
        pg.setVisibility(View.VISIBLE);
        bt.setClickable(false);
        String req = "{\"res_id\":\"res007\",\"user_id\":\""+ MainActivity.userId +"\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(getActivity(), getResources().getString(R.string.mJSONURL_userprofile), "POST", "getuserprofilepayment", req);

        /*
        Api.getClient().getUserProfile("res007",
                MainActivity.userId, new Callback<UserProfileResponse>() {
                    @Override
                    public void success(UserProfileResponse userProfileResponse, Response response) {
                        makePayment.setClickable(true);
                        progressBar.setVisibility(View.GONE);
                        userEmail=userProfileResponse.getEmail();
                        String s = "";
                        if (!userProfileResponse.getLandmark().equalsIgnoreCase("")) {
                            s = ", " + userProfileResponse.getLandmark();
                        }
                        if (userProfileResponse.getFlat().equalsIgnoreCase("")) {
                            addressCheckBox.setChecked(false);
                            addressCheckBox.setVisibility(View.GONE);
                            fillAddress.setVisibility(View.VISIBLE);
                        } else {
                            address = userProfileResponse.getName()
                                    + ", "
                                    + userProfileResponse.getFlat()
                                    + s
                                    + ", " + userProfileResponse.getLocality()
                                    + ", " + userProfileResponse.getCity()
                                    + ", " + userProfileResponse.getState()
                                    + ", " + userProfileResponse.getPincode()
                                    + "\n" + userProfileResponse.getMobile();
                            addressCheckBox.setText(address);
                            mobileNo = userProfileResponse.getMobile();
                            profilePinCode = userProfileResponse.getPincode();
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        makePayment.setClickable(true);
                        progressBar.setVisibility(View.GONE);

                    }
                });
                */
    }

    public static void getProfileResponse(String response){
        try {
            JSONObject userProfileResponse = new JSONObject(response);
            bt.setClickable(true);
            pg.setVisibility(View.GONE);
            userEmail=userProfileResponse.getString("email");
            String s = "";
            if (!userProfileResponse.getString("Landmark").equalsIgnoreCase("")) {
                s = ", " + userProfileResponse.getString("Landmark");
            }
            if (userProfileResponse.getString("flat").equalsIgnoreCase("")) {
                check.setChecked(false);
                check.setVisibility(View.GONE);
                filll.setVisibility(View.VISIBLE);
            } else {
                address = userProfileResponse.getString("name")
                        + ", "
                        + userProfileResponse.getString("flat")
                        + s
                        + ", " + userProfileResponse.getString("Locality")
                        + ", " + userProfileResponse.getString("City")
                        + ", " + userProfileResponse.getString("state")
                        + ", " + userProfileResponse.getString("pincode")
                        + "\n" + userProfileResponse.getString("mobile");
                check.setText(address);
                mobileNo = userProfileResponse.getString("mobile");
                profilePinCode = userProfileResponse.getString("pincode");
            }

        }catch (JSONException ex) {
            Log.d("myTag", "response error : " , ex);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
}
