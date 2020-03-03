package support.fuchsia.deviceinformation.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import support.fuchsia.deviceinformation.R;

public class HomeFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        init(root);
        setDeviceDetails();
        return root;
    }

    private void setDeviceDetails() {

    }

    private void init(View root) {


    }
}
