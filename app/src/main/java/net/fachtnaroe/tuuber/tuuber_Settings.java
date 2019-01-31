package net.fachtnaroe.tuuber;

/* Research:

http://www.java2s.com/Tutorial/Java/0320__Network/ReadingAWebResourceOpeningaURLsstream.htm

*/

import android.os.AsyncTask;

import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.TinyDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class tuuber_Settings {

    private String myTag="fr_";
    public String baseURL = "";
    public String sessionID="";
    public String pID="-1";
    public String lastValue;
    public final String default_baseURL="https://fachtnaroe.net/tuuber";
    public final String default_sessionID="a1b2c3d4";
    public String default_backgroundImageName="img_splashcanvas.png";
    public String backgroundImageName="img_splashcanvas.png";
    public String lastLogin="";
    public String textColor;
    public

    TinyDB localDB;

    public tuuber_Settings(ComponentContainer screenName){
        baseURL=default_baseURL;
        sessionID=default_sessionID;
        backgroundImageName=default_backgroundImageName;
        localDB= new TinyDB(screenName);
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
        localDB.StoreValue(myTag+"lastLogin", lastLogin);
        localDB.StoreValue(myTag+"baseURL",baseURL);
        localDB.StoreValue(myTag+"backgroundImageName", backgroundImageName);
        return "OK";
    }

}
