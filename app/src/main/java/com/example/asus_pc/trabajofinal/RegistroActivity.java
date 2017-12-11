package com.example.asus_pc.trabajofinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistroActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference refUsuarios;

    @BindView(R.id.nombrereg)
    EditText nombre;
    @BindView(R.id.emailreg)
    EditText email;
    @BindView(R.id.passwordreg)
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        refUsuarios = firebaseDatabase.getReference("usuarios");
    }

    @OnClick(R.id.btnCrearUsuario)
    public void crearcuenta() {
        String nombre1 = nombre.getText().toString();
        String email1 = email.getText().toString();
        String password1 = password.getText().toString();
        final Usuario objusuario = new Usuario();
        objusuario.setNombre(nombre1);
        objusuario.setEmail(email1);
        mAuth.createUserWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            objusuario.setUid(task.getResult().getUser().getUid());
                            refUsuarios.child(objusuario.getUid())
                                    .setValue(objusuario);
                            Intent intent = new Intent(getApplicationContext(), MispreguntasActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                        }
                    }
                });
    }

    @OnClick(R.id.txtCuentaCreada)
    public void irALogin() {
        Intent intent = new Intent(getApplicationContext(), IngresarActivity.class);
        startActivity(intent);
        finish();
    }
}
