package com.example.myexampleevaluatest.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myexampleevaluatest.DabaBase.DBHelper;
import com.example.myexampleevaluatest.R;
import com.example.myexampleevaluatest.fragment.SeleccionMultipleFragment;
import com.example.myexampleevaluatest.fragment.TestTypeFragment;
import com.example.myexampleevaluatest.data.TestData;
import com.example.myexampleevaluatest.fragment.VerdaderoFalsoFragment;

import java.util.ArrayList;
import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {


    // variable for our array list and context
    private ArrayList<TestData> testDataArrayList;
    private Context context;
    private CardView cardView, cardViewExpand;
    private DBHelper dbHelper;
    private OnTestDeleteListener deleteListener;
    private OnTestClickListener testClickListener;
    private ViewHolder currentViewHolder;

    // constructor
    public TestAdapter(ArrayList<TestData> testDataArrayList, Context context) {
        this.testDataArrayList = testDataArrayList;
        this.context = context;
    }

    // Definir una interfaz para manejar clics en los elementos
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    // Método para establecer el OnItemClickListener desde fuera del adaptador
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



    // Interfaz para manejar clics en los elementos del adaptador
    public interface OnTestClickListener {
        void onTestClick(TestData testData);
    }

    // Interfaz para manejar eventos de eliminación de test
    public interface OnTestDeleteListener {
        void onTestDeleted();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_items, parent, false);

        dbHelper = new DBHelper(context);

        cardView = view.findViewById(R.id.cardViewListTest);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtener los datos de la posición actual en la lista
        TestData testData = testDataArrayList.get(position);

        // Establecer los datos en las vistas del ViewHolder
        holder.courseNameTV.setText(testData.getStrTestTitle());
        holder.courseDescTV.setText(testData.getCourseDescription());
        holder.courseDurationTV.setText(testData.getStrTestDuration());
        holder.courseTracksTV.setText(testData.getStrTestCategory());
        holder.idUserTV.setText(testData.getUserID());
        holder.TypeTestTV.setText(testData.getTestTypeTest());

        // Configurar el clic en el CardView
        holder.cardView.setOnClickListener(v -> {
            int clickedPosition = holder.getAdapterPosition();

            if (clickedPosition != RecyclerView.NO_POSITION) {
                // Almacenar el ViewHolder actual para su referencia posterior
                currentViewHolder = holder;

                // Mostrar la confirmación para abrir la prueba
                showConfirmationOpenTest();
            } else {
                // Manejar el caso en que la posición no es válida.
                Toast.makeText(v.getContext(), "Posición no válida", Toast.LENGTH_SHORT).show();
            }
        });


        // Configurar el clic en el elemento para el listener general
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si el listener general no es nulo y llamar al método onItemClick
                if (listener != null) {
                    listener.onItemClick(holder.getAdapterPosition());
                }
            }
        });

// Configurar el clic en el elemento para el listener específico de prueba
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si el listener específico de prueba no es nulo y llamar al método onTestClick
                if (testClickListener != null) {
                    testClickListener.onTestClick(testData);
                }
            }
        });

