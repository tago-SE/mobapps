package tiago.currencyconverter.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;

import tiago.currencyconverter.R;

public class CurrencyFragment extends Fragment {


    private static final String LOG_TAG = "CurrencyFragment";

    private RelativeLayout rootLayout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_currency, container, false);
        rootLayout = view.findViewById(R.id.cc_root);
        manageTextField(rootLayout);
        return view;
    }

    private void manageTextField(RelativeLayout layout) {
        EditText editText = layout.findViewById(R.id.cc_editText);
        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);         // Disable "On Next"
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Add currency sign here after text changes
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Convert changes
            }
        });
    }
}
