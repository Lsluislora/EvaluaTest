package com.example.myexampleevaluatest.Adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myexampleevaluatest.DabaBase.DBHelper;
import com.example.myexampleevaluatest.R;
import com.example.myexampleevaluatest.data.TrueFalseTestData;

import java.util.ArrayList;
import java.util.Collections;

public class QuestionsTrueFalseAdapter extends RecyclerView.Adapter<QuestionsTrueFalseAdapter.ViewHolder> {

    // definimos las variables necesarias.
    private ArrayList<TrueFalseTestData> trueFalseTestDataArrayList;
    private Context context;

    private DBHelper dbHelper;
    private ArrayList<Boolean> answerStates;

    private int correctCount = 0;
    private int incorrectCount = 0;

    private boolean isConfirmationButtonPressed = false;


    public QuestionsTrueFalseAdapter(ArrayList<TrueFalseTestData> trueFalseTestDataArrayList, Context context) {
        this.trueFalseTestDataArrayList = trueFalseTestDataArrayList;
        this.context = context;
        this.answerStates = new ArrayList<>(Collections.nCopies(trueFalseTestDataArrayList.size(), false));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.true_false_items, parent, false);
        dbHelper = new DBHelper(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Asignar datos a las vistas de cada elemento en el RecyclerView
        TrueFalseTestData trueFalseTestData = trueFalseTestDataArrayList.get(position);
        holder.questionsTV.setText(trueFalseTestData.getQuestion());



        // Obtener el ID único de la pregunta y el ID de la pregunta específica
        String question = trueFalseTestData.getQuestion();
        int questionId = trueFalseTestData.getId();


        //configuramos la ProgressBar y el TextView
        holder.progressBarTrueFalse.setMax(getItemCount());
        updateProgressBarTrueFalse(holder.progressBarTrueFalse, holder.progressTextTrueFalse, position, correctCount);

        // Manejar el clic del botón Verdadero
        holder.trueButton.setOnClickListener(view -> {
            String answer = dbHelper.getAnswerForQuestionTF(questionId, question);
            boolean isCorrect = answer != null && answer.equalsIgnoreCase("Verdadero");

            // Actualiza el estado de la respuesta
            answerStates.set(position, isCorrect);

            isConfirmationButtonPressed = true;
            // Deshabilitar los botones después de la respuesta
            disableTrueFalseButtons(holder);

            if (isCorrect) {
                correctCount++;
            } else {
                incorrectCount++;
            }
            Log.e("Flujo de código", "Incorrecta agregada: " + correctCount);
            updateProgressBarTrueFalse(holder.progressBarTrueFalse, holder.progressTextTrueFalse, position, correctCount);


            if (answer != null && answer.equalsIgnoreCase("Verdadero")) {
                // Respuesta correcta
                showResultIcon(holder.iconResultCorrect, R.drawable.ic_ok_answer, true);
            } else {
                // Respuesta incorrecta
                showResultIcon(holder.iconResultIncorrect, R.drawable.ic_incorrect_answer, false);
            }
        });


        holder.falseButton.setOnClickListener(view -> {
            String answer = dbHelper.getAnswerForQuestionTF(questionId, question);
            boolean isCorrect = answer != null && answer.equalsIgnoreCase("Falso");

            // Actualiza el estado de la respuesta
            answerStates.set(position, isCorrect);


            isConfirmationButtonPressed = true;
            // Deshabilitar los botones después de la respuesta
            disableTrueFalseButtons(holder);

            if (isCorrect) {
                correctCount++;
            } else {
                incorrectCount++;
            }
            Log.e("Flujo de código", "Incorrecta agregada: " + incorrectCount);

            if (answer != null && answer.equalsIgnoreCase("Falso")) {
                // Respuesta correcta
                showResultIcon(holder.iconResultCorrect, R.drawable.ic_ok_answer, true);
            } else {
                // Respuesta incorrecta
                showResultIcon(holder.iconResultIncorrect, R.drawable.ic_incorrect_answer, false);
            }

            // Actualiza la ProgressBar
            updateProgressBarTrueFalse(holder.progressBarTrueFalse, holder.progressTextTrueFalse, position, correctCount);
        });

    }


    // Método para deshabilitar los botones de Verdadero/Falso
    private void disableTrueFalseButtons(ViewHolder holder) {
        holder.trueButton.setEnabled(false);
        holder.falseButton.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return trueFalseTestDataArrayList.size();
    }

