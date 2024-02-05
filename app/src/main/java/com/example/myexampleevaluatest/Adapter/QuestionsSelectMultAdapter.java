package com.example.myexampleevaluatest.Adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myexampleevaluatest.DabaBase.DBHelper;
import com.example.myexampleevaluatest.R;
import com.example.myexampleevaluatest.data.SelecMultTestData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionsSelectMultAdapter extends RecyclerView.Adapter<QuestionsSelectMultAdapter.ViewHolder> {

    //declaramos las variables
    private ArrayList<SelecMultTestData> selecMultTestDataArrayList;
    private Context context;

    private DBHelper dbHelper;

    private ArrayList<Boolean> answerStates;

    private int correctCount = 0;
    private int incorrectCount = 0;


    public QuestionsSelectMultAdapter(ArrayList<SelecMultTestData> selecMultTestDataArrayList, Context context) {
        this.selecMultTestDataArrayList = selecMultTestDataArrayList;
        this.context = context;
        this.answerStates = new ArrayList<>(Collections.nCopies(selecMultTestDataArrayList.size(), false));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selecion_multiple_items, parent, false);

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_seleccion_multiple, parent, false);
        dbHelper = new DBHelper(context);

        // Obtener la referencia del RecyclerView del diseño inflado
        RecyclerView recyclerView = v.findViewById(R.id.idRVSelecMult);

        // Configurar el LinearLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        return new ViewHolder(view, recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Asignar datos a las vistas de cada elemento en el RecyclerView
        SelecMultTestData selecMultTestData = selecMultTestDataArrayList.get(position);


        // Obtener el ID único de la pregunta y el ID de la pregunta específica
        String uniqueTestId = selecMultTestData.getIdentificadorUnicoDelTestTF();
        String question = selecMultTestData.getQuestion();

        holder.questionsTV.setText(selecMultTestData.getQuestion());
        holder.optionOne.setText(selecMultTestData.getOption_one());
        holder.optionTwo.setText(selecMultTestData.getOption_two());
        holder.optionThree.setText(selecMultTestData.getOption_three());
        holder.optionFour.setText(selecMultTestData.getOption_four());


        //configuramos la ProgressBar y el TextView
        holder.progressBarSelMul.setMax(getItemCount());
        updateProgressBarSelMult(holder.progressBarSelMul, holder.progressTextSelMul, position);

        // Manejar el clic del botón
        holder.confirmButton.setOnClickListener(view -> {
            // Obtener opciones marcadas por el usuario
            List<Boolean> userSelectedOptions = getSelectedOptions(holder);

            // Obtener opciones correctas almacenadas en la tabla
            List<Boolean> correctOptions = dbHelper.getAnswerForQuestionSM(uniqueTestId, question);

            Log.d("Flujo de código", "Opciones seleccionadas por el usuario: " + userSelectedOptions);
            Log.d("Flujo de código", "Opciones correctas almacenadas en la tabla: " + correctOptions.toString());


            setCheckBoxesEnabled(holder, false);

            if (userSelectedOptions.equals(correctOptions)) {
                // Si las opciones seleccionadas por el usuario son correctas

                // Actualizar la vista para indicar una respuesta correcta
                holder.iconResultCorrect.setImageResource(R.drawable.ic_ok_answer);
                holder.iconResultCorrect.setVisibility(View.VISIBLE);

                // Deshabilitar casillas de verificación y botón de confirmación
                setCheckBoxesEnabled(holder, false);
                holder.confirmButton.setEnabled(false);

                // Avanzar a la siguiente pregunta si no es la última
                if (position < getItemCount() - 1) {
                    Log.d("Flujo de POSICION", "AVANZA");
                    holder.recyclerView.smoothScrollToPosition(position + 1);
                }

                // Actualizar el estado de la respuesta en la lista
                answerStates.set(position, true);

                // Incrementar el contador de respuestas correctas
                correctCount++;

                Log.d("Flujo de código", "Acciones adicionales para respuestas correctas");
            } else {
                // Acciones para respuestas incorrectas
                setCheckBoxesEnabled(holder, false);  // Deshabilita los checkboxes
                holder.confirmButton.setEnabled(false);  // Deshabilita el botón de confirmación
                holder.iconResultIncorrect.setImageResource(R.drawable.ic_incorrect_answer);  // Establece el ícono de respuesta incorrecta
                holder.iconResultIncorrect.setVisibility(View.VISIBLE);  // Hace visible el ícono de respuesta incorrecta
                //Toast.makeText(view.getContext(), "Respuesta incorrecta", Toast.LENGTH_SHORT).show();
                Log.d("Flujo de código", "Acciones adicionales para respuestas incorrectas");
            }
        });


    }


    private List<Boolean> getSelectedOptions(ViewHolder holder) {
        List<Boolean> selectedOptions = new ArrayList<>();
        selectedOptions.add(holder.optionOne.isChecked());
        selectedOptions.add(holder.optionTwo.isChecked());
        selectedOptions.add(holder.optionThree.isChecked());
        selectedOptions.add(holder.optionFour.isChecked());


        return selectedOptions;
    }

    // Método para bloquear los CheckBox
    private void setCheckBoxesEnabled(ViewHolder holder, boolean enabled) {
        holder.optionOne.setEnabled(enabled);
        holder.optionTwo.setEnabled(enabled);
        holder.optionThree.setEnabled(enabled);
        holder.optionFour.setEnabled(enabled);
    }



    @Override
    public int getItemCount() {
        // devolviendo el tamaño de nuestra lista de matrices
        return selecMultTestDataArrayList.size();
    }


    // Método para actualizar la ProgressBar y el TextView
    private void updateProgressBarSelMult(ProgressBar progressBar, TextView progressText, int currentPosition) {
        if (progressBar != null) {
            int totalQuestions = getItemCount();
            int answeredQuestions = currentPosition + 1; // Suma 1 para ajustar el índice base 0

            progressBar.setMax(totalQuestions * 2); // Multiplicado por 2 para representar las preguntas acertadas y no acertadas
            progressBar.setProgress(answeredQuestions + correctCount + incorrectCount);

            String progressTextString = String.format("%d/%d ( %d Correctas /%d Incorrectas )",
                    answeredQuestions,
                    totalQuestions,
                    correctCount,
                    incorrectCount);
            progressText.setText(progressTextString);
        }
    }



    // Método para mostrar el resultado de todas las respuestas después
    // de responder a todas las preguntas.
    public void showFinalResultsSelMul(CardView cardViewResultsSM) {
        int correctCount = 0;
        int incorrectCount = 0;

        for (int i = 0; i < answerStates.size(); i++) {
            boolean isCorrect = answerStates.get(i);

            if (isCorrect) {
                correctCount++;
            } else {
                incorrectCount++;
            }
        }

        // Obtener el total de preguntas
        int totalQuestions = getItemCount();

        // Actualizar las barras de progreso y los textos en el CardView
        updateFinalResultsProgressBarSM(cardViewResultsSM.findViewById(R.id.progressBarCorrectSM), correctCount, totalQuestions);
        updateFinalResultsProgressBarSM(cardViewResultsSM.findViewById(R.id.progressBarIncorrectSM), incorrectCount, totalQuestions);

        // Mostrar el CardView con los resultados
        cardViewResultsSM.setVisibility(View.VISIBLE);

        // Mostrar el número de preguntas acertadas e incorrectas en los TextView
        TextView tvCorrectCount = cardViewResultsSM.findViewById(R.id.tvCorrectCountSM);
        TextView tvIncorrectCount = cardViewResultsSM.findViewById(R.id.tvIncorrectCountSM);

        tvCorrectCount.setText(String.format("Has acertado %d preguntas", correctCount));
        tvIncorrectCount.setText(String.format("Has equivocado %d preguntas", incorrectCount));
    }



    private void updateFinalResultsProgressBarSM(ProgressBar progressBar, int count, int totalQuestions) {
        // Aquí establecemos el progreso en función del recuento y el total de preguntas

        progressBar.setMax(totalQuestions);
        progressBar.setProgress(count);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        //definimos las variables
        Button confirmButton;
        ProgressBar progressBarSelMul;
        Handler handler;
        ImageView iconResultCorrect, iconResultIncorrect;
        // se crean las variables para las vistas.
        private TextView questionsTV, progressTextSelMul;
        private CheckBox optionOne, optionTwo, optionThree, optionFour;

        CardView cardViewResultsSM;

        RecyclerView recyclerView;
        public ViewHolder(@NonNull View itemView, RecyclerView recyclerView) {
            super(itemView);

            this.recyclerView = recyclerView;

            // inicializando nuestras vistas de texto
            questionsTV = itemView.findViewById(R.id.idQuestionSM);
            optionOne = itemView.findViewById(R.id.idCBOptionOneSM);
            optionTwo = itemView.findViewById(R.id.idCBOptionTwoSM);
            optionThree = itemView.findViewById(R.id.idCBOptionThreeSM);
            optionFour = itemView.findViewById(R.id.idCBOptionFourSM);


            confirmButton = itemView.findViewById(R.id.idBtnConfirmSelecctionSM);
            cardViewResultsSM = itemView.findViewById(R.id.cardViewResultsSM);

            //Inicializamos la ProgressBar y el TextView
            progressBarSelMul = itemView.findViewById(R.id.progressBarSelMul);
            progressTextSelMul = itemView.findViewById(R.id.progressTextSelMul);

            // Inicializar el icono como invisible por defecto
            iconResultCorrect = itemView.findViewById(R.id.iconResultCorrect);
            iconResultIncorrect = itemView.findViewById(R.id.iconResultIncorrect);
            iconResultCorrect.setVisibility(View.INVISIBLE);
            iconResultIncorrect.setVisibility(View.INVISIBLE);

            handler = new Handler();


        }

    }





}
