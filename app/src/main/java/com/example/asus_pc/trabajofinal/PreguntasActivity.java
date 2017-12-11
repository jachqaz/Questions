package com.example.asus_pc.trabajofinal;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;


public class PreguntasActivity extends AppCompatActivity {
    @BindView(R.id.RecyclerView)
    RecyclerView RecyclerView;

    private Toolbar toolbar;
    private Adapter adapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference refUsuarios;
    List<Preguntas> lsPregunta = new ArrayList<>();
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //recycler
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView.setLayoutManager(llm);
        adapter = new Adapter(lsPregunta);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TextView pregunta = view.findViewById(R.id.cardpregunta);
                final TextView fecha = view.findViewById(R.id.cardfecha);
                final TextView respuesta = view.findViewById(R.id.cardrespuesta);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(PreguntasActivity.this);
                View view1;
                if (respuesta.getText().toString().equals("")) {
                    view1 = getLayoutInflater().inflate(R.layout.respuesta, null);
                    mBuilder.setView(view1);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();
                    final EditText editText = view1.findViewById(R.id.respuesta);
                    Button button = view1.findViewById(R.id.btnrespuesta);
                    ;
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            respuesta.setText(editText.getText().toString());
                            subirrespuesta(pregunta.getText().toString(), respuesta.getText().toString());
                        }
                    });
                } else {
                    view1 = getLayoutInflater().inflate(R.layout.popup1, null);
                    mBuilder.setView(view1);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();
                    TextView pregunta1 = view1.findViewById(R.id.p1pregunta);
                    TextView fecha1 = view1.findViewById(R.id.p1fecha);
                    TextView respuesta1 = view1.findViewById(R.id.p1respuesta);
                    pregunta1.setText(pregunta.getText().toString());
                    fecha1.setText(fecha.getText().toString());
                    respuesta1.setText(respuesta.getText().toString());
                    FloatingActionButton button = view1.findViewById(R.id.compartir);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            startActivity(Intent.createChooser(intent, "Share with"));
                        }
                    });
                }
            }
        });
        RecyclerView.setAdapter(adapter);
        traerpreguntas();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.btnpreguntas);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private void subirrespuesta(String pregunta, final String respuesta) {
        refUsuarios = firebaseDatabase.getReference("preguntas");
        Query query = refUsuarios.orderByChild("pregunta").equalTo(pregunta);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    refUsuarios = snapshot.getRef();
                    refUsuarios.child("respuesta").setValue(respuesta);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void traerpreguntas() {
        if (lsPregunta.isEmpty()) {
            firebaseDatabase.getReference("preguntas").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            lsPregunta.add(child.getValue(Preguntas.class));
                            refrescarListaTareas(lsPregunta);
                        }
                    }catch (Exception e){

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
                case R.id.btnmypreguntas:
                    Intent intent = new Intent(getApplicationContext(), MispreguntasActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
            }
            return false;
        }
    };


}
