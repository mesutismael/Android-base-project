package be.appreciate.androidbasetool.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import be.appreciate.androidbasetool.R;
import be.appreciate.androidbasetool.api.ApiHelper;
import be.appreciate.androidbasetool.fragments.ClientListFragment;
import be.appreciate.androidbasetool.models.api.Client;
import be.appreciate.androidbasetool.utils.Observer;
import be.appreciate.androidbasetool.utils.PreferencesHelper;

/**
 * Created by thijscoorevits on 4/10/16.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    public static Intent getIntent(Context context)
    {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        if (TextUtils.isEmpty(PreferencesHelper.getTechnicianUsername(this)) || TextUtils.isEmpty(PreferencesHelper.getTechnicianPassword(this)))
        {
            this.startActivity(LoginActivity.getIntent(this));
            this.overridePendingTransition(0, 0);
        } else
        {
            Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);

            NavigationView navigationView = (NavigationView) this.findViewById(R.id.navigationView);
            View headerView = navigationView.getHeaderView(0);
            TextView textViewUsername = (TextView) headerView.findViewById(R.id.textView_name);
            textViewUsername.setText(PreferencesHelper.getTechnicianUsername(this));
            navigationView.setNavigationItemSelectedListener(this);
            navigationView.getMenu().findItem(R.id.action_synchronised).setVisible(true);
            navigationView.getMenu().findItem(R.id.action_not_synchronised).setVisible(false);
            navigationView.getMenu().findItem(R.id.action_sync).setVisible(false);

            this.getData();

            MainActivity.this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout_list, ClientListFragment.newInstance())
                    .commit();

            this.setTitle(getString(R.string.client_list_title));
            this.setSupportActionBar(toolbar);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_clients:
                return true;
            case R.id.action_logOut:
                this.logOut();
                return true;
            case R.id.action_sync:
                return true;
        }
        return false;
    }

    private void logOut()
    {
        PreferencesHelper.clearUser(this);
        Intent intent = LoginActivity.getIntent(this);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent);
    }

    private void getData()
    {
        ApiHelper.getData(this).subscribe(this.dataObserver);
    }

    private Observer<List<Client>> dataObserver = new Observer<List<Client>>()
    {

        @Override
        public void onError(Throwable e)
        {
            //Todo: show error dialog
            Log.d("MainActivity", e.getMessage());
        }
    };
}
