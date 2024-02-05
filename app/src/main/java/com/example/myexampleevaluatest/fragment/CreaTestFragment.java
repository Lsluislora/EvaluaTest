package com.example.myexampleevaluatest.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myexampleevaluatest.Adapter.TestAdapter;
import com.example.myexampleevaluatest.DabaBase.DBHelper;
import com.example.myexampleevaluatest.MainActivity;
import com.example.myexampleevaluatest.R;
import com.example.myexampleevaluatest.data.TestData;

import java.util.ArrayList;


public class CreaTestFragment extends Fragment {


    //declaramos las variables
    DBHelper dbHelper;
    private int editTextCount = 4;
    private EditText testNameEdt, testDurationEdt, testDescriptionEdt, myTestIdUserID, questionTF,
            questionSM, optionOneSM, optionTwoSM, optionThreeSM,questionTT,  optionFourSM,
            optionOneTT, optionTwoTT, optionThreeTT, optionFourTT;
    private RadioButton radioButtonCorAnswTF, radioButtonIncorAnswTF, radioButtonOptionOneTT,
            radioButtonOptionTwoTT, radioButtonOptionThreeTT, radioButtonOptionFourTT;
    private int selectedId;
    private Spinner spinnerCategoryTest, spinnerTypeTest;
    private Button addCourseBtn, readCourseBtn, addNewQuestionTF, deleteQuestionTF,
             addNewQuestionSM, deleteQuestionSM, addNewQuestionTT, deleteQuestionTT;

    private ArrayList<TestData> testModalArrayList;
    TestAdapter testAdapter;

