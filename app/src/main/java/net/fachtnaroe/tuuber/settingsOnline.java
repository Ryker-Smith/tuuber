package net.fachtnaroe.tuuber;

/* Research:

http://www.java2s.com/Tutorial/Java/0320__Network/ReadingAWebResourceOpeningaURLsstream.htm

*/

import android.os.AsyncTask;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class settingsOnline {


    public String baseURL = "";
    public String lastValue;
    public final String defaultURL="https://fachtnaroe.net?qnd?";

    public settingsOnline (){
        baseURL = defaultURL;
    }

    public settingsOnline (String URL){
         baseURL = URL;
    }

    private class getDataFromWebInBackground extends AsyncTask<URL, Integer, String> {
        @Override
        protected String doInBackground(URL[] urls) {
            String value="";
            try {
                URL url = new URL(baseURL);
                InputStream inputStream = url.openStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                value = bufferedReader.readLine();
                while (value != null) {
                    System.out.println(value);
                    value.concat(
                            bufferedReader.readLine()
                    );
                }
                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return value;
        }

            protected void onProgressUpdate(Integer n) {
            }

            protected void onPostExecute(String lastValue) {
            }
        }

    public String get (String name) throws MalformedURLException{
        URL target = new URL(baseURL.concat(name));
        new getDataFromWebInBackground().execute(target);
        return lastValue;
    }

    public String set (String name, String value) {
        return "Not implemented";
    }

}
