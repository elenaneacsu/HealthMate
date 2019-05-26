package com.elenaneacsu.healthmate.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elenaneacsu.healthmate.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private List<String> mStrings;

    public ListAdapter(List<String> strings) {
        this.mStrings = strings;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.string_item, viewGroup, false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder listViewHolder, int i) {
        String string = mStrings.get(i);
        if(string!=null) {
            listViewHolder.mTextView.setText(string);
        }
    }

    @Override
    public int getItemCount() {
        if(mStrings == null) {
            return 0;
        } else {
            return mStrings.size();
        }
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.textview);
        }
    }
}
