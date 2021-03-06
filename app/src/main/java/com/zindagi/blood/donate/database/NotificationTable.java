/*
 * MIT License
 * Copyright (c) 2017. Attiq ur Rehman
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished
 * to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED
 * "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM,OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.zindagi.blood.donate.database;

import com.zindagi.blood.donate.base.helps.RealmHelper;
import com.zindagi.blood.donate.notification.model.Notification;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Attiq ur Rehman on 08 Oct 2017.
 * Senior Software Engineer at Systems Ltd
 * attiq.ur.rehman1991@gmail.com
 */

public class NotificationTable extends DbHandler {

    public List<Notification> fetchData() {
        Realm realm = new RealmHelper().realm();
        if (!realm.isInTransaction())
            realm.beginTransaction();

        RealmResults<Notification> realmResults = realm.where(Notification.class).findAll();
        List<Notification> notificationList = new ArrayList<>();
        if (realmResults.size() > 0) {
            for (Notification notification : realmResults)
                notificationList.add(Notification.buildNotification(notification));
        }
        realm.commitTransaction();
        realm.close();
        return notificationList;
    }

    @Override
    public void insertData(Object object) {
        if (object instanceof List) {
            List notificationList = (List) object;

            Realm realm = new RealmHelper().realm();
            if (!realm.isInTransaction())
                realm.beginTransaction();

            realm.copyToRealmOrUpdate(notificationList);
            realm.commitTransaction();
            realm.close();
        }
    }

    @Override
    public void deleteData() {
        Realm realm = new RealmHelper().realm();
        if (!realm.isInTransaction())
            realm.beginTransaction();

        RealmResults<Notification> realmResults = realm.where(Notification.class).findAll();
        realmResults.deleteAllFromRealm();

        realm.commitTransaction();
        realm.close();
    }

    public void approveRequest(final Notification notification) {
        Realm realm = new RealmHelper().realm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Notification notification1 = realm.where(Notification.class)
                        .equalTo("emailID", notification.getEmailID()).findFirst();
                notification1.setApproved(1);
            }
        });
    }

    public void rejectRequest(final Notification notification) {
        Realm realm = new RealmHelper().realm();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Notification notification1 = realm.where(Notification.class)
                        .equalTo("emailID", notification.getEmailID()).findFirst();
                notification1.setApproved(2);
            }
        });
    }
}
