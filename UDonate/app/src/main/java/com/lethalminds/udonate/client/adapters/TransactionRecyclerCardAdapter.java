package com.lethalminds.udonate.client.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.lethalminds.udonate.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by Nishok on 4/4/2016.
 */
public class TransactionRecyclerCardAdapter extends RecyclerView.Adapter<TransactionRecyclerCardAdapter.ViewHolder>{
    ArrayList<JSONObject> items;

    public TransactionRecyclerCardAdapter(ArrayList<JSONObject> items) {
        super();
        this.items = items;
    }
    @Override
    public TransactionRecyclerCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_transaction_cards, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TransactionRecyclerCardAdapter.ViewHolder holder, int position) {
        try {
            holder.header.setText(items.get(position).get("receiver_id").toString());
            holder.status.setText(holder.status.getText().toString() + items.get(position).get("status").toString());
        } catch (JSONException e) {
            //Do nothing
        }
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView header, status, details;
        public ImageView donorImg;

        public ViewHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.trans_header);
            status = (TextView) itemView.findViewById(R.id.status);
            details = (TextView) itemView.findViewById(R.id.more_details);
            donorImg = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
