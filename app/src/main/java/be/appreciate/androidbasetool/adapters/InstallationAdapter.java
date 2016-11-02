package be.appreciate.androidbasetool.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import be.appreciate.androidbasetool.R;
import be.appreciate.androidbasetool.models.Installation;

/**
 * Created by thijscoorevits on 7/10/16.
 */

public class InstallationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Installation> installationsList;
    private OnInstallationClickListener clickListener;

    private static final int TYPE_INSTALLATIONS = 0;
    private static final int TYPE_EMPTY = 1;

    public void setClickListener(OnInstallationClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    public void setInstallationsList(List<Installation> installationsList)
    {
        this.installationsList = installationsList;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch (viewType)
        {
            case TYPE_INSTALLATIONS:
                View installationView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_installation, parent, false);
                return new InstallationsViewHolder(installationView);
            case TYPE_EMPTY:
                View emptyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_empty_installation, parent, false);
                return new EmptyViewHolder(emptyView);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if (this.getEmptyCount() == 0)
            return TYPE_INSTALLATIONS;
        else
            return TYPE_EMPTY;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof InstallationsViewHolder)
        {
            Installation installation = this.installationsList.get(position);
            ((InstallationsViewHolder) holder).bind(installation);
        }
    }

    @Override
    public int getItemCount()
    {
        return this.getInstallationCount() + this.getEmptyCount();
    }

    private int getInstallationCount()
    {
        return this.installationsList != null ? this.installationsList.size() : 0;
    }

    private int getEmptyCount()
    {
        return this.getInstallationCount() == 0 ? 1 : 0;
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder
    {
        public EmptyViewHolder(View view)
        {
            super(view);
        }
    }

    public class InstallationsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView textViewLocationName;
        private Installation installation;

        public InstallationsViewHolder(View itemView)
        {
            super(itemView);

            this.textViewLocationName = (TextView) itemView.findViewById(R.id.textView_installationName);

            itemView.setOnClickListener(this);
        }

        public void bind(Installation installation)
        {
            this.installation = installation;

            this.textViewLocationName.setText(this.installation.getName());
        }

        @Override
        public void onClick(View view)
        {
            InstallationAdapter.this.clickListener.onInstallationClick(this.installation.getId());
        }
    }

    public interface OnInstallationClickListener
    {
        void onInstallationClick(int installationId);
    }
}