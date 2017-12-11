package com.example.asus_pc.trabajofinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IngresarActivity extends AppCompatActivity {
    @BindView(R.id.emaillogin)
    EditText email;
    @BindView(R.id.passwordlogin)
    EditText password;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference refUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        refUsuarios = firebaseDatabase.getReference("usuarios");
    }

    @OnClick(R.id.btnIngresarusuario)
    public void irApreguntas() {
        String email1 = email.getText().toString();
        String password1 = password.getText().toString();
        mAuth.signInWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), PreguntasActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                        }
                    }
                });
    }

    @OnClick(R.id.txtRegistrar)
    public void irARegistro() {
        Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
        startActivity(intent);
        finish();
    }
}
