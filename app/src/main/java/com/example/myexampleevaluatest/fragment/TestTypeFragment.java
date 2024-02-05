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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.example.myexampleevaluatest.Adapter.QuestionsTestTypeAdapter;
import com.example.myexampleevaluatest.DabaBase.DBHelper;
import com.example.myexampleevaluatest.R;
import com.example.myexampleevaluatest.data.TestTypeTestData;

import java.util.ArrayList;

public class TestTypeFragment extends Fragment {

    //declaramos las varisbales.
    private QuestionsTestTypeAdapter questionsTestTypeAdapter;

    private int currentPosition = 0; // Mantener un seguimiento de la posición actual en el RecyclerView

    private RecyclerView questionsRVTT;
    private ImageButton nextButtonTT, backButtonTT;

    private Button btnShowResultsTT;
    private DBHelper dbHelper;

    private RadioGroup radioGroup;

    private CardView cardViewResultsTT;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflamos el diseño de este fragmento
        View view = inflater.inflate(R.layout.fragment_test_type, container, false);

        // Inflamos el diseño del fragmento test_type_items
        View v = inflater.inflate(R.layout.test_type_items, container, false);

        // inicializando todas nuestras variables.
        questionsRVTT = view.findViewById(R.id.idRecViewTestType);
        nextButtonTT = view.findViewById(R.id.idNextQuestionTT);  //boton para pasar a la siguiente pregunta.
        backButtonTT = view.findViewById(R.id.idBackQuestionTT);  //boton para pasar a la pregunta anterior.
        btnShowResultsTT = view.findViewById(R.id.btnShowResultsTT); //boton para mostrar los resultados
        cardViewResultsTT = view.findViewById(R.id.cardViewResultsTT);  //cardView que muestra los resultados
        radioGroup = v.findViewById(R.id.idRadioGroupQuestionsTT);


        // creando variables para nuestra lista de matrices,
        // DBHelper, Adapter y RecyclerView.
        ArrayList<TestTypeTestData> questionsModalArrayList;


        dbHelper = new DBHelper(getActivity());

        // Obtención del ID único de la prueba TestType
        String uniqueTestId = getArguments().getString("uniqueTestId");

        // obteniendo nuestra matriz de test
        // de la clase de controlador de base de datos.
        questionsModalArrayList = dbHelper.readTableTestType(uniqueTestId);

        // en la línea siguiente pasando nuestra lista de matrices a nuestra clase de adaptador.
        questionsTestTypeAdapter = new QuestionsTestTypeAdapter(questionsModalArrayList, getActivity());
        questionsRVTT = view.findViewById(R.id.idRecViewTestType);

        // Configuración del diseño del RecyclerView.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // Para poner el RecyclerView con desplazamiento horizontal.
        linearLayoutManager.canScrollHorizontally();

        questionsRVTT.setLayoutManager(linearLayoutManager);
        questionsRVTT.setNestedScrollingEnabled(false); // Para bloquear el desplazamiento vertical del RecyclerView.

        // Configuración del adaptador para el RecyclerView.
        questionsRVTT.setAdapter(questionsTestTypeAdapter);

        // Invocamos el método que actualiza las vistas de los botones.
        updateButtonVisibility();

        mostrarDatosEnRadioButtons(questionsModalArrayList);

        // Recuperar datos de SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");


        // Bloqueo del desplazamiento horizontal/vertical del RecyclerView.
        questionsRVTT.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return event.getActionMasked() == MotionEvent.ACTION_MOVE || event.getActionMasked() == MotionEvent.ACTION_SCROLL;
            }
        });

        // Definición del botón que permite avanzar a la siguiente pregunta.
        nextButtonTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition < questionsTestTypeAdapter.getItemCount() - 1) {
                    currentPosition++;
                    questionsRVTT.smoothScrollToPosition(currentPosition);
                    updateButtonVisibility();
                }
            }
        });

        // Definición del botón que permite retroceder a la pregunta anterior.
        backButtonTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition > 0) {
                    currentPosition--;
                    questionsRVTT.smoothScrollToPosition(currentPosition);
                    updateButtonVisibility();
                }
            }
        });

        //Manejar el clic del botón "Mostrar Resultados" y mostrar el CardView con el resultado final
        btnShowResultsTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muestra el CardView con los resultados
                cardViewResultsTT.setVisibility(View.VISIBLE);

                // Obtiene el nombre de usuario mediante el dbHelper y los datos de inicio de sesión (username y password)
                String userName = dbHelper.getUserID(username, password);

                // Crea una instancia de la clase DBHelper para manipular la base de datos
                DBHelper dbHelper = new DBHelper(getContext());

                // Agrega un número asociado al nombre de usuario obtenido
                dbHelper.addNumberOfFinishTest(userName);

                // Utiliza el adaptador questionsTestTypeAdapter para mostrar los resultados finales en el CardView
                questionsTestTypeAdapter.showFinalResultsTestType(cardViewResultsTT);
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
            backButtonTT.setVisibility(View.INVISIBLE);
        } else {
            backButtonTT.setVisibility(View.VISIBLE);
        }

        if (currentPosition == questionsTestTypeAdapter.getItemCount() - 1) {
            nextButtonTT.setVisibility(View.INVISIBLE);

            // Muestra el botón de mostrar resultados y ajusta su margen inferior
            btnShowResultsTT.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btnShowResultsTT.getLayoutParams();
            params.setMargins(0, 0, 0, 60); // Ajusta el margen inferior a 60dp
            btnShowResultsTT.setLayoutParams(params);
        } else {
            nextButtonTT.setVisibility(View.VISIBLE);

            // Oculta el botón de mostrar resultados
            btnShowResultsTT.setVisibility(View.INVISIBLE);
        }
    }


    // Método para mostrar datos en RadioButtons
    private void mostrarDatosEnRadioButtons(ArrayList<TestTypeTestData> datos) {
        // Verifica si radioGroup es nulo antes de operar sobre él
        if (radioGroup != null) {
            // Limpia las vistas antiguas en caso de recarga
            radioGroup.removeAllViews();

            for (TestTypeTestData dato : datos) {
                // Verifica que la pregunta no sea nula ni vacía
                if (dato.getQuestion() != null && !dato.getQuestion().trim().isEmpty()) {
                    RadioButton radioButton = new RadioButton(requireContext());
                    radioButton.setText(dato.getQuestion());

                    // Asigna un ID único al RadioButton
                    int radioButtonId = View.generateViewId();
                    radioButton.setId(radioButtonId);

                    // Agrega el RadioButton al RadioGroup
                    radioGroup.addView(radioButton);
                }
            }
        } else {
            // Maneja el caso en el que radioGroup es nulo, mostrando el mensage
            Log.e("TestTypeFragment", "RadioGroup is null");
        }
    }
















}