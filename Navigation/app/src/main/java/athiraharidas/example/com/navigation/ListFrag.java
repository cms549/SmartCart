package athiraharidas.example.com.navigation;

/**
 * Created by AthiraHaridas on 2/6/17.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class ListFrag extends Fragment {
    public ListFrag() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_list, container, false);
    }
}
