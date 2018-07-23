package com.digitalhomeland.foodlibrary;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class FoodVeda {

    public static void restaurantLoader(Context c, String resId){

        Toast.makeText(c,resId,Toast.LENGTH_SHORT).show();
        Log.d("myTag", " <<<<<<<<<<--- hey man , loaded library : " + resId + " ------->>>>>>>>> ");
    try {
        Intent intent = new Intent(c, Class.forName("com.abhiandroid.foodorderingin.Activities.SplashScreen"));
        intent.putExtra("from", "skip");
        c.startActivity(intent);
    }catch(Exception e){
        Log.d("myTag", "error finding class : ", e);
    }
    }
}
