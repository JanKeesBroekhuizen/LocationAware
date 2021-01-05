package com.dlvjkb.locationaware;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.dlvjkb.locationaware.database.geocache.DB_Geocache;

public class GeocacheDetailLocationScreen extends Dialog {

    private final TextView tvFoundGeoPoint;
    private final ImageView ivFoundGeoPoint;
    private final DB_Geocache geocache;

    public GeocacheDetailLocationScreen(@NonNull Context context,DB_Geocache geocache) {
        super(context);
        setContentView(R.layout.dialog_geocachedetaillocation);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tvFoundGeoPoint = findViewById(R.id.tvGeocacheDetailTitle);
        ivFoundGeoPoint = findViewById(R.id.ivGeocacheDetailImage);
        this.geocache = geocache;
        setGeocacheAttributes();
    }

    private void setGeocacheAttributes(){
        tvFoundGeoPoint.setText(geocache.Name);
    }
}
