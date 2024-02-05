package com.example.myexampleevaluatest.DabaBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.example.myexampleevaluatest.data.SelecMultTestData;
import com.example.myexampleevaluatest.data.TestData;
import com.example.myexampleevaluatest.data.TestTypeTestData;
import com.example.myexampleevaluatest.data.TrueFalseTestData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {


    /**
     * Definición de variables para la base de datos y la tabla que contiene todos los test
     */
    private static final String DB_NAME = "evaluaTestDB";

    // Versión de la base de datos
    private static final int DB_VERSION = 1;

    // Nombre de la tabla principal de pruebas
    private static final String TABLE_NAME = "allTest";

    // Columnas de la tabla principal de pruebas
    private static final String ID_COL = "id";
    private static final String TITLE_COL = "name";
    private static final String DURATION_COL = "numbersOfQuestions";
    private static final String DESCRIPTION_COL = "description";
    private static final String CATEGORY_COL = "category";
    private static final String IDUSER_COL = "idUser";
    private static final String TYPETEST_COL = "TypeOfTest";
    private static final String ID_TEST_UNIQUE_ASOCIATION = "uniqueIDForTest";

    /**
     * Definición de variables para la tabla de preguntas Verdadero/Falso
     */
    private static final String TABLE_NAME_TF = "tableTrueFalse";
    private static final String ID_TEST_UNIQUE_ASOCIATION_TF = "uniqueIDForTestTF";
    private static final String ID_QUESTION_COL_TF = "idQuestion";
    private static final String QUESTION_COL_TF = "question";
    private static final String SELECTED_VALUE_COL_TF = "selectecValue";

    /**
     * Definición de variables para la tabla de preguntas Selección Múltiple
     */
    private static final String TABLE_NAME_SM = "tableSelecMult";
    private static final String ID_TEST_UNIQUE_ASOCIATION_SM = "uniqueIDForTestSM";
    private static final String ID_QUESTION_COL_SM = "idQuestion";
    private static final String QUESTION_COL_SM = "question";
    private static final String OPTION_ONE_SM = "optionOne";
    private static final String OPTION_TWO_SM = "optionTwo";
    private static final String OPTION_THREE_SM = "optionThree";
    private static final String OPTION_FOUR_SM = "optionFour";

    /**
     * Definición de variables para la tabla de opciones correctas de Selección Múltiple
     */
    private static final String TABLE_CORRECT_ANSWERS_SM = "tableCorrectAnswerSelecMult";
    private static final String ID_COL_CORRECT_ANSWER_SM = "idQuestion";
    private static final String QUESTION_CORRECT_ANSWERS_SM = "question";
    private static final String ID_TEST_UNIQUE_ASOCIATION_CORRECT_ANSWER_SM = "uniqueIDForTestSM";
    private static final String OPTION_ONE_CORRECT_ANSWERS_SM = "optionOne";
    private static final String OPTION_TWO_CORRECT_ANSWERS_SM = "optionTwo";
    private static final String OPTION_THREE_CORRECT_ANSWERS_SM = "optionThree";
    private static final String OPTION_FOUR_CORRECT_ANSWERS_SM = "optionFour";

    /**
     * Definición de variables para la tabla de preguntas Tipo Test
     */
    private static final String TABLE_NAME_TT = "tableTestType";
    private static final String ID_TEST_UNIQUE_ASOCIATION_TT = "uniqueIDForTestTT";
    private static final String ID_QUESTION_COL_TT = "idQuestion";
    private static final String QUESTION_COL_TT = "question";
    private static final String OPTION_ONE_TT = "optionOne";
    private static final String OPTION_TWO_TT = "optionTwo";
    private static final String OPTION_THREE_TT = "optionThree";
    private static final String OPTION_FOUR_TT = "optionFour";

    /**
     * Definición de variables para la tabla de opciones correctas de Tipo Test
     */
    private static final String TABLE_CORRECT_ANSWERS_TT = "correctAnswerTestType";
    private static final String ID_COL_CORRECT_ANSWER_TT = "idQuestion";
    private static final String QUESTION_CORRECT_ANSWERS_TT = "question";
    private static final String ID_TEST_UNIQUE_ASOCIATION_CORRECT_ANSWER_TT = "uniqueIDForTestTT";
    private static final String OPTION_ONE_CORRECT_ANSWERS_TT = "optionOne";
    private static final String OPTION_TWO_CORRECT_ANSWERS_TT = "optionTwo";
    private static final String OPTION_THREE_CORRECT_ANSWERS_TT = "optionThree";
    private static final String OPTION_FOUR_CORRECT_ANSWERS_TT = "optionFour";

    /**
     * Variables para la tabla de usuarios
     */
    private static final String TABLE_NAME_USERS = "users";
    private static final String USER_ID_COL = "user_id";
    private static final String NAME_COL = "name";
    private static final String USERNAME_COL = "username";
    private static final String EMAIL_COL = "email";
    private static final String PASSWORD_COL = "password";

    private static final String IS_LOGGED_IN = "isLoggedIn";

    private static final String NUM_FINISH_TEST = "numFinishTest";

    /**
     * Nombre de la tabla y columnas
     */
    private static final String TABLE_NAME_USER_STATUS = "UserStatus";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_IS_LOGGED_IN = "is_logged_in";


    // Constructor del manejador de la base de datos
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creación de la tabla principal
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ID_TEST_UNIQUE_ASOCIATION + " TEXT, "
                + TITLE_COL + " TEXT, "
                + DURATION_COL + " TEXT, "
                + DESCRIPTION_COL + " TEXT, "
                + CATEGORY_COL + " TEXT, "
                + IDUSER_COL + " TEXT, "
                + TYPETEST_COL + " TEXT)";

        // Creación de la tabla  Verdadero/Falso
        String queryRespTF = "CREATE TABLE " + TABLE_NAME_TF + " ("
                + ID_QUESTION_COL_TF + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ID_TEST_UNIQUE_ASOCIATION_TF + " TEXT, "
                + QUESTION_COL_TF + " TEXT, "
                + SELECTED_VALUE_COL_TF + " TEXT)";

        // Creación de la tabla  Selección Múltiple
        String queryRespSM = "CREATE TABLE " + TABLE_NAME_SM + " ("
                + ID_QUESTION_COL_SM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ID_TEST_UNIQUE_ASOCIATION_SM + " TEXT, "
                + QUESTION_COL_SM + " TEXT, "
                + OPTION_ONE_SM + " TEXT, "
                + OPTION_TWO_SM + " TEXT, "
                + OPTION_THREE_SM + " TEXT, "
                + OPTION_FOUR_SM + " TEXT)";


        // Creación de la tabla para Opciones correstas de Selección Múltiple
        String queryCorrectAnswerSM = "CREATE TABLE " + TABLE_CORRECT_ANSWERS_SM + " ("
                + ID_COL_CORRECT_ANSWER_SM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ID_TEST_UNIQUE_ASOCIATION_CORRECT_ANSWER_SM + " TEXT, "
                + QUESTION_CORRECT_ANSWERS_SM + " TEXT, "
                + OPTION_ONE_CORRECT_ANSWERS_SM + " TEXT, "
                + OPTION_TWO_CORRECT_ANSWERS_SM + " TEXT, "
                + OPTION_THREE_CORRECT_ANSWERS_SM + " TEXT, "
                + OPTION_FOUR_CORRECT_ANSWERS_SM + " TEXT)";


        // Creación de la tabla de Tipo Test
        String queryRespTT = "CREATE TABLE " + TABLE_NAME_TT + " ("
                + ID_QUESTION_COL_TT + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ID_TEST_UNIQUE_ASOCIATION_TT + " TEXT, "
                + QUESTION_COL_TT + " TEXT, "
                + OPTION_ONE_TT + " TEXT, "
                + OPTION_TWO_TT + " TEXT, "
                + OPTION_THREE_TT + " TEXT, "
                + OPTION_FOUR_TT + " TEXT)";


        // Creación de la tabla para Opciones correstas de Tipo Test
        String queryCorrectAnswerTT = "CREATE TABLE " + TABLE_CORRECT_ANSWERS_TT + " ("
                + ID_COL_CORRECT_ANSWER_TT + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ID_TEST_UNIQUE_ASOCIATION_CORRECT_ANSWER_TT + " TEXT, "
                + QUESTION_CORRECT_ANSWERS_TT + " TEXT, "
                + OPTION_ONE_CORRECT_ANSWERS_TT + " TEXT, "
                + OPTION_TWO_CORRECT_ANSWERS_TT + " TEXT, "
                + OPTION_THREE_CORRECT_ANSWERS_TT + " TEXT, "
                + OPTION_FOUR_CORRECT_ANSWERS_TT + " TEXT)";


        // Creación de la tabla de usuarios
        String queryUsers = "CREATE TABLE " + TABLE_NAME_USERS + " ("
                + USER_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT, "
                + USERNAME_COL + " TEXT, "
                + EMAIL_COL + " TEXT, "
                + PASSWORD_COL + " TEXT, "
                + NUM_FINISH_TEST + " TEXT, "
                + IS_LOGGED_IN +" DEFAULT 0)";


        // Consulta SQL para crear la tabla
         String queryUsersStatus =
                "CREATE TABLE " + TABLE_NAME_USER_STATUS + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_IS_LOGGED_IN + " INTEGER)";

        // Ejecución de las consultas SQL
        db.execSQL(query);
        db.execSQL(queryRespTF);
        db.execSQL(queryRespSM);
        db.execSQL(queryRespTT);
        db.execSQL(queryUsers);
        db.execSQL(queryUsersStatus);
        db.execSQL(queryCorrectAnswerSM);
        db.execSQL(queryCorrectAnswerTT);


    }


    // Este método se utiliza para agregar un nuevo curso/test a nuestra base de datos SQLite.
    public void addNewTest(String courseName, String idUniqueTest, String courseDuration, String courseDescription, String courseTracks, String myIdUser, String typeTest) {
        // Obtenemos una referencia de escritura de la base de datos.
        SQLiteDatabase db = this.getWritableDatabase();

        // Creamos un objeto ContentValues para almacenar los valores que queremos insertar.
        ContentValues values = new ContentValues();

        // Asignamos los valores junto con sus respectivas claves.
        values.put(TITLE_COL, courseName);
        values.put(DURATION_COL, courseDuration);
        values.put(DESCRIPTION_COL, courseDescription);
        values.put(CATEGORY_COL, courseTracks);
        values.put(IDUSER_COL, myIdUser);
        values.put(TYPETEST_COL, typeTest);
        values.put(ID_TEST_UNIQUE_ASOCIATION, idUniqueTest);

        // Insertamos los valores en nuestra tabla.
        db.insert(TABLE_NAME, null, values);

        // Cerramos la base de datos después de agregar los datos.
        db.close();
    }

    // Método para leer todos los tests y devolver una lista de TestData.
    public ArrayList<TestData> readTests() {
        SQLiteDatabase db = this.getReadableDatabase(); // Obtenemos una referencia de lectura de la base de datos.
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_NAME, null); // Creamos un cursor con la consulta SQL para leer datos de la base de datos.
        ArrayList<TestData> courseModalArrayList = new ArrayList<>(); // Creamos una nueva lista de TestData.

        // Movemos el cursor a la primera posición.
        if (cursorCourses.moveToFirst()) {
            do {
                // Añadimos datos del cursor a la lista.
                courseModalArrayList.add(new TestData(
                        cursorCourses.getString(2), // TITLE
                        cursorCourses.getString(5), // CATEGORY
                        cursorCourses.getString(3), // NUMBER_OF_QUESTIONS
                        cursorCourses.getString(4), // DESCRIPTION
                        cursorCourses.getString(6), // USER
                        cursorCourses.getString(7), // IDUSER
                        cursorCourses.getString(1)  // TYPE_TEST
                ));
            } while (cursorCourses.moveToNext()); // Movemos el cursor al siguiente.
        }

        cursorCourses.close(); // Cerramos el cursor.
        return courseModalArrayList; // Devolvemos la lista de TestData.
    }



    // se llama a este método para comprobar si la tabla ya existe.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TF);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_TT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SM);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CORRECT_ANSWERS_SM);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CORRECT_ANSWERS_TT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER_STATUS);
            onCreate(db);
        }

    }

    /**
     * Método para agregar nuevas preguntas de tipo Verdadero/Falso a la base de datos.
     *
     * @param uniqueTestId    Identificador único asociado al test.
     * @param testQuestion    Pregunta del test.
     * @param selectedValue   Valor seleccionado (Verdadero/Falso).
     */
    public void addNewQuestionsTF(String uniqueTestId, String testQuestion, String selectedValue) {
        // Se crea una variable para nuestra base de datos SQLite y se llama al método writable
        // ya que estamos escribiendo datos en nuestra base de datos.
        SQLiteDatabase db = this.getWritableDatabase();

        // Se crea una variable para los valores de contenido.
        ContentValues values = new ContentValues();

        // Se asignan los valores junto con sus pares clave-valor.
        values.put(ID_TEST_UNIQUE_ASOCIATION_TF, uniqueTestId);
        values.put(QUESTION_COL_TF, testQuestion);
        values.put(SELECTED_VALUE_COL_TF, selectedValue);

        // Después de agregar todos los valores, se pasan los valores de contenido a nuestra tabla.
        db.insert(TABLE_NAME_TF, null, values);

        // Finalmente, cerramos nuestra base de datos después de agregar datos.
        db.close();
    }



    /**
     * Agrega una nueva pregunta de seleccion múltiple a la base de datos.
     *
     * @param uniqueTestId Identificador único asociado al test.
     * @param testQuestion Pregunta del test.
     * @param optionOne Opción 1.
     * @param optionTwo Opción 2.
     * @param optionThree Opción 3.
     * @param optionFour Opción 4.
     */
    public void addNewQuestionsSM(String uniqueTestId, String testQuestion, String optionOne, String optionTwo, String optionThree, String optionFour) {
        // Creamos una variable para nuestra base de datos SQLite y llamamos al método writable
        // ya que estamos escribiendo datos en nuestra base de datos.
        SQLiteDatabase db = this.getWritableDatabase();

        // Creamos una variable para los valores de contenido.
        ContentValues values = new ContentValues();

        // Pasamos todos los valores junto con sus claves y pares de valores.
        values.put(ID_TEST_UNIQUE_ASOCIATION_SM, uniqueTestId);
        values.put(QUESTION_COL_SM, testQuestion);
        values.put(OPTION_ONE_SM, optionOne);
        values.put(OPTION_TWO_SM, optionTwo);
        values.put(OPTION_THREE_SM, optionThree);
        values.put(OPTION_FOUR_SM, optionFour);

        // Después de agregar todos los valores, pasamos
        // los valores de contenido a nuestra tabla.
        db.insert(TABLE_NAME_SM, null, values);

        // Finalmente, cerramos nuestra base de datos después de agregar datos.
        db.close();
    }

    /**
     * Agrega nuevas preguntas de tipo TestType a la base de datos.
     *
     * @param uniqueTestId Un identificador único asociado al test.
     * @param testQuestion La pregunta del test.
     * @param optionOne Opción de respuesta 1.
     * @param optionTwo Opción de respuesta 2.
     * @param optionThree Opción de respuesta 3.
     * @param optionFour Opción de respuesta 4.
     */
    public void addNewQuestionsTT(String uniqueTestId, String testQuestion, String optionOne,
                                  String optionTwo, String optionThree, String optionFour) {
        // Obtenemos una instancia de la base de datos en modo escritura.
        SQLiteDatabase db = this.getWritableDatabase();

        // Creamos un objeto ContentValues para almacenar los valores.
        ContentValues values = new ContentValues();

        // Asignamos los valores a las columnas
        values.put(ID_TEST_UNIQUE_ASOCIATION_TT, uniqueTestId);
        values.put(QUESTION_COL_TT, testQuestion);
        values.put(OPTION_ONE_TT, optionOne);
        values.put(OPTION_TWO_TT, optionTwo);
        values.put(OPTION_THREE_TT, optionThree);
        values.put(OPTION_FOUR_TT, optionFour);

        // Insertamos los valores en la tabla.
        db.insert(TABLE_NAME_TT, null, values);

        // Cerramos la conexión a la base de datos.
        db.close();
    }


    /**
     * Agrega una nueva fila de resp. correcta asociada a la tabla de seleccion múltiple a la base de datos.
     *
     * @param uniqueTestId Identificador único asociado al test.
     * @param testQuestion Pregunta del test.
     * @param optionOne Opción 1.
     * @param optionTwo Opción 2.
     * @param optionThree Opción 3.
     * @param optionFour Opción 4.
     */

    public void addNewCorrectAnwserSM(String uniqueTestId, String testQuestion, String optionOne, String optionTwo, String optionThree, String optionFour) {
        // Creamos una variable para nuestra base de datos SQLite y llamamos al método writable
        // ya que estamos escribiendo datos en nuestra base de datos.
        SQLiteDatabase db = this.getWritableDatabase();

        // Creamos una variable para los valores de contenido.
        ContentValues values = new ContentValues();

        // Pasamos todos los valores junto con sus claves y pares de valores.
        values.put(ID_TEST_UNIQUE_ASOCIATION_CORRECT_ANSWER_SM, uniqueTestId);
        values.put(QUESTION_CORRECT_ANSWERS_SM, testQuestion);
        values.put(OPTION_ONE_CORRECT_ANSWERS_SM, optionOne);
        values.put(OPTION_TWO_CORRECT_ANSWERS_SM, optionTwo);
        values.put(OPTION_THREE_CORRECT_ANSWERS_SM, optionThree);
        values.put(OPTION_FOUR_CORRECT_ANSWERS_SM, optionFour);

        // Después de agregar todos los valores, pasamos
        // los valores de contenido a nuestra tabla.
        db.insert(TABLE_CORRECT_ANSWERS_SM, null, values);

        // Finalmente, cerramos nuestra base de datos después de agregar datos.
        db.close();
    }


    /**
     * Agrega una nueva fila de resp. correcta asociada a la tabla de Test Type a la base de datos.
     *
     * @param uniqueTestId Identificador único asociado al test.
     * @param testQuestion Pregunta del test.
     * @param optionOne Opción 1.
     * @param optionTwo Opción 2.
     * @param optionThree Opción 3.
     * @param optionFour Opción 4.
     */

    public void addNewCorrectAnwserTT(String uniqueTestId, String testQuestion, String optionOne, String optionTwo, String optionThree, String optionFour) {
        // Creamos una variable para nuestra base de datos SQLite y llamamos al método writable
        // ya que estamos escribiendo datos en nuestra base de datos.
        SQLiteDatabase db = this.getWritableDatabase();

        // Creamos una variable para los valores de contenido.
        ContentValues values = new ContentValues();

        // Pasamos todos los valores junto con sus claves y pares de valores.
        values.put(ID_TEST_UNIQUE_ASOCIATION_CORRECT_ANSWER_TT, uniqueTestId);
        values.put(QUESTION_CORRECT_ANSWERS_TT, testQuestion);
        values.put(OPTION_ONE_CORRECT_ANSWERS_TT, optionOne);
        values.put(OPTION_TWO_CORRECT_ANSWERS_TT, optionTwo);
        values.put(OPTION_THREE_CORRECT_ANSWERS_TT, optionThree);
        values.put(OPTION_FOUR_CORRECT_ANSWERS_TT, optionFour);

        // Después de agregar todos los valores, pasamos
        // los valores de contenido a nuestra tabla.
        db.insert(TABLE_CORRECT_ANSWERS_TT, null, values);

        // Finalmente, cerramos nuestra base de datos después de agregar datos.
        db.close();
    }



    /**
     * Obtiene y devuelve la lista de preguntas y opciones asociadas desde la tabla Selec. Mult.
     *
     * @param uniqueTestId Identificador único asociado al test.
     * @return Lista de preguntas y opciones asociadas.
     */
    public ArrayList<SelecMultTestData> readTableSelctMult(String uniqueTestId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<SelecMultTestData> questionsAndOptionsList = new ArrayList<>();

        // Consulta para obtener las preguntas y opciones asociadas al uniqueTestId de manera aleatoria
        String selectSQL = "SELECT * FROM " + TABLE_NAME_SM + " WHERE " + ID_TEST_UNIQUE_ASOCIATION_SM + " = ? ORDER BY RANDOM()";
        Cursor cursor = db.rawQuery(selectSQL, new String[]{uniqueTestId});

        if (cursor.moveToFirst()) {
            do {
                // Agrega la pregunta y opciones a la lista
                questionsAndOptionsList.add(new SelecMultTestData(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                ));
            } while (cursor.moveToNext());
        }

        // Cierra el cursor y la conexión a la base de datos
        cursor.close();
        db.close();

        return questionsAndOptionsList;
    }




    /**
     * Obtiene y devuelve la lista de preguntas y opciones asociadas desde la tabla TestType.
     *
     * @param uniqueTestId Identificador único asociado al test.
     * @return Lista de preguntas y opciones asociadas.
     */
    public ArrayList<TestTypeTestData> readTableTestType(String uniqueTestId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<TestTypeTestData> questionsAndOptionsList = new ArrayList<>();

        // Consulta para obtener las preguntas y opciones asociadas al uniqueTestId
        String selectSQL = "SELECT * FROM " + TABLE_NAME_TT + " WHERE " + ID_TEST_UNIQUE_ASOCIATION_TT + " = ?";
        Cursor cursor = db.rawQuery(selectSQL, new String[]{uniqueTestId});

        if (cursor.moveToFirst()) {
            do {
                // Agrega la pregunta y opciones a la lista
                questionsAndOptionsList.add(new TestTypeTestData(
                        cursor.getInt(0),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(1)
                ));
            } while (cursor.moveToNext());
        }

        // Cierra el cursor y la conexión a la base de datos
        cursor.close();
        db.close();

        return questionsAndOptionsList;
    }



    /**
     * Obtiene las respuestas correctas para una pregunta de selección múltiple.
     *
     * @param uniqueTestId El identificador único asociado al test.
     * @param question La pregunta para la que se obtendrán las respuestas correctas.
     * @return Una lista de valores booleanos que indica si cada opción es una respuesta correcta.
     */
    public List<Boolean> getAnswerForQuestionSM(String uniqueTestId, String question) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Especifica las columnas que contienen las respuestas correctas
        String[] columns = {
                OPTION_ONE_CORRECT_ANSWERS_SM,
                OPTION_TWO_CORRECT_ANSWERS_SM,
                OPTION_THREE_CORRECT_ANSWERS_SM,
                OPTION_FOUR_CORRECT_ANSWERS_SM
        };

        // Consulta la tabla de respuestas correctas
        Cursor cursor = db.query(
                TABLE_CORRECT_ANSWERS_SM,
                columns,
                ID_TEST_UNIQUE_ASOCIATION_CORRECT_ANSWER_SM + " = ? AND " +
                        QUESTION_CORRECT_ANSWERS_SM + " = ?",
                new String[]{uniqueTestId, question},
                null, null, null
        );

        // Lista para almacenar las respuestas correctas
        List<Boolean> correctOptions = new ArrayList<>();

        if (cursor.moveToFirst()) {
            // Itera a través de las columnas y agrega el valor booleano correspondiente a la lista
            for (String column : columns) {
                int columnIndex = cursor.getColumnIndex(column);
                if (columnIndex >= 0) {
                    // Obtiene el valor de la columna y lo convierte a booleano
                    String value = cursor.getString(columnIndex);
                    correctOptions.add(value.equals("true"));
                }
            }
        }

        // Cierra el cursor y la conexión a la base de datos
        cursor.close();
        db.close();

        return correctOptions;
    }



    /**
     * Obtiene las respuestas correctas para una pregunta de Test Type
     *
     * @param uniqueTestId El identificador único asociado al test.
     * @param question La pregunta para la que se obtendrán las respuestas correctas.
     * @return Una lista de valores booleanos que indica si cada opción es una respuesta correcta.
     */

    public List<Boolean> getAnswerForQuestionTestType(String uniqueTestId, String question) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Especifica las columnas que contienen las respuestas correctas
        String[] columns = {
                OPTION_ONE_CORRECT_ANSWERS_TT,
                OPTION_TWO_CORRECT_ANSWERS_TT,
                OPTION_THREE_CORRECT_ANSWERS_TT,
                OPTION_FOUR_CORRECT_ANSWERS_TT
        };


        Log.d("Flujo de código", "Consulta SQL: " + SQLiteQueryBuilder.buildQueryString(false, TABLE_CORRECT_ANSWERS_TT, columns,
                ID_TEST_UNIQUE_ASOCIATION_CORRECT_ANSWER_TT + " = ? AND " + QUESTION_CORRECT_ANSWERS_TT + " = ?",
                null, null, null, null));


        // Consulta la tabla de respuestas correctas para el Test Type
        Cursor cursor = db.query(
                TABLE_CORRECT_ANSWERS_TT,
                columns,
                ID_TEST_UNIQUE_ASOCIATION_CORRECT_ANSWER_TT + " = ? AND " +
                        QUESTION_CORRECT_ANSWERS_TT + " = ?",
                new String[]{uniqueTestId, question},
                null, null, null
        );

        // Lista para almacenar las respuestas correctas
        List<Boolean> correctOptions = new ArrayList<>();

        if (cursor.moveToFirst()) {
            // Itera a través de las columnas y agrega el valor booleano correspondiente a la lista
            for (String column : columns) {
                int columnIndex = cursor.getColumnIndex(column);
                if (columnIndex >= 0) {

                    // Obtiene el valor de la columna y lo convierte a booleano
                    String value = cursor.getString(columnIndex);
                    boolean isCorrect = value.trim().equalsIgnoreCase("true");

                    // Agrega un log para mostrar la opción en la columna y si es verdadera o falsa
                    Log.d("Flujo de código", "Opción en la columna " + column + ": " + value + " (Correcta: " + isCorrect + ")");

                    // Agrega la respuesta a la lista
                    correctOptions.add(isCorrect);
                }
            }
        } else{
            Log.e("Flujo de código", "No se movió al primer resultado del cursor");
        }

        // Cierra el cursor y la conexión a la base de datos
        cursor.close();
        db.close();

        // Agrega un log para mostrar las respuestas correctas obtenidas
        Log.d("Flujo de código", "Respuestas correctas: " + correctOptions);

        return correctOptions;
    }





    // Este método se usa para recuperar datos de la tabla TrueFalse basados en un identificador único.
    public ArrayList<TrueFalseTestData> getDataFromTableTF(String uniqueTestIdFromTable1) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<TrueFalseTestData> relatedDataFromTable2 = new ArrayList<>();

        // Consulta para obtener datos de la tabla TrueFalse asociados a un test único
        String query = "SELECT * FROM " + TABLE_NAME_TF + " WHERE " + ID_TEST_UNIQUE_ASOCIATION_TF + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{uniqueTestIdFromTable1});

        // Verificar si hay datos en el cursor
        if (cursor.moveToFirst()) {
            do {
                // Crear un objeto TrueFalseTestData con los datos obtenidos de la consulta
                TrueFalseTestData data = new TrueFalseTestData(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(3));
                relatedDataFromTable2.add(data);
            } while (cursor.moveToNext());
        }
        // Cerrar el cursor para liberar recursos
        cursor.close();
        // Devolver la lista de datos relacionados
        return relatedDataFromTable2;
    }


    /**
     * Método para buscar la respuesta a una pregunta específica de Verdadero/Falso.
     *
     * @param idQuestion ID de la pregunta que se está buscando.
     * @param question   Texto de la pregunta que se está buscando.
     * @return La respuesta correcta para la pregunta encontrada, o null si la pregunta no se encuentra.
     */
    public String getAnswerForQuestionTF(int idQuestion, String question) {
        // Obtener todos los datos de la tabla TrueFalse
        ArrayList<TrueFalseTestData> allData = getAllDataFromTableTF();

        // Iterar a través de los datos para encontrar la respuesta para la pregunta específica
        for (TrueFalseTestData data : allData) {
            // Compara el ID de la pregunta y el texto de la pregunta
            if (data.getId() == idQuestion && data.getQuestion().equals(question)) {
                // Devuelve la respuesta correcta para la pregunta encontrada
                return data.getCorrectAnswer();
            }
        }

        // Si no se encuentra la pregunta, puedes devolver un valor predeterminado o null
        return null;
    }




    //este metodo recupera todos los datos de la tabla TrueFalse
    public ArrayList<TrueFalseTestData> getAllDataFromTableTF() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<TrueFalseTestData> allData = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_NAME_TF;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                TrueFalseTestData data = new TrueFalseTestData(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(3));
                allData.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return allData;


    }


    // Método para buscar pruebas en la base de datos
    public ArrayList<TestData> searchTests(String searchQuery) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<TestData> testModalArrayList = new ArrayList<>();

        // Consulta SQL para buscar pruebas cuyos nombres contienen el valor de searchQuery
        String query = "SELECT " + TITLE_COL + ", " + CATEGORY_COL + ", " + DURATION_COL + ", " +
                DESCRIPTION_COL + ", " + IDUSER_COL + ", " + TYPETEST_COL + ", " + ID_COL +
                " FROM " + TABLE_NAME + " WHERE " + TITLE_COL + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + searchQuery + "%"};

        // Ejecutar la consulta
        Cursor cursorTests = db.rawQuery(query, selectionArgs);

        // Obtener los índices de las columnas en el cursor
        int columnIndexTitle = cursorTests.getColumnIndex(TITLE_COL);
        int columnIndexCategory = cursorTests.getColumnIndex(CATEGORY_COL);
        int columnIndexDuration = cursorTests.getColumnIndex(DURATION_COL);
        int columnIndexDescription = cursorTests.getColumnIndex(DESCRIPTION_COL);
        int columnIndexUserId = cursorTests.getColumnIndex(IDUSER_COL);
        int columnIndexTypeTest = cursorTests.getColumnIndex(TYPETEST_COL);
        int columnIndexId = cursorTests.getColumnIndex(ID_COL);

        // Mostrar información de búsqueda en el log
        Log.d("DBHelper", "Search term: " + searchQuery);
        Log.d("DBHelper", "Search results: " + cursorTests.getCount() + " results");

        // Iterar sobre los resultados de la consulta
        while (cursorTests.moveToNext()) {
            String title = cursorTests.getString(columnIndexTitle);
            String category = cursorTests.getString(columnIndexCategory);
            String duration = cursorTests.getString(columnIndexDuration);
            String description = cursorTests.getString(columnIndexDescription);
            String userId = cursorTests.getString(columnIndexUserId);
            String typeTest = cursorTests.getString(columnIndexTypeTest);
            String id = cursorTests.getString(columnIndexId);

            // Crear un objeto TestData y agregarlo a la lista
            testModalArrayList.add(new TestData(title, category, duration, description, userId, typeTest, id));
        }

        // Cerrar el cursor para liberar recursos
        cursorTests.close();

        // Devolver la lista de resultados de la búsqueda
        return testModalArrayList;
    }


    //metodo que nos permite eliminar un test
    public void eliminarTest(String testId) {
        Log.d("DBHelper", "Eliminando test con ID: " + testId);
        SQLiteDatabase db = this.getWritableDatabase();

        // Definir la condición para la eliminación
        String whereClause = ID_TEST_UNIQUE_ASOCIATION + " = ?";
        String[] whereArgs = {String.valueOf(testId)};

        try {
            // Verificar cuántas filas coinciden con la condición antes de la eliminación
            long rowCountBeforeDeletion = DatabaseUtils.queryNumEntries(db, TABLE_NAME, whereClause, whereArgs);
            Log.d("DBHelper", "Número de filas a eliminar: " + rowCountBeforeDeletion);

            // Ejecutar la operación de eliminación
            int deletedRows = db.delete(TABLE_NAME, whereClause, whereArgs);
            Log.d("DBHelper", "Filas eliminadas: " + deletedRows);

            // Verificar cuántas filas quedan después de la eliminación
            long rowCountAfterDeletion = DatabaseUtils.queryNumEntries(db, TABLE_NAME, whereClause, whereArgs);
            Log.d("DBHelper", "Número de filas después de la eliminación: " + rowCountAfterDeletion);

            // Cerrar la base de datos después de la operación

            Log.d("DBHelper", "Eliminando test con ID: " + testId);
            Log.d("DBHelper", "whereClause: " + whereClause);
            Log.d("DBHelper", "whereArgs: " + Arrays.toString(whereArgs));


            db.close();
        } catch (SQLiteException e) {
            Log.e("DBHelper", "Error al eliminar el test: " + e.getMessage());
        }
    }


    // Método para actualizar el estado de inicio de sesión
    public void updateLoginStatus(String username, boolean isLoggedIn) {
        // Abre la base de datos en modo escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // Crea un objeto ContentValues para almacenar los valores a actualizar
        ContentValues values = new ContentValues();
        values.put(IS_LOGGED_IN, isLoggedIn ? 1 : 0);

        // Actualiza el estado de inicio de sesión en la tabla de usuarios
        db.update(TABLE_NAME_USERS, values, USERNAME_COL + "=?", new String[]{username});

        // Cierra la base de datos
        db.close();
    }

    // Método para obtener el estado de inicio de sesión
    public boolean getLoginStatus() {
        // Abre la base de datos en modo lectura
        SQLiteDatabase db = this.getReadableDatabase();

        // Ejecuta una consulta para obtener el estado de inicio de sesión
        Cursor cursor = db.rawQuery("SELECT " + IS_LOGGED_IN + " FROM " + TABLE_NAME_USERS, null);

        // Inicializa el estado de inicio de sesión
        boolean isLoggedIn = false;

        try {
            // Verifica si hay al menos una fila en el resultado de la consulta
            if (cursor.moveToFirst()) {
                // Obtiene el valor de la columna IS_LOGGED_IN
                isLoggedIn = cursor.getInt(0) == 1;
            }
        } finally {
            // Cierra el cursor y la base de datos
            cursor.close();
            db.close();
        }

        // Devuelve el estado de inicio de sesión
        return isLoggedIn;
    }

    // Método para agregar un nuevo usuario a la tabla
    public void addNewUser(String name, String username, String email, String password) {
        // Abre la base de datos en modo escritura
        SQLiteDatabase db = this.getWritableDatabase();

        // Crea un objeto ContentValues para almacenar los valores a insertar
        ContentValues values = new ContentValues();
        values.put(NAME_COL, name);
        values.put(USERNAME_COL, username);
        values.put(EMAIL_COL, email);
        values.put(PASSWORD_COL, password);

        // Inserta un nuevo usuario en la tabla de usuarios
        db.insert(TABLE_NAME_USERS, null, values);

        // Cierra la base de datos
        db.close();
    }

    // Método para determinar si existe un usuario en la tabla de Users
    public boolean isUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                "Users",
                new String[]{"username"},
                "username=?",
                new String[]{username},
                null, null, null, null
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }



    // Método para verificar las credenciales del usuario
    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {USERNAME_COL, NAME_COL};
        String selection = USERNAME_COL + " =? AND " + PASSWORD_COL + " =?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_NAME_USERS, columns, selection, selectionArgs, null, null, null);

        boolean isValid = cursor.moveToFirst();

        cursor.close();
        db.close();

        return isValid;
    }

    //metodo para obtener un Name del usuario
    public String getUserName(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {NAME_COL};
        String selection = USERNAME_COL + " =? AND " + PASSWORD_COL + " =?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_NAME_USERS, columns, selection, selectionArgs, null, null, null);

        String name = null;

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(NAME_COL);
            if (nameIndex != -1) {
                name = cursor.getString(nameIndex);
            } else {
                Log.e("DBHelper", "La columna " + NAME_COL + " no existe en el cursor.");
            }
        }

        cursor.close();
        db.close();

        return name;
    }

    //metodo para obtener un email del usuario
    public String getUserEMail(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {EMAIL_COL};
        String selection = USERNAME_COL + " =? AND " + PASSWORD_COL + " =?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_NAME_USERS, columns, selection, selectionArgs, null, null, null);

        String name = null;

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(EMAIL_COL);
            if (nameIndex != -1) {
                name = cursor.getString(nameIndex);
            } else {
                Log.e("DBHelper", "La columna " + EMAIL_COL + " no existe en el cursor.");
            }
        }

        cursor.close();
        db.close();

        return name;
    }

    //metodo para obtener un id del usuario
    public String getUserID(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {USERNAME_COL};
        String selection = USERNAME_COL + " =? AND " + PASSWORD_COL + " =?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_NAME_USERS, columns, selection, selectionArgs, null, null, null);

        String name = null;

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(USERNAME_COL);
            if (nameIndex != -1) {
                name = cursor.getString(nameIndex);
            } else {
                Log.e("DBHelper", "La columna " + USERNAME_COL + " no existe en el cursor.");
            }
        }

        cursor.close();
        db.close();

        return name;
    }


    // Método para actualizar los datos de un usuario en la tabla
    public void updateUserData(String username, String name, String email, String password) {
        Log.e("DBHelper", "Updating user data - UserName: " + username + ", Name: " + name + ", Email: " + email + ", Password: " + password);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME_COL, username);
        values.put(NAME_COL, name);
        values.put(EMAIL_COL, email);
        values.put(PASSWORD_COL, password);

        db.update(TABLE_NAME_USERS, values, USERNAME_COL + "=?", new String[]{username});
        db.close();
    }



    public int getCountOfTestsForUser(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Especifica la tabla y las columnas que deseas contar
        String countColumn = "COUNT(*)";

        // Especifica la cláusula WHERE para filtrar por userId
        String selection = "idUser=?";
        String[] selectionArgs = {userId};

        // Ejecuta la consulta y devuelve el resultado como un número entero
        Cursor cursor = db.query(TABLE_NAME, new String[]{countColumn}, selection, selectionArgs, null, null, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        // Cierra la conexión a la base de datos
        db.close();

        return count;
    }



    /**
     * Incrementa el número de pruebas finalizadas para un usuario dado en la base de datos.
     *
     * @param userName El nombre de usuario para el cual se incrementará el número de pruebas finalizadas.
     */
    public void addNumberOfFinishTest(String userName) {
        SQLiteDatabase db = null;

        try {
            db = this.getWritableDatabase();

            // Obtener el número actual almacenado en la base de datos
            int currentNumber = getNumberOfFinishTest(userName);

            Log.d("Database", "Número actual antes de incrementar: " + currentNumber);

            // Incrementar el número
            int newNumber = currentNumber + 1;

            Log.d("Database", "Nuevo número después de incrementar: " + newNumber);

            // Actualizar la base de datos con el nuevo número
            ContentValues values = new ContentValues();
            values.put(NUM_FINISH_TEST, newNumber);

            // Actualizar la fila en la base de datos
            int rowsAffected = db.update(TABLE_NAME_USERS, values, USERNAME_COL + "=?", new String[]{userName});

            if (rowsAffected <= 0) {
                // No se actualizó ninguna fila
                Log.d("Database", "No se actualizó ninguna fila para el usuario: " + userName);
            } else {
                Log.d("Database", "Número actualizado exitosamente para el usuario: " + userName);
            }

        } catch (IllegalStateException e) {
            Log.d("Database", "Error: La base de datos ya está cerrada.");
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Database", "Error durante la operación en la base de datos: " + e.getMessage());

        } finally {
            try {
                // Cerrar la base de datos
                if (db != null && db.isOpen()) {
                    db.close();
                    Log.d("Database", "Base de datos cerrada");
                }
            } catch (Exception e) {
                Log.d("Database", "Error al cerrar la base de datos: " + e.getMessage());
            }
        }
    }



    /**
     * Obtiene el número de pruebas finalizadas para un usuario específico.
     *
     * @param userName Nombre de usuario para el cual se obtiene el número de pruebas finalizadas.
     * @return Número de pruebas finalizadas para el usuario.
     */
    public int getNumberOfFinishTest(String userName) {
        // Obtener una instancia de la base de datos en modo lectura
        SQLiteDatabase db = this.getReadableDatabase();

        // Inicializar el número de pruebas finalizadas
        int number = 0;

        try {
            // Consultar la base de datos para obtener el número de pruebas finalizadas
            Cursor cursor = db.query(
                    TABLE_NAME_USERS,
                    new String[]{NUM_FINISH_TEST},
                    USERNAME_COL + "=?",
                    new String[]{userName},
                    null, null, null
            );

            // Verificar si hay al menos una fila en el resultado de la consulta
            if (cursor.moveToFirst()) {
                // Obtener el índice de la columna que contiene el número de pruebas finalizadas
                int columnIndex = cursor.getColumnIndex(NUM_FINISH_TEST);

                // Verificar si la columna existe en el resultado de la consulta
                if (columnIndex >= 0) {
                    // Obtener el número de pruebas finalizadas
                    number = cursor.getInt(columnIndex);
                }
            }

            // Cerrar el cursor después de utilizarlo para liberar recursos
            cursor.close();

            // Registrar información en el registro (log) para propósitos de depuración
            Log.d("Database", "Número obtenido de la base de datos para " + userName + ": " + number);

        } catch (Exception e) {
            // Manejar la excepción e imprimir la traza del error
            e.printStackTrace();
        }

        // Devolver el número de pruebas finalizadas
        return number;
    }
}
