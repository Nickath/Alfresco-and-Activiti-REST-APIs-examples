package org.nick.java;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.*;

public class Main {
    public static final String USER_AGENT = "Mozilla/5.0";
    public static void main(String[] args){

        String nodeId = "23c0c3cb-aa81-486a-9270-9623829903a0";
        String filePath = "C:\\file.doc6.pdf";
        try {
            String alfrescoTicket = login();
            File file = new File(filePath);
            addContentToNode(alfrescoTicket, nodeId, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addContentToNode(String alfrescoTicket, String nodeId, File file){
        String  postUrl  = "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/nodes/"+nodeId+"/children?alf_ticket="+alfrescoTicket;// put in your url
        System.out.println(postUrl);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(postUrl);

        MultipartEntity entity = new MultipartEntity();
        entity.addPart("filedata", new FileBody(file));
        post.setEntity(entity);

        try {
            HttpResponse response = client.execute(post);
            String res = gerResponseToString(response);
            System.out.println(res);
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
