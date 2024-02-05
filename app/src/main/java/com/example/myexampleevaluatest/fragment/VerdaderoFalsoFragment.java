package com.example.myexampleevaluatest.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.myexampleevaluatest.Adapter.QuestionsTrueFalseAdapter;
import com.example.myexampleevaluatest.DabaBase.DBHelper;
import com.example.myexampleevaluatest.R;
import com.example.myexampleevaluatest.data.TrueFalseTestData;

import java.util.ArrayList;

public class VerdaderoFalsoFragment extends Fragment {


    //declaramos las varisbales.
    private RecyclerView questionsRV;
    private Button  btnShowResults;

    ImageButton backButton, nextButton;
    QuestionsTrueFalseAdapter questionsRVAdapter;
    CardView cardViewResultsTF;

    int currentPosition = 0; // Mantener un seguimiento de la posición actual en el RecyclerView


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del fragmento.
        View view = inflater.inflate(R.layout.fragment_true_false, container, false);

        // Inicialización de vistas y variables.
        questionsRV = view.findViewById(R.id.idRVTrueFalse);
        nextButton = view.findViewById(R.id.idNextQuestionTF);  // Botón para pasar a la siguiente pregunta.
        backButton = view.findViewById(R.id.idBackQuestionTF);  // Botón para pasar a la pregunta anterior.
        btnShowResults = view.findViewById(R.id.btnShowResultsTF); //boton para mostrar los resultados
        cardViewResultsTF = view.findViewById(R.id.cardViewResultsTF); //cardView que muestra los resultados

        // Creación de variables para la lista de preguntas,
        // el manejador de base de datos, el adaptador y el RecyclerView.
        ArrayList<TrueFalseTestData> questionsModalArrayList;
        DBHelper dbHelper;

        dbHelper = new DBHelper(getActivity());

        // Obtención del ID único de la prueba verdadero/falso.
        String uniqueTestId = getArguments().getString("uniqueTestId");
        Log.e("VerdaderoFalsoFragment", "UniqueTestId in onCreateView VerdaderoFalsoFragment: " + uniqueTestId.toString());

        // Obtención de la lista de preguntas desde la base de datos.
        questionsModalArrayList = dbHelper.getDataFromTableTF(uniqueTestId);

        // Creación del adaptador y configuración del RecyclerView.
        questionsRVAdapter = new QuestionsTrueFalseAdapter(questionsModalArrayList, getActivity());
        questionsRV = view.findViewById(R.id.idRVTrueFalse);

        // Configuración del diseño del RecyclerView.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // Para poner el RecyclerView con desplazamiento horizontal.
        linearLayoutManager.canScrollHorizontally();

        questionsRV.setLayoutManager(linearLayoutManager);
        questionsRV.setNestedScrollingEnabled(false); // Para bloquear el desplazamiento del RecyclerView.

        // Configuración del adaptador para el RecyclerView.
        questionsRV.setAdapter(questionsRVAdapter);

        // Recuperar datos de SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        // Invocamos el método que actualiza las vistas de los botones.
        updateButtonVisibility();

        // Bloqueo del desplazamiento horizontal/vertical del RecyclerView.
        questionsRV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getActionMasked() == MotionEvent.ACTION_MOVE || event.getActionMasked() == MotionEvent.ACTION_SCROLL;
            }
        });

        // Definición del botón que permite avanzar a la siguiente pregunta.
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition < questionsRVAdapter.getItemCount() - 1) {
                    currentPosition++;
                    questionsRV.smoothScrollToPosition(currentPosition);
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
                    questionsRV.smoothScrollToPosition(currentPosition);
                    updateButtonVisibility();
                }
            }
        });

        //Manejar el clic del botón "Mostrar Resultados":
        btnShowResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar el CardView con los resultados
                cardViewResultsTF.setVisibility(View.VISIBLE);

                // Obtener el nombre de usuario utilizando el DBHelper
                DBHelper dbHelper = new DBHelper(getContext());
                String userName = dbHelper.getUserID(username, password);

                // Incrementar el contador de pruebas finalizadas para el usuario
                dbHelper.addNumberOfFinishTest(userName);

                // Mostrar los resultados finales utilizando el adaptador de RecyclerView
                questionsRVAdapter.showFinalResultsTrueFalse(cardViewResultsTF);
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

        if (currentPosition == questionsRVAdapter.getItemCount() - 1) {
            nextButton.setVisibility(View.INVISIBLE);

            // Muestra el botón de mostrar resultados y ajusta su margen inferior
            btnShowResults.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnShowResults.getLayoutParams();
            params.setMargins(0, 0, 0, 60); // Ajusta el margen inferior a 60dp
            btnShowResults.setLayoutParams(params);
        } else {
            nextButton.setVisibility(View.VISIBLE);

            // Oculta el botón de mostrar resultados
            btnShowResults.setVisibility(View.INVISIBLE);
        }
    }
}