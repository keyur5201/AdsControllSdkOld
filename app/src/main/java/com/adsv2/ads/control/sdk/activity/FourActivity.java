package com.adsv2.ads.control.sdk.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.adsv2.ads.control.sdk.R;
import com.adssdkmanager.ads_manager.AdsManager;


public class FourActivity extends AppCompatActivity {

    TextView oneB;
    TextView twoB;
    TextView threeB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);

        oneB = findViewById(R.id.oneB);
        twoB = findViewById(R.id.twoB);
        threeB = findViewById(R.id.threeB);

        SharedPreferences sharedPreferences = getSharedPreferences("AdSize", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        oneB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneB.setBackgroundResource(R.drawable.selected_bg);
                twoB.setBackgroundResource(R.drawable.unselected_bg);
                threeB.setBackgroundResource(R.drawable.unselected_bg);
                myEdit.putInt("adsSize", 1);
                myEdit.apply();
            }
        });

        twoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneB.setBackgroundResource(R.drawable.unselected_bg);
                twoB.setBackgroundResource(R.drawable.selected_bg);
                threeB.setBackgroundResource(R.drawable.unselected_bg);
                myEdit.putInt("adsSize", 2);
                myEdit.apply();
            }
        });

        threeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneB.setBackgroundResource(R.drawable.unselected_bg);
                twoB.setBackgroundResource(R.drawable.unselected_bg);
                threeB.setBackgroundResource(R.drawable.selected_bg);
                myEdit.putInt("adsSize", 3);
                myEdit.apply();
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AdsManager(this).onBackPress();
    }

    public void recycler(View view) {
        new AdsManager(this).startActivity(RecyclerActivity.class);
    }

    public void gridlay(View view) {
        new AdsManager(this).startActivity(GridRecyclerActivity.class);
    }
}