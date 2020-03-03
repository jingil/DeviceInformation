package support.fuchsia.deviceinformation.ui.cpu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import support.fuchsia.deviceinfo.CPUInfo;
import support.fuchsia.deviceinformation.R;

public class CPUFragment extends Fragment {
    private TextView text_cpu;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_cpu, container, false);
        init(root);
        setCPUDetails();
        return root;
    }


    private void init(View root) {

        text_cpu = root.findViewById(R.id.text_cpu);
    }

    private void setCPUDetails() {
        text_cpu.setText(CPUInfo.getCPUDetails());
    }
}
