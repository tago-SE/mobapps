package tiago.lab1.model;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLHandler {

    private static final String LOG_TAG = "URLHandler";

    public void read() throws IOException {
        BufferedReader in = null;
        try {
            URL url = new URL("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
            URLConnection urlConnection = url.openConnection();

            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;
            while((line = in.readLine()) != null) {
                Log.d(LOG_TAG, line);
            }

        } finally {
            if (in == null) {
                Log.e(LOG_TAG, "BufferReader is null");
            } else {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        }
    }
}
