package org.nick.java;

import com.google.gson.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.*;

public class Main {

    public static final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args){

        String userAssignee = "testuser1";
        String assigneeFilter = "where=(assignee='" + userAssignee+"')";
        String variableNameToFilter = "bpm_X";
        String filterValue = "2";
        try {
            String alfrescoTicket = login();
            //get task ids that are assigned to the userAssignee person
            List<String> assignedTasksIds = fetchTasksIDsAssignedToUser(assigneeFilter, alfrescoTicket);
            //filter the above taken tasks according to the value of out variable
            List<String> userTasksList = fetchUserTasksWithSpecificVariableX(assignedTasksIds,alfrescoTicket,variableNameToFilter, filterValue);

            for(String task : userTasksList){
                System.out.println(prettyJsonPrinter(task));
            }

            System.out.println("Number of tasks assigned to user " + userAssignee +" that have variable "+variableNameToFilter+"=="+filterValue +" is: "+userTasksList.size());


        } catch (Exception e) {
            e.printStackTrace();
        }



    }



    public static ArrayList<String> fetchUserTasksWithSpecificVariableX(List<String> ids, String alfrescoTicket, String variableNameToFilter, String filterValue){
        String getTaskUri = "http://localhost:8080/alfresco/api/-default-/public/workflow/versions/1/tasks/{0}/variables?alf_ticket=" +alfrescoTicket;
        ArrayList<String> userTasksList = new ArrayList<String>();
        for(String id : ids){
            id = id.replace("\"","");
            String targetUri = MessageFormat.format(getTaskUri, id);
            System.out.println(" ================= TASK ID: " +id +" =====================");
            String response = filterTask(targetUri,variableNameToFilter, filterValue);
            if(response != null){
                userTasksList.add(response);
            }
        }

        return userTasksList;

    }


    public static String filterTask(String targetUri, String variableNameToFilter, String filterValue){
        System.out.println("============= REQUESTED URI (GET) ================\n"+targetUri);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        String strReponse = "";
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
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(strReponse).getAsJsonObject();

        JsonObject list = (JsonObject) jsonObject.get("list");
        JsonArray entries = (JsonArray) list.get(("entries"));
        for(JsonElement entry : entries){
            JsonObject entryJsonObj = (JsonObject) entry;
            JsonObject jsonEntry = (JsonObject) entryJsonObj.get("entry");
            if(jsonEntry.get("name") != null){
                String name = jsonEntry.get("name").toString().replaceAll("\"","");
                if(name.equals(variableNameToFilter)){
                    String value = jsonEntry.get("value").toString().replace("\"","");
                   if(value.equals(filterValue)){
                       return strReponse;
                   }
                }
            }

        }

        return null;
    }


    public static ArrayList<String> fetchTasksIDsAssignedToUser(String assigneeFilter, String alfrescoTicket){
        ArrayList<String> listOfIds = new ArrayList<String>();
        String strReponse = "";
        String getUrl = "http://localhost:8080/alfresco/api/-default-/public/workflow/versions/1/tasks?"+ assigneeFilter +"&alf_ticket="+alfrescoTicket;
        System.out.println("=============== GET REQUEST ==================");
        System.out.println(getUrl);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(getUrl);
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
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(strReponse).getAsJsonObject();

        JsonObject data = (JsonObject) jsonObject.get("list");
        JsonArray data2 = (JsonArray) data.get(("entries"));
        for(JsonElement entry : data2){
            JsonObject jsonObject1 = (JsonObject) entry;
            JsonObject jsonEntry = (JsonObject) jsonObject1.get("entry");
            String id = jsonEntry.get("id").toString();
            listOfIds.add(id);
        }
        return listOfIds;
    }




    // HTTP GET request
    private static String login() throws Exception {
        String url = "http://localhost:8080/alfresco/s/api/login?u=admin&pw=admin";
        HttpClient client = HttpClientBuilder.create().build();
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
