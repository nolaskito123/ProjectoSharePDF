package auth;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
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
import android.widget.TextView;
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
 * Created by SharePDF developers
 *
 * Registro es una clase donde los nuevos usuarios se dan de alta en sharePDF
 */
public class Registro extends AppCompatActivity implements View.OnClickListener {
    private boolean aceptaPolitica;
    Button registro_registrar;
    EditText registro_correo, registro_usuario, registro_password_0, registro_password_1;
    CheckBox registro_check;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    Dialog myDialog;
MediaPlayer mp;
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
        mp = MediaPlayer.create(this, R.raw.click);

        registro_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    aceptaPolitica = false;
                }else{
                    aceptaPolitica = true;
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
        myDialog = new Dialog(this);
    }

    /**
     * Método utilizado para mostrar políticas.
     * @param v : parámetro para mostrar la ventana de políticas.
     */
    public void ShowPopup(View v) {
        TextView txtclose;

        myDialog.setContentView(R.layout.custompopup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    /**
     * Método utilizado para dar de alta a un nuevo usuario.
     * @param correo : podrá acceder a nuestra app con el correo.
     * @param password : contraseña asociada al correo anterior.
     * @param usuario : nombre del usuario para usarlo en la aplicación.
     */
    private void registrar(final String correo, final String password, final String usuario){
        mAuth.createUserWithEmailAndPassword(correo, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

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
                    Toast.makeText(Registro.this, getResources().getString(R.string.Toast13), Toast.LENGTH_SHORT).show();
                    Intent cuenta = new Intent(Registro.this, MiCuenta.class);
                    startActivity(cuenta);
                    finish();
                } else {
                    Log.i("Sesion", task.getException().getMessage() + "");
                    Toast.makeText(Registro.this, getResources().getString(R.string.Toast14), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registro_registrar:
                mp.start();
                final String correo = registro_correo.getText().toString();
                final String password_0 = registro_password_0.getText().toString();
                final String usuario = registro_usuario.getText().toString();
                final String password_1 = registro_password_1.getText().toString();
                if(completa_registro(usuario,password_0,password_1,correo)) {
                    if(!aceptaPolitica) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Toast15), Toast.LENGTH_SHORT).show();
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

    /**
     * Método para la verificación del usuario, antes de darlo de alta en nuestra base de datos.
     * @param usuario : Que no sea un usuario nulo.
     * @param password_0 : mínimo 6 carácteres, una mayúscula y un número, para ser dada como
     *                   contraseña válida.
     * @param password_1 : se verifica que coincida con la contraseña dada anterorimente.
     * @param correo : se verifica si el correo tiene el formato correcto.
     * @return : si se cumple con los requisitos básicos se devuelto un true, en caso contrario un false.
     */
    private boolean completa_registro(String usuario, String password_0, String password_1,String correo){
        if (TextUtils.isEmpty(usuario)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Toast16), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!util.Util.isValidEmail(correo)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Toast17), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!util.Util.isValidPassword(password_0)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Toast18), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password_0.equals(password_1)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Toast19), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Registro.this, Login.class);
        startActivity(i);
        this.finish();
    }

}
