package org.nick.java;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.nick.java.model.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class Main {

    public static final String USER_AGENT = "Mozilla/5.0";
    public static void main(String[] args){

        String taskToBeUpdatedId = "6261";
        String state = "completed";
        try {
            String alfrescoTicket = login();
            updateTask(alfrescoTicket, state, taskToBeUpdatedId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void updateTask(String alfrescoTicket, String state, String taskId){
        Task task = new Task(state);
        String  updateUrl  = "http://localhost:8080/alfresco/api/-default-/public/workflow/versions/1/tasks/"+taskId+"?select=state&alf_ticket="+alfrescoTicket;// put in your url
        Gson gson  = new Gson();
        HttpClient httpClient  = HttpClientBuilder.create().build();
        HttpPut put = new HttpPut(updateUrl);
        StringEntity updatingString = null;
        try {
            updatingString = new StringEntity(gson.toJson(task));
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
