package org.nick.java.dao;

public class Parameters {

    private String category;
    private String key;
    private String name;
    private String deploymentId;
    private String version;

    public Parameters() {

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Parameters(String category, String key, String name, String deploymentId, String version) {
        this.category = category;
        this.key = key;
        this.name = name;
        this.deploymentId = deploymentId;
        this.version = version;
    }

    public int getParametersSize(){
        int size = 0;
        if(this.getCategory() != null){
            size++;
        }
        if(this.getDeploymentId() != null){
            size++;
        }
        if(this.getKey() != null){
            size++;
        }
        if(this.getName() != null){
            size++;
        }
        if(this.getVersion() != null){
            size++;
        }
        return size;
    }


}
