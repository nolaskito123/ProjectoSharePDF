package principal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.sharepdf.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by SharePDF developers
 * Clase Info, es la clase del Activity Info el cual muestra datos más concretos del archivo seleccionado en el activity 'Búsqueda'
 * si el usuario sigue conforme con esto datos podrá descargar el archivo.
 */
public class Info extends AppCompatActivity {
    private DatabaseReference mDatabase;
    TextView nombre, categoria, descripcion, puntuacion, fecha;
    Button descargar;
    String name, category, description, LinkDescarga, date;
    int scores;
    FirebaseStorage fs;
    StorageReference http;
    long creditos;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    MediaPlayer mp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_info);
        mDatabase = FirebaseDatabase.getInstance().getReference("Usuarios").child(user.getUid());
        nombre = (TextView) findViewById(R.id.PonerNombre);
        categoria = (TextView) findViewById(R.id.PonerCategoria);
        fecha = (TextView) findViewById(R.id.PonerFecha);
        descripcion = (TextView) findViewById(R.id.PonerDescripcion);
        descargar = (Button) findViewById(R.id.Descargar);
        puntuacion = (TextView) findViewById(R.id.PonerPuntuacion);
        mp = MediaPlayer.create(this,R.raw.click);
        fs = FirebaseStorage.getInstance();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                creditos = (long) dataSnapshot.child("creditos").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        name = (String) getIntent().getExtras().get("Nombre");
        category = (String) getIntent().getExtras().get("Categoria");
        date = (String) getIntent().getExtras().get("Fecha");
        description = (String) getIntent().getExtras().get("Descripcion");
        LinkDescarga = (String) getIntent().getExtras().get("Url");
        scores = (int) getIntent().getExtras().get("Puntuacion");
        nombre.setText(name);
        categoria.setText(category);
        fecha.setText(date);
        descripcion.setText(description);
        puntuacion.setText("" + scores);
        http = fs.getReferenceFromUrl(LinkDescarga);
        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
                if (user.isEmailVerified()) {
                    if (creditos >= 6) {
                        Descarga();
                    } else {
                        Toast.makeText(Info.this, getResources().getString(R.string.Toast12), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Info.this, getResources().getString(R.string.Toast28), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    /**
     * Método que descarga el archivo seleccionado préviamente en el activity 'Busqueda'. Guarda el archivo en una ruta
     * predeterminada de la aplicación. Una vez descargado el archivo actualiza los créditos disponibles del usuario.
     */
    private void Descarga() {

        File localFile = null;
        try {
            localFile = File.createTempFile("application", name, getExternalFilesDir(null));

            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle(getResources().getString(R.string.Texto4));
            pd.show();
            http.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    creditos=creditos-6;
                    mDatabase.child("creditos").setValue(creditos);
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Toast5), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    double porcentaje = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    pd.setMessage(((int) porcentaje) + getResources().getString(R.string.Texto5));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {

        mp.start();
        super.onBackPressed();
        Intent i = new Intent(Info.this, Busqueda.class);
        startActivity(i);
        this.finish();
    }
}