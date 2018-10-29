package org.nick.java;

import com.google.gson.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.nick.java.models.Item;
import org.nick.java.models.Task;

import java.io.*;

public class Main {

    public static final String USER_AGENT = "Mozilla/5.0";
    public static void main(String[] args){

        String nodeId = "23c0c3cb-aa81-486a-9270-9623829903a0";
        String taskId = "11368";
        String filePath = "C:\\Users\\file.pdf";
        String statusToUpdate = "completed";
        try {
            String alfrescoTicket = login();
            //upload file to a node
            File file = new File(filePath);
            String response = addFileToNode(alfrescoTicket, nodeId, file);
            System.out.println(prettyJsonPrinter(response));
            //get the id of the attachment created
            String attachmentId = getIdOfAttachmentCreated(response);
            System.out.println("The id of the newly created attachment is: "+ attachmentId);
            //update the task (add the item-attachment to id)
            addAttachmentToTask(alfrescoTicket, taskId, attachmentId);
            //complete this task (state=completed)
            completeTask(alfrescoTicket, taskId, statusToUpdate);

        } catch (Exception e) {
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
     * @param alfrescoTicket the alfresco ticket to login as administrator
     * @param nodeId the id of the node where the attachment will be stored
     * @param file the file that we will store to the node
     * @return the response of the request
     */
    private static String addFileToNode(String alfrescoTicket, String nodeId, File file){
        String res = null;
        String  postUrl  = "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/nodes/"+nodeId+"/children?alf_ticket="+alfrescoTicket;// put in your url
        System.out.println(postUrl);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(postUrl);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        FileBody fileBody = new FileBody(file);
        builder.addPart("filedata", fileBody);
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        try {
            HttpResponse response = httpClient.execute(post);
            System.out.println("RESPONSE STATUS =================> " +response.getStatusLine());
            res = gerResponseToString(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     *
     * @param responseStr the String formatted response of our try to upload a file on alfresco
     * @return the id of the newly created attachment
     */
    private static String getIdOfAttachmentCreated(String responseStr){
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(responseStr).getAsJsonObject();
        JsonObject entry = (JsonObject) jsonObject.get("entry");
        String id = entry.get("id").toString();
        return id.replaceAll("\"","");
    }

    /**
     *
     * @param taskId the id of the task we want to update
     * @param attachmentId the id of the attachment created to be added in the task
     * @param alfrescoTicket  the alfresco ticket to login as administrator
     */
    private static void addAttachmentToTask(String alfrescoTicket, String taskId, String attachmentId){
        String postUrl = "http://localhost:8080/alfresco/api/-default-/public/workflow/versions/1/tasks/" +taskId+"/items?alf_ticket="+alfrescoTicket;
        Item item = new Item(attachmentId);
        Gson gson  = new Gson();
        HttpClient httpClient  = HttpClientBuilder.create().build();
        System.out.println(postUrl);
        HttpPost post = new HttpPost(postUrl);
        StringEntity postingString = null;
        try {
            postingString = new StringEntity(gson.toJson(item));
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
     * @param taskId taskId the id of the task we want update its state
     * @param state the new state we will set to the task
     * @param alfrescoTicket  the alfresco ticket to login as administrator
     */
    private static void completeTask(String alfrescoTicket, String taskId, String state){
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


    /**
     *
     * @param jsonStr the json string
     * @return the jsonStr in JSON-format
     */
    private static String prettyJsonPrinter(String jsonStr){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(jsonStr);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }














































    private static String gerResponseToString(HttpResponse httpResponse){
        BufferedReader rd = null;
        String res = null;
        try {
            rd = new BufferedReader(
                    new InputStreamReader(httpResponse.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            res = result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
