package download;

import java.io.Serializable;
import java.util.List;

public class DownloadedQuestion implements Serializable{

    private long courseId;
    private long ticketNumber;
    private long questionNumber;
    private String question;
    private List<DownloadedAnswer> answers;

    public DownloadedQuestion(int courseId, int ticketNumber, int questionNumber, String question) {
        this.courseId = courseId;
        this.ticketNumber = ticketNumber;
        this.questionNumber = questionNumber;
        this.question = question;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public long getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(long ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public long getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(long questionNumber) {
        this.questionNumber = questionNumber;
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
