package org.nick.java;

import com.alfresco.client.AlfrescoClient;
import com.alfresco.client.api.common.representation.ResultPaging;
import com.alfresco.client.api.core.PeopleAPI;
import com.alfresco.client.api.core.model.body.PersonBodyCreate;
import com.alfresco.client.api.core.model.representation.PersonRepresentation;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static javafx.css.StyleOrigin.USER_AGENT;

public class Main {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String alfrescoEndpoint = "http://localhost:8080/alfresco";

    public static void main(String[] args){

        //authenticate
        AlfrescoClient alfrescoClient = getLoggedInClient("admin","admin");
        //create user
        PersonRepresentation person = createUser(alfrescoClient);
        //list users-people
        ResultPaging<PersonRepresentation> people = getUsers(alfrescoClient);
        System.out.println(people.getList().size() + " users exist");
        //find users
        ResultPaging<PersonRepresentation> users = findPeople(alfrescoClient,"user");
        System.out.println(users.getList().size()+" users found");
        //







    }

    private static AlfrescoClient getLoggedInClient(String username, String password){
        return new AlfrescoClient.Builder().connect(alfrescoEndpoint,username,password).build();
    }

    private static PersonRepresentation createUser(AlfrescoClient alfrescoClient){
        PersonRepresentation personRepresentation = null;
        PeopleAPI peopleAPI = alfrescoClient.getPeopleAPI();
        PersonBodyCreate bodyCreate = new PersonBodyCreate("nickath").firstName("nick").lastName("ath")
                .email("nickath@gmail.com").password("noneofyourbusiness").skypeId("nickath").jobTitle("Software Engineer");
        try {
            personRepresentation = peopleAPI.createPersonCall(bodyCreate).execute().body();
            System.out.println("Person successfully created");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return personRepresentation;
    }

    private static ResultPaging<PersonRepresentation> getUsers(AlfrescoClient client){
        PeopleAPI peopleAPI = client.getPeopleAPI();
        try {
            ResultPaging<PersonRepresentation> personList = peopleAPI.listPeopleCall().execute().body();
            return  personList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ResultPaging<PersonRepresentation> findPeople(AlfrescoClient client, String username){
        try {
            ResultPaging<PersonRepresentation> personList = client.getQueriesAPI().findPeopleCall(username).execute().body();
            return  personList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
