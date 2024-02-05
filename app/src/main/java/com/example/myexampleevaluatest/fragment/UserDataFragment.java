package com.example.myexampleevaluatest.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myexampleevaluatest.DabaBase.DBHelper;
import com.example.myexampleevaluatest.MainActivity;
import com.example.myexampleevaluatest.R;
import com.example.myexampleevaluatest.data.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class UserDataFragment extends Fragment {


    //declaramos las variables
    private View view;

    private DBHelper dbHelper;
    private TextView  LogInLogOut, EditUserData, tv_name_user_data, editTextNewName,
            idUserEmailData, idUserIdData, editTextNewUsername, editTextNewEmail, editTextNewPassword;
    private CardView cardViewEditNewUser, cardViewTestData;
    private Button buttonCancel, buttonConfirmUpdate;
    private SessionManager sessionManager;

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflamos la vista
        view = inflater.inflate(R.layout.fragment_user_data, container, false);

        // inicializando todas nuestras variables.
        dbHelper = new DBHelper(getContext());
        LogInLogOut = view.findViewById(R.id.logInLogOut);
        EditUserData = view.findViewById(R.id.editUserData);

        //cardView para modificar los datos del ususario
        cardViewEditNewUser = view.findViewById(R.id.cardViewEditNewUserData);
        cardViewTestData = view.findViewById(R.id.cardViewTestData);
        buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonConfirmUpdate = view.findViewById(R.id.buttonConfirmUpdate);
        editTextNewName = view.findViewById(R.id.editTextNewName);
        editTextNewUsername = view.findViewById(R.id.editTextNewUsername);
        editTextNewEmail = view.findViewById(R.id.editTextNewEmail);
        editTextNewPassword = view.findViewById(R.id.editTextNewPassword);

        tv_name_user_data = view.findViewById(R.id.tv_name_user_data);
        idUserEmailData = view.findViewById(R.id.idUserEmailData);
        idUserIdData = view.findViewById(R.id.idUserIdData);

        sessionManager = new SessionManager(requireContext());

        toolbar = view.findViewById(R.id.toolBarEvaluatest);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);

        // Recuperar datos de SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");



        // Verificar credenciales
        if (!username.isEmpty() && !password.isEmpty() && dbHelper.checkUserCredentials(username, password)) {
            // Si las credenciales son válidas

            // Obtener información del usuario desde la base de datos
            String userName = dbHelper.getUserName(username, password);
            String userEMail = dbHelper.getUserEMail(username, password);
            String userId = dbHelper.getUserID(username, password);
            int userNumTest = dbHelper.getCountOfTestsForUser(userId); // Obtener el número de tests creados por el usuario
            int numFinishTest = dbHelper.getNumberOfFinishTest(userId); // Obtener el número de tests completados por el usuario

            // Referencias a las vistas en el diseño
            TextView textViewUserName = view.findViewById(R.id.tv_name_user_data);
            TextView textViewUserEmail = view.findViewById(R.id.idUserEmailData);
            TextView textViewUserId = view.findViewById(R.id.idUserIdData);
            TextView textViewNumOfTest = view.findViewById(R.id.textViewNumOfTest);
            TextView textViewFinishTest = view.findViewById(R.id.numFinishTest);


            // Establecer los datos del usuario en las vistas
            textViewFinishTest.setText(String.valueOf(numFinishTest));
            textViewUserName.setText(userName);
            textViewUserEmail.setText(userEMail);
            textViewUserId.setText(userId);
            textViewNumOfTest.setText(String.valueOf(userNumTest));

        } else {
            // Si las credenciales no son válidas o no se encuentran en la base de datos
            Log.e("UserDataFragment", "Credenciales incorrectas o no almacenadas.");
        }


        //configuramos el texto de Cerrar/Iniciar secion
        if (sessionManager.isLoggedIn() == true){
            TextView textViewlogInLogOut = view.findViewById(R.id.logInLogOut);
            textViewlogInLogOut.setText("Cerrar Sesion");
        }else if (sessionManager.isLoggedIn() == false){
            TextView textViewlogInLogOut = view.findViewById(R.id.logInLogOut);
            textViewlogInLogOut.setText("Iniciar Sesion");
        }

        // Configurar el listener para el botón de inicio/cierre de sesión
        LogInLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sessionManager.isLoggedIn() == true) {
                    logout();
                    navigateToMainScreen();
                    updateLoginStatus(username, false);

                }else if (sessionManager.isLoggedIn() == false) {
                    navigateToLoginScreen();

                    // ocultamos el Toolbar y el BottonNavigation cuando navegamos al LoginScreen
                    ((MainActivity) requireActivity()).hideToolbar();
                    ((MainActivity) requireActivity()).hideBottomNavigation();
                } else {

                    Log.e("UserDataFragment", "sessionManager no se ha iniciado");
                }


            }
        });

        // Configuración de un Listener para el TextView de Editar Usuario
        EditUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el texto actual y el Hint del EditText
                String currentText = tv_name_user_data.getText().toString();
                String hint = tv_name_user_data.getHint().toString();

                // Verificar si el usuario está registrado (basado en el Hint y el texto actual)
                if (hint.equals("Registrar para ver tu nombre") && currentText.isEmpty()) {
                    Toast.makeText(getContext(), "Usuario no registrado!", Toast.LENGTH_SHORT).show();
                } else {
                    // Habilitar el botón EditUserData
                    EditUserData.setEnabled(true);
                    cardViewTestData.setVisibility(View.GONE);


                    // Mostrar el CardView y configurar los campos
                    cardViewEditNewUser.setVisibility(View.VISIBLE);
                    editTextNewName.setText(tv_name_user_data.getText().toString());
                    editTextNewUsername.setText(idUserIdData.getText().toString());
                    editTextNewEmail.setText(idUserEmailData.getText().toString());
                    Log.e("UserDataFragment", "EditText Name:" + currentText);

                    // Deshabilitar el botón después de hacer clic
                    EditUserData.setEnabled(false);
                }
            }
        });


        // Configuración de un Listener para el botón de cancelar
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardViewTestData.setVisibility(View.VISIBLE);
                // Ocultar la vista del cardView que se utiliza para editar o agregar un nuevo usuario
                cardViewEditNewUser.setVisibility(View.GONE);

                // Habilitar la edición de datos del usuario
                EditUserData.setEnabled(true);
            }
        });

        // Configuración de un Listener para el botón de confirmar cambios
        buttonConfirmUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Obtener los nuevos datos ingresados por el usuario
                String newName = editTextNewName.getText().toString();
                String newUserName = editTextNewUsername.getText().toString();
                String newEmail = editTextNewEmail.getText().toString();
                String newPassword = editTextNewPassword.getText().toString();

                // Verificar si la contraseña es vacía
                if (newPassword.isEmpty()) {
                    // Mostrar un mensaje de error si la contraseña está vacía
                    Toast.makeText(getContext(), "Confirma la contraseña", Toast.LENGTH_SHORT).show();
                } else {
                    cardViewTestData.setVisibility(View.VISIBLE);
                    // Actualizar los datos del usuario en la base de datos
                    dbHelper.updateUserData(newUserName, newName, newEmail, newPassword);

                    // Mostrar un mensaje de éxito
                    Toast.makeText(getContext(), "se han modificado los datos", Toast.LENGTH_SHORT).show();

                    // Actualizar la interfaz de usuario con los nuevos datos
                    tv_name_user_data.setText(editTextNewName.getText().toString());
                    idUserEmailData.setText(editTextNewEmail.getText().toString());
                    idUserIdData.setText(editTextNewUsername.getText().toString());

                    // Ocultar la interfaz/cardView de edición y habilitar el botón de edición
                    cardViewEditNewUser.setVisibility(View.GONE);
                    EditUserData.setEnabled(true);
                }
            }
        });

        return view;
    }



    /**
     * Actualiza el estado de inicio de sesión para un usuario en la base de datos.
     *
     * @param username   El nombre de usuario del usuario cuyo estado de inicio de sesión se actualizará.
     * @param isLoggedIn El nuevo estado de inicio de sesión (true si está conectado, false si no lo está).
     */
    private void updateLoginStatus(String username, boolean isLoggedIn) {
        // Obtener una instancia de la base de datos para escritura
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Crear un objeto ContentValues para almacenar los nuevos valores
        ContentValues values = new ContentValues();
        values.put("isLoggedIn", isLoggedIn ? 1 : 0);  // Almacena 1 si isLoggedIn es true, 0 si es false

        // Actualizar la tabla "Users" con los nuevos valores para el usuario especificado
        db.update("Users", values, "username=?", new String[]{username});

        // Cerrar la conexión a la base de datos
        db.close();
    }



    private void logout() {

        // Recuperar datos de SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "");

        Log.d("CurrectUser", "Estado del Usuario: " + username );
        // Verificar si el nombre de usuario no es nulo
        if (username != null) {
            // Actualizar el estado de inicio de sesión a falso
            updateLoginStatus(username, false);

            // Limpiar los datos de la sesión
            clearSessionData();

            // Mostrar un mensaje de cierre de sesión exitoso utilizando un Toast
            Toast.makeText(getContext(), "Cierre de sesión exitoso", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearSessionData() {
        // Limpiar datos almacenados en SharedPreferences
        SharedPreferences preferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }


    // Método para navegar a la pantalla de inicio de sesión
    private void navigateToLoginScreen() {
        // Obtener el controlador de navegación
        NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);

        // Realizar la navegación a la pantalla de inicio de sesión
        navController.navigate(R.id.userLogSingFragment);
    }

    // Método para navegar a la pantalla de inicio / home
    private void navigateToMainScreen() {

        // Obtener el controlador de navegación
        NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);

        // Agregar un Listener para la transición
        navController.navigate(R.id.homeFragment);

    }


}