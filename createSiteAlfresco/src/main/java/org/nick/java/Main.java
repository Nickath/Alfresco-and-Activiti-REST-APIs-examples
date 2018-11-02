package org.nick.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.nick.java.models.SiteBodyCreate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Main {
    public static final String USER_AGENT = "Mozilla/5.0";

    //non-mandatory parameters
    boolean skipConfiguration = false;
    boolean skipAddToFavorites = false;
    String[] fields = new String[0];

    public static void main(String[] args){

        //create a siteBodyCreate (mandatory)
        SiteBodyCreate siteBodyCreate = new SiteBodyCreate("Test-Site","Test-Site","A test site","PUBLIC");
        try {
            String alfrescoTicket = login();
            //create a site
            createSite(alfrescoTicket, siteBodyCreate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private static void createSite(String alfrescoTicket, SiteBodyCreate siteBodyCreate){
        String  postUrl  = "http://192.168.1.236:8080/alfresco/api/-default-/public/alfresco/versions/1/sites?skipConfiguration=false&skipAddToFavorites=false"+"&alf_ticket="+alfrescoTicket;// put in your url
        Gson gson  = new Gson();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(postUrl);
        StringEntity postingString = null;
        try {
            postingString = new StringEntity(gson.toJson(siteBodyCreate));
            System.out.println(postingString);
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse  response = httpClient.execute(post);
            System.out.println(response.getStatusLine());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return the ticket we need to perform operations as administrators to alfresco
     * @throws Exception
     */
    private static String login() throws Exception {
        String url = "http://192.168.1.236:8080/alfresco/s/api/login?u=admin&pw=admin";
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        // add request header
        request.addHeader("models.User-Agent", USER_AGENT);
        HttpResponse response = client.execute(request);
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        String ticket = result.toString();
        return ticket.substring((ticket.indexOf("<ticket>"))+"<ticket>".length(), ticket.indexOf("</ticket>"));
    }

    /**
     *
     * @param jsonStr a json String
     * @return the jsonStr in JSON format
     */
    private static String prettyJsonPrinter(String jsonStr){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(jsonStr);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }
}
