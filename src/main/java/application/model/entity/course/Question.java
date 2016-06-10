package application.model.entity.course;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private String question;
    private List<Answer> answers = new ArrayList<Answer>();
    private String qid;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }
}
