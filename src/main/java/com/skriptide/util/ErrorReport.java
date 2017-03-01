package com.skriptide.util;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yannh on 28.02.2017.
 */
public class ErrorReport {

    public static void reportError(String error) {

        System.out.println("called");

        String os = System.getProperty("os.name");
        HashMap<String, String> v = new HashMap<>();
        v.put("os", os);
        v.put("data", error);

        try {
            System.out.println( postRequest("https://skriptide.com/api/report/", v));;

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static String postRequest(String url, Map<String, String> fields) throws Exception {


        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");

        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        con.addRequestProperty("user-agent", " Mozilla/5.0 (Windows NT x.y; rv:10.0) Gecko/20100101 Firefox/10.0");

        String urlParameters = "";

        for(String key : fields.keySet()) {
            String value = URLEncoder.encode(fields.get(key), "utf-8");

            urlParameters +="&" + URLEncoder.encode(key,"utf-8") + "=" + value;
        }

        if(urlParameters.length() != 0) {
            urlParameters = urlParameters.substring(1);



        }
        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        return response.toString();

    }
}
