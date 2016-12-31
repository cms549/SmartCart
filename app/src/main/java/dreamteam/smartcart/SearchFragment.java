package dreamteam.smartcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;

public class SearchFragment extends Fragment implements Serializable{

    /**
     * Item name for searching
     */
    transient EditText etitem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        etitem = (EditText) getView().findViewById(R.id.etitem);
    }


    /**
     * Jumps to item scan -> barcode reader
     * @param view
     */
    public void goToItemScan(View view) {
        Intent nextScreen = new Intent(getActivity(), ItemScanScreen.class);
        //start next screen
        startActivity(nextScreen);
    }

    /**
     * Jumps to item types -> lets you piok the type of item you want
     * @param view
     */
    public void goToItemTypes(View view) {
        Intent nextScreen = new Intent(getActivity(), ItemTypesScreen.class);
        //start next screen
        startActivity(nextScreen);
    }

    /**
     * Jumps to list of items -> uses searchable word
     * @param view
     */
    public void goToListItemsScreen(View view) {
        Intent nextScreen = new Intent(getActivity(), ItemListScreen.class);

        //Sending data to another Activity
        String selectedItem = ((TextView) etitem).getText().toString();

        //Error check item name

        nextScreen.putExtra("itemName", selectedItem);

        //start next screen
        startActivity(nextScreen);

    }

}
