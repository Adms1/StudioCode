package com.waterworks.sheduling;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.waterworks.DashBoardActivity;
import com.waterworks.R;
import com.waterworks.asyncTasks.AddToCartAsyncTask;
import com.waterworks.asyncTasks.GetBasketIDAsyncTask;
import com.waterworks.asyncTasks.GetProductDetailsByIDAsyncTask;
import com.waterworks.asyncTasks.GetThermalProductDetailsAsyncTask;
import com.waterworks.asyncTasks.SitesListAsyncTask;
import com.waterworks.model.ThermalProductDetailModel;
import com.waterworks.utils.AppConfiguration;
import com.waterworks.utils.Utility;
import com.wscall.WebServicesCall;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class BuyMoreRetailStoreProductDetail extends Activity {
    Button BackButton, btn_viewCart, btnAddToCart;
    LinearLayout swimLsn, retailStore, otherPrograms, llColorSize;
    View selected_1, selected_2, selected_3;
    TextView txt_1, txt_2, txt_3, txtOurPrice, txtRegPrice, txtLblRegPrice, txtLblOurPrice, txtLblSizeChart;
    Spinner spinSize, spinQuantity;
    ImageView imgProduct;
    RadioGroup radioGroup;
    WebView wvDescription;
    ImageLoader imageLoader;
    Context mContext = this;
    String token, familyID, siteid, basketID, colorName, colorID, productID;
    ArrayList<ThermalProductDetailModel.ColorArray.Size> sizesPink = new ArrayList<>();
    ArrayList<ThermalProductDetailModel.ColorArray.Size> sizesRoyal = new ArrayList<>();
    ArrayList<String> productGoggleCapsDetails = new ArrayList<>();
    ArrayAdapter<String> adapterPink, adapterRoyal;
    ThermalProductDetailModel thermalProductDetailModel = null;
    GetThermalProductDetailsAsyncTask getThermalProductDetailsAsyncTask = null;
    AddToCartAsyncTask addToCartAsyncTask = null;
    GetProductDetailsByIDAsyncTask getProductDetailsByIDAsyncTask = null;
    Boolean isInternetPresent = false;
    ArrayList<String> listQuantitydisplay = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_more_retail_store_product_details);
        this.overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        // getting token
        SharedPreferences prefs = AppConfiguration.getSharedPrefs(mContext);
        token = prefs.getString("Token", "");
        familyID = prefs.getString("FamilyID", "");
        siteid = AppConfiguration.salStep1SiteID;
        productID = getIntent().getStringExtra("productID");
        if (AppConfiguration.BasketID.equalsIgnoreCase("0")) {
            try {
                basketID = new GetBasketIDAsyncTask(this, siteid).execute().get();
                AppConfiguration.BasketID = basketID;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            basketID = AppConfiguration.BasketID;
        }
        isInternetPresent = Utility.isNetworkConnected(BuyMoreRetailStoreProductDetail.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            init();
            setListners();
            typeFace();
        }
    }

    public void init() {
        setImageLoader();
        llColorSize = (LinearLayout) findViewById(R.id.llColorSize);

        View view = findViewById(R.id.schdl_top);
        BackButton = (Button) view.findViewById(R.id.returnStack);
        btn_viewCart = (Button) view.findViewById(R.id.btn_viewCart);

        swimLsn = (LinearLayout) findViewById(R.id.scdl_lsn);
        retailStore = (LinearLayout) findViewById(R.id.scdl_mkp);
        otherPrograms = (LinearLayout) findViewById(R.id.waitlist);
        txtLblSizeChart = (TextView) findViewById(R.id.txtLblSizeChart);

        txt_1 = (TextView) view.findViewById(R.id.txt_1);
        txt_2 = (TextView) view.findViewById(R.id.txt_2);
        txt_3 = (TextView) view.findViewById(R.id.txt_3);

        selected_1 = (View) view.findViewById(R.id.selected_1);
        selected_2 = (View) view.findViewById(R.id.selected_2);
        selected_3 = (View) view.findViewById(R.id.selected_3);

        selected_1.setVisibility(View.GONE);
        selected_2.setVisibility(View.VISIBLE);
        selected_3.setVisibility(View.GONE);
        Button btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCheckin = new Intent(BuyMoreRetailStoreProductDetail.this, DashBoardActivity.class);
                intentCheckin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentCheckin);
                finish();
            }
        });
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        imgProduct = (ImageView) findViewById(R.id.imgProduct);
        spinSize = (Spinner) findViewById(R.id.spinSize);
        spinQuantity = (Spinner) findViewById(R.id.spinQuantity);
        txtOurPrice = (TextView) findViewById(R.id.txtOurPrice);
        txtRegPrice = (TextView) findViewById(R.id.txtRegPrice);
        btnAddToCart = (Button) findViewById(R.id.btnAddToCart);
        wvDescription = (WebView) findViewById(R.id.wvDescription);
        txtLblRegPrice = (TextView) findViewById(R.id.txtLblRegPrice);
        txtLblOurPrice = (TextView) findViewById(R.id.txtLblOurPrice);

        ((AnimationDrawable) selected_2.getBackground()).start();

        setUI();
    }

    public void setUI() {
        if (AppConfiguration.buyMoreRetailProductTypeID == "1") {
            //thermal
            thermalUI();
        } else if (AppConfiguration.buyMoreRetailProductTypeID == "2") {
            //goggle
            goggleAndCapsUI();
        } else if (AppConfiguration.buyMoreRetailProductTypeID == "3") {
            //swimcaps
            goggleAndCapsUI();
        }

        //Commom UI
        if (AppConfiguration.buyMoreRetailProductTypeID == "1") {
            ArrayList<String> listQuantity = new ArrayList<>();
            for (int i = 1; i < 50; i++) {
                listQuantity.add(String.valueOf(i));
            }
            final ArrayAdapter<String> adapterQuantity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listQuantity);
            spinQuantity.setAdapter(adapterQuantity);
        } else if (AppConfiguration.buyMoreRetailProductTypeID == "2") {
            int loop=Integer.parseInt(AppConfiguration.listQuantityotherproduct);
            for (int i = 1; i <=loop; i++) {
                listQuantitydisplay.add(String.valueOf(i));
            }
            final ArrayAdapter<String> adapterQuantity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listQuantitydisplay);
            spinQuantity.setAdapter(adapterQuantity);
        }else if (AppConfiguration.buyMoreRetailProductTypeID == "3") {
            int loop=Integer.parseInt(AppConfiguration.listQuantityotherproduct);
            for (int i = 1; i <=loop; i++) {
                listQuantitydisplay.add(String.valueOf(i));
            }
            final ArrayAdapter<String> adapterQuantity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listQuantitydisplay);
            spinQuantity.setAdapter(adapterQuantity);
        }
    }

    public void goggleAndCapsUI() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("Token", token);
            hashMap.put("ProductID", productID);

            getProductDetailsByIDAsyncTask = new GetProductDetailsByIDAsyncTask(mContext, hashMap);
            String responseString = getProductDetailsByIDAsyncTask.execute().get();
            productGoggleCapsDetails = readAndParseGoggleCapsJSON(responseString);

            llColorSize.setVisibility(View.GONE);
            txtRegPrice.setVisibility(View.INVISIBLE);
            txtOurPrice.setText("$" + productGoggleCapsDetails.get(2));
            txtRegPrice.setVisibility(View.GONE);
            imageLoader.displayImage(productGoggleCapsDetails.get(0), imgProduct);
            txtLblRegPrice.setVisibility(View.GONE);
            txtLblOurPrice.setText("Price: ");
            wvDescription.getSettings().setJavaScriptEnabled(true);
            wvDescription.loadDataWithBaseURL("", productGoggleCapsDetails.get(3), "text/html", "UTF-8", "");

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public ArrayList<String> readAndParseGoggleCapsJSON(String in) {
        ArrayList<String> stringArrayList = new ArrayList<>();
        try {

            JSONObject reader = new JSONObject(in);
            stringArrayList.add(reader.getString("ImageUrl"));
            stringArrayList.add(reader.getString("ProductName"));
            stringArrayList.add(reader.getString("Price"));
            stringArrayList.add(reader.getString("Description"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringArrayList;
    }

    public void thermalUI() {
        try {
            getThermalProductDetailsAsyncTask = new GetThermalProductDetailsAsyncTask(mContext, token);
            String responseString = getThermalProductDetailsAsyncTask.execute().get();
            thermalProductDetailModel = readAndParseJSON(responseString);

        } catch (Exception e) {
            e.printStackTrace();
        }

        imageLoader.displayImage(thermalProductDetailModel.getImageUrl(), imgProduct);
        sizesPink = thermalProductDetailModel.getColorArray().get(0).getSize();
        sizesRoyal = thermalProductDetailModel.getColorArray().get(1).getSize();
        ArrayList<String> pinkSizeList = new ArrayList<>();
        for (int i = 0; i < sizesPink.size(); i++) {
            pinkSizeList.add(sizesPink.get(i).getSizeName());
        }
        ArrayList<String> royalSizeList = new ArrayList<>();
        for (int i = 0; i < sizesRoyal.size(); i++) {
            royalSizeList.add(sizesRoyal.get(i).getSizeName());
        }

        adapterPink = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, pinkSizeList);
        adapterRoyal = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, royalSizeList);

        txtRegPrice.setText("$" + thermalProductDetailModel.getOriginalPrice());
        txtOurPrice.setText("$" + thermalProductDetailModel.getPrice());

        wvDescription.getSettings().setJavaScriptEnabled(true);
        wvDescription.loadDataWithBaseURL("", thermalProductDetailModel.getDescription(), "text/html", "UTF-8", "");
    }

    public ThermalProductDetailModel readAndParseJSON(String in) {
        ThermalProductDetailModel thermalProductDetailModel = new ThermalProductDetailModel();
        try {

            JSONObject reader = new JSONObject(in);
            thermalProductDetailModel.setProductID(reader.getString("ProductID"));
            thermalProductDetailModel.setDepartmentID(reader.getString("DepartmentID"));
            thermalProductDetailModel.setImageUrl(reader.getString("ImageUrl"));
            thermalProductDetailModel.setProductName(reader.getString("ProductName"));
            thermalProductDetailModel.setPrice(reader.getString("Price"));
            thermalProductDetailModel.setOriginalPrice(reader.getString("OriginalPrice"));
            thermalProductDetailModel.setDescription(reader.getString("Description"));

            JSONArray jsonMainNode = reader.optJSONArray("Color");
            ThermalProductDetailModel.ColorArray colorArray = null;
            ArrayList<ThermalProductDetailModel.ColorArray> colorArrays = new ArrayList<>();

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                colorArray = thermalProductDetailModel.new ColorArray();

                colorArray.setColorImage(jsonChildNode.getString("ColorImage"));
                colorArray.setColorName(jsonChildNode.getString("ColorName"));
                colorArray.setColorID(jsonChildNode.getString("ColorID"));
                colorArrays.add(colorArray);

                JSONArray jsonMainNode1 = jsonChildNode.optJSONArray("Size");
                ThermalProductDetailModel.ColorArray.Size size = null;
                ArrayList<ThermalProductDetailModel.ColorArray.Size> sizes = new ArrayList<>();
                for (int j = 0; j < jsonMainNode1.length(); j++) {
                    size = colorArray.new Size();

                    JSONObject jsonChildNode1 = jsonMainNode1.getJSONObject(j);
                    size.setSizeID(jsonChildNode1.getString("SizeID"));
                    size.setSizeName(jsonChildNode1.getString("SizeName"));
                    sizes.add(size);
                }
                colorArray.setSize(sizes);
            }
            thermalProductDetailModel.setColorArray(colorArrays);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thermalProductDetailModel;
    }

    public void readCategoryParseJSON(String in) {
        try {

            JSONObject reader = new JSONObject(in);
            String successStr = reader.getString("Success");
            if (successStr.equalsIgnoreCase("True")) {
                Utility.ping(this, "Product added to cart successfully");
                Intent i = new Intent(getApplicationContext(), ByMoreMyCart.class);
                startActivity(i);
            } else {
                Utility.ping(this, "Some Problem in adding product to cart");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListners() {

        btnAddToCart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    HashMap<String, String> hashMap = new HashMap<String, String>();

                    if (AppConfiguration.buyMoreRetailProductTypeID == "1") {
                        //thermal
                        hashMap.put("Token", token);
                        hashMap.put("siteid", siteid);
                        hashMap.put("basketid", basketID);
                        hashMap.put("wu_Price", txtOurPrice.getText().toString().replace("$", ""));
                        hashMap.put("qty", spinQuantity.getSelectedItem().toString());
                        hashMap.put("ProductID", thermalProductDetailModel.getProductID());
                        hashMap.put("DepartmentID", thermalProductDetailModel.getDepartmentID());
                        hashMap.put("ColorName", colorName);
                        hashMap.put("ColorID", colorID);
                        hashMap.put("SizeName", spinSize.getSelectedItem().toString());
                        hashMap.put("SizeID", String.valueOf(spinSize.getSelectedItemPosition() + 1));
                        hashMap.put("typeid", AppConfiguration.buyMoreRetailProductTypeID);
                    } else if (AppConfiguration.buyMoreRetailProductTypeID == "2") {
                        //goggle set DepartmentID=0, ColorName="", ColorID=0, SizeName="", SizeID=0
                        hashMap.put("Token", token);
                        hashMap.put("siteid", siteid);
                        hashMap.put("basketid", basketID);
                        hashMap.put("wu_Price", txtOurPrice.getText().toString().replace("$", ""));
                        hashMap.put("qty", spinQuantity.getSelectedItem().toString());
                        hashMap.put("ProductID", productID);
                        hashMap.put("DepartmentID", "0");
                        hashMap.put("ColorName", "");
                        hashMap.put("ColorID", "0");
                        hashMap.put("SizeName", "");
                        hashMap.put("SizeID", "0");
                        hashMap.put("typeid", AppConfiguration.buyMoreRetailProductTypeID);
                    } else if (AppConfiguration.buyMoreRetailProductTypeID == "3") {
                        //swimcaps set DepartmentID=0, ColorName="", ColorID=0, SizeName="", SizeID=0
                        hashMap.put("Token", token);
                        hashMap.put("siteid", siteid);
                        hashMap.put("basketid", basketID);
                        hashMap.put("wu_Price", txtOurPrice.getText().toString().replace("$", ""));
                        hashMap.put("qty", spinQuantity.getSelectedItem().toString());
                        hashMap.put("ProductID", productID);
                        hashMap.put("DepartmentID", "0");
                        hashMap.put("ColorName", "");
                        hashMap.put("ColorID", "0");
                        hashMap.put("SizeName", "");
                        hashMap.put("SizeID", "0");
                        hashMap.put("typeid", AppConfiguration.buyMoreRetailProductTypeID);
                    }

                    addToCartAsyncTask = new AddToCartAsyncTask(BuyMoreRetailStoreProductDetail.this, hashMap);
                    String responseString = addToCartAsyncTask.execute().get();
                    readCategoryParseJSON(responseString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        BackButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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
                BuyMoreRetailStoreProductDetail.this.overridePendingTransition(0, 0);
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

                Intent i = new Intent(mContext, BuyMoreRetailStoreProductDetail.class);
                startActivity(i);
                BuyMoreRetailStoreProductDetail.this.overridePendingTransition(0, 0);
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
                BuyMoreRetailStoreProductDetail.this.overridePendingTransition(0, 0);
                finish();

                ((AnimationDrawable) selected_3.getBackground()).start();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (group.getCheckedRadioButtonId() == R.id.rbPink) {
                    imgProduct.setImageResource(R.drawable.thermal_pink);
                    spinSize.setAdapter(adapterPink);
                    colorName = thermalProductDetailModel.getColorArray().get(0).getColorName();
                    colorID = thermalProductDetailModel.getColorArray().get(0).getColorID();
                } else {
                    imgProduct.setImageResource(R.drawable.thermal_blue);
                    spinSize.setAdapter(adapterRoyal);
                    colorName = thermalProductDetailModel.getColorArray().get(1).getColorName();
                    colorID = thermalProductDetailModel.getColorArray().get(1).getColorID();
                }
            }
        });

        txtLblSizeChart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_size_chart);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            }
        });
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

    public void setImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .threadPriority(Thread.MAX_PRIORITY)
                .defaultDisplayImageOptions(defaultOptions)


                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)// .enableLogging()
                .build();
        imageLoader.init(config.createDefault(mContext));
    }

}