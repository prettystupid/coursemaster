package application.view;

public enum EntityType {

    COURSE("Курс"),
    NTD("Нормативно-технический документ");

    EntityType(String description) {
        this.description = description;
    }

    private final String description;

    public String getDescription() {
        return description;
    }
}