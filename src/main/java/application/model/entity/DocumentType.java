package application.model.entity;

public enum DocumentType {

    COURSE("Курс"),
    NTD("Нормативно-технический документ");

    DocumentType(String description) {
        this.description = description;
    }

    private final String description;

    public String getDescription() {
        return description;
    }
}