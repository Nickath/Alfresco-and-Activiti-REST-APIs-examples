package org.nick.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.nick.java.models.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Main {
    public static final String USER_AGENT = "Mozilla/5.0";
    public static void main(String[] args){

        String documentRepositoryIdOfSite = "8027dfc1-f854-45ee-9871-f8c3108f3031";

        try {
            String alfrescoTicket = login();

            //create the folders
            Node folder1 = createFolder("folder1");
            Node folder2 = createFolder("folder2");

            //store the folders in the document repository of the site
            createNode(alfrescoTicket, folder1, documentRepositoryIdOfSite);
            createNode(alfrescoTicket, folder2, documentRepositoryIdOfSite);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     *
     * @param folderName the name of the newly created folder
     * @return the Node (folder)
     */
    private static Node createFolder(String folderName){
        String nodeType = "cm:folder";
        return new Node(folderName,nodeType);
    }

    /**
     *
     * @param alfrescoTicket the alfresco ticket to login as administrator
     * @param node the node created which represents the folder
     * @param nodeId the nodeId where the new folders will be stored under of
     */
    private static void createNode(String alfrescoTicket, Node node, String nodeId){
        String  postUrl  = "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/nodes/"+nodeId+"/children?alf_ticket="+alfrescoTicket;// put in your url
        System.out.println(postUrl);
        Gson gson  = new Gson();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(postUrl);
        StringEntity postingString = null;
        try {
            postingString = new StringEntity(gson.toJson(node));
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
