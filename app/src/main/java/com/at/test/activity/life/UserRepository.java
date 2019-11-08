package com.at.test.activity.life;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.ItemKeyedDataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

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
        List<UserTable> users = db.userDao().getUsers();
    }

    public AppDatabase getAppDatabase() {
        return Room.databaseBuilder(TestApplication.getGlobalApplication(),
                AppDatabase.class, "and_test").build();
    }

    public static abstract class HttpExecuteCallback<T> {

        abstract void onResponse(T data);

        abstract void onFailure();

    }

    public static class Executor {

        public static void execute(HttpExecuteCallback httpExecuteCallback) {

        }
    }

    @Entity(tableName = "user", indices = {@Index(name = "user_index", value = {"id"})})
    public static class UserTable {

        @ColumnInfo(name = "name")
        String userName;
        @ColumnInfo(name = "pass")
        String userPass;

        @PrimaryKey
        @NonNull
        String id;

    }

    @Dao
    public interface UserDao {
        @Query("select * from user")
        List<UserTable> getUsers();

        @Query("select * from user where id = :id")
        UserTable getUserById(String id);

        @Query("select * from user limit :pageCount offset :page")
        DataSource.Factory<Integer, UserTable> getUsersPaging(int page, int pageCount);
    }

    @Database(entities = {UserTable.class}, version = 1)
    public static abstract class AppDatabase extends RoomDatabase {

        public abstract UserDao userDao();
    }

    public void upgrade() {
        AppDatabase db = Room.databaseBuilder(TestApplication.getGlobalApplication(),
                AppDatabase.class, "and_test").addMigrations(migration).build();
    }

    Migration migration = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

        }
    };

    public void paging() {

        DataSource<Integer, UserTable> userTableDataSourceNet = new ItemKeyedDataSource<Integer, UserTable>() {

            @Override
            public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<UserTable> callback) {

            }

            @Override
            public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<UserTable> callback) {

            }

            @Override
            public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<UserTable> callback) {
//                   params.key
//
            }

            @NonNull
            @Override
            public Integer getKey(@NonNull UserTable item) {
                return null;
            }
        };

        DataSource.Factory<Integer, UserTable> userTableDataSource = getAppDatabase().userDao().getUsersPaging(0, 20);
        LiveData<PagedList<UserTable>> pagedList = new LivePagedListBuilder<Integer, UserTable>(userTableDataSource, 20).setBoundaryCallback(new PagedList.BoundaryCallback<UserTable>() {
        }).build();

    }

}
