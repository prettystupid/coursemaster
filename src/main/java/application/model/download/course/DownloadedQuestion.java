package application.model.download.course;

import java.io.Serializable;
import java.util.List;

public class DownloadedQuestion implements Serializable{

    private QuestionIdentity id;
    private String question;
    private List<DownloadedAnswer> answers;

    public DownloadedQuestion(int courseId, int ticketNumber, int questionNumber, String question) {
        id = new QuestionIdentity(courseId, ticketNumber, questionNumber);
        this.question = question;
    }

    public QuestionIdentity getId() {
        return id;
    }

    public void setId(QuestionIdentity id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<DownloadedAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<DownloadedAnswer> answers) {
        this.answers = answers;
    }
}
