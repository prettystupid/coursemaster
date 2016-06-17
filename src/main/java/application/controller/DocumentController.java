package application.controller;

import application.model.entity.Organization;
import application.view.EntityChooserWindow;
import org.apache.commons.configuration.ConfigurationException;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.SQLException;

public abstract class DocumentController extends EntityController {

    public DocumentController(MainController mainController) {
        super(mainController);
    }

    public abstract void change(Long id) throws SQLException, ConfigurationException;

    public abstract void upload() throws SQLException, ConfigurationException;

    public abstract void download(Long id) throws SQLException, ConfigurationException;

    protected String getKeyByOrg() {
        JFrame frame = new JFrame();
        EntityChooserWindow tableWindow = new EntityChooserWindow(frame, new OrganizationController(mainController));
        Organization org = ((Organization) tableWindow.getEntity());
        tableWindow.dispose();
        return org.getKey();
    }



    protected byte[] objToBytes(Object object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            byte[] result = bos.toByteArray();
            if (out != null) {
                out.close();
            }
            bos.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
