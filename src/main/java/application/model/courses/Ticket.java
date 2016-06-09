package application.model.courses;

import java.util.ArrayList;
import java.util.List;

public class Ticket {

    private int number;
    private List<Question> questions = new ArrayList<Question>();

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
