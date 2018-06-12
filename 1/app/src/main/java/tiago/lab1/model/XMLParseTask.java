package tiago.lab1.model;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XMLParseTask extends AsyncTask<String, Integer, String> {

    private static final String LOG_TAG = "XMLParseTask";

    private void forNodes(Node node) {
        Log.w(LOG_TAG, "v: " + node.getTextContent());
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node curNode = nodeList.item(i);
            if (curNode.getNodeType() == Node.ELEMENT_NODE) {
                forNodes(curNode);
            } else {
                Log.w(LOG_TAG, "e: " + curNode.getTextContent());
            }
        }
    }


    @Override
    protected String doInBackground(String... strings) {
        Log.w(LOG_TAG, "doInBackground:started");
        URL url = null;
        try {
            url = new URL("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
            InputStream stream = url.openStream();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(stream);

            Element p = doc.getDocumentElement();
            forNodes(p);

            stream.close();
            Log.w(LOG_TAG, "closed");
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        //Document doc = docBuilder.parse(stream);

        return null;
    }

    /*
    @Override
    protected String doInBackground(String... strings) {
        BufferedReader in = null;
        URL url = null;

        try {
            url = new URL("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URLConnection urlConnection = null;

        try {
            urlConnection = url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                Log.d(LOG_TAG, line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    */
}
