package com.example.myexampleevaluatest.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myexampleevaluatest.DabaBase.DBHelper;
import com.example.myexampleevaluatest.MainActivity;
import com.example.myexampleevaluatest.R;
import com.example.myexampleevaluatest.data.SessionManager;
import com.example.myexampleevaluatest.data.UserData;

public class UserLogSingFragment extends Fragment {

    private DBHelper dbHelper;
    private SessionManager sessionManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_log_sing, container, false);

        // Inicializar el DBHelper
        dbHelper = new DBHelper(requireContext());
        sessionManager = new SessionManager(requireContext());

        // Obtener referencias a las vistas desde el diseño del fragmento
        CardView cardViewLogin = view.findViewById(R.id.cardViewLogin);
        CardView cardViewSingUp = view.findViewById(R.id.cardViewSingUp);


        //datos para el registro del usuario
        EditText editTextUsername = view.findViewById(R.id.editTextUsernameSU);
        EditText editTextPassword = view.findViewById(R.id.editTextPasswordSU);
        EditText editTextRepPassword = view.findViewById(R.id.editTextRepPasswordSU);
        EditText editTextName = view.findViewById(R.id.editTextNameSU);
        EditText editTextEmail = view.findViewById(R.id.editTextEmailSU);

        //datos para el Inicio de secion del usuario.
        EditText editTextUsernameLog = view.findViewById(R.id.editTextUsername);
        EditText editTextPasswordLog = view.findViewById(R.id.editTextPassword);

        Button buttonAccessWithoutRegistration = view.findViewById(R.id.buttonAccessWithoutRegistration);
        Button buttonLogin = view.findViewById(R.id.buttonLogin);
        Button buttonSingUp = view.findViewById(R.id.buttonSingUp);
        TextView textViewRegister = view.findViewById(R.id.textViewUserRegister);
        TextView textViewLogIn = view.findViewById(R.id.editTextlogInFromReg);



        // Verificar si la sesión está iniciada
        if (sessionManager.isLoggedIn()) {
            // Verificar si sessionManager no es nulo
            if (sessionManager != null) {
                // Mostrar la barra de herramientas y la navegación inferior en la actividad principal
                ((MainActivity) requireActivity()).showToolbar();
                ((MainActivity) requireActivity()).showBottomNavigation();

                // Registrar un mensaje de éxito en el inicio de sesión
                Log.e("UserLogSingFragment", "sessionManager INICIADO CORRECTAMENTE!");
            } else {
                // Registrar un mensaje si sessionManager es nulo
                Log.e("UserLogSingFragment", "sessionManager is not initialized");
            }
        }



        // Listener para el TextView de inicio de sesión
        textViewLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ocultar el diseño de registro y mostrar el diseño de inicio de sesión
                cardViewSingUp.setVisibility(View.GONE);
                cardViewLogin.setVisibility(View.VISIBLE);
            }
        });

        // Listener para el TextView de registro
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mostrar el diseño de registro y ocultar el diseño de inicio de sesión
                cardViewSingUp.setVisibility(View.VISIBLE);
                cardViewLogin.setVisibility(View.GONE);
            }
        });

        // Listener para el botón de acceso sin registro
        buttonAccessWithoutRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navegar al fragmento de inicio desde el NavController
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.homeFragment);

                // Mostrar la barra de herramientas y la navegación inferior en la actividad principal
                ((MainActivity) requireActivity()).showToolbar();
                ((MainActivity) requireActivity()).showBottomNavigation();
            }
        });




        // En el método que maneja el clic del botón de inicio de sesión
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsernameLog.getText().toString().trim();
                String password = editTextPasswordLog.getText().toString().trim();

                // Verificar las credenciales del usuario
                if (dbHelper.checkUserCredentials(username, password)) {
                    // Usuario autenticado con éxito, realizar acciones necesarias
                    Toast.makeText(requireContext(), "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();


                    // Guardar datos en SharedPreferences
                    saveUserDataToSharedPreferences(username, password);

                    sessionManager.setLogin(username);

                    //actualizamos el estado del usuario
                    dbHelper.updateLoginStatus(username, true);

                    boolean userIsLoggedIn = dbHelper.getLoginStatus();
                    Log.e("UserLogSignFragment", "Desde UserDataFragment - estado de logado:" + sessionManager.isLoggedIn());

                    // Navegar al HomeFragment
                    NavController navController = Navigation.findNavController(requireView());
                    navController.navigate(R.id.homeFragment);

                    ((MainActivity) requireActivity()).showToolbar();
                    ((MainActivity) requireActivity()).showBottomNavigation();
                } else {
                    // Usuario no autenticado, mostrar un mensaje de error
                    Toast.makeText(requireContext(), "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configurar listeners para los botones
        buttonSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para registrarse
                registerUser(editTextName, editTextUsername, editTextEmail, editTextPassword, editTextRepPassword);
            }
        });



        // Crear un drawable con gradiente
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{getResources().getColor(R.color.gradStart), getResources().getColor(R.color.gradEnd)}
        );

        // Establecer el radio de las esquinas, si lo deseas
        gradientDrawable.setCornerRadius(15f);

        // Establecer el fondo del CardView
        cardViewLogin.setBackground(gradientDrawable);
        cardViewSingUp.setBackground(gradientDrawable);

        return view;

    }




    private boolean checkCredentials(String username, String password) {
        // Aquí debes realizar la lógica para verificar las credenciales
        // Por ejemplo, puedes comparar con los datos almacenados en la base de datos
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                "Users",
                new String[]{"password"},
                "username=?",
                new String[]{username},
                null, null, null
        );

        boolean credentialsAreValid = false;

        if (cursor.moveToFirst()) {
            int passwordColumnIndex = cursor.getColumnIndex("password");

            if (passwordColumnIndex >= 0) {
                String storedPassword = cursor.getString(passwordColumnIndex);
                credentialsAreValid = password.equals(storedPassword);
            }
        }

        cursor.close();
        db.close();

        return credentialsAreValid;
    }



    // Método modificado para aceptar los parámetros
    private void registerUser(EditText nameUser, EditText editTextUsername, EditText editTextEmail,
                              EditText editTextPassword, EditText editTextRepPass) {
        // Obtener los datos del usuario desde las vistas

        String name = nameUser.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String passwordRep = editTextRepPass.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || name.isEmpty() || passwordRep.isEmpty() ) {
            // Mostrar un mensaje al usuario indicando que todos los campos son obligatorios
            Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si el nombre de usuario ya está en uso
        if (dbHelper.isUserExists(username)) {
            // Mostrar un mensaje de error indicando que el nombre de usuario ya está en uso
            Toast.makeText(requireContext(), "Nombre de usuario ya está en uso, elige otro", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si las contraseñas son iguales
        if (!password.equals(passwordRep)){
            Toast.makeText(requireContext(), "Las contraseñas no son iguales", Toast.LENGTH_SHORT).show();
            return;
        }

        // Agregar el nuevo usuario a la base de datos en que caso de que todos los campos esten bien
        dbHelper.addNewUser(name, username, email, password);

        // Mostrar un mensaje indicando que el usuario se registró exitosamente
        Toast.makeText(requireContext(), "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();

        // Vaciar los campos una vez se registra el usuario.
        nameUser.setText("");
        editTextUsername.setText("");
        editTextEmail.setText("");
        editTextPassword.setText("");
        editTextRepPass.setText("");

        // cambiamos de CardView para que el usuario se registre.

        CardView cardViewLogin = getView().findViewById(R.id.cardViewLogin);
        CardView cardViewSingUp = getView().findViewById(R.id.cardViewSingUp);

        //cambiamos la vista de los cardView para que una vez registrado, pueda acceder.
        cardViewSingUp.setVisibility(View.GONE);
        cardViewLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Mostrar el Toolbar y BottomNavigationView al destruir la vista del fragmento
        ((MainActivity) requireActivity()).showToolbar();
        ((MainActivity) requireActivity()).showBottomNavigation();
    }

    // Método para guardar datos en SharedPreferences
    private void saveUserDataToSharedPreferences(String username, String password) {
        SharedPreferences preferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.apply();
    }
}