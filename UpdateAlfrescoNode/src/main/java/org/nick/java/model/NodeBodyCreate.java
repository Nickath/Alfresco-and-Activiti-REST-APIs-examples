package org.nick.java.model;

import javax.xml.soap.Node;
import java.util.HashMap;

public class NodeBodyCreate {

    private String name;
    private String nodeType;
    private String[] aspectNames;
    private HashMap<String,String> properties;

    public String[] getAspectNames() {
        return aspectNames;
    }

    public void setAspectNames(String[] aspectNames) {
        this.aspectNames = aspectNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public HashMap<String, String> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }

    public NodeBodyCreate(String name, String nodeType, String[] aspectNames) {
        this.name = name;
        this.nodeType = nodeType;
        this.aspectNames = aspectNames;
    }


    public NodeBodyCreate(String name, String nodeType, String[] aspectNames, HashMap<String, String> properties) {
        this.name = name;
        this.nodeType = nodeType;
        this.aspectNames = aspectNames;
        this.properties = properties;
    }

    public NodeBodyCreate(String name, String[] aspectNames, HashMap<String, String> properties) {
        this.name = name;
        this.aspectNames = aspectNames;
        this.properties = properties;
    }

    public NodeBodyCreate(){

    }
}