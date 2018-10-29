package org.nick.java;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.nick.java.models.Node;
import org.nick.java.models.NodeBodyCreate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Main {
    public static final String USER_AGENT = "Mozilla/5.0";
    public static void main(String[] args){

        String nodeName = "testFolder";
        String nodeType = "cm:folder";
        String nodeId   = "-shared-";
        try {
            String alfrescoTicket = login();
            //construct the Node
            Node node = new Node(nodeName, nodeType);
            createNode(alfrescoTicket, node, nodeId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void createNode(String alfrescoTicket, Node node, String nodeId){
        String  postUrl  = "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/nodes/"+nodeId+"/children?alf_ticket="+alfrescoTicket;// put in your url
        System.out.println(postUrl);
        Gson gson  = new Gson();
        HttpClient httpClient  = HttpClientBuilder.create().build();
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





    // HTTP GET request
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
}
