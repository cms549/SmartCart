package dreamteam.smartcart;

/**
 * Created by AthiraHaridas on 3/30/17.
 */import java.io.Serializable;

/**
 * Data structure to hold one entry's information for ItemListScreen
 */
public class ItemType implements Serializable {
    String name;
    double price;
    String barcode;
    String type;

    public ItemType(String name, double price, String barcode, String type){
        this.name = name;
        this.price =price;
        this.barcode =barcode;
        this.type=type;
    }
    public ItemType(){

    }



}
