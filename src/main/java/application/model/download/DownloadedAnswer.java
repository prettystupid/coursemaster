package application.model.download;

import java.io.Serializable;

public class DownloadedAnswer implements Serializable{

    private String answer;
    private boolean correct;

    public DownloadedAnswer(String answer, int correct) {
        this.answer = answer;
        this.correct = (correct == 1);
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
