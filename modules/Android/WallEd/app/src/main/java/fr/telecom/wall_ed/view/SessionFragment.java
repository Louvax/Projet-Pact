package fr.telecom.wall_ed.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.telecom.wall_ed.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SessionFragment extends Fragment {

    public SessionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_session, container, false);
    }
}
