package agent;

import javax.xml.crypto.Data;

public class Agent {
    private String NameAgent;
    private String ContentAgent;
    private String id;
    private String date;
    private String predicted;
    private String result;

    public String getPredicted() {
        return predicted;
    }

    public void setPredicted(String predicted) {
        this.predicted = predicted;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Agent(String nameAgent, String contentAgent, String id, String date, String labelContent) {
        NameAgent = nameAgent;
        ContentAgent = contentAgent;
        this.id = id;
        this.date = date;
        LabelContent = labelContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNameAgent() {
        return NameAgent;
    }

    public void setNameAgent(String nameAgent) {
        NameAgent = nameAgent;
    }

    public String getContentAgent() {
        return ContentAgent;
    }

    public void setContentAgent(String contentAgent) {
        ContentAgent = contentAgent;
    }

    public String getLabelContent() {
        return LabelContent;
    }

    public void setLabelContent(String labelContent) {
        LabelContent = labelContent;
    }

    private String LabelContent;
}
