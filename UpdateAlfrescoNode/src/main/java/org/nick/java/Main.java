package org.nick.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.nick.java.model.NodeBodyCreate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class Main {
    public static final String USER_AGENT = "Mozilla/5.0";
    public static void main(String[] args){

        String nodeId = "791ff341-ffb5-4610-93a5-af0c5cb9aa4d";
        HashMap<String, String> properties = new HashMap();
        properties.put("cm:title","Folder title updated example");
        properties.put("cm:description" , "test description");

        try {
            String alfrescoTicket = login();
            NodeBodyCreate nodeBodyCreate =  new NodeBodyCreate("updated folder1", new String[]{"cm:titled"},properties);
            updateNode(alfrescoTicket,nodeId, nodeBodyCreate);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *
     * @param alfrescoTicket the alfresco ticket to login as administrator
     * @param nodeId the id of the node we perform the update
     * @param nodeBodyCreate the nodeBodyCreate object to update
     */
    private static void updateNode(String alfrescoTicket, String nodeId, NodeBodyCreate nodeBodyCreate){
        String  putUrl  = "http://192.168.1.236:8080/alfresco/api/-default-/public/alfresco/versions/1/nodes/"+nodeId+"/?alf_ticket="+alfrescoTicket;// put in your url
        System.out.println(putUrl);
        Gson gson  = new Gson();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPut put = new HttpPut(putUrl);
        StringEntity updatingString = null;
        try {
            updatingString = new StringEntity(gson.toJson(nodeBodyCreate));
            System.out.println(gson.toJson(nodeBodyCreate));
            System.out.println(updatingString);
            put.setEntity(updatingString);
            put.setHeader("Content-type", "application/json");
            HttpResponse  response = httpClient.execute(put);
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
