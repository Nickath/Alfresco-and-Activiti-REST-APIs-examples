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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Main {
    public static final String USER_AGENT = "Mozilla/5.0";
    public static void main(String[] args){

        String processDefinitionId = "activitiParallelGroupReview:1:2720";
        try {
            String alfrescoTicket = login();
            String imageResponse = getProcessDefinitionImageById(alfrescoTicket, processDefinitionId);
            System.out.println("====================== RESPONSE =====================");
            System.out.println(prettyJsonPrinter(imageResponse));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param alfrescoTicket the alfresco ticket to login as administrator
     * @param processDefinitionId the ID of the process-definition we search for
     * @return The image of the process-definition
     */
    private static String getProcessDefinitionImageById(String alfrescoTicket, String processDefinitionId){
        String strReponse = "";
            //String processDefinitionIdUrlFormatted = URLEncoder.encode(processDefinitionId, "UTF-8").replaceAll("\\+", "%20") + ")";
            String url = "http://192.168.1.236:8080/alfresco/api/-default-/public/workflow/versions/1/process-definitions/"+processDefinitionId+"/image";
            url += "?alf_ticket="+alfrescoTicket;
            System.out.println("=============== GET REQUEST ==================");
            System.out.println(url);
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
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
        return strReponse;
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
