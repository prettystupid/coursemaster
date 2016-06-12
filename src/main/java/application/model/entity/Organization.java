package application.model.entity;


public class Organization implements IEntity {

    private long id;
    private String name;
    private String key;

    public Organization(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public Organization(long id, String name, String key) {
        this.id = id;
        this.name = name;
        this.key = key;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
}
