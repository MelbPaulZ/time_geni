package org.unimelb.itime.ui.contact.Model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import org.unimelb.itime.ui.contact.Beans.BaseContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 37925 on 2016/12/7.
 */

public class ContactHelper {

    public static List<BaseContact> getAllPhoneContacts(Context context){
        List<BaseContact> listContacts = new ArrayList<>();
        int id = -1;
        ContentResolver cr = context.getContentResolver();
        String[] mContactsProjection = new String[] {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID
        };

        String contactsId;
        String phoneNum;
        String name;
        long photoId;
        byte[] photoBytes = null;

        //查询contacts表中的所有数据
        Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, mContactsProjection, null, null, null);
        if(cursor.getCount() > 0){
            while (cursor.moveToNext()){
                contactsId = cursor.getString(0);
                phoneNum = cursor.getString(1);
                name = cursor.getString(2);
                photoId = cursor.getLong(3);

                if(photoId > 0){//有头像
                    Cursor cursorPhoto = cr.query(ContactsContract.RawContactsEntity.CONTENT_URI,
                            new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO},
                            ContactsContract.RawContactsEntity.CONTACT_ID + " = ? and " + ContactsContract.RawContactsEntity.MIMETYPE + " = ? and " + ContactsContract.RawContactsEntity.DELETED + " = ?",
                            new String[]{contactsId, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE, "0"},
                            null);
                    if(cursorPhoto.moveToNext()){
                        photoBytes = cursorPhoto.getBlob(0);
                    }
                    cursorPhoto.close();
                }else{
                    photoBytes = null;
                }

//                // 对手机号码进行预处理（去掉号码前的+86、首尾空格、“-”号等）
//                phoneNum = phoneNum.replaceAll("^(\\+86)", "");
//                phoneNum = phoneNum.replaceAll("^(86)", "");
//                phoneNum = phoneNum.replaceAll("-", "");
//                phoneNum = phoneNum.replaceAll(" ", "");
//                phoneNum = phoneNum.trim();
                // 如果当前号码是手机号码
//                if (CheckUtil.PhoneNumberCheck(phoneNum)) {
//                    UserExaminee user = new UserExaminee();
//                    user.setId(String.valueOf(id));
//                    user.setUsername(name);
//                    user.setPhonenumber(phoneNum);
//                    user.setContactPhoto(photoBytes);
//                    user.setModelType(2);
//                    listContacts.add(user);
//                    id -= 1;
//                }

                BaseContact contact = new BaseContact();
                contact.setName(name);
                contact.setContactId(phoneNum);
                listContacts.add(contact);
            }
        }
        cursor.close();
        return listContacts;
    }

//    public static List<BaseContact> getContacts(Context context){
//        Map<String, String> map = getAllCallRecords(context);
//        List<BaseContact> list = new ArrayList<BaseContact>();
//        for(Map.Entry<String, String> entry:map.entrySet()){
//            BaseContact contact = new BaseContact();
//            contact.setName(entry.getKey());
//            contact.setContactId(entry.getValue());
//            list.add(contact);
//        }
//        return list;
//    }
}
