package com.lethalminds.udonate.client.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lethalminds.udonate.R;
import com.lethalminds.udonate.client.fragments.DetailsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nishok on 4/4/2016.
 */
public class NewsRecyclerCardAdapter extends RecyclerView.Adapter<NewsRecyclerCardAdapter.ViewHolder> {
    ArrayList<JSONObject> items;
    final FragmentActivity activity;
    final FragmentManager fragmentManager;

    public NewsRecyclerCardAdapter(FragmentActivity activity, ArrayList<JSONObject> items) {
        super();
        this.activity = activity;
        this.items = items;
        fragmentManager = activity.getSupportFragmentManager();
    }

    @Override
    public NewsRecyclerCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_transaction_cards, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NewsRecyclerCardAdapter.ViewHolder holder, final int position) {
        try {
            holder.header.setText(items.get(position).get("receiver_id").toString());
            holder.briefInfo.setText(items.get(position).get("category") + " : " + items.get(position).get("details").toString());
        } catch (JSONException e) {
            //Do nothing
        }
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment detailsFrag = new DetailsFragment();
                Bundle jsonBundle = new Bundle();
                jsonBundle.putString("json", items.get(position).toString());
                jsonBundle.putString("fragID", "news");
                detailsFrag.setArguments(jsonBundle);
                fragmentTransaction.replace(R.id.frame_container, detailsFrag).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView header, briefInfo, details;
        public ImageView donorImg;

        public ViewHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.trans_header);
            briefInfo = (TextView) itemView.findViewById(R.id.status);
            details = (TextView) itemView.findViewById(R.id.more_details);
            donorImg = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
