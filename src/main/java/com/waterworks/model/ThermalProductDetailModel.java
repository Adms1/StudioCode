package com.waterworks.model;

import java.util.ArrayList;
import java.util.HashMap;

public class ThermalProductDetailModel {
    private String ProductID, DepartmentID, ImageUrl, ProductName, Price, OriginalPrice, Description;
    private ArrayList<ColorArray> colorArray;

    public ThermalProductDetailModel() {
    }

    public ArrayList<ColorArray> getColorArray() {
        return colorArray;
    }

    public void setColorArray(ArrayList<ColorArray> colorArray) {
        this.colorArray = colorArray;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(String departmentID) {
        DepartmentID = departmentID;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
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

    public String getOriginalPrice() {
        return OriginalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        OriginalPrice = originalPrice;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public class ColorArray {
        private String ColorImage, ColorName, ColorID;
        private ArrayList<Size> size;

        public ArrayList<Size> getSize() {
            return size;
        }

        public void setSize(ArrayList<Size> size) {
            this.size = size;
        }

        public ColorArray() {
        }

        public String getColorImage() {
            return ColorImage;
        }

        public void setColorImage(String colorImage) {
            ColorImage = colorImage;
        }

        public String getColorName() {
            return ColorName;
        }

        public void setColorName(String colorName) {
            ColorName = colorName;
        }

        public String getColorID() {
            return ColorID;
        }

        public void setColorID(String colorID) {
            ColorID = colorID;
        }

        public class Size {
            String SizeName, SizeID;

            public String getSizeName() {
                return SizeName;
            }

            public void setSizeName(String sizeName) {
                SizeName = sizeName;
            }

            public String getSizeID() {
                return SizeID;
            }

            public void setSizeID(String sizeID) {
                SizeID = sizeID;
            }

            public Size() {
            }
        }
    }

}
