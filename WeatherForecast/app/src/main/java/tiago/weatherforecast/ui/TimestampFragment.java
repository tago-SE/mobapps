package tiago.weatherforecast.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tiago.weatherforecast.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link TimestampFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimestampFragment extends Fragment {

    private TextView tvTimestamp;

    public TimestampFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment TimestampFragment.
     */
    public static TimestampFragment newInstance() {
        TimestampFragment fragment = new TimestampFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setTimeStamp(String date) {
        tvTimestamp.setText(date);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_timestamp, container, false);
        tvTimestamp = view.findViewById(R.id.timestamp);
        return view;
    }

}
