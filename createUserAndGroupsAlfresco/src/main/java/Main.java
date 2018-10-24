import com.google.gson.Gson;
import models.Group;
import models.User;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;

public class Main {

    public static final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args){
        //login to get the alfresco ticket
        String alfrescoTicket = null;
        try {
            alfrescoTicket = login();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(alfrescoTicket);
        //create user
        createUser(alfrescoTicket);
        //create group
        createGroup(alfrescoTicket);






    }

    private static void createUser(String alfrescoTicket){
        User user = new User("nick2","nick2","nick2","nick2","nick2","nick2");
        String  postUrl  = "http://localhost:8080/alfresco/s/api/people?alf_ticket="+alfrescoTicket;// put in your url
        Gson gson  = new Gson();
        HttpClient  httpClient  = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(postUrl);
        StringEntity postingString = null;
        try {
            postingString = new StringEntity(gson.toJson(user));
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


    private static void createGroup(String alfrescoTicket){
        Group group = new Group("groupId1","groupDisplayName1");
        String       postUrl       = "http://localhost:8080/alfresco/api/-default-/public/alfresco/versions/1/groups?alf_ticket="+alfrescoTicket;// put in your url
        Gson gson          = new Gson();
        HttpClient   httpClient    = HttpClientBuilder.create().build();
        HttpPost     post          = new HttpPost(postUrl);
        StringEntity postingString = null;
        try {
            postingString = new StringEntity(gson.toJson(group));
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


}
