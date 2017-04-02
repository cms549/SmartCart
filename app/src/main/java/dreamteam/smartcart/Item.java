package dreamteam.smartcart;

import java.io.Serializable;

/**
 * Data structure to hold one entry's information for my cart screen
 */
public class Item implements Serializable {
    String item;
    int quant;
    double price;
    String barcode;

    public Item(String name, int q, double p, String b){
        item = name;
        quant =q;
        price =p;
        barcode = b;
    }

    public boolean equals(Item i){
        if(item.toLowerCase().equals(i.item.toLowerCase())){
            return true;
        }
        return false;
    }

}
