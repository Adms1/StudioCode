package com.waterworks.sheduling;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.adapter.ProductListAdapter;
import com.waterworks.asyncTasks.AddToCartAsyncTask;
import com.waterworks.asyncTasks.GetBasketIDAsyncTask;
import com.waterworks.asyncTasks.GetProductListAsyncTask;
import com.waterworks.asyncTasks.GetThermalProductDetailsAsyncTask;
import com.waterworks.model.ProductListModel;
import com.waterworks.model.ThermalProductDetailModel;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuyMoreRetailStoreProductList extends Activity {
    Button BackButton, btn_viewCart;
    LinearLayout swimLsn, retailStore, otherPrograms;
    View selected_1, selected_2, selected_3;
    TextView txt_1, txt_2, txt_3;
    ListView listviewProduct;
    Context mContext = this;
    String token, familyID, siteid, basketID, type;
    private GetProductListAsyncTask getProductListAsyncTask = null;
    private ArrayList<ProductListModel> productListModels = new ArrayList<>();
    private ProductListAdapter productListAdapter = null;
    private ArrayList<Boolean> isSection = new ArrayList<>();
    Boolean isInternetPresent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_more_product_listing);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        // getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        siteid = AppConfiguration.salStep1SiteID;
        isInternetPresent = Utility.isNetworkConnected(BuyMoreRetailStoreProductList.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            if (AppConfiguration.BasketID.equalsIgnoreCase("0")) {
                try {
                    basketID = new GetBasketIDAsyncTask(this, siteid).execute().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                basketID = AppConfiguration.BasketID;
            }

            type = AppConfiguration.buyMoreRetailProductTypeID;

            init();
            setListners();
            typeFace();
        }
    }

    public void init() {

        listviewProduct = (ListView) findViewById(R.id.listviewProduct);

        View view = findViewById(R.id.schdl_top);
        BackButton = (Button) view.findViewById(R.id.returnStack);
        btn_viewCart = (Button) view.findViewById(R.id.btn_viewCart);
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(BuyMoreRetailStoreProductList.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        swimLsn = (LinearLayout) findViewById(R.id.scdl_lsn);
        retailStore = (LinearLayout) findViewById(R.id.scdl_mkp);
        otherPrograms = (LinearLayout) findViewById(R.id.waitlist);

        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);

        selected_1 = (View) view.findViewById(R.id.selected_1);
        selected_2 = (View) view.findViewById(R.id.selected_2);
        selected_3 = (View) view.findViewById(R.id.selected_3);

        selected_1.setVisibility(View.GONE);
        selected_2.setVisibility(View.VISIBLE);
        selected_3.setVisibility(View.GONE);

        ((AnimationDrawable) selected_2.getBackground()).start();
        setUI();
    }

    public void setUI() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Token", token);
            hashMap.put("type", type);
            hashMap.put("siteid", siteid);
            hashMap.put("basketID", basketID);

            getProductListAsyncTask = new GetProductListAsyncTask(this, hashMap);
            String responseString = getProductListAsyncTask.execute().get();
            readAndParseJSON(responseString);
            productListAdapter = new ProductListAdapter(this, productListModels, isSection);
            listviewProduct.setAdapter(productListAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readAndParseJSON(String in) {
        try {
            ProductListModel productListModel;
            JSONObject reader = new JSONObject(in);
            JSONArray jsonMainNode = reader.optJSONArray("Categories");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                productListModel = new ProductListModel();
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                productListModel.setCategoryName(jsonChildNode.getString("CategoryName"));
                isSection.add(true);

                JSONArray jsonMainNode1 = jsonChildNode.optJSONArray("Products");
                ArrayList<ProductListModel.ProductDetail> list = new ArrayList<>();
                ;
                for (int j = 0; j < jsonMainNode1.length(); j++) {
                    ProductListModel.ProductDetail productDetail = productListModel.new ProductDetail();

                    JSONObject jsonChildNode1 = jsonMainNode1.getJSONObject(j);

                    productDetail.setProductStock(jsonChildNode1.getString("ProductStock"));
                    productDetail.setImageURL(jsonChildNode1.getString("ImageURL"));
                    productDetail.setProductID(jsonChildNode1.getString("ProductID"));
                    productDetail.setProductName(jsonChildNode1.getString("ProductName"));
                    productDetail.setPrice(jsonChildNode1.getString("Price"));
                    list.add(productDetail);
                    isSection.add(false);
                    AppConfiguration.listQuantityotherproduct=jsonChildNode1.getString("ProductStock");

                }
                productListModel.setProductDetails(list);
                productListModels.add(productListModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListners() {

        BackButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /*Intent i = new Intent(getApplicationContext(), BuyMoreRetailStore.class);
                startActivity(i);*/
                finish();
            }
        });

        btn_viewCart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ByMoreMyCart.class);
                startActivity(i);
            }
        });

        swimLsn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                swimLsn.setBackgroundResource(R.color.design_change_d2);
                retailStore.setBackgroundResource(R.color.design_change_d2);
                otherPrograms.setBackgroundResource(R.color.design_change_d2);
                selected_1.setVisibility(View.VISIBLE);
                selected_2.setVisibility(View.INVISIBLE);
                selected_3.setVisibility(View.INVISIBLE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, BuyMoreSwimLession.class);
                startActivity(i);
                BuyMoreRetailStoreProductList.this.overridePendingTransition(0, 0);
                finish();
                ((AnimationDrawable) selected_1.getBackground()).start();
            }
        });

        retailStore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                swimLsn.setBackgroundResource(R.color.design_change_d2);
                retailStore.setBackgroundResource(R.color.design_change_d2);
                otherPrograms.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.INVISIBLE);
                selected_2.setVisibility(View.VISIBLE);
                selected_3.setVisibility(View.INVISIBLE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, BuyMoreRetailStoreProductList.class);
                startActivity(i);
                BuyMoreRetailStoreProductList.this.overridePendingTransition(0, 0);
                finish();
                ((AnimationDrawable) selected_2.getBackground()).start();
            }
        });

        otherPrograms.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                swimLsn.setBackgroundResource(R.color.design_change_d2);
                retailStore.setBackgroundResource(R.color.design_change_d2);
                otherPrograms.setBackgroundResource(R.color.design_change_d2);

                selected_1.setVisibility(View.INVISIBLE);
                selected_2.setVisibility(View.INVISIBLE);
                selected_3.setVisibility(View.VISIBLE);

                txt_1.setTextColor(Color.WHITE);
                txt_2.setTextColor(Color.WHITE);
                txt_3.setTextColor(Color.WHITE);

                Intent i = new Intent(mContext, ByMoreOtherPrograms.class);
                startActivity(i);
                BuyMoreRetailStoreProductList.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setIcon(R.drawable.logo);
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
        return builder1.create();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    public void typeFace() {
        Typeface regular = Typeface.createFromAsset(mContext.getAssets(),
                "RobotoRegular.ttf");
        BackButton.setTypeface(regular);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), BuyMoreRetailStore.class);
        startActivity(i);
        finish();
    }

}