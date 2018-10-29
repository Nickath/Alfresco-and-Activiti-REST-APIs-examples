package org.nick.java.models;

public class Node {

    private String id;
    private String name;
    private String nodeType;
    private boolean isFolder;
    private boolean isFile;
    private boolean isLocked;
    private String modifiedAt;
    private User modifiedByUser;
    private String createdAt;
    private User createdByUser;
    private String parentId;
    private boolean isLink;
    private Content content;

    public NodeBodyCreate getNodeBodyCreate() {
        return nodeBodyCreate;
    }

    public void setNodeBodyCreate(NodeBodyCreate nodeBodyCreate) {
        this.nodeBodyCreate = nodeBodyCreate;
    }

    private NodeBodyCreate nodeBodyCreate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public User getModifiedByUser() {
        return modifiedByUser;
    }

    public void setModifiedByUser(User modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isLink() {
        return isLink;
    }

    public void setLink(boolean link) {
        isLink = link;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Node(String name, String nodeType){
        this.name = name;
        this.nodeType = nodeType;
    }

    public Node(NodeBodyCreate nodeBodyCreate){
        this.nodeBodyCreate = nodeBodyCreate;
    }
}
