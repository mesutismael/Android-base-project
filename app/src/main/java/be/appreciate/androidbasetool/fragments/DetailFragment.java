package be.appreciate.androidbasetool.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;

import be.appreciate.androidbasetool.R;
import be.appreciate.androidbasetool.utils.Constants;

/**
 * Created by thijscoorevits on 5/10/16.
 */

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    private int installationId;
    private MaterialDialog dialogProgress;

    public static DetailFragment newInstance(int installationId)
    {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_LOCATION_ID, installationId);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        if (this.getArguments() != null)
            this.installationId = this.getArguments().getInt(Constants.KEY_LOCATION_ID);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().initLoader(Constants.LOADER_TODOLIST, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {

    }

    private void showErrorDialog(Context context)
    {
        new MaterialDialog.Builder(context)
                .title(R.string.dialog_error)
                .content(R.string.add_todo_error)
                .positiveText(R.string.dialog_positive)
                .show();
    }

    private void showProgressDialog(Context context)
    {
        this.dialogProgress = new MaterialDialog.Builder(context)
                .content(R.string.add_todo_progress)
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

}