    private CheckBox checkBoxOneSM, checkBoxTwoSM, checkBoxThreeSM, checkBoxFourSM;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_crea_test, container, false);

        // creando una nueva clase dbhandler y pasandole nuestro contexto.
        dbHelper = new DBHelper(getActivity());
        testModalArrayList = dbHelper.readTests();

        testAdapter = new TestAdapter(testModalArrayList, getActivity());;


        // inicializando todas nuestras variables.
        testNameEdt = view.findViewById(R.id.idEdtCourseName);
        spinnerCategoryTest = view.findViewById(R.id.spnCategory);
        testDurationEdt = view.findViewById(R.id.idEdtNumberOfQuestions);
        testDescriptionEdt = view.findViewById(R.id.idEdtCourseDescription);
        myTestIdUserID = view.findViewById(R.id.idUserID);
        spinnerTypeTest = view.findViewById(R.id.spnTypeTest);


        //preguntas TrueFalse Test
        questionTF = view.findViewById(R.id.idEdtQuestionTF);
        radioButtonCorAnswTF = view.findViewById(R.id.rBtnTrueTF);
        radioButtonIncorAnswTF = view.findViewById(R.id.rBtnFalseTF);

        //preguntas Selecction Multiple Test
        questionSM = view.findViewById(R.id.idEdtQuestionSM);
        optionOneSM = view.findViewById(R.id.idEdtOptionOneSM);
        optionTwoSM = view.findViewById(R.id.idEdtOptionTwoSM);
        optionThreeSM = view.findViewById(R.id.idEdtOptionThreeSM);
        optionFourSM = view.findViewById(R.id.idEdtOptionFourSM);

        //los checkBox del Test Sel. Mul.
        checkBoxOneSM = view.findViewById(R.id.idCBOptionOne);
        checkBoxTwoSM = view.findViewById(R.id.idCBOptionTwo);
        checkBoxThreeSM = view.findViewById(R.id.idCBOptionThree);
        checkBoxFourSM = view.findViewById(R.id.idCBOptionFour);

        //preguntas Tipo Test
        questionTT = view.findViewById(R.id.idEdtQuestionTT);
        optionOneTT = view.findViewById(R.id.idEdtOptionOneTT);
        optionTwoTT = view.findViewById(R.id.idEdtOptionTwoTT);
        optionThreeTT = view.findViewById(R.id.idEdtOptionThreeTT);
        optionFourTT = view.findViewById(R.id.idEdtOptionFourTT);

        //RadioButtons Tipo Test
        radioButtonOptionOneTT = view.findViewById(R.id.idCBOptionOneTT);
        radioButtonOptionTwoTT = view.findViewById(R.id.idCBOptionTwoTT);
        radioButtonOptionThreeTT = view.findViewById(R.id.idCBOptionThreeTT);
        radioButtonOptionFourTT = view.findViewById(R.id.idCBOptionFourTT);

        //inicializamos los Botones
        addCourseBtn = view.findViewById(R.id.idBtnAddCourse);      //agrega un curso/test
        readCourseBtn = view.findViewById(R.id.idBtnReadCourse);    //leemos los cursos/test disponibles
        //True False
        addNewQuestionTF = view.findViewById(R.id.idBtnAddNewQuestionTF);   //agrega una nueva pregunta de TrueFalse
        deleteQuestionTF = view.findViewById(R.id.idBtnDeleleQuestionTF);   //borramos la pregunta de TrueFalse
        //Sel. Multiple
        addNewQuestionSM = view.findViewById(R.id.idBtnAddQuestionSM);      //agrega una nueva pregunta de Sel. Mult.
        deleteQuestionSM = view.findViewById(R.id.idBtnDeleleQuestionSM);   //borramos la pregunta de Sel. Mult.

        //Tipo Test.
        addNewQuestionTT = view.findViewById(R.id.idBtnAddQuestionTT);      //agrega una nueva pregunta de Tipo Test.
        deleteQuestionTT = view.findViewById(R.id.idBtnDeleleQuestionTT);   //borramos la pregunta de Tipo Test.


        //para la nueva vista del nuevo CardView definimos cada una de ellas.
        CardView cardViewQuestionSM = view.findViewById(R.id.idCardViewQuestionSM); // Encuentra la referencia al CardView QuestionsSelecionMultiple
        CardView cardViewQuestionTT = view.findViewById(R.id.idCardViewQuestionTT); // Encuentra la referencia al CardView QuestionsTypeTest
        CardView cardViewQuestionTF = view.findViewById(R.id.idCardViewQuestionTF); // Encuentra la referencia al CardView QuestionsTrueFalse

        // Recuperar datos de SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");

        String userId= dbHelper.getUserID(username, password);

        //En caso de que el usuario este logado, establecemos el usuario al crear el Test
        myTestIdUserID.setText(userId);

        //en caso de que este vacio permite editar el EditText, de lo contrario no lo permite.
        if (userId != null && !userId.isEmpty()) {
            myTestIdUserID.setText(userId);
            myTestIdUserID.setEnabled(false); // Esto deshabilitará el EditText
        } else {
            myTestIdUserID.setEnabled(true); // Esto habilitará el EditText si está vacío
        }


        //Se configura la selección del Spinner, dependiente de que opcion se selecciona se muestra un cardView o otro
        spinnerTypeTest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { // Condición basada en la selección del usuario
                    cardViewQuestionTF.setVisibility(View.GONE);    // Oculta la CardView
                    cardViewQuestionSM.setVisibility(View.GONE);    // Oculta la CardView
                    cardViewQuestionTT.setVisibility(View.GONE);    // Oculta la CardView
                } else if (position == 1) {
                    cardViewQuestionTF.setVisibility(View.VISIBLE); //Muestra la CardView
                    cardViewQuestionSM.setVisibility(View.GONE);    // Oculta la CardView
                    cardViewQuestionTT.setVisibility(View.GONE);    // Oculta la CardView
                } else if (position == 2) {
                    cardViewQuestionTF.setVisibility(View.GONE);    //Oculta la CardView
                    cardViewQuestionSM.setVisibility(View.VISIBLE); // Muestra la CardView
                    cardViewQuestionTT.setVisibility(View.GONE);    // Oculta la CardView
                }else if (position == 3) {
                    cardViewQuestionTF.setVisibility(View.GONE);    //Oculta la CardView
                    cardViewQuestionSM.setVisibility(View.GONE);    // Muestra la CardView
                    cardViewQuestionTT.setVisibility(View.VISIBLE); // Muestra la CardView
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


        /**
         * boton que nos permite agregar el test a la tabla cuando los datos se han introducido correctamente.
         */
        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Obtener datos de todos los campos de texto.
                String testTitle = testNameEdt.getText().toString();
                String testCategory = spinnerCategoryTest.getSelectedItem().toString();
                String testDuration = testDurationEdt.getText().toString();
                String testDescription = testDescriptionEdt.getText().toString();
                String testIdUserID = myTestIdUserID.getText().toString();
                String testTypeTest = spinnerTypeTest.getSelectedItem().toString();
                String idTestUniqueAssociation = (testTitle + testTypeTest + testIdUserID).replaceAll(" ", "_");

                // Validar si los campos de texto están vacíos.
                if (testTitle.isEmpty() || testCategory.isEmpty() || testDuration.isEmpty() ||
                        testDescription.isEmpty() || testCategory.matches("Selecciona la Categoria") ||
                        testTypeTest.matches("Selecciona el tipo de Test")) {

                    // Mostrar mensajes de error y un asterisco en el Spinner si es necesario.
                    testNameEdt.setError("Introduce el Titulo");
                    myTestIdUserID.setError("Introduce tu usuario");

                    if (spinnerTypeTest.getSelectedItemPosition() == 0) {
                        View selectedView = spinnerTypeTest.getSelectedView();
                        if (selectedView != null && selectedView instanceof TextView) {
                            ((TextView) selectedView).setError("*");
                        }
                    }

                    Toast.makeText(getActivity(), "Introduce todos los datos..", Toast.LENGTH_LONG).show();
                    return;
                }

                // Agregar un nuevo test a la base de datos SQLite.
                dbHelper.addNewTest(testTitle, idTestUniqueAssociation, testDuration, testDescription, testCategory, testIdUserID, testTypeTest);

                // Notificar al adaptador que los datos han cambiado.
                testAdapter.notifyDataSetChanged();

                // Actualizar el contador en el BottomNavigationView.
                int contador = testModalArrayList.size();
                ((MainActivity) getActivity()).actualizarContadorEnBottomNav(contador);

                // Mostrar mensaje de éxito.
                Toast.makeText(getActivity(), "Se ha agregado el Test.", Toast.LENGTH_LONG).show();

                // Limpiar campos de entrada.
                testNameEdt.setText("");
                testDurationEdt.setText("");
                spinnerCategoryTest.setSelection(0);
                spinnerTypeTest.setSelection(0);
                testDescriptionEdt.setText("");
                myTestIdUserID.setText("");
                questionTF.setText("");
            }
        });

        //boton que nos permite leer los cursos/test disponibles.
        readCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // abriendo un nuevo fragment.
                NavController navController = Navigation.findNavController(getActivity(), R.id.navHostFragment);
                navController.navigate(R.id.verTestFragment);

            }
        });


        RadioButton radioButtonTrue = view.findViewById(R.id.rBtnTrueTF);
        RadioButton radioButtonFalse = view.findViewById(R.id.rBtnFalseTF);


        /**
         * Configura el método para agregar preguntas del tipo TrueFalse al test.
         */
        addNewQuestionTF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtenemos los datos necesarios
                String testTitle = testNameEdt.getText().toString();
                String testTypeTest = spinnerTypeTest.getSelectedItem().toString();
                String testIdUserID = myTestIdUserID.getText().toString();

                // Creamos una clave única para la asociación de la pregunta con el test
                String idTestUniqueAsociation = (testTitle + testTypeTest + testIdUserID).replaceAll(" ", "_");

                // Obtenemos la pregunta y la opción seleccionada
                String strQuestion = questionTF.getText().toString();
                String valorSeleccionado = null;

                // Validamos que se hayan ingresado todos los datos necesarios
                if (testTitle.isEmpty() || testIdUserID.isEmpty() || testTypeTest.matches("Selecciona el tipo de Test") || spinnerTypeTest.getSelectedItemPosition() == 0) {
                    Toast.makeText(getActivity(), "Tienes que llenar los datos de Configuración del Test", Toast.LENGTH_LONG).show();

                    // Mostramos errores en los campos correspondientes
                    testNameEdt.setError("Introduce el Título");
                    myTestIdUserID.setError("Introduce tu usuario");

                    // Verificamos si no se ha seleccionado un elemento del Spinner
                    if (spinnerTypeTest.getSelectedItemPosition() == 0) {
                        View selectedView = spinnerTypeTest.getSelectedView();
                        if (selectedView != null && selectedView instanceof TextView) {
                            ((TextView) selectedView).setError("*"); // Muestra un asterisco como mensaje de error
                        }
                    }
                } else if (strQuestion.isEmpty() || (!radioButtonTrue.isChecked() && !radioButtonFalse.isChecked())) {
                    // Validamos que se haya ingresado la pregunta y seleccionado al menos una opción
                    Toast.makeText(getActivity(), "Completa la pregunta y selecciona una respuesta", Toast.LENGTH_LONG).show();
                } else {
                    // Verificamos cuál RadioButton está seleccionado
                    if (radioButtonTrue.isChecked()) {
                        valorSeleccionado = radioButtonTrue.getText().toString();
                    } else if (radioButtonFalse.isChecked()) {
                        valorSeleccionado = radioButtonFalse.getText().toString();
                    }

                    // Agregamos la pregunta al almacenamiento (posiblemente, a una base de datos)
                    dbHelper.addNewQuestionsTF(idTestUniqueAsociation, strQuestion, valorSeleccionado);

                    // Mostramos un mensaje de éxito
                    Toast.makeText(getActivity(), "Pregunta agregada", Toast.LENGTH_LONG).show();

                    // Limpiamos los campos después de agregar la pregunta
                    questionTF.setText("");
                    radioButtonTrue.setChecked(false);
                    radioButtonFalse.setChecked(false);
                }
            }
        });



        /**
         * //configuramos el boton para agregar las preguntas del test Seleccion Multiple.
         * al precionar el boton de agregar pregunta.
         */
        addNewQuestionSM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Verifica si el CheckBox está marcado y establece el valor correspondiente
                boolean isOptionOneCorrect = checkBoxOneSM.isChecked();
                boolean isOptionTwoCorrect = checkBoxTwoSM.isChecked();
                boolean isOptionThreeCorrect = checkBoxThreeSM.isChecked();
                boolean isOptionFourCorrect = checkBoxFourSM.isChecked();


                String idTestUniqueAsociation;
                String strAnswOptionOneSM = String.valueOf(isOptionOneCorrect);
                String strAnswOptionTwoSM = String.valueOf(isOptionTwoCorrect);;
                String strAnswOptionThreeSM = String.valueOf(isOptionThreeCorrect);;
                String strAnswOptionFourSM = String.valueOf(isOptionFourCorrect);;



                //opcioines de la configuracion del test.
                String testTitle = testNameEdt.getText().toString();
                String testTypeTest = spinnerTypeTest.getSelectedItem().toString();
                String testIdUserID = myTestIdUserID.getText().toString();
                idTestUniqueAsociation = (testTitle + testTypeTest + testIdUserID).replaceAll(" ", "_");


                //verificamos que los datos necesarios esten correctamente completos para poder continuar
                if (testTitle.isEmpty() || testIdUserID.isEmpty() || testTypeTest.matches("Selecciona el tipo de Test")) {

                    testNameEdt.setError("Introduce el Titulo");
                    myTestIdUserID.setError("Introduce tu usuario");

                    // Verifica si no se ha seleccionado un elemento del Spinner
                    if (spinnerTypeTest.getSelectedItemPosition() == 0) {
                        View selectedView = spinnerTypeTest.getSelectedView();
                        if (selectedView != null && selectedView instanceof TextView) {
                            ((TextView) selectedView).setError("*"); // Muestra un asterisco como mensaje de error
                        }
                    }

                    Toast.makeText(getActivity(), "Tienes que llenar los datos de Configuracion del Test ", Toast.LENGTH_LONG).show();
                    return;
                }


                String strQuestionSM = questionSM.getText().toString();
                String strPOptionOneSM = optionOneSM.getText().toString();
                String strOptionTwoSM = optionTwoSM.getText().toString();
                String srtOptionThreeSM = optionThreeSM.getText().toString();
                String srtOptionFourSM = optionFourSM.getText().toString();

                // Agrega la pregunta y las opciones a la base de datos
                dbHelper.addNewQuestionsSM(idTestUniqueAsociation, strQuestionSM, strPOptionOneSM, strOptionTwoSM,
                        srtOptionThreeSM, srtOptionFourSM);

                dbHelper.addNewCorrectAnwserSM(idTestUniqueAsociation, strQuestionSM, strAnswOptionOneSM,
                        strAnswOptionTwoSM, strAnswOptionThreeSM, strAnswOptionFourSM);

                // despues de agregados, mostramos el mensage de exito al agregar.
                Toast.makeText(getActivity(), "Pregunta y respuestas agregadas", Toast.LENGTH_LONG).show();

                //limpiamos los datos de la preguntas y las respuestas
                questionSM.setText("");
                optionOneSM.setText("");
                optionTwoSM.setText("");
                optionThreeSM.setText("");
                optionFourSM.setText("");

                //limpiamos los CheckBox
                checkBoxOneSM.setChecked(false);
                checkBoxTwoSM.setChecked(false);
                checkBoxThreeSM.setChecked(false);
                checkBoxFourSM.setChecked(false);


            }
        });


        /**
         * //configuramos el boton para agregar las preguntas de Tipo Test
         * al precionar el boton de agregar pregunta.
         */
        addNewQuestionTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //opcioines de la configuracion del test.
                String testTitle = testNameEdt.getText().toString();
                String testTypeTest = spinnerTypeTest.getSelectedItem().toString();
                String testIdUserID = myTestIdUserID.getText().toString();
                String idTestUniqueAsociation = (testTitle + testTypeTest + testIdUserID).replaceAll(" ", "_");


                // Verifica si el CheckBox está marcado y establece el valor correspondiente
                boolean isOptionOneCorrect = radioButtonOptionOneTT.isChecked();
                boolean isOptionTwoCorrect = radioButtonOptionTwoTT.isChecked();
                boolean isOptionThreeCorrect = radioButtonOptionThreeTT.isChecked();
                boolean isOptionFourCorrect = radioButtonOptionFourTT.isChecked();

                String strAnswOptionOneSM = String.valueOf(isOptionOneCorrect);
                String strAnswOptionTwoSM = String.valueOf(isOptionTwoCorrect);;
                String strAnswOptionThreeSM = String.valueOf(isOptionThreeCorrect);;
                String strAnswOptionFourSM = String.valueOf(isOptionFourCorrect);;


                //verificamos que los datos necesarios esten correctamente completos para poder continuar
                if (testTitle.isEmpty() || testIdUserID.isEmpty() || testTypeTest.matches("Selecciona el tipo de Test")) {

                    testNameEdt.setError("Introduce el Titulo");
                    myTestIdUserID.setError("Introduce tu usuario");

                    // Verifica si no se ha seleccionado un elemento del Spinner
                    if (spinnerTypeTest.getSelectedItemPosition() == 0) {
                        View selectedView = spinnerTypeTest.getSelectedView();
                        if (selectedView != null && selectedView instanceof TextView) {
                            ((TextView) selectedView).setError("*"); // Muestra un asterisco como mensaje de error
                        }
                    }

                    //mostramos un mensage para notificar la accion a realizar.
                    Toast.makeText(getActivity(), "Tienes que llenar los datos de Configuracion del Test ", Toast.LENGTH_LONG).show();
                    return;
                }

                // Verificar que al menos un RadioButton esté seleccionado
                if (!radioButtonOptionOneTT.isChecked() && !radioButtonOptionTwoTT.isChecked()
                        && !radioButtonOptionThreeTT.isChecked() && !radioButtonOptionFourTT.isChecked()) {

                    // Marcar los RadioButtons como error cambiando el fondo o el color del texto
                    radioButtonOptionOneTT.setBackgroundColor(Color.RED);
                    radioButtonOptionTwoTT.setBackgroundColor(Color.RED);
                    radioButtonOptionThreeTT.setBackgroundColor(Color.RED);
                    radioButtonOptionFourTT.setBackgroundColor(Color.RED);

                    //mostramos un mensage para notificar la accion a realizar.
                    Toast.makeText(getActivity(), "Selecciona al menos una opción correcta", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Obtener las respuestas ingresadas por el usuario desde los campos de texto
                String strQuestionTT = questionTT.getText().toString();
                String strPOptionOneTT = optionOneTT.getText().toString();
                String strOptionTwoTT = optionTwoTT.getText().toString();
                String srtOptionThreeTT = optionThreeTT.getText().toString();
                String srtOptionFourTT = optionFourTT.getText().toString();

                // Agregar la nueva pregunta a la base de datos utilizando un helper de base de datos personalizado
                dbHelper.addNewQuestionsTT(idTestUniqueAsociation, strQuestionTT, strPOptionOneTT, strOptionTwoTT,
                        srtOptionThreeTT, srtOptionFourTT);

                // Agregar la respuesta correcta asociada a la nueva pregunta a la base de datos
                dbHelper.addNewCorrectAnwserTT(idTestUniqueAsociation, strQuestionTT, strAnswOptionOneSM,
                        strAnswOptionTwoSM, strAnswOptionThreeSM, strAnswOptionFourSM);

                // Mostrar un mensaje Toast para indicar que la pregunta ha sido agregada exitosamente
                Toast.makeText(getActivity(), "Pregunta agregada", Toast.LENGTH_SHORT).show();

                // Restaurar el fondo original de los RadioButtons después de agregar la pregunta
                radioButtonOptionOneTT.setBackgroundColor(Color.TRANSPARENT);
                radioButtonOptionTwoTT.setBackgroundColor(Color.TRANSPARENT);
                radioButtonOptionThreeTT.setBackgroundColor(Color.TRANSPARENT);
                radioButtonOptionFourTT.setBackgroundColor(Color.TRANSPARENT);

                // Limpiar los campos de texto para la siguiente entrada
                questionTT.setText("");
                optionOneTT.setText("");
                optionTwoTT.setText("");
                optionThreeTT.setText("");
                optionFourTT.setText("");

                // Desmarcar todos los RadioButtons para la siguiente pregunta
                radioButtonOptionOneTT.setChecked(false);
                radioButtonOptionTwoTT.setChecked(false);
                radioButtonOptionThreeTT.setChecked(false);
                radioButtonOptionFourTT.setChecked(false);

            }
        });

        //Configuramos el Spinner de Categorias
        spinnerCategoryTest = view.findViewById(R.id.spnCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_test_category, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoryTest.setAdapter(adapter);

        //COnfiguramos el Spinner de Tipos de Test
        Spinner spinnerTypeTest = view.findViewById(R.id.spnTypeTest);
        ArrayAdapter<CharSequence> adapterTypeTest = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_test_type, android.R.layout.simple_spinner_item);
        adapterTypeTest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeTest.setAdapter(adapterTypeTest);


        // Método para borrar los datos de la pregunta de Verdadero/Falso
        deleteQuestionTF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dejamos el campo de la pregunta vacío
                questionTF.setText("");

                // Desmarcamos los RadioButtons
                radioButtonTrue.setChecked(false);
                radioButtonFalse.setChecked(false);
            }
        });

        // Método para borrar los datos de la pregunta de Opción Múltiple
        deleteQuestionTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Restaurar el fondo original de los RadioButtons después de agregar la pregunta
                radioButtonOptionOneTT.setChecked(false);
                radioButtonOptionTwoTT.setChecked(false);
                radioButtonOptionThreeTT.setChecked(false);
                radioButtonOptionFourTT.setChecked(false);

                // Dejamos los campos de la pregunta y opciones vacíos
                questionTT.setText("");
                optionOneTT.setText("");
                optionTwoTT.setText("");
                optionThreeTT.setText("");
                optionFourTT.setText("");
            }
        });

        // Método para borrar los datos de la pregunta de Selección Múltiple
        deleteQuestionSM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Limpiamos los datos de la pregunta y las opciones de respuesta
                questionSM.setText("");
                optionOneSM.setText("");
                optionTwoSM.setText("");
                optionThreeSM.setText("");
                optionFourSM.setText("");

                // Desmarcamos los CheckBox
                checkBoxOneSM.setChecked(false);
                checkBoxTwoSM.setChecked(false);
                checkBoxThreeSM.setChecked(false);
                checkBoxFourSM.setChecked(false);
            }
        });

        return view;
    }


}