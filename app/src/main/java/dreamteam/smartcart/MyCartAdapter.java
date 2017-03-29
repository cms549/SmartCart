package dreamteam.smartcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Custom adapter that allows caller to set the row view's description and title so when we use long item click,
 * the view returned will include the title and description of the corresponding task.
 */
public class MyCartAdapter extends ArrayAdapter<Item> {

        private static LayoutInflater inflater = null;
        public MyCartAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public MyCartAdapter(Context context, int resource, List<Item> items) {
            super(context, resource, items);
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if(v==null){
                v = inflater.inflate(R.layout.row_mycart, null);
            }

            Item t = getItem(position);

            if (t != null) {
                TextView rowi = (TextView) v.findViewById(R.id.rowi);
                TextView rowq = (TextView) v.findViewById(R.id.rowq);
                TextView rowp = (TextView) v.findViewById(R.id.rowp);

                if (rowi != null) {
                    rowi.setText(t.item);
                }

                if (rowq != null) {
                    rowq.setText(String.valueOf(t.quant));
                }
                if (rowp != null) {
                    rowp.setText("$"+t.price);
                }

            }

            return v;
        }

    }
