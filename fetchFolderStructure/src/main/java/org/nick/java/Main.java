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

public class Main {

    public static final String USER_AGENT = "Mozilla/5.0";
    public static void main(String[] args){

        String folderId = "791ff341-ffb5-4610-93a5-af0c5cb9aa4d";
        try {
            String alfrescoTicket = login();
            String folderStructure = getFolderStrucure(alfrescoTicket, folderId);
            System.out.println("================== FOLDER STRUCTURE RESPONSE ===================");
            System.out.println(prettyJsonPrinter(folderStructure));
            String folderChildrenResponse = fetchChildrenOfFolder(alfrescoTicket,folderId);
            System.out.println("=================== FOLDER CHILDREN RESPONSE ===================");
            System.out.println(prettyJsonPrinter(folderChildrenResponse));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param alfrescoTicket the alfresco ticket to login as administrator
     * @param folderId the ID of the folder we want to get its structure
     * @return The folder structure
     */
    private static String getFolderStrucure(String alfrescoTicket, String folderId){
        String strReponse = "";
        String url = "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/nodes/"+folderId;
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
     * @param alfrescoTicket the alfresco ticket to login as administrator
     * @param folderId the id of the folder node
     * @return the children of the folder node as JSON response
     */
    private static String fetchChildrenOfFolder(String alfrescoTicket, String folderId){
        String strReponse = "";
        String url = "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/nodes/"+folderId+"/children?skipCount=0&maxItems=100";
        url += "&alf_ticket="+alfrescoTicket;
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
        String url = "http://localhost:8080/alfresco/s/api/login?u=admin&pw=admin";
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
