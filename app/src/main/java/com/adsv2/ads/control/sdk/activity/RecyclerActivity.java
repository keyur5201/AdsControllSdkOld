package com.adsv2.ads.control.sdk.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.adsv2.ads.control.sdk.R;
import com.adsv2.ads.control.sdk.recycler.ItemModel;
import com.adsv2.ads.control.sdk.recycler.RecyclerAdapter;
import com.adssdkmanager.ads_manager.AdsManager;
import com.adssdkmanager.common.CommonAppMethodes;
import com.adssdkmanager.rv_native.AdspterSet;
import com.adssdkmanager.rv_native.RecycleviewAdManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RecyclerActivity extends AppCompatActivity {

    private final ArrayList<Object> mainList = new ArrayList<>();

    RecyclerView recyclerView;

    RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        recyclerView = findViewById(R.id.recyclerView);

        new CommonAppMethodes(this).StatusbarColor(R.color.primary);

        SharedPreferences sharedPreferences = getSharedPreferences("AdSize", MODE_PRIVATE);

        int adSize = sharedPreferences.getInt("adsSize", 0);

        addMenuItemsFromJson(adSize);

    }

    private void addMenuItemsFromJson(int adSize) {
        try {
            String jsonDataString = readJsonDataFromFile();
            JSONArray menuItemsJsonArray = new JSONArray(jsonDataString);

            for (int i = 0; i < menuItemsJsonArray.length(); ++i) {

                JSONObject menuItemObject = menuItemsJsonArray.getJSONObject(i);

                String imagePath = menuItemObject.getString("photo");

                ItemModel menuItem = new ItemModel(imagePath);
                mainList.add(menuItem);
            }

            new RecycleviewAdManager(this).ShowNativeAdInRecyclerView(adSize, mainList, false, new AdspterSet() {
                @Override
                public void onAddDataFinish(RecyclerView.LayoutManager manager) {
                    recyclerView.setLayoutManager(manager);
                    adapter = new RecyclerAdapter(RecyclerActivity.this, mainList);
                    recyclerView.setAdapter(adapter);
                }
            });

        } catch (IOException | JSONException exception) {
        }
    }

    private String readJsonDataFromFile() throws IOException {

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {
            String jsonDataString = null;
            inputStream = getResources().openRawResource(R.raw.menu_items_json);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));
            while ((jsonDataString = bufferedReader.readLine()) != null) {
                builder.append(jsonDataString);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return new String(builder);
    }

    @Override
    public void onBackPressed() {
        new AdsManager(this).onBackPress();
    }
}