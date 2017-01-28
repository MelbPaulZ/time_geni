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
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhaoliu on 28/08/16.
 */
public class DBManager {
    private final static String dbName = "test_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;
    private DaoMaster daoMaster;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        daoMaster = new DaoMaster(getWritableDatabase());
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
        DaoSession daoSession = daoMaster.newSession();
        // TODO: 28/12/2016 insertOrReplace SettingWrapper to db
    }

    public synchronized void insertEvent(Event event) {
        DaoSession daoSession = daoMaster.newSession();
        EventDao eventDaoDao = daoSession.getEventDao();
        eventDaoDao.insertOrReplace(event);
    }

    public synchronized void insertMessage(Message message) {
        DaoSession daoSession = daoMaster.newSession();
        MessageDao messageDao = daoSession.getMessageDao();
        messageDao.insertOrReplace(message);
    }

    public synchronized void insertMessageList(List<Message> messageList) {
        for (Message message : messageList) {
            insertMessage(message);
        }
    }

    public synchronized void insertContact(Contact contact) {
        if(contact==null){
            return;
        }
        DaoSession daoSession = daoMaster.newSession();
        ContactDao contactDao = daoSession.getContactDao();
        contactDao.insertOrReplace(contact);
    }

    public synchronized void deleteContact(Contact contact){
        if(contact==null){
            return;
        }
        DaoSession daoSession = daoMaster.newSession();
        ContactDao contactDao = daoSession.getContactDao();
        contactDao.delete(contact);
    }

    public synchronized void updateContact(Contact contact){
        DaoSession daoSession = daoMaster.newSession();
        ContactDao contactDao = daoSession.getContactDao();
        contactDao.update(contact);
    }

    public synchronized Contact searchContact(String contactUid){
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

    public synchronized void insertUser(User user){
        if(user!=null) {
            DaoSession daoSession = daoMaster.newSession();
            UserDao userDao = daoSession.getUserDao();
            userDao.insertOrReplace(user);
        }
    }


//    public void insertTimeSlot(Timeslot timeSlot){
//
//        DaoSession daoSession = daoMaster.newSession();
//        TimeslotDao timeSlotDao = daoSession.getTimeslotDao();
//        timeSlotDao.insertOrReplace(timeSlot);
//    }



    public synchronized void insertEventList(List<Event> events) {
        if (events == null || events.isEmpty()) {
            return;
        }
        DaoSession daoSession = daoMaster.newSession();
        EventDao eventDaoDao = daoSession.getEventDao();
        eventDaoDao.insertInTx(events);
    }

//    public void insertInviteeList(List<Invitee> invitees) {
//        if (invitees == null || invitees.isEmpty()) {
//            return;
//        }
//        DaoSession daoSession = daoMaster.newSession();
//        InviteeDao inviteeDao = daoSession.getInviteeDao();
//        inviteeDao.insertInTx(invitees);
//    }

    public synchronized List<Event> queryEventList(long startTime, long endTime) {
        DaoSession daoSession = daoMaster.newSession();
        EventDao eventDao = daoSession.getEventDao();
        QueryBuilder<Event> qb = eventDao.queryBuilder();
        qb.where(qb.and(EventDao.Properties.StartTime.gt(startTime - 1), EventDao.Properties.StartTime.le(endTime)));
        List<Event> list = qb.list();
        return list;
    }

    public synchronized List<Event> getAllEvents() {
        DaoSession daoSession = daoMaster.newSession();
        EventDao eventDao = daoSession.getEventDao();
        QueryBuilder<Event> qb = eventDao.queryBuilder();
        List<Event> list = qb.list();
        return list;
    }

    public synchronized List<Message> getAllMessages() {
        DaoSession daoSession = daoMaster.newSession();
        MessageDao messageDao = daoSession.getMessageDao();
        QueryBuilder<Message> qb = messageDao.queryBuilder();
        qb.where(MessageDao.Properties.DeleteLevel.eq(0));
        qb.orderDesc(MessageDao.Properties.CreatedAt);
        List<Message> list = qb.list();
        return list;
    }

    public synchronized void deleteAllMessages() {
        DaoSession daoSession = daoMaster.newSession();
        MessageDao messageDao = daoSession.getMessageDao();
        messageDao.deleteAll();
    }

    public synchronized List<Contact> getAllContact() {
        DaoSession daoSession = daoMaster.newSession();
        ContactDao contactDao = daoSession.getContactDao();
        QueryBuilder<Contact> qb = contactDao.queryBuilder();
        qb.where(qb.and(ContactDao.Properties.UserUid.eq(UserUtil.getInstance(context).getUserUid()),
                 qb.and(ContactDao.Properties.Status.eq(Contact.ACTIVATED),
                         ContactDao.Properties.BlockLevel.eq(0)))).orderAsc(ContactDao.Properties.AliasName);
        List<Contact> list = qb.list();
        return list;
    }

    public synchronized List<Block> getBlockContacts() {
        DaoSession daoSession = daoMaster.newSession();
        BlockDao blockDao = daoSession.getBlockDao();
        QueryBuilder<Block> qb = blockDao.queryBuilder();
        qb.where(qb.and(BlockDao.Properties.UserUid.eq(UserUtil.getInstance(context).getUserUid()),
                BlockDao.Properties.BlockLevel.gt(0)));
        List<Block> list = qb.list();
        return list;
    }

    public synchronized void insertBlock(Block block) {
        if(block==null){
            return;
        }
        DaoSession daoSession = daoMaster.newSession();
        BlockDao blockDao = daoSession.getBlockDao();
        blockDao.insertOrReplace(block);
    }

    public synchronized void deleteBlock(Block block) {
        if(block==null){
            return;
        }

        DaoSession daoSession = daoMaster.newSession();
        BlockDao blockDao = daoSession.getBlockDao();
        blockDao.delete(block);
    }

    public synchronized void deleteAllContact(){

        DaoSession daoSession = daoMaster.newSession();
        ContactDao contactDao = daoSession.getContactDao();
        contactDao.deleteAll();
    }

    public Event getEvent(String uid) {

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

    public synchronized void insertFriendRequest(FriendRequest request) {
        if(request==null){
            return;
        }
        DaoSession daoSession = daoMaster.newSession();
        FriendRequestDao friendRequestDao = daoSession.getFriendRequestDao();
        friendRequestDao.insertOrReplace(request);

    }

    public List<FriendRequest> getAllFriendRequest(){
        DaoSession daoSession = daoMaster.newSession();
        FriendRequestDao friendRequestDao = daoSession.getFriendRequestDao();
        QueryBuilder<FriendRequest> qb = friendRequestDao.queryBuilder();
        qb.where(FriendRequestDao.Properties.FreqUserUid.eq(UserUtil.getInstance(context).getUserUid()));
        qb.orderDesc(FriendRequestDao.Properties.UpdatedAt);
        List<FriendRequest> list = qb.list();
        return list;
    }

    public List<Calendar> getAllCalendars(){

        DaoSession daoSession = daoMaster.newSession();
        CalendarDao calendarDao = daoSession.getCalendarDao();
        QueryBuilder<Calendar> qb = calendarDao.queryBuilder();
        List<Calendar> list = qb.list();
        return list;
    }

    public synchronized void insertCalendars(List<Calendar> cals){
        if (cals == null || cals.isEmpty()) {
            return;
        }

        DaoSession daoSession = daoMaster.newSession();
        CalendarDao calDao = daoSession.getCalendarDao();
        calDao.insertInTx(cals);
    }

    public synchronized void clearCalendars(){

        DaoSession daoSession = daoMaster.newSession();
        CalendarDao calDao = daoSession.getCalendarDao();
        calDao.deleteAll();
    }

    /*********************************** delete above *************************************/
    @SuppressWarnings("unchecked")
    public <T extends Object,V> List<T> find(Class<T> className, String name, V value){
        AbstractDao abd =  daoMaster.newSession().getDao(className);
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

        List<T> list = qb.where(attr.eq(value)).list();
        return list;
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> AbstractDao getQueryDao(Class<T> className){
        return daoMaster.newSession().getDao(className);
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> List<T> getAll(Class<T> className){
        return ((AbstractDao) daoMaster.newSession().getDao(className)).queryBuilder().list();
    }


    @SuppressWarnings("unchecked")
    public synchronized  <T extends Object> void deleteAll(Class<T> className){
        ((AbstractDao) (daoMaster.newSession()).getDao(className)).deleteAll();
    }

    @SuppressWarnings("unchecked")
    public synchronized  <T extends Object> void insertOrReplace(List<T> objs){
        if (objs == null || objs.isEmpty()) {
            return;
        }
        for (T obj:objs) {
            ((AbstractDao) (daoMaster.newSession()).getDao(obj.getClass())).insertOrReplace(obj);
        }
    }

    public List<Event> getAllAvailableEvents(List<org.unimelb.itime.bean.Calendar> calendars, String userUid){
        List<Event> events = new ArrayList<>();
        AbstractDao query = DBManager.getInstance(context).getQueryDao(Event.class);
        for (org.unimelb.itime.bean.Calendar cal:calendars
                ) {
            if (cal.getDeleteLevel() == 0 && cal.getVisibility() == 1){
                List<Event> dbEvents = query.queryBuilder().where(
                        EventDao.Properties.UserUid.eq(userUid),
                        EventDao.Properties.CalendarUid.eq(cal.getCalendarUid()),
                        EventDao.Properties.ShowLevel.gt(0)
                ).list();

                events.addAll(dbEvents);
            }
        }

        return events;
    }


    public List<Calendar> getAllCalendarsForUser(){
        String uid = UserUtil.getInstance(context).getUserUid();

        DaoSession daoSession = daoMaster.newSession();
        CalendarDao calendarDao = daoSession.getCalendarDao();
        List<Calendar> calendarList = calendarDao.queryBuilder().where(
                CalendarDao.Properties.UserUid.eq(UserUtil.getInstance(context).getUserUid())
        ).orderAsc(CalendarDao.Properties.CreatedAt).list();
        return calendarList;
    }

    public DaoSession getNewSession(){
        return daoMaster.newSession();
    }

}
