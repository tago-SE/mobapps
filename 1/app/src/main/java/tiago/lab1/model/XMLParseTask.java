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
        if (node == null)
            return;
        Log.w(LOG_TAG, "a: " + node.getAttributes());
        Log.w(LOG_TAG, "v: " + node.getNodeValue());
        Log.w(LOG_TAG, "n: " + node.getLocalName());
        Log.w(LOG_TAG, "N: " + node.getNodeName());
        //if (node.getAttributes() != null)
            //Log.w(LOG_TAG, "A: " + node.getAttributes().item(0));
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node curNode = nodeList.item(i);
            if (curNode.getLocalName() == "Cube") {
                Log.w(LOG_TAG, "CUBE!!!!");
            }
            forNodes(curNode);
        }
    }


    @Override
    protected String doInBackground(String... strings) {
        Log.w(LOG_TAG, "doInBackground:started");
        URL url = null;
        InputStream stream = null;
        try {
            //url = new URL("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
            url = new URL("http://maceo.sth.kth.se/Home/eurofxref");
            stream = url.openStream();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(stream);

            Element p = doc.getDocumentElement();
            NodeList nodeList = doc.getElementsByTagName("Cube");
            Node node = nodeList.item(1);
            Element e = (Element) node;
            Log.w(LOG_TAG, "time: " + e.getAttribute("time"));
            for (int i = 2; i < nodeList.getLength(); i++) {
                node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    e = (Element) node;

                    Currency currency = new Currency(
                            e.getAttribute("currency"),
                            Float.parseFloat( e.getAttribute("rate")));

                    Log.w(LOG_TAG, currency.toString());
                    Log.w(LOG_TAG, currency.getLabel());
                    Log.w(LOG_TAG, "" +currency.getRate());


                    //Currency currency = new Currency(e.getAttribute("currency"))
                    //Log.w(LOG_TAG, e.getAttribute("currency") + "," +
                    //e.getAttribute("rate"));

                }
            }
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Document doc = docBuilder.parse(stream);

        return null;
    }
}
