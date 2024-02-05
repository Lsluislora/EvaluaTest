package com.example.myexampleevaluatest.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myexampleevaluatest.DabaBase.DBHelper;
import com.example.myexampleevaluatest.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeFragment extends Fragment {

    CardView crearTestCardView, userCardView, listTestCardView, userGuideCardView, searchTest;
    TextView textViewUserNameHome;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Crear una instancia de DBHelper para acceder a la base de datos
        DBHelper dbHelper = new DBHelper(getContext());

        // Recuperar datos de SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        // Verificar credenciales
        if (!username.isEmpty() && !password.isEmpty() && dbHelper.checkUserCredentials(username, password)) {
            // Obtener el nombre del usuario si las credenciales son válidas
            String userName = dbHelper.getUserName(username, password);

            // Mostrar el nombre del usuario en un TextView
            TextView textView = view.findViewById(R.id.textViewUserNameHome);
            textView.setText(userName);

            // Registrar el nombre del usuario en el log
            Log.d("DBHelper", "Nombre del usuario: " + userName);
            Log.d("HomeFragment", "onCreateView - Checking Arguments");
        } else {
            // Mostrar un mensaje de error en el log si las credenciales son incorrectas o no están almacenadas
            Log.e("DBHelper", "Credenciales incorrectas o no almacenadas.");
        }

        // Inicializar CardViews
        crearTestCardView = view.findViewById(R.id.folderCardDoTest);
        userCardView = view.findViewById(R.id.userCard);
        listTestCardView = view.findViewById(R.id.listTestCard);
        userGuideCardView = view.findViewById(R.id.userGuideCard);
        searchTest = view.findViewById(R.id.searchCard);
        textViewUserNameHome = view.findViewById(R.id.textViewUserNameHome);



        // Verifica si hay datos para mostrar en la segunda línea
        if (!textViewUserNameHome.getText().toString().isEmpty()) {
            // Hay datos, muestra la segunda línea y establece el texto
            textViewUserNameHome.setVisibility(View.VISIBLE);
        } else {
            // No hay datos, oculta la segunda línea
            textViewUserNameHome.setVisibility(View.GONE);
        }



        // se configuran los clics en los CardViews para navegar a diferentes fragmentos
        crearTestCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navegar al fragmento CreaTestFragment
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.creaTestFragment);
            }
        });

        userCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navegar al fragmento UserPageDataFragment
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.userPageDataFragment);
            }
        });

        listTestCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navegar al fragmento VerTestFragment
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.verTestFragment);
            }
        });

        userGuideCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navegar al fragmento UserGuideFragment
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.userGuideFragment);
            }
        });

        searchTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navegar al fragmento VerTestFragment
                NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.verTestFragment);

                // Abrir el teclado automáticamente
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });


        return view;
    }

}