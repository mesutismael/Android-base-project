package be.appreciate.androidbasetool.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import be.appreciate.androidbasetool.R;
import be.appreciate.androidbasetool.fragments.InstallationDocumentFragment;
import be.appreciate.androidbasetool.utils.Constants;


/**
 * Created by thijscoorevits on 12/10/16.
 */

public class InstallationDocumentActivity extends AppCompatActivity
{
    public static Intent getIntent(Context context, int locationId, String locationName, String clientName)
    {
        Intent intent = new Intent(context, InstallationDocumentActivity.class);
        intent.putExtra(Constants.KEY_LOCATION_ID, locationId);
        intent.putExtra(Constants.KEY_LOCATION_NAME, locationName);
        intent.putExtra(Constants.KEY_CLIENT_NAME, clientName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_installation_document);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(this.getString(R.string.toolbar_installation_document,
                this.getIntent().getStringExtra(Constants.KEY_LOCATION_NAME),
                this.getIntent().getStringExtra(Constants.KEY_CLIENT_NAME)));

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout_content, InstallationDocumentFragment.newInstance(this.getIntent().getIntExtra(Constants.KEY_LOCATION_ID, 0)))
                .commit();

    }
}
