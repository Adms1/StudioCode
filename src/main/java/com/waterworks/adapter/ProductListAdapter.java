package com.waterworks.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.meg7.widget.CircleImageView;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.waterworks.R;
import com.waterworks.model.ProductListModel;
import com.waterworks.sheduling.BuyMoreRetailStoreProductDetail;
import com.waterworks.sheduling.BuyMoreRetailStoreProductList;
import com.waterworks.utils.Utility;

import java.util.ArrayList;

public class ProductListAdapter extends BaseAdapter {

    Context context;
    ImageLoader imageLoader;
    ArrayList<ProductListModel> productList = new ArrayList<>();
    private ArrayList<Boolean> isSection = new ArrayList<>();
    RelativeLayout rlListRowProductList;
    int sectionCount = 0;
    int rowCount = 0;

    public ProductListAdapter(Context context, ArrayList<ProductListModel> productList, ArrayList<Boolean> isSection) {
        super();
        this.context = context;
        this.productList = productList;
        this.isSection = isSection;

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPriority(Thread.MAX_PRIORITY)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)// .enableLogging()
                .build();
         imageLoader.init(config.createDefault(context));
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return isSection.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public class ViewHolder {
        TextView txtProductName, txtProductPrice;
        ImageView imgProductImage;
        RelativeLayout rlListRowProductList;

    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View rowView = convertView;
        try {
            if (rowView == null) {
                holder = new ViewHolder();
                rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_buy_more_product_list, null);
                holder.imgProductImage = (ImageView) rowView.findViewById(R.id.imgProductImage);
                holder.txtProductName = (TextView) rowView.findViewById(R.id.txtProductName);
                holder.txtProductPrice = (TextView) rowView.findViewById(R.id.txtProductPrice);
                holder.rlListRowProductList = (RelativeLayout) rowView.findViewById(R.id.rlListRowProductList);

                if(isSection.get(position)){
                    holder.txtProductName.setText(productList.get(sectionCount).getCategoryName());
                    holder.imgProductImage.setVisibility(View.GONE);
                    holder.txtProductPrice.setVisibility(View.GONE);
                    holder.rlListRowProductList.setBackgroundColor(context.getResources().getColor(R.color.design_change_d2));
                    holder.txtProductName.setTextColor(context.getResources().getColor(R.color.white));
                    sectionCount++;
                    rowCount = 0;
                }else{
                    imageLoader.displayImage(productList.get(sectionCount-1).getProductDetails().get(rowCount).getImageURL(), holder.imgProductImage);
                    holder.txtProductName.setText(productList.get(sectionCount - 1).getProductDetails().get(rowCount).getProductName());
                    holder.txtProductPrice.setText("$" + productList.get(sectionCount - 1).getProductDetails().get(rowCount).getPrice());
                    holder.imgProductImage.setTag(productList.get(sectionCount - 1).getProductDetails().get(rowCount).getProductID());
                    rowCount++;
                }

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            rowView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.imgProductImage.getVisibility() == View.VISIBLE){
                        Intent intentDetail = new Intent(context, BuyMoreRetailStoreProductDetail.class);
                        intentDetail.putExtra("productID", holder.imgProductImage.getTag().toString());
                        context.startActivity(intentDetail);
                    }
                }
            });

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            // TODO: handle exception
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowView;
    }


}
