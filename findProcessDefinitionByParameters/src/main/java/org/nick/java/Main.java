package org.nick.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.nick.java.dao.Parameters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class Main {
    public static final String USER_AGENT = "Mozilla/5.0";
    public static void main(String[] args){

        try {
            String alfrescoTicket = login();
            Parameters parameters = new Parameters();
            parameters.setCategory("http://alfresco.org");
            parameters.setKey("activitiAdhoc");
            String response = findProcessDefinitionByParameters(alfrescoTicket,parameters);
            System.out.println("===================== RESPONSE =============================");
            System.out.println(prettyJsonPrinter(response));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     *
     * @param alfrescoTicket the alfresco ticket to login as administrator
     * @param parameters the parameters DAO to use in the query
     * @return a json String with the response of the query
     */
    private static String findProcessDefinitionByParameters(String alfrescoTicket, Parameters parameters){
        int parametersPassed  = parameters.getParametersSize();
        String queryParameters="where=(";
        String params ="";
        String url = "http://localhost:8080/alfresco/api/-default-/public/workflow/versions/1/process-definitions?maxItems=1000";
        if(parameters.getParametersSize() < 1){
            url += "?alf_ticket="+alfrescoTicket;
        }
        else{
            int currentParameters = 0;
            if(parameters.getCategory() != null){
                params += "category='" +parameters.getCategory()+"'";
                if(currentParameters < parametersPassed-1){
                    params += " AND ";
                }
                currentParameters++;
            }
            if(parameters.getKey() != null){
                params += "key='" +parameters.getKey()+"'";
                if(currentParameters < parametersPassed-1){
                    params += " AND ";
                }
                currentParameters++;
            }
            if(parameters.getName() != null){
                params += "name='" +parameters.getName()+"'";
                if(currentParameters < parametersPassed-1){
                    params += " AND ";
                }
                currentParameters++;
            }
            if(parameters.getDeploymentId() != null){
                params += "deploymentId='" +parameters.getDeploymentId()+"'";
                if(currentParameters < parametersPassed-1){
                    params += " AND ";
                }
                currentParameters++;
            }
            if(parameters.getVersion() != null){
                params += "version='" +parameters.getVersion()+"'";
                if(currentParameters < parametersPassed-1){
                    params += " AND ";
                }
                currentParameters++;
            }
            try {
                queryParameters += URLEncoder.encode(params, "UTF-8").replaceAll("\\+", "%20") + ")";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            url += "&"+queryParameters+"&alf_ticket="+alfrescoTicket;
        }
            String strReponse = "";
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            request.addHeader("models.User-Agent", USER_AGENT);
            try {
                HttpResponse response = null;
                try {
                    response = client.execute(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
