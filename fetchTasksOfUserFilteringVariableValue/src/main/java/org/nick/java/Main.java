package org.nick.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;


public class Main {

    public static final String USER_AGENT = "Mozilla/5.0";
    public static void main(String[] args){

        String varNameToFilter = "X";
        String basicUrl = "http://localhost:8080/alfresco/api/-default-/public/workflow/versions/1/tasks?";
        String varNameValueToFilter = "'22'";
        String userNameAssignedToTask = "'testuser1'";
        String parameters = varNameValueToFilter+" AND assignee="+userNameAssignedToTask;
        try {
            String alfrescoTicket = login();
            String urlParameters = "where=(variables/"+varNameToFilter+"=" + URLEncoder.encode(parameters, "UTF-8").replaceAll("\\+", "%20") + ")";
            String url = basicUrl + urlParameters +"&alf_ticket=" + alfrescoTicket;
            String response = fetchTasks(url);
            System.out.println(prettyJsonPrinter(response));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String fetchTasks(String targetUri){
        String strReponse = "";
        System.out.println("=============== GET REQUEST ==================");
        System.out.println(targetUri);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(targetUri);
        request.addHeader("models.User-Agent", USER_AGENT);
        try {
            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            strReponse = result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("================ JSON RESPONSE ===============");
        return strReponse;
    }


    // HTTP GET request
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

    private static String prettyJsonPrinter(String jsonStr){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(jsonStr);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }
}
