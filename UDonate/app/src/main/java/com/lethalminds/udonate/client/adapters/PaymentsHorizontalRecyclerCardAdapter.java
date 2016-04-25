package com.lethalminds.udonate.client.adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lethalminds.udonate.R;
import com.lethalminds.udonate.client.fragments.ChoosePaymentFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nishok on 4/4/2016.
 */
public class PaymentsHorizontalRecyclerCardAdapter extends RecyclerView.Adapter<PaymentsHorizontalRecyclerCardAdapter.ViewHolder> {
    private final ChoosePaymentFragment paymentFragment;
    public ArrayList<JSONObject> items;
    public int selectedPosition = -1;
    View selectedView = null;

    public PaymentsHorizontalRecyclerCardAdapter(ChoosePaymentFragment paymentFragment, ArrayList<JSONObject> items) {
        super();
        this.items = items;
        this.paymentFragment = paymentFragment;
    }

    @Override
    public PaymentsHorizontalRecyclerCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_payment_cards, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PaymentsHorizontalRecyclerCardAdapter.ViewHolder holder, final int position) {
        String cardId = null;
        try {
            holder.cardNumber.setText(items.get(position).get("number").toString());
            holder.expiry.setText(items.get(position).get("expiry").toString());
            holder.name.setText(items.get(position).get("name").toString());
            cardId = items.get(position).get("_id").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String finalCardId = cardId;
        holder.container.setOnClickListener(onClickListener(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public JSONObject getSelectedItem() {
        return selectedPosition != -1 ? items.get(selectedPosition) : null;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView cardNumber, expiry, name;
        public ImageView deleteBtn;
        public CardView container;

        public ViewHolder(View itemView) {
            super(itemView);
            cardNumber = (TextView) itemView.findViewById(R.id.card_number);
            expiry = (TextView) itemView.findViewById(R.id.card_expiry);
            name = (TextView) itemView.findViewById(R.id.card_name);
            deleteBtn = (ImageView) itemView.findViewById(R.id.delete_img);
            container = (CardView) itemView.findViewById(R.id.card_view);
            deleteBtn.setVisibility(View.GONE);
        }
    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedView != null){
                    selectedView.setBackgroundColor(android.R.color.transparent);
                }
                v.setBackgroundColor(R.color.colorPrimaryDark);
                selectedPosition = position;
                selectedView = v;
            }
        };
    }

}
