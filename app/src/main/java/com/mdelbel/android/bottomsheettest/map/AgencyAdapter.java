package com.mdelbel.android.bottomsheettest.map;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mdelbel.android.bottomsheettest.R;

class AgencyAdapter extends RecyclerView.Adapter<AgencyAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agency, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText("Sucursal " + position);
    }

    @Override
    public int getItemCount() {
        return 10;
    }



    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_agency_title);
        }
    }
}
