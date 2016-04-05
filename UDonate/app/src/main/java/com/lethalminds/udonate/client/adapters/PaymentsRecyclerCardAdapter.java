package com.lethalminds.udonate.client.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lethalminds.udonate.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nishok on 4/4/2016.
 */
public class PaymentsRecyclerCardAdapter extends RecyclerView.Adapter<PaymentsRecyclerCardAdapter.ViewHolder>{
    JSONArray items;

    public PaymentsRecyclerCardAdapter(JSONArray items) {
        super();
        this.items = items;
    }
    @Override
    public PaymentsRecyclerCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_payment_cards, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PaymentsRecyclerCardAdapter.ViewHolder holder, int position) {
        try {
            holder.cardNumber.setText(((JSONObject)items.get(position)).get("number").toString());
            holder.expiry.setText(((JSONObject)items.get(position)).get("expiry").toString());
            holder.name.setText(((JSONObject)items.get(position)).get("name").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView cardNumber, expiry, name;

        public ViewHolder(View itemView) {
            super(itemView);
            cardNumber = (TextView) itemView.findViewById(R.id.card_number);
            expiry = (TextView) itemView.findViewById(R.id.card_expiry);
            name = (TextView) itemView.findViewById(R.id.card_name);
        }
    }
}
