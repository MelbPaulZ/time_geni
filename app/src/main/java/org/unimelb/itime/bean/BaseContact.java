package org.unimelb.itime.bean;

import org.unimelb.itime.util.CharacterParser;
import org.unimelb.itime.vendor.listener.ITimeContactInterface;

import java.io.Serializable;

/**
 * Created by 37925 on 2016/12/5.
 */

public class BaseContact implements ITimeContactInterface, Serializable,Comparable<BaseContact>{
    private String name="";
    private String photo="";
    private String contactId ="";
    private String contactUid = "";
    private String sortLetters = "";
    private String pinyin = "";
    private String matchStr = "";

    private Contact contact;

    public BaseContact(){}

    public BaseContact(Contact contact){
        this.contact = contact;

        if(contact.getAliasName()==null||"".equals(contact.getAliasName())){
            setName(contact.getUserDetail().getPersonalAlias());
        }else{
            setName(contact.getAliasName());
        }

        if(contact.getAliasPhoto()==null||"".equals(contact.getAliasPhoto())){
            setPhoto(contact.getUserDetail().getPhoto());
        }else{
            setPhoto(contact.getAliasPhoto());
        }
        if(contact.getUserDetail()!=null) {
            setContactId(contact.getUserDetail().getUserId());
        }
        if(contact.getUserDetail()!=null) {
            setContactUid(contact.getContactUid());
        }
    }

    public String getContactUid() {
        return contactUid;
    }

    public void setContactUid(String contactUid) {
        this.contactUid = contactUid;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public void setName(String name) {
        this.name = name;
        CharacterParser characterParser = CharacterParser.getInstance();

        // 汉字转换成拼音
        pinyin = characterParser.getSelling(name);
        if(pinyin.length()>0) {
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                setSortLetters(sortString.toUpperCase());
            } else {
                setSortLetters("#");
            }
        }
    }

    public String getMatchStr() {
        return matchStr;
    }

    public void setMatchStr(String matchStr) {
        this.matchStr = matchStr;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getSortLetters() {
        return sortLetters;
    }
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    @Override
    public int compareTo(BaseContact baseContact) {
        if(this.getSortLetters().equals("#")&&!baseContact.getSortLetters().equals("#")){
            return 1;
        }

        if(!this.getSortLetters().equals("#")&&baseContact.getSortLetters().equals("#")){
            return -1;
        }

        return this.getPinyin().compareTo(baseContact.getPinyin());
    }
}
