package com.lethalminds.udonate.client.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lethalminds.udonate.R;
import com.lethalminds.udonate.client.fragments.PaymentFragment;
import com.lethalminds.udonate.client.utilities.User;
import com.lethalminds.udonate.server.model.NodeRequests;
import com.lethalminds.udonate.server.model.callbacks.GetCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nishok on 4/4/2016.
 */
public class PaymentsRecyclerCardAdapter extends RecyclerView.Adapter<PaymentsRecyclerCardAdapter.ViewHolder> {
    private final PaymentFragment paymentFragment;
    ArrayList<JSONObject> items;

    public PaymentsRecyclerCardAdapter(PaymentFragment paymentFragment, ArrayList<JSONObject> items) {
        super();
        this.items = items;
        this.paymentFragment = paymentFragment;
    }

    @Override
    public PaymentsRecyclerCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_payment_cards, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PaymentsRecyclerCardAdapter.ViewHolder holder, final int position) {
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
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(items.get(position).toString());
                NodeRequests findModifyReq = new NodeRequests(paymentFragment.getContext());
                findModifyReq.obsoleteCardToUserCollectionAsyncTask(paymentFragment.user.username,
                        finalCardId, new GetCallback() {
                    @Override
                    public <T> void done(T items) {
                        removeItem(holder.getAdapterPosition());
                        paymentFragment.mAdapter.notifyItemRemoved(holder.getAdapterPosition());
                        paymentFragment.userLocalStore.storeUserData((User) items);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView cardNumber, expiry, name;
        public ImageView deleteBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            cardNumber = (TextView) itemView.findViewById(R.id.card_number);
            expiry = (TextView) itemView.findViewById(R.id.card_expiry);
            name = (TextView) itemView.findViewById(R.id.card_name);
            deleteBtn = (ImageView) itemView.findViewById(R.id.delete_img);
        }
    }

    public void removeItem(int position) {
        this.items.remove(position);
    }
}
