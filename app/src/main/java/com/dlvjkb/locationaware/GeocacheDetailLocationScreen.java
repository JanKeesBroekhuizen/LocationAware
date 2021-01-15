package com.dlvjkb.locationaware;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.dlvjkb.locationaware.database.geocache.DB_Geocache;

public class GeocacheDetailLocationScreen extends Dialog {

    private final TextView tvFoundGeoPoint;
    private final ImageView ivFoundGeoPoint;
    private final Button btnFinish;
    private final DB_Geocache geocache;
    private final Context context;

    public GeocacheDetailLocationScreen(@NonNull Context context,DB_Geocache geocache) {
        super(context);
        setContentView(R.layout.dialog_geocachedetaillocation);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.context = context;
        tvFoundGeoPoint = findViewById(R.id.tvGeocacheDetailTitle);
        ivFoundGeoPoint = findViewById(R.id.ivGeocacheDetailImage);
        btnFinish = findViewById(R.id.btnGeocacheDetailOK);
        btnFinish.setOnClickListener(v -> GeocacheDetailLocationScreen.this.dismiss());
        this.geocache = geocache;
        Log.d(GeocacheDetailLocationScreen.class.getName(),"" + geocache.Name);
        Log.d(GeocacheDetailLocationScreen.class.getName(),"OWN" + this.geocache.Name);
        setGeocacheAttributes();
    }

    public void setGeocacheAttributes(){
        tvFoundGeoPoint.setText(geocache.Name);
        Glide.with(context).load(geocache.imageLink).placeholder(R.drawable.btn_moreinfo).into(ivFoundGeoPoint);
    }
}
