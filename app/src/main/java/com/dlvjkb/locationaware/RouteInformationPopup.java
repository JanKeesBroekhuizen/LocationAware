package com.dlvjkb.locationaware;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

public class RouteInformationPopup extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_routeinformation);

//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getRealMetrics(dm);
//
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;

        //getWindow().setLayout((int)(width * 7), (int)(height * 7));
//
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
    }
}
