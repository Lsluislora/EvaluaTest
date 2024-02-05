package com.example.myexampleevaluatest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myexampleevaluatest.DabaBase.DBHelper;
import com.example.myexampleevaluatest.data.SessionManager;
import com.example.myexampleevaluatest.data.UserData;
import com.example.myexampleevaluatest.fragment.TestTypeFragment;
import com.example.myexampleevaluatest.fragment.UserLogSingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.badge.BadgeDrawable;



public class MainActivity extends AppCompatActivity {

    //declaracion de las variables
    private NavController navController;
    private TestTypeFragment testTypeFragment;

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    DBHelper dbHelper;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Inicializar DBHelper y SessionManager
        dbHelper = new DBHelper(this);
        sessionManager = new SessionManager(this);

        // Obtener instancia de TestTypeFragment
        testTypeFragment = (TestTypeFragment) getSupportFragmentManager().findFragmentById(R.id.testTypeFragment);

        // Obtener vistas
        toolbar = findViewById(R.id.toolBarEvaluatest);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Configurar la barra de herramientas y la navegación inferior
        setupToolbar(); //Invocamos el metodo setupToolbar
        setupBottonNavigation(); //Invocamos el metodo BottonNavigation


        // Configurar la selección predeterminada en la navegación inferior
        bottomNavigationView.setSelectedItemId(R.id.page_home);

