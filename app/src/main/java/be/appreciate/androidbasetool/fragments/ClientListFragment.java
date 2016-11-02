package be.appreciate.androidbasetool.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import be.appreciate.androidbasetool.R;
import be.appreciate.androidbasetool.activities.InstallationDocumentActivity;
import be.appreciate.androidbasetool.adapters.ClientLocationAdapter;
import be.appreciate.androidbasetool.api.ApiHelper;
import be.appreciate.androidbasetool.contentproviders.LocationContentProvider;
import be.appreciate.androidbasetool.decorations.DividerDecoration;
import be.appreciate.androidbasetool.models.ClientLocation;
import be.appreciate.androidbasetool.models.api.Client;
import be.appreciate.androidbasetool.utils.Constants;
import be.appreciate.androidbasetool.utils.Observer;


/**
 * Created by thijscoorevits on 5/10/16.
 */

public class ClientListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener,
        ClientLocationAdapter.OnClientLocationClickListener, SearchView.OnQueryTextListener
{
    private SwipeRefreshLayout swipeRefreshLayout;
    private ClientLocationAdapter clientLocationAdapter;

    public static ClientListFragment newInstance()
    {
        Bundle args = new Bundle();

        ClientListFragment fragment = new ClientListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_client_list, container, false);

        this.swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout_dataList);
        RecyclerView recyclerViewDataList = (RecyclerView) view.findViewById(R.id.recyclerView_dataList);

        DividerDecoration dividerDecoration = new DividerDecoration(view.getContext());
        recyclerViewDataList.addItemDecoration(dividerDecoration);

        this.clientLocationAdapter = new ClientLocationAdapter();
        this.clientLocationAdapter.setClickListener(this);
        recyclerViewDataList.setAdapter(this.clientLocationAdapter);
        this.swipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.getLoaderManager().initLoader(Constants.LOADER_DATALIST, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        if(this.getContext() != null)
        {
            SearchManager searchManager = (SearchManager) this.getContext().getSystemService(Context.SEARCH_SERVICE);

            SearchView searchView = null;
            if (searchItem != null)
            {
                searchView = (SearchView) searchItem.getActionView();
            }
            if (searchView != null && ClientListFragment.this.getActivity() != null)
            {
                //searchView.setSearchableInfo(searchManager.getSearchableInfo(null));
                searchView.setOnQueryTextListener(this);
            }

        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {
        switch (id)
        {
            case Constants.LOADER_DATALIST:
                return new CursorLoader(this.getView().getContext(), LocationContentProvider.CONTENT_URI_CLIENT, null, null, null, null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data)
    {
        DatabaseUtils.dumpCursor(data);
        switch (loader.getId())
        {
            case Constants.LOADER_DATALIST:
                List<ClientLocation> locationList = ClientLocation.constructListFromCursor(data);
                ClientListFragment.this.clientLocationAdapter.setClientLocationList(locationList);
                Log.d("ClientListFragment", "" + locationList.size());
                if (ClientListFragment.this.swipeRefreshLayout.isRefreshing())
                    ClientListFragment.this.swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {

    }

    @Override
    public void onRefresh()
    {
        this.getData();
    }

    private void getData()
    {
        if (this.getContext() != null)
            ApiHelper.getData(this.getContext()).subscribe(this.dataObserver);
    }

    private Observer<List<Client>> dataObserver = new Observer<List<Client>>()
    {

        @Override
        public void onCompleted()
        {
            ClientListFragment.this.getLoaderManager().initLoader(Constants.LOADER_DATALIST, null, ClientListFragment.this);

        }

        @Override
        public void onError(Throwable e)
        {
            //Todo: show error dialog
            if (ClientListFragment.this.swipeRefreshLayout.isRefreshing())
                ClientListFragment.this.swipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText)
    {
        this.clientLocationAdapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public void onClientLocationClick(int locationId, String locationName, String clientName)
    {
        if (this.getContext() != null)
            this.startActivity(InstallationDocumentActivity.getIntent(this.getContext(), locationId, locationName, clientName));
    }
}
