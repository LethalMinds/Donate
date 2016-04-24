package com.lethalminds.udonate.client.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.lethalminds.udonate.R;
import com.lethalminds.udonate.client.utilities.User;
import com.lethalminds.udonate.client.utilities.UserLocalStore;
import com.lethalminds.udonate.server.model.NodeRequests;
import com.lethalminds.udonate.server.model.callbacks.GetCallback;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    UserLocalStore userLocalStore;
    User user;

    private OnFragmentInteractionListener mListener;

    public AddCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCardFragment newInstance(String param1, String param2) {
        AddCardFragment fragment = new AddCardFragment();
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
        View aView = inflater.inflate(R.layout.fragment_add_card, container, false);
        userLocalStore = new UserLocalStore(getContext());
        user = userLocalStore.getLoggedInUser();
        final EditText cardnumber = (EditText) aView.findViewById(R.id.card_number);
        final EditText cardName = (EditText) aView.findViewById(R.id.name_Card);
        final EditText cvv = (EditText) aView.findViewById(R.id.cvv_Card);
        final EditText expiry = (EditText) aView.findViewById(R.id.expiry_Card);
        final Button save = (Button) aView.findViewById(R.id.save_card);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NodeRequests findUserAndAddCard = new NodeRequests(getContext());
                findUserAndAddCard.addCardToUserCollectionAsyncTask(user.username,cardnumber.getText().toString(), cardName.getText().toString(),
                        cvv.getText().toString(), expiry.getText().toString(), new GetCallback() {
                    @Override
                    public <T> void done(T items) {
                        userLocalStore.storeUserData((User) items);
                        getFragmentManager().popBackStackImmediate();
                    }
                });
            }
        });


        return aView;
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
