package net.fachtnaroe.tuuber;

/* Research:

http://www.java2s.com/Tutorial/Java/0320__Network/ReadingAWebResourceOpeningaURLsstream.htm

*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class settingsOnline {

    public String baseURL = new String("w");
    public final String defaultURL="https://fachtnaroe.net?qnd?";

    public settingsOnline (){
        baseURL = defaultURL;
    }

    public settingsOnline (String URL){
         baseURL = URL;
    }

    public String get (String name) {
         String value = new String();
         try {
             URL url = new URL(baseURL + name);
             InputStream inputStream = url.openStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
             value = bufferedReader.readLine();
             while (value != null) {
//                 System.out.println(line);
                 value.concat(
                         bufferedReader.readLine()
                 );
             }
             bufferedReader.close();
         }
         catch (MalformedURLException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return value;
    }

    public String set (String name, String value) {
        String result = new String();
        try {
            URL url = new URL(baseURL + name + "=" +value);
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            result = bufferedReader.readLine();
            while (result != null) {
                result.concat(
                        bufferedReader.readLine()
                );
            }
            bufferedReader.close();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
