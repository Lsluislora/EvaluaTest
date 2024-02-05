package com.example.myexampleevaluatest.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myexampleevaluatest.R;


public class UserGuideFragment extends Fragment {

    // Método estático para crear una nueva instancia del fragmento
    public static UserGuideFragment newInstance(String param1, String param2) {
        UserGuideFragment fragment = new UserGuideFragment();
        Bundle args = new Bundle();
        // Puedes realizar operaciones adicionales aquí si es necesario
        return fragment;
    }

    // Método llamado al crear el fragmento
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Puedes realizar operaciones adicionales aquí si es necesario
    }

    // Método llamado al crear la vista del fragmento
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño para este fragmento
        View view = inflater.inflate(R.layout.fragment_user_guide, container, false);
        // Puedes realizar operaciones adicionales aquí si es necesario

        return view;
    }
}