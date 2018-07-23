package com.abhiandroid.foodorderingin.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.abhiandroid.foodorderingin.Extras.Config;
import com.abhiandroid.foodorderingin.MVP.SignUpResponse;
import com.abhiandroid.foodorderingin.R;
import com.abhiandroid.foodorderingin.Retrofit.Api;
import com.abhiandroid.foodorderingin.Volley.Volley_Request;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ForgotPassword extends AppCompatActivity {
    @BindViews({R.id.emailId})
    List<EditText> editTexts;
static SweetAlertDialog ps = null;
static Context mContext;
static Activity mActivity;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        mContext = ForgotPassword.this;
        mActivity = this;
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
    // sending gcm token to server

    @OnClick({R.id.back,R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                if (Config.validateEmail(editTexts.get(0),ForgotPassword.this)) {
                    forgotPassword();
                }
                break;
        }
    }

    private void forgotPassword() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(ForgotPassword.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        ps = pDialog;
        String req = "{\"res_id\":\"res007\",\"email\":\""+ editTexts.get(0).getText().toString().trim() +"\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(ForgotPassword.this, getResources().getString(R.string.mJSONURL_forgot), "POST", "forgotPwd", req);


        /*
        Api.getClient().forgotPassword("res007", editTexts.get(0).getText().toString().trim(),
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        pDialog.dismiss();
                        Log.d("signUpResponse", signUpResponse.getMessage());
                        Toast.makeText(ForgotPassword.this, signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if (signUpResponse.getSuccess().equalsIgnoreCase("true")) {
                            Config.moveTo(ForgotPassword.this, Login.class);
                            finishAffinity();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();

                        Log.e("error", error.toString());
                    }
                });
                */
    }

    public static void forgotPasswordResponse(String response){
        ps.dismiss();
        try {
            JSONObject jObj = new JSONObject(response);
            Log.d("signUpResponse", jObj.getString("message"));
            Toast.makeText(mContext, jObj.getString("message"), Toast.LENGTH_SHORT).show();
            if (jObj.getString("success").equalsIgnoreCase("true")) {
                Config.moveTo(mContext, Login.class);
                mActivity.finish();
            }
        }catch(Exception ex){
            Log.d("myTag", "error : ", ex);
        }
    }
}
