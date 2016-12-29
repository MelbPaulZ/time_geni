package org.unimelb.itime.bean;

/**
 * Created by 37925 on 2016/12/9.
 */

public class ITimeUser extends BaseContact {
    private String sex="";
    private String country="";
    private String state="";
    private String location="";
    private String phone="";
    private String email="";
    private boolean blocked=false;
    private User user;

    public User getUser() {
        return user;
    }

    public ITimeUser(Contact contact) {
        super(contact);
        setSex(contact.getUserDetail().getGender());
        if(contact.getUserDetail().getLocation()!=null){
            setLocation(contact.getUserDetail().getLocation());
        }

        if(contact.getUserDetail().getPhone()!=null){
            setPhone(contact.getUserDetail().getPhone());
        }

        if(contact.getUserDetail().getEmail()!=null){
            setEmail(contact.getUserDetail().getEmail());
        }

        if(contact.getBlockLevel()>0){
            setBlocked(true);
        }else{
            setBlocked(false);
        }
        //setCountry(contact.getUserDetail());
    }

    public ITimeUser(User user){
        this.user = user;
        if(user.getLocation()!=null){
            setLocation(user.getLocation());
        }

        if(user.getPhone()!=null){
            setPhone(user.getPhone());
        }

        if(user.getEmail()!=null){
            setEmail(user.getEmail());
        }

        if(user.getPhoto()!=null){
            setPhoto(user.getPhoto());
        }

        if(user.getPersonalAlias()!=null){
            setName(user.getPersonalAlias());
        }

        if(user.getGender()!=null){
            setSex(user.getGender());
        }

        if(user.getUserUid()!=null){
            setContactUid(user.getUserUid());
        }

        if (user.getUserId()!=null){
            setContactId(user.getUserId());
        }
    }

    public ITimeUser(){}

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
