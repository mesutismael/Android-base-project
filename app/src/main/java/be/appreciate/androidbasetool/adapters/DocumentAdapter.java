package be.appreciate.androidbasetool.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import be.appreciate.androidbasetool.R;
import be.appreciate.androidbasetool.models.Document;


/**
 * Created by thijscoorevits on 7/10/16.
 */

public class DocumentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Document> documentList;
    private OnDocumentClickListener clickListener;

    private static final int TYPE_DOCUMENT = 0;
    private static final int TYPE_EMPTY = 1;

    public void setClickListener(OnDocumentClickListener clickListener)
    {
        this.clickListener = clickListener;
    }

    public void setDocumentList(List<Document> documentList)
    {
        this.documentList = documentList;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch (viewType)
        {
            case TYPE_DOCUMENT:
                View todoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_document, parent, false);
                return new DocumentViewHolder(todoView);
            case TYPE_EMPTY:
                View emptyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_empty_document, parent, false);
                return new EmptyViewHolder(emptyView);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if (this.getEmptyCount() == 0)
            return TYPE_DOCUMENT;
        else
            return TYPE_EMPTY;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if (holder instanceof DocumentViewHolder)
        {
            Document document = this.documentList.get(position);
            ((DocumentViewHolder) holder).bind(document);
        }
    }

    @Override
    public int getItemCount()
    {
        return this.getTodoCount() + this.getEmptyCount();
    }

    private int getTodoCount()
    {
        return this.documentList != null ? this.documentList.size() : 0;
    }

    private int getEmptyCount()
    {
        return this.getTodoCount() == 0 ? 1 : 0;
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder
    {
        public EmptyViewHolder(View view)
        {
            super(view);
        }
    }

    public class DocumentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView textViewDocument;
        private Document document;

        public DocumentViewHolder(View itemView)
        {
            super(itemView);

            this.textViewDocument = (TextView) itemView.findViewById(R.id.textView_documentName);

            itemView.setOnClickListener(this);
        }

        public void bind(Document document)
        {
            this.document = document;

            this.textViewDocument.setText(this.document.getName());
        }

        @Override
        public void onClick(View view)
        {
            DocumentAdapter.this.clickListener.onDocumentClick(this.document);
        }
    }

    public interface OnDocumentClickListener
    {
        void onDocumentClick(Document document);
    }
}