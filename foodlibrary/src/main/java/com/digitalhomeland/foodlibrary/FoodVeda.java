package com.digitalhomeland.foodlibrary;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

class FoodVeda {

    public static void restaurantLoader(Context c, String resId){

        Toast.makeText(c,resId,Toast.LENGTH_SHORT).show();
        Log.d("myTag", " <<<<<<<<<<--- hey man , loaded library : " + resId + " ------->>>>>>>>> ");

    }
}
