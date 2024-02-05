package com.example.myexampleevaluatest.data;

import android.content.Context;
import android.content.SharedPreferences;


// Clase que gestiona la sesión del usuario utilizando SharedPreferences
public class SessionManager {

    // Objeto para acceder a las preferencias compartidas
    private SharedPreferences sharedPreferences;

    // Nombres de claves para las preferencias compartidas
    private static final String PREF_NAME = "MyAppPreferences";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    // Constructor que inicializa el objeto SharedPreferences
    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Método para establecer el inicio de sesión con el nombre de usuario proporcionado
    public void setLogin(String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    // Método para cerrar sesión eliminando el nombre de usuario y estableciendo isLoggedIn a falso
    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_USERNAME);
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();
    }

    // Método para verificar si el usuario ha iniciado sesión
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Método para obtener el nombre de usuario almacenado en las preferencias compartidas
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    // Método para limpiar todos los datos de la sesión (cerrar sesión)
    public void clearSessionData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Elimina todos los datos almacenados en SharedPreferences
        editor.apply();
    }
}
