package courses;

public class Course {

    private long id;
    private String uuid;
    private int version;
    private String name;
    private int mistakesAllowed;

    public Course (long id, String uuid, int version, String name, int mistakesAllowed) {
        this.id = id;
        this.uuid = uuid;
        this.version = version;
        this.name = name;
        this.mistakesAllowed = mistakesAllowed;
    }

    public Course (Course course) {
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

    public boolean equals(Course course){
        return (id == course.getId())&&(uuid.equals(course.getUuid()))&&(version == course.getVersion())&&(name.equals(course.getName()))&&(mistakesAllowed == course.getMistakesAllowed());
    }
}