        // Obtener BadgeDrawable para el ítem específico
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.page_list_test);

        // Inicializar NavController
        navController = Navigation.findNavController(this, R.id.navHostFragment);

        // Reemplazar el fragmento actual con UserLogSingFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new UserLogSingFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menú desde el recurso de menú
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        // Verificar si el usuario ha iniciado sesión utilizando el SessionManager
        if (sessionManager.isLoggedIn()) {
            // Si está conectado, mostrar el ítem de cierre de sesión y ocultar el de inicio de sesión
            menu.findItem(R.id.user_logout).setVisible(true);
            menu.findItem(R.id.user_login).setVisible(false);
        } else {
            // Si no está conectado, mostrar el ítem de inicio de sesión y ocultar el de cierre de sesión
            menu.findItem(R.id.user_logout).setVisible(false);
            menu.findItem(R.id.user_login).setVisible(true);
        }

        return true; // Indica que el menú se ha creado exitosamente
    }


    // Sobrescribe el método onOptionsItemSelected para manejar eventos de los elementos del menú.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Obtiene el ID del elemento seleccionado.
        int id = item.getItemId();

        // Verifica qué elemento del menú fue seleccionado.
        if (id == R.id.user_login) {
            // Verifica si el usuario ya está autenticado.
            if (sessionManager.isLoggedIn()) {
                // Si el usuario ya está autenticado, registra el clic en el botón de inicio de sesión.
                Log.d("MainActivity", "LogIn clicked");
            } else {
                // Si el usuario no está autenticado, muestra un mensaje de error, navega a la pantalla de inicio de sesión y oculta elementos de la interfaz.
                Log.e("MainActivity", "onOptionsItemSelected sessionManager --> " + sessionManager.isLoggedIn() );
                navigateToLoginScreen();
                hideToolbar();
                hideBottomNavigation();
            }
            // Devuelve true para indicar que el evento del menú fue manejado.
            return true;

        } else if (id == R.id.user_logout) {
            // Si se selecciona el botón de cierre de sesión, realiza la operación de cierre de sesión y registra el clic.
            logout();
            Log.d("MainActivity", "Logout clicked");
            // Devuelve true para indicar que el evento del menú fue manejado.
            return true;
        }

        // Si el ID no coincide con ningún caso, llama al método de la clase base.
        return super.onOptionsItemSelected(item);
    }


    // Método para configurar la barra de herramientas
    private void setupToolbar() {
        // Encontrar y asignar la barra de herramientas
        Toolbar toolbar = findViewById(R.id.toolBarEvaluatest);
        setSupportActionBar(toolbar);
    }

    // Método para actualizar el contador en la barra de navegación inferior
    public void actualizarContadorEnBottomNav(int contador) {
        // Encontrar la vista de navegación inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Obtener o crear un distintivo para la pestaña de la lista de pruebas
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.page_list_test);

        // Actualizar el número y la visibilidad del distintivo según el contador
        if (contador > 0) {
            badge.setNumber(contador);
            badge.setVisible(true);
        } else {
            badge.setVisible(false);
        }
    }


    /**
     * configuramos el BottonNavigation para que se desplace al fragment que solicitado.
     */

    public void setupBottonNavigation(){
    // Obtener la referencia al BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Obtener el NavController para manejar la navegación
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);

        // Establecer un listener para el evento de selección del BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Obtener el ID del elemento seleccionado
                int itemId = menuItem.getItemId();

                // Navegar a los fragmentos correspondientes según el elemento seleccionado
                if (itemId == R.id.page_home) {
                    navController.navigate(R.id.homeFragment);
                    return true;
                } else if (itemId == R.id.page_create) {
                    navController.navigate(R.id.creaTestFragment);
                    return true;
                } else if (itemId == R.id.page_list_test) {
                    navController.navigate(R.id.verTestFragment);
                    return true;
                } else if (itemId == R.id.page_user) {
                    navController.navigate(R.id.userPageDataFragment);
                    return true;
                }

                // Si no se trata de ninguno de los elementos anteriores, seleccionar el elemento en la interfaz
                selectBottomNavigationItem(itemId);
                return false;
            }
        });

    }

    private void selectBottomNavigationItem(int itemId) {
        bottomNavigationView.setSelectedItemId(itemId);
    }

    // Método para ocultar el Toolbar
    public void hideToolbar() {
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
            Log.d("HideToolbar", "Toolbar hidden");
        }
    }

    // Método para mostrar el Toolbar
    public void showToolbar() {
        if (toolbar != null) {
            toolbar.setVisibility(View.VISIBLE);
            Log.d("ShowToolbar", "Toolbar shown");
        }
    }

    // Método para ocultar el BottomNavigationView
    public void hideBottomNavigation() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
            invalidateOptionsMenu();
            Log.d("HideNavigation", "Bottom Navigation hidden");
        } else {
            Log.d("HideNavigation", "Bottom Navigation is null");
        }
    }

    // Método para mostrar el BottomNavigationView
    public void showBottomNavigation() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
            invalidateOptionsMenu();
        }
    }

    @Override
    public void onBackPressed() {
        // Verificar si hay fragmentos en la pila de retroceso
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // Si hay fragmentos en la pila, quitar el fragmento actual
            getSupportFragmentManager().popBackStack();

            // Mostrar el Toolbar y BottomNavigationView al retroceder
            showToolbar();
            showBottomNavigation();
        } else {
            // Si no hay fragmentos en la pila, realizar el comportamiento predeterminado del botón de retroceso
            super.onBackPressed();
        }
    }


    private void clearSessionData() {
        // Limpiar datos almacenados en SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

    }


    private void navigateToLoginScreen() {
        // Obtener el controlador de navegación
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);

        // Navegar al fragmento de inicio de sesión
        navController.navigate(R.id.userLogSingFragment);
    }

    // Método para actualizar el estado de inicio de sesión en la base de datos
    private void updateLoginStatus(String username, boolean isLoggedIn) {
        // Obtener una instancia escribible de la base de datos
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Crear un objeto ContentValues para almacenar los valores a actualizar
        ContentValues values = new ContentValues();
        // Asignar el valor de isLoggedIn (1 si está conectado, 0 si no está conectado)
        values.put("isLoggedIn", isLoggedIn ? 1 : 0);

        // Actualizar la fila correspondiente en la tabla "Users" con el nuevo valor de isLoggedIn
        db.update("Users", values, "username=?", new String[]{username});

        // Actualizar el menú después de cambiar el estado de inicio de sesión
        invalidateOptionsMenu();

        // Cerrar la conexión a la base de datos
        db.close();
    }


    private void logout() {
        // Recuperar datos de SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        String username = preferences.getString("username", "");

        Log.d("CurrectUser", "Estado del Usuario: " + username );
        if (username != null) {
            updateLoginStatus(username, false);
            clearSessionData();
            sessionManager.logout();
            recreate();
            Toast.makeText(this, "Cierre de sesión exitoso", Toast.LENGTH_SHORT).show();

            // Actualiza el menú después de cerrar sesión
            invalidateOptionsMenu();
        }
    }


}