package be.appreciate.androidbasetool.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import be.appreciate.androidbasetool.R;
import be.appreciate.androidbasetool.activities.DetailActivity;
import be.appreciate.androidbasetool.adapters.DocumentAdapter;
import be.appreciate.androidbasetool.adapters.InstallationAdapter;
import be.appreciate.androidbasetool.api.ApiHelper;
import be.appreciate.androidbasetool.contentproviders.DocumentContentProvider;
import be.appreciate.androidbasetool.contentproviders.InstallationContentProvider;
import be.appreciate.androidbasetool.database.DocumentTable;
import be.appreciate.androidbasetool.database.InstallationTable;
import be.appreciate.androidbasetool.decorations.DividerDecoration;
import be.appreciate.androidbasetool.models.Document;
import be.appreciate.androidbasetool.models.api.Installation;
import be.appreciate.androidbasetool.utils.Constants;
import be.appreciate.androidbasetool.utils.DialogHelper;
import be.appreciate.androidbasetool.utils.Observer;


/**
 * Created by thijscoorevits on 12/10/16.
 */

public class InstallationDocumentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        InstallationAdapter.OnInstallationClickListener, View.OnClickListener, DocumentAdapter.OnDocumentClickListener
{
    private InstallationAdapter installationAdapter;
    private DocumentAdapter documentAdapter;
    private MaterialDialog dialogProgress;
    private int locationId;

    public static InstallationDocumentFragment newInstance(int locationId)
    {
        InstallationDocumentFragment installationDocumentFragment = new InstallationDocumentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_LOCATION_ID, locationId);
        installationDocumentFragment.setArguments(bundle);
        return installationDocumentFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_installation_document, container, false);

        RecyclerView recyclerViewInstallationList = (RecyclerView) view.findViewById(R.id.recyclerView_installationList);
        FloatingActionButton floatingActionButtonAddInstallation = (FloatingActionButton) view.findViewById(R.id.floatingActionButton_addInstallation);
        floatingActionButtonAddInstallation.setOnClickListener(this);
        RecyclerView recyclerViewDocumentList = (RecyclerView) view.findViewById(R.id.recyclerView_DocumentList);
        DividerDecoration dividerDecoration = new DividerDecoration(view.getContext());
        recyclerViewInstallationList.addItemDecoration(dividerDecoration);
        recyclerViewDocumentList.addItemDecoration(dividerDecoration);

        this.installationAdapter = new InstallationAdapter();
        this.installationAdapter.setClickListener(this);
        recyclerViewInstallationList.setAdapter(this.installationAdapter);

        this.documentAdapter = new DocumentAdapter();
        this.documentAdapter.setClickListener(this);
        recyclerViewDocumentList.setAdapter(this.documentAdapter);

        if (this.getArguments() != null)
            this.locationId = this.getArguments().getInt(Constants.KEY_LOCATION_ID);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().initLoader(Constants.LOADER_INSTALLATIONLIST, null, this);
        this.getLoaderManager().initLoader(Constants.LOADER_DOCUMENTLIST, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        if (this.getContext() != null)
        {
            String selection;
            switch (id)
            {
                case Constants.LOADER_INSTALLATIONLIST:
                    selection = InstallationTable.COLUMN_LOCATION_ID + " = " + this.locationId;
                    return new CursorLoader(this.getContext(), InstallationContentProvider.CONTENT_URI, null, selection, null, null);
                case Constants.LOADER_DOCUMENTLIST:
                    selection = DocumentTable.COLUMN_LOCATION_ID + " = " + this.locationId;
                    return new CursorLoader(this.getContext(), DocumentContentProvider.CONTENT_URI, null, selection, null, null);
                default:
                    return null;
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        DatabaseUtils.dumpCursor(data);
        switch (loader.getId())
        {
            case Constants.LOADER_INSTALLATIONLIST:
                if (data != null && data.moveToFirst())
                {
                    InstallationDocumentFragment.this.installationAdapter.setInstallationsList(be.appreciate.androidbasetool.models.Installation.constructListFromCursor(data));
                }
                break;
            case Constants.LOADER_DOCUMENTLIST:
                if (data != null && data.moveToFirst())
                {
                    InstallationDocumentFragment.this.documentAdapter.setDocumentList(Document.constructListFromCursor(data));
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {

    }

    @Override
    public void onInstallationClick(int installationId)
    {
        if(this.getContext() != null)
            this.startActivity(DetailActivity.getIntent(this.getContext(), installationId));
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.floatingActionButton_addInstallation:
                showNewInstallationDialog(view.getContext());
                break;
        }
    }

    private void showNewInstallationDialog(Context context)
    {
        new MaterialDialog.Builder(context)
                .title(R.string.dialog_add_installation)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .cancelable(true)
                .negativeText(R.string.dialog_cancel)
                .positiveText(R.string.dialog_save)
                .input(R.string.dialog_installation_hint, 0, new MaterialDialog.InputCallback()
                {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input)
                    {
                        if (!TextUtils.isEmpty(input))
                            InstallationDocumentFragment.this.addNewInstallation(input.toString());
                        else
                            dialog.dismiss();
                    }
                }).show();
    }

    private void showErrorDialog(Context context)
    {
        new MaterialDialog.Builder(context)
                .title(R.string.dialog_error)
                .content(R.string.add_installation_error)
                .positiveText(R.string.dialog_positive)
                .show();
    }

    private void showProgressDialog(Context context)
    {
        this.dialogProgress = new MaterialDialog.Builder(context)
                .content(R.string.login_progress)
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    private void addNewInstallation(String installationName)
    {
        if (this.getContext() != null)
        {
            this.showProgressDialog(this.getContext());
            ApiHelper.addNewInstallation(this.getContext(), installationName, this.locationId).subscribe(this.newInstallationObserver);
        }
    }

    private Observer<Installation> newInstallationObserver = new Observer<Installation>()
    {
        @Override
        public void onCompleted()
        {
            if (InstallationDocumentFragment.this.dialogProgress != null)
            {
                InstallationDocumentFragment.this.dialogProgress.dismiss();
            }
            InstallationDocumentFragment.this.getLoaderManager().initLoader(Constants.LOADER_INSTALLATIONLIST, null, InstallationDocumentFragment.this);

        }

        @Override
        public void onError(Throwable e)
        {
            if (InstallationDocumentFragment.this.dialogProgress != null)
            {
                InstallationDocumentFragment.this.dialogProgress.dismiss();
            }

            if (InstallationDocumentFragment.this.getContext() != null && DialogHelper.canShowDialog(InstallationDocumentFragment.this))
            {
                InstallationDocumentFragment.this.showErrorDialog(InstallationDocumentFragment.this.getContext());
            }
        }
    };

    @Override
    public void onDocumentClick(Document document)
    {
        if(this.getContext() != null)
            Toast.makeText(this.getContext(), this.getString(R.string.download_document, document.getName()), Toast.LENGTH_SHORT).show();
    }
}
