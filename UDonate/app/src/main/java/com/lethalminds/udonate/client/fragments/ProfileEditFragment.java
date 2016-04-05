package com.lethalminds.udonate.client.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileEditFragment extends Fragment {
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

    public ProfileEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileEditFragment newInstance(String param1, String param2) {
        ProfileEditFragment fragment = new ProfileEditFragment();
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
        View pEditView = inflater.inflate(R.layout.fragment_profile_edit, container, false);
        userLocalStore = new UserLocalStore(getContext());
        user = userLocalStore.getLoggedInUser();
        final EditText uname = (EditText) pEditView.findViewById(R.id.edit_uname);
        uname.setText(user.username);
        final EditText email = (EditText) pEditView.findViewById(R.id.edit_email);
        email.setText(user.email);
        final EditText address = (EditText) pEditView.findViewById(R.id.edit_address);
        address.setText(user.address);
        final EditText dob = (EditText) pEditView.findViewById(R.id.edit_dob);
        dob.setText(user.dob);
        final String[] date = user.dob.split("/");
        dob.setOnClickListener(new View.OnClickListener() {
            //Using DatePickerDialog for selecting the calendar.
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener datepickerlistener = new DatePickerDialog.OnDateSetListener() {
                    //set the selected date value from the date picker dialog
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dob.setText(new StringBuilder()
                                .append(monthOfYear + 1).append("/").append(dayOfMonth).append("/")
                                .append(year));
                    }
                };
                DatePickerDialog dobDiag = new DatePickerDialog(getContext(), datepickerlistener, Integer.parseInt(date[2]), Integer.parseInt(date[0]), Integer.parseInt(date[1]));
                dobDiag.show();
            }
        });
        final EditText pass = (EditText) pEditView.findViewById(R.id.edit_pass);
        pass.setText(user.password);

        Button submit = (Button) pEditView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NodeRequests findModifyReq = new NodeRequests(uname.getText().toString(),pass.getText().toString()
                ,email.getText().toString(),address.getText().toString(),dob.getText().toString(),getContext());
                findModifyReq.updateBasicUserCollectionAsyncTask(new GetCallback() {
                    @Override
                    public <T> void done(T items) {
                        userLocalStore.storeUserData((User) items);
                        getFragmentManager().popBackStackImmediate();
                    }
                });
            }
        });
        return pEditView;
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
