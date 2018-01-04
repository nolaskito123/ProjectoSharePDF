package auth;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

import principal.MiCuenta;
import util.Util;

/**
 *  Created by SharePDF developers
 *
 * Login es una clase para Logear a los usuarios de SharePDF
 */

public class Login extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    Button login_registrar, login_entrar, login_olvidaste_password;
    EditText login_password, login_correo;
    CheckBox login_ver_password;
    FirebaseAuth.AuthStateListener mAuthListener;

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_login);

        login_registrar = (Button) findViewById(R.id.login_registrar);
        login_entrar = (Button) findViewById(R.id.login_entrar);
        login_correo = (EditText) findViewById(R.id.login_correo);
        login_password = (EditText) findViewById(R.id.login_password);
        login_olvidaste_password = (Button) findViewById(R.id.login_olvido_password);
        login_ver_password = (CheckBox) findViewById(R.id.login_ver_password);
        mp = MediaPlayer.create(this, R.raw.click);
        mAuth = FirebaseAuth.getInstance();
        login_ver_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischeck) {
                if (!ischeck) {
                    login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                } else {
                    login_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        login_entrar.setOnClickListener(this);
        login_registrar.setOnClickListener(this);
        login_olvidaste_password.setOnClickListener(this);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //SI se ha iniciado sesión
                    Log.i("Sesion", "Sesion iniciaciada con email" + user.getEmail());
                    Intent principal = new Intent(Login.this, MiCuenta.class);
                    startActivity(principal);
                    finish();
                    //Aqui podria ir la siguiente activity ya que si el usuario ya esta inciado la sesion se slatria esta parte
                } else {
                    Log.i("Sesion", "sesión cerrada");
                }
            }
        };
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

    /**
     * Método para el inicio de sesión en Firebase
     * @param correo : se verifica si el correo es correcto y e si existe en su base de datos
     * @param password : se verifica que la contraseña sea la que corresponde con el correo dado
     *                anteriormente
     */
    private void iniciarSesion(final String correo, String password) {
        if (Util.isValidEmail(correo)) {
            if (Util.isValidPassword(password)) {
                mAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { //Si la operación a sido correcta osea si se ha creado el usuario
                            Log.i("Sesion", "Sesion iniciada");
                        } else {
                            Log.i("Sesion", task.getException().getMessage() + "");
                            if (task.getException().getMessage() == "There is no user record corresponding to this identifier. The user may have been deleted.") {
                                Toast.makeText(Login.this, getResources().getString(R.string.Toast23) + correo, Toast.LENGTH_SHORT).show();
                            } else if (task.getException().getMessage() == "The password is invalid or the user does not have a password.") {
                                Toast.makeText(Login.this, getResources().getString(R.string.Toast24) + correo, Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(Login.this, getResources().getString(R.string.Toast25), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Toast26), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Toast27), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_entrar:
                mp.start();
                String correo = login_correo.getText().toString();
                String password = login_password.getText().toString();
                iniciarSesion(correo, password);
                break;
            case R.id.login_registrar:
                mp.start();
                Intent registro = new Intent(Login.this, Registro.class);
                startActivity(registro);
                finish();
                break;
            case R.id.login_olvido_password:
                Intent olvido_password = new Intent(Login.this, ResetPasswordActivity.class);
                startActivity(olvido_password);
                finish();
                break;

        }
    }
}