package application.model.entity.course;

import application.model.entity.Document;

import java.util.ArrayList;
import java.util.List;

public class Course extends Document {

    private int mistakesAllowed;
    private List<Question> questions;

    public Course(CourseInfo courseInfo, String uuid, long version) {
        questions = new ArrayList<Question>();
        int ticketNumber = 1;
        int questionNumber = 1;
        for (Ticket ticket: courseInfo.getTickets()) {
            for(Question question: ticket.getQuestions()) {
                question.setQid(ticketNumber + " " + questionNumber);
                questions.add(question);
                questionNumber++;
            }
            questionNumber = 1;
            ticketNumber++;
        }
        this.uuid = uuid;
        this.version = version;
        name = courseInfo.getName();
        mistakesAllowed = courseInfo.getMistakesAllowed();
    }

    public Course() {}

    public Course(long id, String uuid, int version, String name, int mistakesAllowed) {
        this.id = id;
        this.uuid = uuid;
        this.version = version;
        this.name = name;
        this.mistakesAllowed = mistakesAllowed;
    }

    public Course(Course course) {
        id = course.getId();
        uuid = course.getUuid();
        version = course.getVersion();
        name = course.getName();
        mistakesAllowed = course.getMistakesAllowed();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public boolean equals(Course course){
        return (id == course.getId())&&(uuid.equals(course.getUuid()))&&(version == course.getVersion())&&(name.equals(course.getName()))&&(mistakesAllowed == course.getMistakesAllowed());
    }
}
