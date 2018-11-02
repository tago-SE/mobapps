package tiago.currencyconverter.utility;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackbarUtil {
    public static void makeSimpleActionSnackbar(View view, int stringId) {
        Snackbar.make(view, stringId, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
