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
    public String sessionID="a1b2c3d4";
    public String pID="2";
    public String lastValue;
    public final String defaultURL="https://fachtnaroe.net/tuuber-test";
    public final String fileLocation="settings.txt";
    public String status;

    public settingsOnline (){
        baseURL = defaultURL;
    }
    public settingsOnline (String URL){
         baseURL = URL;
    }

    public String get (String name) throws MalformedURLException{

        class getDataFromWebInBackground extends AsyncTask<URL, Integer, String> {

            public String dataline=new String("");
            @Override
            protected String doInBackground(URL... urls) {
                String value= new String("");
                lastValue=urls[0].toString();
                try {
                    URL url = urls[0];
                    InputStream inputStream = url.openStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    value = bufferedReader.readLine();
                    lastValue.concat(value);
                    while (value != null) {
                            value= bufferedReader.readLine();
                    }
                    return value;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return value;
            }

            protected void onProgressUpdate(Integer n) {
            }

            protected void onPostExecute(String data) {
                lastValue=data;
            }
        }

        URL target = new URL(baseURL.concat("?method=GET&name="+name));
        new getDataFromWebInBackground().execute(target);
        return lastValue;
    }

    public String set (String name, String value) {
        return "Not implemented";
    }

}
