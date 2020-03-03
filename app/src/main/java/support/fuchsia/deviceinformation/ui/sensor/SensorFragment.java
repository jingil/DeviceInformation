package support.fuchsia.deviceinformation.ui.sensor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import support.fuchsia.deviceinfo.SensorInfo;
import support.fuchsia.deviceinformation.R;

public class SensorFragment extends Fragment {

    private SensorInfo sensorInfo;
    private ListView sensorListView;
    private ArrayList<String> sensornames;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_sensor, container, false);
        init(root);
        setDeviceDetails();
        return root;
    }

    private void setDeviceDetails() {
        sensornames = sensorInfo.getSensorList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, sensornames);
        sensorListView.setAdapter(adapter);
    }

    private void init(View root) {
        sensorListView = (ListView) root.findViewById(R.id.sensorlist);
        //  sensorListView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        sensorInfo = new SensorInfo(getActivity());
    }


}
