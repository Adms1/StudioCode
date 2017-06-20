package com.waterworks.model;

import java.util.ArrayList;

public class ProductListModel {
    private String CategoryName;
    private ArrayList<ProductDetail> productDetails;

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public ArrayList<ProductDetail> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(ArrayList<ProductDetail> productDetails) {
        this.productDetails = productDetails;
    }

    public ProductListModel() {
    }

    public class ProductDetail {
        private String ProductStock, ImageURL, ProductID, ProductName, Price;

        public String getProductStock() {
            return ProductStock;
        }

        public void setProductStock(String productStock) {
            ProductStock = productStock;
        }

        public String getImageURL() {
            return ImageURL;
        }

        public void setImageURL(String imageURL) {
            ImageURL = imageURL;
        }

        public String getProductID() {
            return ProductID;
        }

        public void setProductID(String productID) {
            ProductID = productID;
        }

        public String getProductName() {
            return ProductName;
        }

        public void setProductName(String productName) {
            ProductName = productName;
        }

        public String getPrice() {
            return Price;
        }

        public void setPrice(String price) {
            Price = price;
        }

        public ProductDetail() {
        }
    }

}
