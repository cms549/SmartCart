package dreamteam.smartcart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        Button scan = (Button) getView().findViewById(R.id.bscan);
        scan.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent nextScreen = new Intent(getActivity(), ItemScanScreen.class);
                        //start next screen
                        startActivity(nextScreen);
                    }
                }
        );
        Button type = (Button) getView().findViewById(R.id.btype);
        type.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent nextScreen = new Intent(getActivity(), ItemTypesScreen.class);
                        //start next screen
                        startActivity(nextScreen);
                    }
                }
        );
        Button search = (Button) getView().findViewById(R.id.bsearch);
        search.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent nextScreen = new Intent(getActivity(), ItemListScreen.class);

                        //Sending data to another Activity
                        String selectedItem = ((TextView) etitem).getText().toString();

                        //Error check item name

                        nextScreen.putExtra("itemName", selectedItem);

                        //start next screen
                        startActivity(nextScreen);
                    }
                }
        );
    }

}