// Configurar el clic en el botón de eliminar prueba
        holder.btnDeleteTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Guardar el ViewHolder actual
                currentViewHolder = holder;

                // Mostrar el diálogo de confirmación para eliminar la prueba
                showConfirmationDeleteTest();
            }
        });
    }



    @Override
    public int getItemCount() {
        return testDataArrayList.size();
    }

    private void showConfirmationOpenTest() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("¿Estás seguro de realizar este test?");
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Sí",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Aquí se ejecuta la acción tras la confirmación
                        dialog.dismiss(); // Cerrar el diálogo

                        TestData clickedTestData = testDataArrayList.get(currentViewHolder.getAdapterPosition());
                        openFragmentForTest(context, clickedTestData);

                    }
                });

        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // Cerrar el diálogo
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showConfirmationDeleteTest() {
        // Crear un AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Establecer el mensaje y la cancelabilidad del diálogo
        builder.setMessage("¿Estás seguro de Eliminar este test?");
        builder.setCancelable(true);

        // Configurar el botón positivo (Sí)
        builder.setPositiveButton(
                "Sí",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Acciones después de la confirmación
                        dialog.dismiss(); // Cerrar el diálogo

                        // Obtener el ID único del test a ser eliminado
                        String uniqueTestId = testDataArrayList.get(currentViewHolder.getAdapterPosition()).getIdentificadorUnicoDelTest();

                        // Eliminar el test de la base de datos
                        dbHelper.eliminarTest(uniqueTestId);

                        // Eliminar el test de la lista actual
                        testDataArrayList.remove(currentViewHolder.getAdapterPosition());

                        Log.e("TestAdapter", "uniqueTestID " + uniqueTestId);

                        // Notificar al adaptador sobre el cambio
                        notifyDataSetChanged();

                        // Notificar al fragmento sobre la eliminación
                        if (deleteListener != null) {
                            deleteListener.onTestDeleted();
                        }

                        Toast.makeText(context, "Test eliminado", Toast.LENGTH_SHORT).show();
                    }
                });

        // Configurar el botón negativo (No)
        builder.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cerrar el diálogo
                        dialog.dismiss();
                    }
                });

        // Crear y mostrar el AlertDialog
        AlertDialog alert = builder.create();
        alert.show();
    }





    // Método para abrir el fragmento correspondiente al hacer clic en un CardView
    public void openFragmentForTest(Context context, TestData testData) {
        Log.e("TestAdapter", "Abriendo fragmento para la prueba: " + testData.getStrTestTitle());

        // Obtener el identificador único de la prueba
        String uniqueTestIdFromAllTest = testData.getIdentificadorUnicoDelTest();

        // Crear el fragmento correspondiente según el tipo de prueba
        Fragment nuevoFragmento = null;
        Bundle args = new Bundle();
        args.putString("uniqueTestId", uniqueTestIdFromAllTest);

        Log.e("TestAdapter", "¡Abriendo Fragmento con id" + args);

        if ("Verdadero y Falso".equals(testData.getTestTypeTest())) {
            nuevoFragmento = new VerdaderoFalsoFragment();
            Log.e("TestAdapter", "¡Abriendo Fragmento VerdaderoFalsoFragment!");
        } else if ("Tipo Test".equals(testData.getTestTypeTest())) {
            nuevoFragmento = new TestTypeFragment();
            Log.e("TestAdapter", "¡Abriendo Fragmento TestTypeFragment!");
        } else if ("Seleccion Multiple".equals(testData.getTestTypeTest())) {
            nuevoFragmento = new SeleccionMultipleFragment();
            Log.e("TestAdapter", "¡Abriendo Fragmento SeleccionMultipleFragment!");
        }

        // Pasar los argumentos al fragmento
        if (nuevoFragmento != null) {
            nuevoFragmento.setArguments(args);

            // Realizar la transacción del fragmento
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.navHostFragment, nuevoFragmento);
            transaction.addToBackStack(null);

            // Asegurarse de que la transacción sea exitosa antes de notificar cambios
            if (transaction.commit() != -1) {
                // Notificar cambios en el adaptador
                notifyDataSetChanged();
                Log.d("TestAdapter", "¡transacion del Fragmento Confirmada!");
            } else {
                Log.e("TestAdapter", "Error al confirmar la transacción del fragmento");
            }
        }
    }

    // Método para actualizar el conjunto de datos del adaptador
    public void setTestList(List<TestData> listaDePruebas) {
        // Log para verificar el tamaño de la lista antes de la actualización
        Log.d("RecyclerViewDebug", "Tamaño de la lista antes de la actualización: " + testDataArrayList.size());

        // Actualizar la lista interna del adaptador
        testDataArrayList.clear();
        testDataArrayList.addAll(listaDePruebas);

        // Log para verificar el tamaño de la lista después de la actualización
        Log.d("RecyclerViewDebug", "Tamaño de la lista después de la actualización: " + testDataArrayList.size());

        notifyDataSetChanged();
    }

    public ArrayList<TestData> getTestList() {
        return testDataArrayList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        // Variables para nuestros elementos de vista.
        CardView cardView;
        private TextView courseNameTV, courseDescTV, courseDurationTV, courseTracksTV, idUserTV, TypeTestTV;

        private ImageButton btnDeleteTest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Inicializando nuestros elementos de vista
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
            courseDescTV = itemView.findViewById(R.id.idTestDescription);
            courseDurationTV = itemView.findViewById(R.id.idTVCourseCategory);
            courseTracksTV = itemView.findViewById(R.id.idTVTestNumberOfQuestions);
            idUserTV = itemView.findViewById(R.id.idUserIdTA);
            TypeTestTV = itemView.findViewById(R.id.idTestTypeTest);
            cardView = itemView.findViewById(R.id.cardViewListTest);

            btnDeleteTest = itemView.findViewById(R.id.btnDeleteTest);

            // Estableciendo un OnClickListener para manejar clics en los elementos
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (testClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            testClickListener.onTestClick(testDataArrayList.get(position));
                        }
                    }
                }
            });
        }
    }
}


