package download;

import java.io.Serializable;

public class DownloadedAnswer implements Serializable{

    private long courseId;
    private long ticketNumber;
    private long questionNumber;
    private String answer;
    private boolean correct;

    public DownloadedAnswer(int courseId, int ticketNumber, int questionNumber, String answer, int correct) {
        this.courseId = courseId;
        this.ticketNumber = ticketNumber;
        this.questionNumber = questionNumber;
        this.answer = answer;
        this.correct = (correct == 1);
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
