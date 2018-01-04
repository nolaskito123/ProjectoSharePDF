package principal;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.sharepdf.R;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;

import auth.Login;


/**
 * created by SharePDF developers
 *
 * Clase para la descripción general de cada usuario.
 */
public class MiCuenta extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private int CAMERA_REQUEST_CODE = 0;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    TextView mi_cuenta_usuario, mi_cuenta_frase_del_dia, mi_cuenta_credito_disponible,
            mi_cuenta_archivos_descargados, mi_cuenta_archivos_subidos, mi_cuenta_ruta, mi_cuenta_verificacion;
    ImageView mi_cuenta_foto;
    FloatingActionMenu MenuAccion;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_mi_cuenta);

        MenuAccion = findViewById(R.id.Principal);
        MenuAccion.setClosedOnTouchOutside(true);
        MenuAccion.setIconAnimated(false);
        mi_cuenta_usuario = (TextView) findViewById(R.id.mi_cuenta_usuario);
        mi_cuenta_frase_del_dia = (TextView) findViewById(R.id.mi_cuenta_frase_del_dia);
        mi_cuenta_credito_disponible = (TextView) findViewById(R.id.mi_cuenta_creditos);
        mi_cuenta_archivos_descargados = (TextView) findViewById(R.id.mi_cuenta_descargados);
        mi_cuenta_archivos_subidos = (TextView) findViewById(R.id.mi_cuenta_subidos);
        mi_cuenta_verificacion = (TextView) findViewById(R.id.mi_cuenta_verificacion);
        mi_cuenta_ruta = (TextView) findViewById(R.id.mi_cuenta_ruta);
        mi_cuenta_foto = (ImageView) findViewById(R.id.mi_cuenta_foto);
        mp = MediaPlayer.create(this, R.raw.click);


        DatabaseReference dbCielo =
                FirebaseDatabase.getInstance().getReference()
                        .child("Frases")
                        .child(numAleatorio());

        dbCielo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String valor = (String) dataSnapshot.getValue();
                mi_cuenta_frase_del_dia.setText(valor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mi_cuenta_foto.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    mStorage = FirebaseStorage.getInstance().getReference();
                    mDatabase = FirebaseDatabase.getInstance().getReference("Usuarios");
                    mDatabase.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mi_cuenta_usuario.setText(String.valueOf(dataSnapshot.child("usuario").getValue().toString()));
                            String imageUrl = String.valueOf(dataSnapshot.child("foto_url").getValue().toString());
                            mi_cuenta_credito_disponible.setText(""+(long) dataSnapshot.child("creditos").getValue());
                            mi_cuenta_archivos_descargados.setText(""+(long) dataSnapshot.child("archivos_descargados").getValue());
                            mi_cuenta_archivos_subidos.setText(""+(long) dataSnapshot.child("archivos_subidos").getValue());
                            mi_cuenta_ruta.setText("" + getExternalFilesDir(null));
                            System.out.println("*****"+getExternalFilesDir(null));
                            if (URLUtil.isValidUrl(imageUrl))
                                Picasso.with(MiCuenta.this).load(Uri.parse(imageUrl)).into(mi_cuenta_foto);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    startActivity(new Intent(MiCuenta.this, Login.class));
                    finish();
                }
            }
        };
        if (user.isEmailVerified()) {
            mi_cuenta_verificacion.setText(getResources().getString(R.string.Verificacion1));
        } else {
            mi_cuenta_verificacion.setText(getResources().getString(R.string.Verificacion2));
        }
    }
    @Override
    public void onBackPressed() {
        mp.start();
        super.onBackPressed();
        this.finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Método que sirve de soporte para dar una url a la foto del usuario.
     * @return : el número aleatorio en formato de cadena de texto.
     */
    private String numAleatorio() {
        int numero = (int) (Math.random() * 5) + 1;
        return String.valueOf(numero);
    }


    public String getRandomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    /**
     * método para deslogearse
     * @param view : opción elegida por el usuario.
     */
    public void ClickSalir(View view) {
        mp.start();
        FirebaseAuth.getInstance().signOut();
        Intent login = new Intent(this, Login.class);
        startActivity(login);
    }

    /**
     * método para ir a la clase Subir
     * @param view : opción elegida por el usuario.
     */
    public void ClickSubir(View view) {
        mp.start();
        Intent i = new Intent(this, Subir.class);
        startActivity(i);
    }
    /**
     * método para ir a la clase Busqueda
     * @param view : opción elegida por el usuario.
     */
    public void ClickBusqueda(View view) {
        mp.start();
        Intent i = new Intent(this, Busqueda.class);
        startActivity(i);
    }
    /**
     * método para ir a la clase Ajustes
     * @param view : opción elegida por el usuario.
     */
    public void ClickAjustes(View view) {
        mp.start();
        Intent i = new Intent(this, Ajustes.class);
        startActivity(i);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mi_cuenta_foto:
                mp.start();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(Intent.createChooser(intent, "Select a picture for your profile"), CAMERA_REQUEST_CODE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            if (mAuth.getCurrentUser() == null)
                return;

            final Uri uri = data.getData();
            if (uri == null)
                return;
            if (mAuth.getCurrentUser() == null)
                return;

            if (mStorage == null)
                mStorage = FirebaseStorage.getInstance().getReference();
            if (mDatabase == null)
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Usuarios");

            final StorageReference filepath = mStorage.child("Fotos_usuarios").child(getRandomString());/*uri.getLastPathSegment()*/
            final DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());
            currentUserDB.child("foto_url").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String image = dataSnapshot.getValue().toString();

                    if (!image.equals("default") && !image.isEmpty()) {
                        Task<Void> task = FirebaseStorage.getInstance().getReferenceFromUrl(image).delete();
                        task.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                    Toast.makeText(MiCuenta.this, getResources().getString(R.string.Toast29), Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(MiCuenta.this, getResources().getString(R.string.Toast30), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    currentUserDB.child("foto_url").removeEventListener(this);

                    filepath.putFile(uri).addOnSuccessListener(MiCuenta.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUri = taskSnapshot.getDownloadUrl();
                            Toast.makeText(MiCuenta.this, getResources().getString(R.string.Toast31), Toast.LENGTH_SHORT).show();
                            Picasso.with(MiCuenta.this).load(uri).fit().centerCrop().into(mi_cuenta_foto);
                            DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());
                            currentUserDB.child("foto_url").setValue(downloadUri.toString());
                        }
                    }).addOnFailureListener(MiCuenta.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MiCuenta.this, getResources().getString(R.string.Toast32), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

}