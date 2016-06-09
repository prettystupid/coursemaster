package application.controller;

import application.utils.dao.DAO;
import application.view.main.ShowPanel;

public abstract class Controller {

    protected ShowPanel view;
    protected DAO dao;

    public abstract void upload();

    public abstract void download();

    public abstract void change();

    public abstract void delete();

    public abstract void update();

    public void setView(ShowPanel view) {
        this.view = view;
    }
}
