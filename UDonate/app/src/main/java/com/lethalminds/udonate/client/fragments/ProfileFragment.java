package com.lethalminds.udonate.client.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lethalminds.udonate.R;
import com.lethalminds.udonate.client.activities.MainActivity;
import com.lethalminds.udonate.client.adapters.NavDrawerListAdapter;
import com.lethalminds.udonate.client.utilities.NavDrawerItem;
import com.lethalminds.udonate.client.utilities.User;
import com.lethalminds.udonate.client.utilities.UserLocalStore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String[] navMenuTitles;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private TypedArray navMenuIcons;
    private NavDrawerListAdapter adapter;
    private ListView mDrawerList;
    UserLocalStore userLocalStore;
    User user;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View pView = inflater.inflate(R.layout.fragment_profile, container, false);
        userLocalStore = new UserLocalStore(getContext());
        user = userLocalStore.getLoggedInUser();
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        navDrawerItems = new ArrayList<NavDrawerItem>();
        NavDrawerItem logoutItem = new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1));
        navDrawerItems.add(logoutItem);
        navMenuIcons.recycle();
        //Logout button
        adapter = new NavDrawerListAdapter(getContext(), navDrawerItems);
        mDrawerList = (ListView) pView.findViewById(R.id.profile_list);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        userLocalStore.setUserLoggedIn(false);
                        userLocalStore.clearUserData();
                        user = null;
                        Intent mainIntent = new Intent(getContext(), MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                        break;
                    default:
                        break;
                }
            }
        });


        TextView profileName = (TextView) pView.findViewById(R.id.profile_name);
        profileName.setText(user.username);
        TextView profileEmail = (TextView) pView.findViewById(R.id.profile_email);
        profileEmail.setText(user.email);

        ImageView editImg = (ImageView) pView.findViewById(R.id.edit_img);
        editImg.setOnClickListener(this);

        TextView viewTransactions = (TextView) pView.findViewById(R.id.view_transaction);
        viewTransactions.setOnClickListener(this);

        TextView viewPayments = (TextView) pView.findViewById(R.id.view_payments);
        viewPayments.setOnClickListener(this);

        TextView addCard = (TextView) pView.findViewById(R.id.addcard);
        addCard.setOnClickListener(this);

        return pView;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_img:
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, new ProfileEditFragment()).addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.view_transaction:
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, new TransactionFragment()).addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.view_payments:
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, new PaymentFragment()).addToBackStack(null);
                fragmentTransaction.commit();
                break;
            case R.id.addcard:
                break;
            default:
                break;
        }
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
