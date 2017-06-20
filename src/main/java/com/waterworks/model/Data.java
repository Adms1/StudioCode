package com.waterworks.model;

import java.util.ArrayList;

public class Data{
        private ArrayList<ArrayList<ArrayList<String>>> arrayLists;

        public Data(ArrayList<ArrayList<ArrayList<String>>> arrayLists) {
            this.arrayLists = arrayLists;
        }

        public ArrayList<ArrayList<ArrayList<String>>> getArrayLists() {
            return arrayLists;
        }
    }