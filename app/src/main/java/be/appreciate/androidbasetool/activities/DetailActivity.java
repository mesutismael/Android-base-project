package be.appreciate.androidbasetool.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import be.appreciate.androidbasetool.R;
import be.appreciate.androidbasetool.fragments.DetailFragment;
import be.appreciate.androidbasetool.utils.Constants;


/**
 * Created by thijscoorevits on 12/10/16.
 */

public class DetailActivity extends AppCompatActivity
{
    public static Intent getIntent(Context context, int installationId)
    {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(Constants.KEY_INSTALLATION_ID, installationId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout_content, DetailFragment.newInstance(this.getIntent().getIntExtra(Constants.KEY_INSTALLATION_ID, 0)))
                .commit();
    }
}