    private void updateProgressBarTrueFalse(ProgressBar progressBar, TextView progressText, int currentPosition, int correctCount) {
        if (progressBar != null) {
            int totalQuestions = getItemCount();
            int answeredQuestions = currentPosition + 1; // Suma 1 para ajustar el índice base 0

            progressBar.setMax(totalQuestions * 2); // Multiplicado por 2 para representar las preguntas acertadas y no acertadas
            progressBar.setProgress(answeredQuestions * 2 + correctCount + incorrectCount); // Multiplicado por 2 para reflejar ambos tipos de respuestas

            String progressTextString = String.format("%d/%d ( %d Correctas /%d Incorrectas )",
                    answeredQuestions,
                    totalQuestions,
                    correctCount,
                    incorrectCount);
            progressText.setText(progressTextString);
        }
    }


    // Método común para mostrar iconos y manejar respuestas
    private void showResultIcon(ImageView resultIcon, int iconResource, boolean isCorrect) {
        resultIcon.setImageResource(iconResource);
        resultIcon.setVisibility(View.VISIBLE);

        // Programar una tarea para ocultar el icono después de 1.5 segundos (1500 milisegundos)
        new Handler().postDelayed(() -> {
            resultIcon.setVisibility(View.INVISIBLE);
        }, 1500);

    }

    //método para mostrar el resultado de todas las respuestas después
    //de responder a todas las preguntas.
    public void showFinalResultsTrueFalse(CardView cardViewResultsTF) {

        // Obtener el total de preguntas
        int totalQuestions = getItemCount();

        // Actualizar las barras de progreso y los textos en el CardView
        updateFinalResultsProgressBarSM(cardViewResultsTF.findViewById(R.id.progressBarFinalCorrectTF), correctCount, totalQuestions);
        updateFinalResultsProgressBarSM(cardViewResultsTF.findViewById(R.id.progressFInalBarIncorrectTF), incorrectCount, totalQuestions);

        // Mostrar el CardView con los resultados
        cardViewResultsTF.setVisibility(View.VISIBLE);

        // Mostrar el número de preguntas acertadas e incorrectas en los TextView
        TextView tvCorrectCount = cardViewResultsTF.findViewById(R.id.tvFinalCorrectCountTF);
        TextView tvIncorrectCount = cardViewResultsTF.findViewById(R.id.tvIncorrectCountTF);

        // Modificar el texto basado en la cantidad de preguntas
        String correctCountText = (correctCount == 1) ? "Has acertado 1 pregunta" : String.format("Has acertado %d preguntas", correctCount);
        String incorrectCountText = (incorrectCount == 1) ? "Has equivocado 1 pregunta" : String.format("Has equivocado %d preguntas", incorrectCount);

        tvCorrectCount.setText(correctCountText);
        tvIncorrectCount.setText(incorrectCountText);
    }

    private void updateFinalResultsProgressBarSM(ProgressBar progressBar, int count, int totalQuestions) {
        // Aquí establecemos el progreso en función del recuento y el total de preguntas
        progressBar.setMax(totalQuestions);
        progressBar.setProgress(count);
    }

    // Método para limpiar los RadioButtons
    public void clearRadioButtons() {
        for (int i = 0; i < answerStates.size(); i++) {
            answerStates.set(i, false);
        }
        notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        // se crean las variables para las vistas
        private TextView questionsTV , progressTextTrueFalse;
        Button trueButton, falseButton;

        ProgressBar progressBarTrueFalse;
        Handler handler;

        ImageView iconResultCorrect, iconResultIncorrect;

        CardView cardViewResultsTF;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            questionsTV = itemView.findViewById(R.id.idQuestionTF);
            trueButton = itemView.findViewById(R.id.idTrueAnswerTest);
            falseButton = itemView.findViewById(R.id.idFalseAnswerTest);

            cardViewResultsTF = itemView.findViewById(R.id.cardViewResultsTF);

            //Inicializamos la ProgressBar y el TextView
            progressBarTrueFalse = itemView.findViewById(R.id.progressBarTrueFalse);
            progressTextTrueFalse = itemView.findViewById(R.id.progressTextTrueFalse);

            // Inicializar el icono como invisible por defecto
            iconResultCorrect = itemView.findViewById(R.id.iconResultCorrect);
            iconResultIncorrect = itemView.findViewById(R.id.iconResultIncorrect);
            iconResultCorrect.setVisibility(View.INVISIBLE);
            iconResultIncorrect.setVisibility(View.INVISIBLE);

            handler = new Handler();
        }

    }





}
