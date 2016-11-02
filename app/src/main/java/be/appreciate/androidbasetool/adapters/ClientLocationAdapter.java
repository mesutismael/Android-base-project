package be.appreciate.androidbasetool.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import be.appreciate.androidbasetool.R;
import be.appreciate.androidbasetool.models.ClientLocation;

/**
 * Created by thijscoorevits on 7/10/16.
 */

public class ClientLocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable
{
    private List<ClientLocation> clientLocationList;
    private List<ClientLocation> filterClientLocationList;
    private OnClientLocationClickListener clickListener;

    private static final int TYPE_CLIENT_LOCATIONS = 0;
    private static final int TYPE_EMPTY = 1;

    public void setClickListener(OnClientLocationClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    public void setClientLocationList(List<ClientLocation> clientLocationList)
    {
        this.clientLocationList = clientLocationList;
        this.filterClientLocationList = clientLocationList;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch (viewType)
        {
            case TYPE_CLIENT_LOCATIONS:
                View clientLocationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_client_location, parent, false);
                return new ClientLocationViewHolder(clientLocationView);
            case TYPE_EMPTY:
                View emptyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_empty_client_location, parent, false);
                return new EmptyViewHolder(emptyView);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if (this.getEmptyCount() == 0)
            return TYPE_CLIENT_LOCATIONS;
        else
            return TYPE_EMPTY;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof ClientLocationViewHolder)
        {
            ClientLocation clientLocation = this.filterClientLocationList.get(position);
            ((ClientLocationViewHolder) holder).bind(clientLocation);
        }
    }

    @Override
    public int getItemCount()
    {
        return this.getClientLocationCount() + this.getEmptyCount();
    }

    private int getClientLocationCount()
    {
        return this.filterClientLocationList != null ? this.filterClientLocationList.size() : 0;
    }

    private int getEmptyCount()
    {
        return this.getClientLocationCount() == 0 ? 1 : 0;
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder
    {
        public EmptyViewHolder(View view)
        {
            super(view);
        }
    }

    public class ClientLocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView textViewLocationName;
        private TextView textViewClientNameAndCity;
        private TextView textViewAddress;
        private ClientLocation clientLocation;

        public ClientLocationViewHolder(View itemView)
        {
            super(itemView);

            this.textViewLocationName = (TextView) itemView.findViewById(R.id.textView_locationName);
            this.textViewClientNameAndCity = (TextView) itemView.findViewById(R.id.textView_clientnameAndCity);
            this.textViewAddress = (TextView) itemView.findViewById(R.id.textView_address);

            itemView.setOnClickListener(this);
        }

        public void bind(ClientLocation clientLocation)
        {
            this.clientLocation = clientLocation;

            this.textViewLocationName.setText(this.clientLocation.getName());
            this.textViewClientNameAndCity.setText(this.itemView.getContext().getString(R.string.list_client_city, this.clientLocation.getClientName(), this.clientLocation.getCity()));
            this.textViewAddress.setText(this.itemView.getContext().getString(R.string.list_address, this.clientLocation.getStreet(), this.clientLocation.getZipcode(), this.clientLocation.getCity()));
        }

        @Override
        public void onClick(View view)
        {
            ClientLocationAdapter.this.clickListener.onClientLocationClick(this.clientLocation.getId(), this.clientLocation.getName(), this.clientLocation.getClientName());
        }
    }

    public interface OnClientLocationClickListener
    {
        void onClientLocationClick(int locationId, String locationName, String clientName);
    }

    @Override
    public Filter getFilter()
    {
        Filter filter = new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                String filterString = constraint.toString().toLowerCase();

                FilterResults results = new FilterResults();

                final List<ClientLocation> list = ClientLocationAdapter.this.clientLocationList;

                int count = list.size();
                final ArrayList<ClientLocation> startsWith = new ArrayList<>();
                final ArrayList<ClientLocation> contains = new ArrayList<>();

                ClientLocation filterableClientLocations;

                for (int i = 0; i < count; i++)
                {
                    filterableClientLocations = list.get(i);
                    if (filterableClientLocations.getClientName().toLowerCase().startsWith(filterString) || filterableClientLocations.getZipcode().toLowerCase().startsWith(filterString) || filterableClientLocations.getStreet().toLowerCase().startsWith(filterString) || filterableClientLocations.getName().toLowerCase().startsWith(filterString) || filterableClientLocations.getCity().toLowerCase().startsWith(filterString))
                    {
                        startsWith.add(filterableClientLocations);
                    } else if (filterableClientLocations.getCity().toLowerCase().contains(filterString) || filterableClientLocations.getZipcode().toLowerCase().contains(filterString) || filterableClientLocations.getStreet().toLowerCase().contains(filterString)|| filterableClientLocations.getName().toLowerCase().contains(filterString) || filterableClientLocations.getCity().toLowerCase().contains(filterString))
                    {
                        contains.add(filterableClientLocations);
                    }
                }

                startsWith.addAll(contains);

                results.values = startsWith;
                results.count = startsWith.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                ClientLocationAdapter.this.filterClientLocationList = (ArrayList<ClientLocation>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
