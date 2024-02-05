package com.example.myexampleevaluatest.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.myexampleevaluatest.Adapter.TestAdapter;
import com.example.myexampleevaluatest.DabaBase.DBHelper;
import com.example.myexampleevaluatest.R;
import com.example.myexampleevaluatest.data.TestData;

import java.util.ArrayList;
import java.util.List;


public class VerTestFragment extends Fragment {

    //declaracion de variables
    DBHelper dbHelper;
    TestAdapter testAdapter;
    EditText editTextSerch;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ver_test, container, false);

        // Inicializar DBHelper y obtener la lista de pruebas
        dbHelper = new DBHelper(getActivity());
        ArrayList<TestData> testModalArrayList = dbHelper.readTests();

        // Configurar el RecyclerView
        RecyclerView testsRecyclerView = view.findViewById(R.id.idRVAllTest);
        testsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Inicializar el adaptador con la lista de pruebas
        testAdapter = new TestAdapter(testModalArrayList, getActivity());
        testsRecyclerView.setAdapter(testAdapter);

        // Configurar el EditText y el TextWatcher para la búsqueda
        EditText editTextSearch = view.findViewById(R.id.editTextSearch);

        //obtenemos el focus del buscador
        editTextSerch = view.findViewById(R.id.editTextSearch);
        editTextSerch.requestFocus();

        if (testModalArrayList != null && !testModalArrayList.isEmpty()) {
            // Configurar el RecyclerView
            testsRecyclerView = view.findViewById(R.id.idRVAllTest);
            testsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            testsRecyclerView.setAdapter(testAdapter);
        } else {
            // Manejar el caso en que no hay datos disponibles, mostramos un error en el Log
            Log.e("VerTestFragment", "No hay datos disponibles en testModalArrayList");
        }



        // Configura el OnItemClickListener para manejar clics en los elementos
        testAdapter.setOnItemClickListener(new TestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Obtén el objeto TestData haciendo clic en la posición del adaptador
                TestData clickedTest = testAdapter.getTestList().get(position);

                // Abre el fragmento utilizando el método openFragmentForTest
                testAdapter.openFragmentForTest(getContext(), clickedTest);
            }
        });

        // Agregar un TextWatcher al EditText para realizar búsquedas dinámicas
        editTextSearch.addTextChangedListener(new TextWatcher() {
            // Este método se llama antes de que el texto cambie
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No se realiza ninguna acción antes de que el texto cambie
            }

            // Este método se llama cuando el texto en el EditText cambia
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Obtener el término de búsqueda y eliminar espacios en blanco al principio y al final
                String searchTerm = charSequence.toString().trim();

                // Llamar a la función para realizar la búsqueda en la base de datos
                searchInDatabase(searchTerm);
            }

            // Este método se llama después de que el texto cambió
            @Override
            public void afterTextChanged(Editable editable) {
                // No se realiza ninguna acción después de que el texto cambió
            }
        });

        return view;
    }

    private void searchInDatabase(String searchTerm) {
        // Mostrar el término de búsqueda en el registro
        Log.d("SearchTerm", "Search term: " + searchTerm);

        // Realizar la búsqueda en la base de datos utilizando el término de búsqueda
        List<TestData> searchResults = dbHelper.searchTests(searchTerm);

        // Actualizar la interfaz de usuario con los resultados de la búsqueda
        updateUIForTests(searchResults);
    }

    // Método para actualizar la interfaz de usuario con los resultados de la búsqueda
    private void updateUIForTests(List<TestData> searchResults) {
        // Actualizar la lista de resultados en el adaptador
        testAdapter.setTestList(searchResults);
        testAdapter.notifyDataSetChanged();
    }



}