package auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alex.sharepdf.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import principal.MiCuenta;

/**
 * Created by Alex on 12/11/2017.
 */

public class Registro extends AppCompatActivity implements View.OnClickListener {
    private boolean acpeta_politicas;
    Button registro_registrar;
    EditText registro_correo, registro_usuario, registro_password_0, registro_password_1;
    CheckBox registro_check;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_registro);
        mAuth = FirebaseAuth.getInstance();
        registro_registrar = (Button) findViewById(R.id.registro_registrar);
        registro_correo = (EditText) findViewById(R.id.registro_correo);
        registro_usuario = (EditText)findViewById(R.id.registro_usuario);
        registro_password_0 = (EditText) findViewById(R.id.registro_password_0);
        registro_password_1 = (EditText)findViewById(R.id.registro_password_1);
        registro_check = (CheckBox)findViewById(R.id.registro_check);
        registro_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    acpeta_politicas = false;
                }else{
                    acpeta_politicas = true;
                }
            }
        });
        registro_registrar.setOnClickListener(this);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user =  firebaseAuth.getCurrentUser();
                if(user != null){
                    //SI se ha iniciado sesión
                    Log.i("Sesion","Sesion iniciaciada con email"+user.getEmail());

                    //Aqui podria ir la siguiente activity ya que si el usuario ya esta inciado la sesion se slatria esta parte
                }else{
                    Log.i("Sesion","sesión cerrada");

                }
            }
        };
    }

    private void registrar(final String correo, final String password, final String usuario){
        mAuth.createUserWithEmailAndPassword(correo, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mAuth.signInWithEmailAndPassword(correo, password);
                    //Si la operación a sido correcta osea si se ha creado el usuario
                    Log.i("Sesion","Usuario creado correctamente");

                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");
                    DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());
                    currentUserDB.child("usuario").setValue(usuario);
                    currentUserDB.child("foto_url").setValue("default");
                    currentUserDB.child("creditos").setValue(6);
                    currentUserDB.child("archivos_subidos").setValue(0);
                    currentUserDB.child("archivos_descargados").setValue(0);
                   FirebaseUser user = mAuth.getCurrentUser();
                   user.sendEmailVerification();
                    Toast.makeText(Registro.this, "Verifica tu correo antes de 3 días",
                            Toast.LENGTH_SHORT).show();
                    Intent cuenta = new Intent(Registro.this, MiCuenta.class);
                    startActivity(cuenta);
                }else{
                    Log.i("Sesion",task.getException().getMessage()+"");
                    Toast.makeText(Registro.this, "Autentificación fallida.",
                            Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registro_registrar:
                Intent cuenta = new Intent(Registro.this, MiCuenta.class);
                startActivity(cuenta);
                final String correo = registro_correo.getText().toString();
                final String password_0 = registro_password_0.getText().toString();
                final String usuario = registro_usuario.getText().toString();
                final String password_1 = registro_password_1.getText().toString();
                if(completa_registro(usuario,password_0,password_1,correo)) {
                    if(!acpeta_politicas) {
                        Toast.makeText(getApplicationContext(),"Acepta las términos y condiciones",Toast.LENGTH_SHORT).show();

                    }else {
                        registrar(correo, password_0, usuario);
                    }
                }
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }


    private boolean completa_registro(String usuario, String password_0, String password_1,String correo){
        if(TextUtils.isEmpty(usuario)){
            Toast.makeText(getApplicationContext(),"Ingresa un usuario",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!util.Util.isValidEmail(correo)){
            Toast.makeText(getApplicationContext(),"formato de correo no válido",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!util.Util.isValidPassword(password_0)){
            Toast.makeText(getApplicationContext(),"Recuerda que la contraseña debe" +
                    " tener una mayúscula, una minúscula y un número, como mínimo 6 carácteres",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password_0.equals(password_1)){
            Toast.makeText(getApplicationContext(),"contraseñas no coinciden",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }



}
