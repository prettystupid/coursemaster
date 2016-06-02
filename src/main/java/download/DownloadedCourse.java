package download;

import java.io.Serializable;
import java.util.List;

public class DownloadedCourse implements Serializable{

    private long id;
    private String uuid;
    private int version;
    private String name;
    private int mistakesAllowed;
    private List<DownloadedQuestion> questions;

    public DownloadedCourse (long id, String uuid, int version, String name, int mistakesAllowed) {
        this.id = id;
        this.uuid = uuid;
        this.version = version;
        this.name = name;
        this.mistakesAllowed = mistakesAllowed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMistakesAllowed() {
        return mistakesAllowed;
    }

    public void setMistakesAllowed(int mistakesAllowed) {
        this.mistakesAllowed = mistakesAllowed;
    }

    public List<DownloadedQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<DownloadedQuestion> questions) {
        this.questions = questions;
    }
}
