package application.model.entity;

public class Document implements IEntity {

    protected long id;
    protected String uuid;
    protected long version;
    protected String name;

    public Document(Document document) {
        id = document.getId();
        uuid = document.getUuid();
        version = document.getVersion();
        name = document.getName();
    }

    public Document() {}

    public String getUuid() {
        return uuid;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
