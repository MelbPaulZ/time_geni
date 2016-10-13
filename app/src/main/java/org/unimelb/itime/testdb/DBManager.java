package org.unimelb.itime.testdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.query.QueryBuilder;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.bean.Timeslot;
import org.unimelb.itime.dao.ContactDao;
import org.unimelb.itime.dao.DaoMaster;
import org.unimelb.itime.dao.DaoSession;
import org.unimelb.itime.dao.EventDao;
import org.unimelb.itime.dao.InviteeDao;
import org.unimelb.itime.dao.PhotoUrlDao;
import org.unimelb.itime.dao.TimeslotDao;

import java.util.List;

/**
 * Created by yuhaoliu on 28/08/16.
 */
public class DBManager {
    private final static String dbName = "test_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }


    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class)
        {
            if (mInstance == null)
                {
                    mInstance = new DBManager(context);
                }
        }
        }
            return mInstance;
    }


    public void insertEvent(Event event) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        EventDao eventDaoDao = daoSession.getEventDao();
        eventDaoDao.insert(event);
    }

    public void insertPhoto(PhotoUrl photoUrl){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        PhotoUrlDao photoUrlDao = daoSession.getPhotoUrlDao();
        photoUrlDao.insert(photoUrl);
    }

    public void insertInvitee(Invitee invitee) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        InviteeDao inviteeDao = daoSession.getInviteeDao();
        inviteeDao.insert(invitee);
    }

    public void insertContact(Contact contact) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ContactDao contactDao = daoSession.getContactDao();
        contactDao.insert(contact);
    }

    public void insertTimeSlot(Timeslot timeSlot){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        TimeslotDao timeSlotDao = daoSession.getTimeslotDao();
        timeSlotDao.insert(timeSlot);
    }

    public void insertEventList(List<Event> events) {
        if (events == null || events.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        EventDao eventDaoDao = daoSession.getEventDao();
        eventDaoDao.insertInTx(events);
    }

    public void insertInviteeList(List<Invitee> invitees) {
        if (invitees == null || invitees.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        InviteeDao inviteeDao = daoSession.getInviteeDao();
        inviteeDao.insertInTx(invitees);
    }

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

    public List<Invitee> getAllInvitee(){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        InviteeDao inviteeDao = daoSession.getInviteeDao();
        QueryBuilder<Invitee> qb = inviteeDao.queryBuilder();
        List<Invitee> list = qb.list();
        return list;
    }

    public List<Contact> getAllContact(){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        ContactDao contactDao = daoSession.getContactDao();
        QueryBuilder<Contact> qb = contactDao.queryBuilder();
        List<Contact> list = qb.list();
        return list;
    }

    public Event getEvent(String uid){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        EventDao eventDao = daoSession.getEventDao();
        QueryBuilder<Event> qb = eventDao.queryBuilder();
        qb.where(EventDao.Properties.EventUid.eq(uid));
        return  qb.list().get(0);
    }

    public void clearDB(){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        EventDao eventDao = daoSession.getEventDao();
        ContactDao contactDao = daoSession.getContactDao();
        InviteeDao inviteeDao = daoSession.getInviteeDao();
        TimeslotDao timeSlotDao = daoSession.getTimeslotDao();
        eventDao.deleteAll();
        contactDao.deleteAll();
        inviteeDao.deleteAll();
        timeSlotDao.deleteAll();

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
}
