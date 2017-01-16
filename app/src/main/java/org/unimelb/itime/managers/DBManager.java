package org.unimelb.itime.managers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.QueryBuilder;
import org.unimelb.itime.bean.Block;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.Message;
import org.unimelb.itime.bean.SettingWrapper;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.dao.BlockDao;
import org.unimelb.itime.dao.CalendarDao;
import org.unimelb.itime.dao.ContactDao;
import org.unimelb.itime.dao.DaoMaster;
import org.unimelb.itime.dao.DaoSession;
import org.unimelb.itime.dao.EventDao;
import org.unimelb.itime.dao.FriendRequestDao;
import org.unimelb.itime.dao.MessageDao;
import org.unimelb.itime.dao.UserDao;


import java.util.List;

/**
 * Created by yuhaoliu on 28/08/16.
 */
public class DBManager {
    private final static String dbName = "test_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;
    private DaoSession daoSession;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        daoSession = new DaoMaster(getWritableDatabase()).newSession();
    }


    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }

    public synchronized void insertSetting(SettingWrapper setting){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        // TODO: 28/12/2016 insert SettingWrapper to db
    }

    public synchronized void insertEvent(Event event) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        EventDao eventDaoDao = daoSession.getEventDao();
        eventDaoDao.insertOrReplace(event);
    }

    public synchronized void insertMessage(Message message) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MessageDao messageDao = daoSession.getMessageDao();
        messageDao.insert(message);
    }

    public void insertMessageList(List<Message> messageList) {
        for (Message message : messageList) {
            insertMessage(message);
        }
    }

    public void insertContact(Contact contact) {
        if(contact==null){
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ContactDao contactDao = daoSession.getContactDao();
        contactDao.insertOrReplace(contact);
    }

    public void deleteContact(Contact contact){
        if(contact==null){
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ContactDao contactDao = daoSession.getContactDao();
        contactDao.delete(contact);
    }

    public void updateContact(Contact contact){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ContactDao contactDao = daoSession.getContactDao();
        contactDao.update(contact);
    }

    public Contact searchContact(String contactUid){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ContactDao contactDao = daoSession.getContactDao();
        QueryBuilder<Contact> qb = contactDao.queryBuilder();
        qb.where(
                ContactDao.Properties.ContactUid.eq(contactUid));
        List<Contact> list = qb.list();
        if(list.isEmpty()){
            return null;
        }else {
            return list.get(0);
        }
    }

    public void insertUser(User user){
        if(user!=null) {
            DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
            DaoSession daoSession = daoMaster.newSession();
            UserDao userDao = daoSession.getUserDao();
            userDao.insertOrReplace(user);
        }
    }


//    public void insertTimeSlot(Timeslot timeSlot){
//        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        TimeslotDao timeSlotDao = daoSession.getTimeslotDao();
//        timeSlotDao.insert(timeSlot);
//    }



    public void insertEventList(List<Event> events) {
        if (events == null || events.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        EventDao eventDaoDao = daoSession.getEventDao();
        eventDaoDao.insertInTx(events);
    }

//    public void insertInviteeList(List<Invitee> invitees) {
//        if (invitees == null || invitees.isEmpty()) {
//            return;
//        }
//        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        InviteeDao inviteeDao = daoSession.getInviteeDao();
//        inviteeDao.insertInTx(invitees);
//    }

    public List<Event> queryEventList(long startTime, long endTime) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        EventDao eventDao = daoSession.getEventDao();
        QueryBuilder<Event> qb = eventDao.queryBuilder();
        qb.where(qb.and(EventDao.Properties.StartTime.gt(startTime - 1), EventDao.Properties.StartTime.le(endTime)));
        List<Event> list = qb.list();
        return list;
    }

    public List<Event> getAllEvents() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        EventDao eventDao = daoSession.getEventDao();
        QueryBuilder<Event> qb = eventDao.queryBuilder();
        List<Event> list = qb.list();
        return list;
    }

    public List<Message> getAllMessages() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MessageDao messageDao = daoSession.getMessageDao();
        QueryBuilder<Message> qb = messageDao.queryBuilder();
        List<Message> list = qb.list();
        return list;
    }

    public synchronized void deleteAllMessages() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MessageDao messageDao = daoSession.getMessageDao();
        messageDao.deleteAll();
    }

    public List<Contact> getAllContact() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ContactDao contactDao = daoSession.getContactDao();
        QueryBuilder<Contact> qb = contactDao.queryBuilder();
        qb.where(
                 qb.and(ContactDao.Properties.Status.eq(Contact.ACTIVATED),
                         ContactDao.Properties.BlockLevel.eq(0))).orderAsc(ContactDao.Properties.AliasName);
        List<Contact> list = qb.list();
        return list;
    }

    public List<Block> getBlockContacts() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BlockDao blockDao = daoSession.getBlockDao();
        QueryBuilder<Block> qb = blockDao.queryBuilder();
        qb.where(BlockDao.Properties.BlockLevel.gt(0));
        List<Block> list = qb.list();
        return list;
    }

    public void insertBlock(Block block) {
        if(block==null){
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BlockDao blockDao = daoSession.getBlockDao();
        blockDao.insertOrReplace(block);
    }

    public void deleteBlock(Block block) {
        if(block==null){
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        BlockDao blockDao = daoSession.getBlockDao();
        blockDao.delete(block);
    }

    public synchronized void deleteAllContact(){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ContactDao contactDao = daoSession.getContactDao();
        contactDao.deleteAll();
    }

    public Event getEvent(String uid) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        EventDao eventDao = daoSession.getEventDao();
        QueryBuilder<Event> qb = eventDao.queryBuilder();
        qb.where(EventDao.Properties.EventUid.eq(uid));
        List<Event> list = qb.list();

        return list.size() > 0 ? list.get(0) : null;
    }

    public synchronized void clearDB(){
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(
                context.getApplicationContext(), dbName, null);
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        devOpenHelper.onUpgrade(db,0,0);
    }

    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    public SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    public void insertFriendRequest(FriendRequest request) {
        if(request==null){
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FriendRequestDao friendRequestDao = daoSession.getFriendRequestDao();
        friendRequestDao.insertOrReplace(request);

    }

    public List<FriendRequest> getAllFriendRequest(){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FriendRequestDao friendRequestDao = daoSession.getFriendRequestDao();
        QueryBuilder<FriendRequest> qb = friendRequestDao.queryBuilder();
        qb.orderDesc(FriendRequestDao.Properties.UpdatedAt);
        List<FriendRequest> list = qb.list();
        return list;
    }

    public List<Calendar> getAllCalendars(){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CalendarDao calendarDao = daoSession.getCalendarDao();
        QueryBuilder<Calendar> qb = calendarDao.queryBuilder();
        List<Calendar> list = qb.list();
        return list;
    }

    public void insertCalendars(List<Calendar> cals){
        if (cals == null || cals.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CalendarDao calDao = daoSession.getCalendarDao();
        calDao.insertInTx(cals);
    }

    public void clearCalendars(){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        CalendarDao calDao = daoSession.getCalendarDao();
        calDao.deleteAll();
    }

    /*********************************** delete above *************************************/
    @SuppressWarnings("unchecked")
    public <T extends Object,V> List<T> find(Class<T> className, String name, V value){
        AbstractDao abd =  daoSession.getDao(className);
        QueryBuilder<T> qb = abd.queryBuilder();
        Property[] ptys = abd.getProperties();
        Property attr = null;

        for (Property pty:ptys
             ) {
            if (pty.name.equals(name)){
                attr = pty;
                break;
            }
        }
        if (attr == null){
            return null;
        }

        return qb.where(
                attr.eq(value)).list();
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> AbstractDao getQueryDao(Class<T> className){
        return daoSession.getDao(className);
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> List<T> getAll(Class<T> className){
        return ((AbstractDao) daoSession.getDao(className)).queryBuilder().list();
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> void deleteAll(Class<T> className){
        ((AbstractDao) daoSession.getDao(className)).deleteAll();
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> void insert(List<T> objs){
        if (objs == null || objs.isEmpty()) {
            return;
        }
        ((AbstractDao) daoSession.getDao(objs.get(0).getClass())).insertInTx(objs);
    }

}
