package be.appreciate.androidbasetool.api;


import java.util.List;

import be.appreciate.androidbasetool.models.api.Client;
import be.appreciate.androidbasetool.models.api.Installation;
import be.appreciate.androidbasetool.models.api.Technician;
import be.appreciate.androidbasetool.models.api.Todo;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by thijscoorevits on 4/10/16.
 */

public interface ImcApi
{

    @POST("authenticate")
    Observable<Technician> getTechnician(@Query("username") String username,
                                         @Query("password") String password);

    @GET("data_sync")
    Observable<List<Client>> getData(@Header("username") String username,
                                     @Header("password") String password);

    @POST("installs")
    Observable<Installation> addNewInstallation(@Header("username") String username,
                                                @Header("password") String password,
                                                @Query("name") String installationName,
                                                @Query("location_id") int locationId);

    @POST("todo")
    Observable<Todo> addNewTodo(@Header("username") String username,
                                @Header("password") String password,
                                @Query("todo") String todo,
                                @Query("status") int status,
                                @Query("installation_id") int installationId);


    @PUT("todo/{todo_id}")
    Observable<Todo> updateTodo(@Header("username") String username,
                                @Header("password") String password,
                                @Path("todo_id") int todoId,
                                @Query("todo") String todo,
                                @Query("status") int status,
                                @Query("installation_id") int installationId);
}