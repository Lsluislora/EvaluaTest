package com.example.myexampleevaluatest.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myexampleevaluatest.DabaBase.DBHelper;
import com.example.myexampleevaluatest.R;
import com.example.myexampleevaluatest.data.TestTypeTestData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Handler;

public class QuestionsTestTypeAdapter extends RecyclerView.Adapter<QuestionsTestTypeAdapter.ViewHolder> {

    //variables para nuestra lista de matrices y contexto
    private ArrayList<TestTypeTestData> testTypeTestDataArrayList;
    private Context context;
    private DBHelper dbHelper;
    private ArrayList<Boolean> answerStates;
    //private HashMap<String, Boolean> answerStatesTT;
    private int correctCount = 0;
    private int incorrectCount = 0;

    private boolean isConfirmationButtonClicked = false;
    private boolean isConfirmationButtonPressed = false;


    // Constructor que toma también el CardView
    public QuestionsTestTypeAdapter(ArrayList<TestTypeTestData> testTypeTestDataArrayList, Context context) {
        this.testTypeTestDataArrayList = testTypeTestDataArrayList;
        this.context = context;
        this.answerStates = new ArrayList<>(Collections.nCopies(testTypeTestDataArrayList.size(), false));
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_type_items, parent, false);
        dbHelper = new DBHelper(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Asignar datos a las vistas de cada elemento en el RecyclerView
        TestTypeTestData testTypeTestData = testTypeTestDataArrayList.get(position);


        // Obtiene los datos para la posición actual
        TestTypeTestData selectedData = testTypeTestDataArrayList.get(position);
        int questionId = testTypeTestData.getId();

        // Obtener el ID único de la pregunta y el ID de la pregunta específica
        String uniqueTestId = testTypeTestData.getIdentificadorUnicoDelTestTT();
        String question = testTypeTestData.getQuestion();


        // Asignar opciones de respuesta a los RadioButton
        holder.questionsTV.setText(testTypeTestData.getQuestion());
        holder.optionOne.setText(testTypeTestData.getOptionOne());
        holder.optionTwo.setText(testTypeTestData.getOptionTwo());
        holder.optionThree.setText(testTypeTestData.getOptionThree());
        holder.optionFour.setText(testTypeTestData.getOptionFour());


        //configuramos la ProgressBar y el TextView
        holder.progressBarTestType.setMax(getItemCount());
        updateProgressBarTestType(holder.progressBarTestType, holder.progressTextTestType, position, correctCount);

        // Verificar la respuesta cuando se hace clic en el botón confirmButtonTestType
        holder.confirmButtonTestType.setOnClickListener(view -> {

            // Obtener el ID de la opción seleccionada
            int selectedRadioButtonId = holder.radioGroupChoices_layoutTT.getCheckedRadioButtonId();

            // Verificar si se ha seleccionado algún RadioButton
            if (selectedRadioButtonId != -1) {

                // Obtener opciones marcadas por el usuario
                List<Boolean> userSelectedOptions = getSelectedOptions(holder);

                // Obtener opciones correctas almacenadas en la tabla
                List<Boolean> correctOptions = dbHelper.getAnswerForQuestionTestType(uniqueTestId, question);

                Log.d("Flujo de código", "Opciones correctas almacenadas en la tabla: " + correctOptions.toString());

                // Verificar si se ha seleccionado algún RadioButton
                if (selectedRadioButtonId != -1) {

                    // Deshabilitar todos los RadioButtons después de hacer clic en confirmButtonTestType
                    disableRadioGroup(holder.radioGroupChoices_layoutTT);

                    // Marcar que se ha hecho clic en el botón de confirmación
                    isConfirmationButtonClicked = true;
                } else {
                    // No se ha seleccionado ninguna opción
                    Toast.makeText(view.getContext(), "Por favor, selecciona una opción", Toast.LENGTH_SHORT).show();
                }

                if (!isConfirmationButtonPressed) {

                    // Deshabilitar todos los RadioButtons después de hacer clic en confirmButtonTestType
                    disableRadioGroup(holder.radioGroupChoices_layoutTT);

                    // Marcar que el botón de confirmación ha sido presionado
                    isConfirmationButtonPressed = true;

                    // Deshabilitar el botón de confirmación
                    holder.confirmButtonTestType.setEnabled(false);
                } else {
                    // El botón de confirmación ya ha sido presionado previamente
                    Toast.makeText(view.getContext(), "Ya has confirmado tu respuesta", Toast.LENGTH_SHORT).show();
                }

                isConfirmationButtonPressed = false;

                // Verificar la respuesta
                if (correctOptions != null) {


                    if (userSelectedOptions.equals(correctOptions)) { // Comparación sin distinción entre mayúsculas y minúsculas

                        String answer = dbHelper.getAnswerForQuestionTF(questionId, question);
                        boolean isCorrect = answer != null && answer.equalsIgnoreCase("Verdadero");

                        // Actualiza el estado de la respuesta
                        answerStates.set(position, isCorrect);

                        if (isCorrect) {
                            incorrectCount++;
                        } else {
                            correctCount++;
                        }

                        updateProgressBarTestType(holder.progressBarTestType, holder.progressTextTestType, position, correctCount);

                        Log.e("Flujo de código", "Correcta agregada " + correctCount);

                        // Respuesta correcta
                        holder.iconResultCorrect.setImageResource(R.drawable.ic_ok_answer);
                        holder.iconResultCorrect.setVisibility(View.VISIBLE);

                        // Programar una tarea para ocultar el icono después de 1.5 segundos (1500 milisegundos)
                        new Handler().postDelayed(() -> {
                            holder.iconResultCorrect.setVisibility(View.INVISIBLE);
                        }, 1500);


                    } else {

                        String answer = dbHelper.getAnswerForQuestionTF(questionId, question);
                        boolean isCorrect = answer != null && answer.equalsIgnoreCase("Falso");

                        // Actualiza el estado de la respuesta
                        answerStates.set(position, isCorrect);

                        if (isCorrect) {
                            correctCount++;
                        } else {
                            incorrectCount++;
                        }

                        Log.e("Flujo de código", "Incorrecta agregada: " + incorrectCount);
                        updateProgressBarTestType(holder.progressBarTestType, holder.progressTextTestType, position, correctCount);

                        // Respuesta incorrecta
                        holder.iconResultIncorrect.setImageResource(R.drawable.ic_incorrect_answer);
                        holder.iconResultIncorrect.setVisibility(View.VISIBLE);

                        // Programar una tarea para ocultar el icono después de 1.5 segundos (1500 milisegundos)
                        new Handler().postDelayed(() -> {
                            holder.iconResultIncorrect.setVisibility(View.INVISIBLE);
                        }, 1500);

                    }
                }

            } else {
                // No se ha seleccionado ninguna opción
                Toast.makeText(view.getContext(), "Por favor, selecciona una opción", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private List<Boolean> getSelectedOptions(QuestionsTestTypeAdapter.ViewHolder holder) {
        List<Boolean> selectedOptions = new ArrayList<>();
        selectedOptions.add(holder.optionOne.isChecked());
        selectedOptions.add(holder.optionTwo.isChecked());
        selectedOptions.add(holder.optionThree.isChecked());
        selectedOptions.add(holder.optionFour.isChecked());

        return selectedOptions;
    }


    @Override
    public int getItemCount() {
        // devolviendo el tamaño de nuestra lista de matrices
        return testTypeTestDataArrayList.size();
    }


    // Método para actualizar la ProgressBar y el TextView
    private void updateProgressBarTestType(ProgressBar progressBar, TextView progressText, int currentPosition, int correctCount) {
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


    // Método para mostrar el resultado de todas las respuestas después
    // de responder a todas las preguntas.
    public void showFinalResultsTestType(CardView cardViewResultsTT) {
        // Obtener el total de preguntas
        int totalQuestions = getItemCount();

        // Actualizar las barras de progreso y los textos en el CardView
        updateFinalResultsProgressBarTT(cardViewResultsTT.findViewById(R.id.progressBarFinalCorrectTT), correctCount, totalQuestions);
        updateFinalResultsProgressBarTT(cardViewResultsTT.findViewById(R.id.progressBarFinalIcorrectTT), incorrectCount, totalQuestions);

        // Mostrar el CardView con los resultados
        cardViewResultsTT.setVisibility(View.VISIBLE);

        // Mostrar el número de preguntas acertadas e incorrectas en los TextView
        TextView tvCorrectCount = cardViewResultsTT.findViewById(R.id.tvCorrectCountTT);
        TextView tvIncorrectCount = cardViewResultsTT.findViewById(R.id.tvIncorrectCountTT);

        // Modificar el texto basado en la cantidad de preguntas
        String correctCountText = (correctCount == 1) ? "Has acertado 1 pregunta" : String.format("Has acertado %d preguntas", correctCount);
        String incorrectCountText = (incorrectCount == 1) ? "Has equivocado 1 pregunta" : String.format("Has equivocado %d preguntas", incorrectCount);

        tvCorrectCount.setText(correctCountText);
        tvIncorrectCount.setText(incorrectCountText);
    }





    private void updateFinalResultsProgressBarTT(ProgressBar progressBar, int count, int totalQuestions) {
        // Aquí establecemos el progreso en función del recuento y el total de preguntas
        progressBar.setMax(totalQuestions);
        progressBar.setProgress(count);
    }



    // Método para deshabilitar todos los RadioButtons en el RadioGroup
    // una vez el usuario ha precionado el botton de MostrarResultado
    private void disableRadioGroup(RadioGroup radioGroup) {
        int childCount = radioGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = radioGroup.getChildAt(i);
            if (child instanceof RadioButton) {
                ((RadioButton) child).setEnabled(false);
            }
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        // se crean las variables para las vistas.
        private TextView questionsTV, progressTextTestType;
        private RadioButton optionOne, optionTwo, optionThree, optionFour;
        private Button confirmButtonTestType;

        private RadioGroup radioGroupChoices_layoutTT;
        ProgressBar progressBarTestType;

        Handler handler;

        CardView cardViewResultsTT;

        ImageView iconResultCorrect, iconResultIncorrect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // inicializando nuestras vistas de texto
            questionsTV = itemView.findViewById(R.id.idQuestionTT);
            optionOne = itemView.findViewById(R.id.idRBOptionOne);
            optionTwo = itemView.findViewById(R.id.idRBOptionTwo);
            optionThree = itemView.findViewById(R.id.idRBOptionThree);
            optionFour = itemView.findViewById(R.id.idRBOptionFour);

            radioGroupChoices_layoutTT = itemView.findViewById(R.id.idRadioGroupQuestionsTT);
            confirmButtonTestType = itemView.findViewById(R.id.idBtnConfirmSelectionTT);
            cardViewResultsTT = itemView.findViewById(R.id.cardViewResultsTT);


            //Inicializamos la ProgressBar y el TextView
            progressBarTestType = itemView.findViewById(R.id.progressBarTestType);
            progressTextTestType = itemView.findViewById(R.id.progressTextTestType);

            // Inicializar el icono como invisible por defecto
            iconResultCorrect = itemView.findViewById(R.id.iconResultCorrect);
            iconResultIncorrect = itemView.findViewById(R.id.iconResultIncorrect);
            iconResultCorrect.setVisibility(View.INVISIBLE);
            iconResultIncorrect.setVisibility(View.INVISIBLE);

            handler = new Handler();

        }



    }






}
