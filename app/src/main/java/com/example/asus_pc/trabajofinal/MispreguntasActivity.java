package com.example.asus_pc.trabajofinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MispreguntasActivity extends AppCompatActivity {
    @BindView(R.id.RecyclerView)
    android.support.v7.widget.RecyclerView RecyclerView;
    private Toolbar toolbar;
    private Adapter adapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    List<Preguntas> lsmisPreguntas = new ArrayList<>();
    String uid;
    String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mispreguntas);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //recycler
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView.setLayoutManager(llm);
        adapter = new Adapter(lsmisPreguntas);
        RecyclerView.setAdapter(adapter);
        traermispreguntas();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.btnmypreguntas);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void traermispreguntas() {
        //todo
        firebaseDatabase.getReference("usuarios").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario1 = new Usuario();
                usuario1 = dataSnapshot.getValue(Usuario.class);
                nombre = usuario1.getNombre();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if (lsmisPreguntas.isEmpty()) {
            firebaseDatabase.getReference("mispreguntas").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            lsmisPreguntas.add(child.getValue(Preguntas.class));
                            refrescarListaTareas(lsmisPreguntas);
                        }


                    } catch (Exception e) {

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    public void refrescarListaTareas(List<Preguntas> lstTareas) {

        adapter.setDataset(lstTareas);

        RecyclerView.getAdapter().notifyDataSetChanged();

        RecyclerView.scrollToPosition(
                RecyclerView.getAdapter().getItemCount() - 1);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.btnpreguntas:
                    Intent intent = new Intent(getApplicationContext(), PreguntasActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
            }
            return false;
        }
    };

    @OnClick(R.id.btncrearpregunta)
    public void crearpregunta() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.popup, null);
        mBuilder.setView(view);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        Button add = view.findViewById(R.id.btnguardar);

        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        final Date date = new Date();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText pregunta = view.findViewById(R.id.ppregunta);
                EditText categoria = view.findViewById(R.id.pcategoria);
                Preguntas preguntas = new Preguntas();
                preguntas.setNombre(nombre);
                preguntas.setPregunta(pregunta.getText().toString());
                preguntas.setCategoria(categoria.getText().toString());
                preguntas.setFecha(dateFormat.format(date));
                preguntas.setRespuesta("");
                lsmisPreguntas.add(preguntas);
                refrescarListaTareas(lsmisPreguntas);
                dialog.dismiss();
                fcrearmipregunta(preguntas);
                fcrearpregunta(preguntas);
            }
        });
    }

    private void fcrearpregunta(Preguntas preguntas) {
        firebaseDatabase.getReference("mispreguntas").child(firebaseAuth.getCurrentUser().getUid()).child(String.valueOf(lsmisPreguntas.size())).setValue(preguntas);
    }

    private void fcrearmipregunta(final Preguntas preguntas) {
        final ArrayList<Preguntas> preguntasArrayList = new ArrayList<>();
        firebaseDatabase.getReference("preguntas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    preguntasArrayList.add(child.getValue(Preguntas.class));
                }
                firebaseDatabase.getReference("preguntas").child(String.valueOf(preguntasArrayList.size() + 1)).setValue(preguntas);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
