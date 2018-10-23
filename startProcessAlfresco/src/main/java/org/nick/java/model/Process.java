package org.nick.java.model;

public class Process {

    private String processDefinitionKey;
    private Variables variables;

    public Process(String processDefinitionKey, Variables variables) {
        this.processDefinitionKey = processDefinitionKey;
        this.variables = variables;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public Variables getVariables() {
        return variables;
    }

    public void setVariables(Variables variables) {
        this.variables = variables;
    }
}
