package com.lethalminds.udonate.client.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.lethalminds.udonate.R;
import com.lethalminds.udonate.client.utilities.User;
import com.lethalminds.udonate.client.utilities.UserLocalStore;
import com.lethalminds.udonate.server.model.NodeRequests;
import com.lethalminds.udonate.server.model.callbacks.GetCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
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

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        View rView = inflater.inflate(R.layout.fragment_register, container, false);
        userLocalStore = new UserLocalStore(getContext());
        user = userLocalStore.getLoggedInUser();
        //Drop-down menu list of gender from string-array using spinner
        final Spinner regGender = (Spinner) rView.findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.gender, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        regGender.setAdapter(adapter);

        final EditText dob = (EditText) rView.findViewById(R.id.Reg_Dob);
        //Fetch the current calendar timings
        final Calendar cal = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);
        //set the default value with current calendar time
        dob.setText(userLocalStore.getDateFormat(year, (month + 1), day));
        dob.setOnClickListener(new View.OnClickListener() {
            //Using DatePickerDialog for selecting the calendar.
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener datepickerlistener = new DatePickerDialog.OnDateSetListener() {
                    //set the selected date value from the date picker dialog
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dob.setText(userLocalStore.getDateFormat(year, monthOfYear, dayOfMonth));
                    }
                };
                DatePickerDialog dobDiag = new DatePickerDialog(getActivity(), datepickerlistener, year, month, day);
                dobDiag.show();
            }
        });
        final EditText email = (EditText) rView.findViewById(R.id.Reg_Email);
        final EditText address = (EditText) rView.findViewById(R.id.Reg_address);
        final EditText reg_username = (EditText) rView.findViewById(R.id.Reg_UserName);
        final EditText reg_password1 = (EditText) rView.findViewById(R.id.Reg_pass1);
        final EditText reg_password2 = (EditText) rView.findViewById(R.id.Reg_pass2);
        final EditText reg_name = (EditText) rView.findViewById(R.id.reg_name);
        final Button reg = (Button) rView.findViewById(R.id.btn_Reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validate all the fields before storing in preferences
                if (validateAll(reg_username, reg_name, email, reg_password1, reg_password2, address, dob)) {
                    JSONObject userJson = new JSONObject();
                    try {
                        userJson.put("_id",email.getText().toString());
                        userJson.put("email",email.getText().toString());
                        userJson.put("username",reg_username.getText().toString());
                        userJson.put("firstname",reg_name.getText().toString());
                        userJson.put("address",address.getText().toString());
                        userJson.put("dob",dob.getText().toString());
                        userJson.put("gender",regGender.getSelectedItem().toString());
                        userJson.put("password",reg_password1.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    NodeRequests insertUser = new NodeRequests(getContext());
                    insertUser.addUserCollectionAsyncTask(userJson,new GetCallback() {
                        @Override
                        public <T> void done(T result) {
                            getFragmentManager().popBackStackImmediate();
                            Toast.makeText(getContext(),result.toString(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        return rView;
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

    //Function to validate all the passed fields, whether they are empty or specific condition
    public final boolean validateAll(EditText uName, EditText name, EditText email, EditText pass1, EditText pass2, EditText address, EditText dob) {
        boolean result = true;
        if (uName.getText().toString().trim().length() == 0) {
            uName.setError("Username is empty.");
            result = false;
        }
        if (email.getText().toString().trim().length() == 0) {
            email.setError("Email is empty.");
            result = false;
        } else if (!isValidEmail(email.getText().toString())) {
            email.setError("Invalid email address");
            result = false;
        }
        if (name.getText().toString().trim().length() == 0) {
            name.setError("Name is empty.");
            result = false;
        }
        if (address.getText().toString().trim().length() == 0) {
            address.setError("Address is empty.");
            result = false;
        }
        if (dob.getText().toString().trim().length() == 0) {
            dob.setError("DOB is empty.");
            result = false;
        }
        if (pass1.getText().toString().trim().length() == 0) {
            pass1.setError("Password is empty.");
            result = false;
        }
        if (pass2.getText().toString().trim().length() == 0) {
            pass2.setError("Password is empty.");
            result = false;
        } else if (!pass2.getText().toString().equals(pass1.getText().toString())) {
            pass2.setError("Passwords did not match.");
            pass1.setError("Passwords did not match.");
            result = false;
        }
        return result;
    }

    //Function to validate the email
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
