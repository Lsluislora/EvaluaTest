package com.example.myexampleevaluatest.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.myexampleevaluatest.Adapter.QuestionsSelectMultAdapter;
import com.example.myexampleevaluatest.DabaBase.DBHelper;
import com.example.myexampleevaluatest.R;
import com.example.myexampleevaluatest.data.SelecMultTestData;

import java.util.ArrayList;

public class SeleccionMultipleFragment extends Fragment {

    //declaramos las variables
    private RecyclerView questionsRV_SM;
    private ImageButton nextButton, backButton;
    private Button btnShowResultsSM;
    QuestionsSelectMultAdapter questionsSelectMultAdapter;
    DBHelper dbHelper;
    CardView cardViewResultsSM;

    int currentPosition = 0; // Mantener un seguimiento de la posición actual en el RecyclerView


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflamos el diseño de este fragmento
        View view = inflater.inflate(R.layout.fragment_seleccion_multiple, container, false);

        // inicializando todas nuestras variables.
        questionsRV_SM = view.findViewById(R.id.idRVSelecMult);
        nextButton = view.findViewById(R.id.idNextQuestionSM);  //boton para pasar a la siguiente pregunta.
        backButton = view.findViewById(R.id.idBackQuestionSM);  //boton para pasar a la pregunta anterior.
        btnShowResultsSM = view.findViewById(R.id.btnShowResultsSM); //boton para mostrar los resultados
        cardViewResultsSM = view.findViewById(R.id.cardViewResultsSM); //cardView que muestra los resultados


        // creando variables para nuestra lista de matrices,
        // DBHelper, Adapter y RecyclerView.
        ArrayList<SelecMultTestData> questionsModalArrayList;

        dbHelper = new DBHelper(getActivity());

        // Obtención del ID único de la prueba Selc. Mult.
        String uniqueTestId = getArguments().getString("uniqueTestId");

        // obteniendo nuestra matriz de test de la clase de controlador de base de datos.
        questionsModalArrayList = dbHelper.readTableSelctMult(uniqueTestId);

        // en la línea siguiente pasando nuestra lista de matrices a nuestra clase de adaptador.
        questionsSelectMultAdapter = new QuestionsSelectMultAdapter(questionsModalArrayList, getActivity());
        questionsRV_SM = view.findViewById(R.id.idRVSelecMult);

        // Configuración del diseño del RecyclerView.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        questionsRV_SM.setLayoutManager(linearLayoutManager);
        questionsRV_SM.setNestedScrollingEnabled(false); // Para bloquear el desplazamiento vertical del RecyclerView.

        // Recuperar datos de SharedPreferences
        android.content.SharedPreferences preferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        // configurando nuestro adaptador
        questionsRV_SM.setAdapter(questionsSelectMultAdapter);

        // Invocamos el método que actualiza las vistas de los botones.
        updateButtonVisibility();

        // Definición del botón que permite avanzar a la siguiente pregunta.
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition < questionsSelectMultAdapter.getItemCount() - 1) {
                    currentPosition++;
                    questionsRV_SM.smoothScrollToPosition(currentPosition);
                    updateButtonVisibility();
                }
            }
        });

        // Definición del botón que permite retroceder a la pregunta anterior.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition > 0) {
                    currentPosition--;
                    questionsRV_SM.smoothScrollToPosition(currentPosition);
                    updateButtonVisibility();
                }
            }
        });

        // Definición del evento de clic en el botón btnShowResultsSM
        btnShowResultsSM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muestra el CardView con los resultados
                cardViewResultsSM.setVisibility(View.VISIBLE);

                // Obtiene el nombre de usuario mediante el dbHelper y los datos de inicio de sesión (username y password)
                String userName = dbHelper.getUserID(username, password);

                // Crea una instancia de la clase DBHelper para manipular la base de datos
                DBHelper dbHelper = new DBHelper(getContext());

                // Agrega un número asociado al nombre de usuario obtenido
                dbHelper.addNumberOfFinishTest(userName);

                // Utiliza el adaptador questionsSelectMultAdapter para mostrar los resultados finales en el CardView
                questionsSelectMultAdapter.showFinalResultsSelMul(cardViewResultsSM);
            }
        });

        return view;
    }

    /**
     * Método que actualiza la visibilidad de los botones para que pueda mostrar
     * solo el backButton o el nextButton cuando sea necesario.
     */
    private void updateButtonVisibility() {
        if (currentPosition == 0) {
            backButton.setVisibility(View.INVISIBLE);
        } else {
            backButton.setVisibility(View.VISIBLE);
        }

        if (currentPosition == questionsSelectMultAdapter.getItemCount() - 1) {
            nextButton.setVisibility(View.INVISIBLE);

            // Muestra el botón de mostrar resultados y ajusta su margen inferior
            btnShowResultsSM.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnShowResultsSM.getLayoutParams();
            params.setMargins(0, 0, 0, 60); // Ajusta el margen inferior a 60dp
            btnShowResultsSM.setLayoutParams(params);
        } else {
            nextButton.setVisibility(View.VISIBLE);

            // Oculta el botón de mostrar resultados
            btnShowResultsSM.setVisibility(View.INVISIBLE);
        }
    }

}