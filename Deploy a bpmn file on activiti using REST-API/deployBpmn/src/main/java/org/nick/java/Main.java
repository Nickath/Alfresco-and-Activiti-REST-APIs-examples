package org.nick.java;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;

public class Main {

    private static final String REST_URI = "http://localhost:8080/activiti-rest/service";
    private static final String bpmFilePath = "path_to_bpmn//file.bpmn";

    public static void main(String[] args){

        try {
            createDeployment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void createDeployment() throws Exception {
        String deploymentURI = REST_URI + "/repository/deployments";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(deploymentURI);
        httpPost.addHeader("Authorization", "Basic a2VybWl0Omtlcm1pdA==");

        File file  = new File(bpmFilePath);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, file.getName());

        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);

        CloseableHttpResponse response = client.execute(httpPost);
        response.getStatusLine();
        System.out.println("response: " + response.toString());
        client.close();
    }
}
