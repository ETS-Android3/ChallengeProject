package fr.eurecom.bottomnavigationdemo;

import android.net.Uri;

import java.util.Comparator;

public class Item {

    private String ID;
    private String name;
    private double price;


    private String pictureURL;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public static Comparator<Item> ItemNameComparator
            = new Comparator<Item>() {

        public int compare(Item item1, Item item2) {

            String itemName1 = item1.getName().toUpperCase();
            String itemName2 = item2.getName().toUpperCase();

            //ascending order
            return itemName1.compareTo(itemName2);
        }

    };
}
