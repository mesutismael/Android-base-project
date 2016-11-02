package be.appreciate.androidbasetool.api;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import java.io.IOException;
import java.util.List;

import be.appreciate.androidbasetool.contentproviders.ClientContentProvider;
import be.appreciate.androidbasetool.contentproviders.DocumentContentProvider;
import be.appreciate.androidbasetool.contentproviders.InstallationContentProvider;
import be.appreciate.androidbasetool.contentproviders.LocationContentProvider;
import be.appreciate.androidbasetool.contentproviders.TodoContentProvider;
import be.appreciate.androidbasetool.database.TodoTable;
import be.appreciate.androidbasetool.models.api.Client;
import be.appreciate.androidbasetool.models.api.Installation;
import be.appreciate.androidbasetool.models.api.Technician;
import be.appreciate.androidbasetool.models.api.Todo;
import be.appreciate.androidbasetool.utils.PreferencesHelper;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by thijscoorevits on 4/10/16.
 */

public class ApiHelper
{
    private static ImcApi service;
    public static String BASE_URL="http://test.imc.appreciate.be/api/";

    private static ImcApi getService()
    {
        if (service == null)
        {
            Interceptor logInterceptor = new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(logInterceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();

            service = retrofit.create(ImcApi.class);
        }
        return service;
    }

    public static Observable<Technician> logIn(Context context, String username, String password)
    {
        return ApiHelper.getService().getTechnician(username, password)
                .flatMap(technician ->
                {
                    if (technician != null)
                    {
                        return Observable.just(technician);
                    } else
                    {
                        String error = "something went wrong";
                        return Observable.error(new IOException(error));
                    }
                })
                .doOnNext(technician ->
                {
                    PreferencesHelper.saveTechnicianUsername(context, technician.getUsername());
                    PreferencesHelper.saveTechnicianPassword(context, technician.getPassword());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<List<Client>> getData(Context context)
    {
        return ApiHelper.getService().getData(PreferencesHelper.getTechnicianUsername(context), PreferencesHelper.getTechnicianPassword(context))
                .flatMap(clients ->
                {
                    if (clients != null)
                    {
                        return Observable.just(clients);
                    } else
                    {
                        String error = "something went wrong";
                        return Observable.error(new IOException(error));
                    }
                })
                .doOnNext(technician ->
                {
                    ContentResolver contentResolver = context.getContentResolver();
                    contentResolver.delete(ClientContentProvider.CONTENT_URI, null, null);
                    contentResolver.delete(LocationContentProvider.CONTENT_URI, null, null);
                    contentResolver.delete(InstallationContentProvider.CONTENT_URI, null, null);
                    contentResolver.delete(TodoContentProvider.CONTENT_URI, null, null);
                    contentResolver.delete(DocumentContentProvider.CONTENT_URI, null, null);

                })
                .flatMap(clients -> Observable.from(clients))
                .doOnNext(client ->
                {
                    ContentResolver contentResolver = context.getContentResolver();
                    ContentValues cvClient = client.getContentValues();
                    ContentValues[] cvLocations = client.getLocationContentValues();
                    ContentValues[] cvInstallations = client.getInstallationContentValues();
                    ContentValues[] cvTodo = client.getTodoContentValues();
                    ContentValues[] cvDocument = client.getDocumentContentValues();
                    contentResolver.insert(ClientContentProvider.CONTENT_URI, cvClient);
                    contentResolver.bulkInsert(LocationContentProvider.CONTENT_URI, cvLocations);
                    contentResolver.bulkInsert(InstallationContentProvider.CONTENT_URI, cvInstallations);
                    contentResolver.bulkInsert(TodoContentProvider.CONTENT_URI, cvTodo);
                    contentResolver.bulkInsert(DocumentContentProvider.CONTENT_URI, cvDocument);
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Installation> addNewInstallation(Context context, String installationName, int locationId)
    {
        return ApiHelper.getService().addNewInstallation(PreferencesHelper.getTechnicianUsername(context), PreferencesHelper.getTechnicianPassword(context), installationName, locationId)
                .flatMap(installation ->
                {
                    if (installation != null)
                    {
                        return Observable.just(installation);
                    } else
                    {
                        String error = "Adding new installation went wrong";
                        return Observable.error(new IOException(error));
                    }
                })
                .doOnNext(installation ->
                {
                    ContentResolver contentResolver = context.getContentResolver();
                    ContentValues cvInstallation = installation.getContentValues();
                    contentResolver.insert(InstallationContentProvider.CONTENT_URI, cvInstallation);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Todo> addNewTodo(Context context, String todoText, boolean status, int installationId)
    {
        return ApiHelper.getService().addNewTodo(PreferencesHelper.getTechnicianUsername(context), PreferencesHelper.getTechnicianPassword(context), todoText, status ? 1 : 0, installationId)
                .flatMap(todo ->
                {
                    if (todo != null)
                    {
                        return Observable.just(todo);
                    } else
                    {
                        String error = "Adding new todo went wrong";
                        return Observable.error(new IOException(error));
                    }
                })
                .doOnNext(todo ->
                {
                    ContentResolver contentResolver = context.getContentResolver();
                    ContentValues cvTodo = todo.getContentValues(installationId);
                    contentResolver.insert(TodoContentProvider.CONTENT_URI, cvTodo);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Todo> updateTodo(Context context, be.appreciate.androidbasetool.models.Todo todoToUpdate, int installationId)
    {
        return ApiHelper.getService().updateTodo(PreferencesHelper.getTechnicianUsername(context), PreferencesHelper.getTechnicianPassword(context), todoToUpdate.getId(), todoToUpdate.getTodo(), todoToUpdate.isStatus() ? 0 : 1, installationId)
                .flatMap(todo ->
                {
                    if (todo != null)
                    {
                        return Observable.just(todo);
                    } else
                    {
                        String error = "Adding new todo went wrong";
                        return Observable.error(new IOException(error));
                    }
                })
                .doOnNext(todo ->
                {
                    ContentResolver contentResolver = context.getContentResolver();
                    ContentValues cvTodo = todo.getContentValues(installationId);
                    String where = TodoTable.COLUMN_TODO_ID_FULL + " = " + todoToUpdate.getId();
                    contentResolver.update(TodoContentProvider.CONTENT_URI, cvTodo, where, null);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
