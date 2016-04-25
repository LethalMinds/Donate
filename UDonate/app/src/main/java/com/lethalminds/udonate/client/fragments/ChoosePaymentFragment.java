package com.lethalminds.udonate.client.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.lethalminds.udonate.R;
import com.lethalminds.udonate.client.adapters.PaymentsHorizontalRecyclerCardAdapter;
import com.lethalminds.udonate.client.utilities.User;
import com.lethalminds.udonate.client.utilities.UserLocalStore;
import com.lethalminds.udonate.server.model.NodeRequests;
import com.lethalminds.udonate.server.model.callbacks.GetCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChoosePaymentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChoosePaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChoosePaymentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2, mFragID;
    private JSONObject rItem;

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    public UserLocalStore userLocalStore;
    public User user;
    public PaymentsHorizontalRecyclerCardAdapter mAdapter;
    public ArrayList<JSONObject> cardList;

    private OnFragmentInteractionListener mListener;

    public ChoosePaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChoosePaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChoosePaymentFragment newInstance(String param1, String param2) {
        ChoosePaymentFragment fragment = new ChoosePaymentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mFragID = getArguments().getString("fragID");
            try {
                rItem = new JSONObject(getArguments().getString("json"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View chPayView = inflater.inflate(R.layout.fragment_choose_payment, container, false);
        mRecyclerView = (RecyclerView) chPayView.findViewById(R.id.recycler_view_payments_horizontal);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        userLocalStore = new UserLocalStore(getContext());
        user = userLocalStore.getLoggedInUser();
        cardList = new ArrayList<JSONObject>();
        try {
            JSONArray cardsArray = (JSONArray) (new JSONObject(user.userJSON)).get("cards");
            //Display only active cards
            for (int i = 0; i < cardsArray.length(); i++) {
                if (cardsArray.getJSONObject(i).get("status").equals("active")) {
                    cardList.add(cardsArray.getJSONObject(i));
                }
            }
            mAdapter = new PaymentsHorizontalRecyclerCardAdapter(this, cardList);
            mRecyclerView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final EditText amount = (EditText) chPayView.findViewById(R.id.payment_amount);
        final EditText message = (EditText) chPayView.findViewById(R.id.payment_detail);
        CardView pay = (CardView) chPayView.findViewById(R.id.pay_card);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject paymentInfo = new JSONObject();
                try {
                    paymentInfo.put("payment", amount.getText().toString());
                    paymentInfo.put("message", message.getText().toString());
                    paymentInfo.put("card_id", mAdapter.getSelectedItem().get("_id").toString());
                    paymentInfo.put("details", "Transaction Initiated");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                NodeRequests addTransaction = new NodeRequests(getContext());
                addTransaction.addTransactionCollectionAsyncTask(rItem , user.username,
                        paymentInfo, new GetCallback() {
                            @Override
                            public <T> void done(T items) {
                                userLocalStore.storeUserData((User) items);
                                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            }
                        });
            }
        });
        return chPayView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
