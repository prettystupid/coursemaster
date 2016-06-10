package application.model.download.course;

import java.io.Serializable;

public class QuestionIdentity implements Serializable {

    private long courseId;

    private long ticketNumber;

    private long questionNumber;

    public QuestionIdentity(int courseId, int ticketNumber, int questionNumber) {
        this.courseId = courseId;
        this.ticketNumber = ticketNumber;
        this.questionNumber = questionNumber;
    }

    public long getCourseId() {
        return courseId;
    }

    public long getTicketNumber() {
        return ticketNumber;
    }

    public long getQuestionNumber() {
        return questionNumber;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public void setTicketNumber(long ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public void setQuestionNumber(long questionNumber) {
        this.questionNumber = questionNumber;
    }
}