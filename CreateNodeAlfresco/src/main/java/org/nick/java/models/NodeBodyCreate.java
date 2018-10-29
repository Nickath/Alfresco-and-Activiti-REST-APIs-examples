package org.nick.java.models;

public class NodeBodyCreate {

    private String name;
    private String nodeType;

    public NodeBodyCreate(String name, String nodeType) {
        this.name = name;
        this.nodeType = nodeType;
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
}
