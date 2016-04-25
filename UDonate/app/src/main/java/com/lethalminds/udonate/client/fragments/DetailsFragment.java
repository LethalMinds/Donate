package com.lethalminds.udonate.client.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lethalminds.udonate.R;
import com.lethalminds.udonate.client.utilities.User;
import com.lethalminds.udonate.client.utilities.UserLocalStore;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    UserLocalStore userLocalStore;
    User user;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2, mFragID;
    private JSONObject item;

    private OnFragmentInteractionListener mListener;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
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
                item = new JSONObject(getArguments().getString("json"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View detailsView = inflater.inflate(R.layout.fragment_details, container, false);
        ImageView img = (ImageView) detailsView.findViewById(R.id.img_detail);
        TextView name = (TextView) detailsView.findViewById(R.id.name_detail);
        TextView status = (TextView) detailsView.findViewById(R.id.status_detail);
        TextView details = (TextView) detailsView.findViewById(R.id.detail_detail);
        TextView donate = (TextView) detailsView.findViewById(R.id.donate_btn);
        CardView donateCard = (CardView) detailsView.findViewById(R.id.donate_card);

        switch (mFragID) {
            case "donate":
                try {
                    name.setText((String) item.get("receiver_id"));
                    status.setText((String) item.get("category"));
                    details.setText((String) item.get("details"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                donateCard.setVisibility(View.VISIBLE);
                donateCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        ChoosePaymentFragment chPayFrag = new ChoosePaymentFragment();
                        Bundle jsonBundle = new Bundle();
                        jsonBundle.putString("json", item.toString());
                        jsonBundle.putString("fragID","details");
                        chPayFrag.setArguments(jsonBundle);
                        fragmentTransaction.replace(R.id.frame_container, chPayFrag).addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
                break;
            case "news":
                try {
                    name.setText((String) item.get("receiver_id"));
                    status.setText((String) item.get("category"));
                    details.setText((String) item.get("details"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                donateCard.setVisibility(View.GONE);
                break;
            case "donationTransaction":
                try {
                    name.setText((String) item.get("receiver_id"));
                    status.setText((String) item.get("status"));
                    String dtString = "Payment : " + ((JSONObject) item.get("payment_info")).get("payment") + "\n"
                            + "Transaction details : " + ((JSONObject) item.get("payment_info")).get("details");
                    details.setText(dtString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                donateCard.setVisibility(View.GONE);
                break;
        }

        return detailsView;
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
