package application.model.entity.organization;

import application.model.entity.IEntity;

public class Organization implements IEntity {

    private long id;
    private String name;
    private String key;

    public Organization(String name, String key) {
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
