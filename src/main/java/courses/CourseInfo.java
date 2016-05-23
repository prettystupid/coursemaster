package courses;


import java.util.List;

public class CourseInfo {

    private String name;
    private int mistakesAllowed;
    private List<Ticket> tickets;


    public String getName() {
        return name;
    }

    public int getMistakesAllowed() {
        return mistakesAllowed;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMistakesAllowed(int mistakesAllowed) {
        this.mistakesAllowed = mistakesAllowed;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
}
