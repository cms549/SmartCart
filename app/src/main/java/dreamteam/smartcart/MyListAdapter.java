package dreamteam.smartcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by AthiraHaridas on 3/30/17.
 */

public class MyListAdapter extends ArrayAdapter<ItemType> {

    private static LayoutInflater inflater = null;
    public MyListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
        public MyListAdapter(Context context, int resource, List<ItemType> items){
            super(context,resource,items);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if(v==null){
            v = inflater.inflate(R.layout.row_itemlist, null);
        }

        ItemType t = getItem(position);


        if (t != null) {
            TextView rowName = (TextView) v.findViewById(R.id.rowName);
            TextView rowPrice = (TextView) v.findViewById(R.id.rowPrice);
            TextView rowType = (TextView) v.findViewById(R.id.rowType);

            if (rowName != null) {
                rowName.setText(t.name);
            }

            if ( rowType!= null) {
                rowType.setText(t.type);
            }
            if (rowPrice != null) {
                rowPrice.setText("$"+t.price);
            }

        }

        return v;
    }

}

