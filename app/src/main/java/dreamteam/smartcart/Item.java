package dreamteam.smartcart;


import java.io.Serializable;

/**
 * Data structure to hold one entry's information for my cart screen
 */
public class Item implements Serializable {
    String item;
    int quant;
    double price;

    public Item(String name, int q, double p){
        item = name;
        quant =q;
        price =p;
    }

}
