package org.nick.java;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static final String USER_AGENT = "Mozilla/5.0";
    public static void main(String[] args){

        //will add a simple .pdf file inside the created folder "folder1" which lies under the document library of our site
        String folderId = "791ff341-ffb5-4610-93a5-af0c5cb9aa4d";
        String filePath = "file.pdf";
        // returns the Class object associated with this class
        Class cls = null;
        try {
            cls = Class.forName("org.nick.java.Main");
            // returns the ClassLoader object associated with this Class.
            ClassLoader cLoader = cls.getClassLoader();
            File file = new File(filePath);
            try {
                FileUtils.copyInputStreamToFile(cLoader.getResourceAsStream(filePath), file);
                String alfrescoTicket = login();
                addFileToFolder(alfrescoTicket, folderId, file);
            }
            catch(IOException e){

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }



    private static void addFileToFolder(String alfrescoTicket, String folderId, File file){
        String  postUrl  = "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/nodes/"+folderId+"/children?alf_ticket="+alfrescoTicket;// put in your url
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
            String res = gerResponseToString(response);
            System.out.println(res);
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

    /**
     *
     * @param httpResponse the response we get from the post request
     * @return the response as a String
     */
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
