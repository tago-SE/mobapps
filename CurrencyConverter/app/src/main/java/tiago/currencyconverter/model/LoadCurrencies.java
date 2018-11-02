package tiago.currencyconverter.model;

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
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class LoadCurrencies  extends AsyncTask<Void, Void, CurrenciesPackage> {

    private static final String LOG_TAG = "XMLParseTask";


    /*

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


                    //Currency currency = new Currency(e.getAttribute("currency"))
                    //Log.w(LOG_TAG, e.getAttribute("currency") + "," +
                    //e.getAttribute("rate"));

                }
            }
        }  catch (IOException e) {
            Log.e(LOG_TAG, "IOException");
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            Log.e(LOG_TAG, "Parse");
            e.printStackTrace();
        } catch (SAXException e) {
            Log.e(LOG_TAG, "SAXE");
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
    */

    @Override
    protected CurrenciesPackage doInBackground(Void... voids) {
        Log.w(LOG_TAG, "doInBackground:started");
        URL url = null;
        InputStream stream = null;
        List<Currency> currencyList = new ArrayList<>();
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

                    currencyList.add(new Currency(
                            e.getAttribute("currency"),
                            Float.parseFloat( e.getAttribute("rate"))));


                    //Currency currency = new Currency(e.getAttribute("currency"))
                    //Log.w(LOG_TAG, e.getAttribute("currency") + "," +
                    //e.getAttribute("rate"));

                }
            }
        }  catch (IOException e) {
            Log.e(LOG_TAG, "IOException");
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            Log.e(LOG_TAG, "Parse");
            e.printStackTrace();
        } catch (SAXException e) {
            Log.e(LOG_TAG, "SAXE");
            e.printStackTrace();
        } finally {
            if (stream != null) try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new CurrenciesPackage("null", currencyList);
    }
}

