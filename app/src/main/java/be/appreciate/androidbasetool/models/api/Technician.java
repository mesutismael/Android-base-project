package be.appreciate.androidbasetool.models.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by thijscoorevits on 4/10/16.
 */

public class Technician
{
    @SerializedName("id")
    private int id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("gv_number")
    private String gvNumber;
    @SerializedName("tv_number")
    private String tvNumber;

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }
}
