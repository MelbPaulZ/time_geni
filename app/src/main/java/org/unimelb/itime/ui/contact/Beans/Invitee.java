package org.unimelb.itime.ui.contact.Beans;

/**
 * Created by 37925 on test/12/2.
 */

public class Invitee implements ITimeContactInterface {
    private String name="";
    private String photo="";
    private String contactUid="";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPhoto() {
        return photo;
    }

    @Override
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getContactId() {
        return contactUid;
    }

    public void setContactId(String contactId) {
        this.contactUid = contactId;
    }
}
