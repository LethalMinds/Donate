package com.lethalminds.udonate.client.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lethalminds.udonate.R;
import com.lethalminds.udonate.client.adapters.DonationRecyclerCardAdapter;
import com.lethalminds.udonate.client.utilities.User;
import com.lethalminds.udonate.client.utilities.UserLocalStore;
import com.lethalminds.udonate.server.model.NodeRequests;
import com.lethalminds.udonate.server.model.callbacks.GetCallback;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DonateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DonateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView mEducationRecyclerView;
    RecyclerView mHealthRecyclerView;
    RecyclerView mPovertyRecyclerView;
    LinearLayoutManager mLayoutManager;
    UserLocalStore userLocalStore;
    User user;
    DonationRecyclerCardAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    public DonateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DonateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DonateFragment newInstance(String param1, String param2) {
        DonateFragment fragment = new DonateFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View dView = inflater.inflate(R.layout.fragment_donate, container, false);
        mEducationRecyclerView = (RecyclerView) dView.findViewById(R.id.recycler_view_education_donation);
        mEducationRecyclerView.setHasFixedSize(true);

        mHealthRecyclerView = (RecyclerView) dView.findViewById(R.id.recycler_view_health_donation);
        mHealthRecyclerView.setHasFixedSize(true);

        mPovertyRecyclerView = (RecyclerView) dView.findViewById(R.id.recycler_view_poverty_donation);
        mPovertyRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mEducationRecyclerView.setLayoutManager(mLayoutManager);

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mHealthRecyclerView.setLayoutManager(mLayoutManager);

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mPovertyRecyclerView.setLayoutManager(mLayoutManager);

        userLocalStore = new UserLocalStore(getContext());
        user = userLocalStore.getLoggedInUser();

        getPopulateEducations();
        getPopulateHealth();
        getPopulatePoverty();

        return dView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void getPopulateEducations() {
        NodeRequests serverRequest = new NodeRequests("education",  this.getActivity());
        serverRequest.fetchDonationCollectionByCategoryAsyncTask(new GetCallback() {
            @Override
            public <T> void done(T items) {
                populateEducations((ArrayList<JSONObject>) items);
            }
        });
    }

    private void populateEducations(ArrayList<JSONObject> courses) {
        mAdapter = new DonationRecyclerCardAdapter(this.getActivity(), courses);
        mEducationRecyclerView.setAdapter(mAdapter);
    }

    private void getPopulateHealth() {
        NodeRequests serverRequest = new NodeRequests("health", this.getActivity());
        serverRequest.fetchDonationCollectionByCategoryAsyncTask(new GetCallback() {
            @Override
            public <T> void done(T items) {
                populateHealth((ArrayList<JSONObject>) items);
            }
        });
    }

    private void populateHealth(ArrayList<JSONObject> courses) {
        mAdapter = new DonationRecyclerCardAdapter(this.getActivity(), courses);
        mHealthRecyclerView.setAdapter(mAdapter);
    }

    private void getPopulatePoverty() {
        NodeRequests serverRequest = new NodeRequests("poverty", this.getActivity());
        serverRequest.fetchDonationCollectionByCategoryAsyncTask(new GetCallback() {
            @Override
            public <T> void done(T items) {
                populatePoverty((ArrayList<JSONObject>) items);
            }
        });
    }

    private void populatePoverty(ArrayList<JSONObject> courses) {
        mAdapter = new DonationRecyclerCardAdapter(this.getActivity(), courses);
        mPovertyRecyclerView.setAdapter(mAdapter);
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
