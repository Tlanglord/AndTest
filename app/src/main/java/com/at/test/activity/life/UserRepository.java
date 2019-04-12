package com.at.test.activity.life;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.at.test.TestApplication;

import java.util.List;

/**
 * Created by dqq on 2019/4/11.
 */
public class UserRepository {

    public ILoading iLoading;

    public void setLoading(ILoading loading) {
        this.iLoading = loading;
    }

    public static <T> void login(IResponse<T> iResponse) {

    }

    public static <T> void loginOut(IResponse<T> iResponse) {

    }

    public <T> void submit(final IResponse<T> iResponse) {

        if (iLoading != null) {
            iLoading.showLoading();
        }

        Executor.execute(new HttpExecuteCallback<T>() {
            @Override
            void onResponse(T data) {
                if (iLoading != null) {
                    iLoading.hideLoading();
                }
                if (iResponse != null) {
                    iResponse.onSuccess(data);
                }
            }

            @Override
            void onFailure() {
                if (iLoading != null) {
                    iLoading.hideLoading();
                }
            }
        });
    }

    public void loadUsers() {
        AppDatabase db = Room.databaseBuilder(TestApplication.getGlobalApplication(),
                AppDatabase.class, "and_test").build();
        List<User> users = db.userDao().getUsers();
    }

    public static abstract class HttpExecuteCallback<T> {

        abstract void onResponse(T data);

        abstract void onFailure();

    }

    public static class Executor {

        public static void execute(HttpExecuteCallback httpExecuteCallback) {

        }
    }

    @Entity(tableName = "user")
    public static class UserTable {

        @ColumnInfo(name = "id")
        String userName;
        @ColumnInfo(name = "id")
        String userPass;
    }

    @Dao
    public interface UserDao {
        @Query("select * from user")
        List<User> getUsers();
    }

    @Database(entities = {User.class}, version = 1)
    public abstract class AppDatabase extends RoomDatabase {
        public abstract UserDao userDao();
    }


}
