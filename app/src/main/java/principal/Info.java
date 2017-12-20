package principal;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.sharepdf.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Alex on 07/12/2017.
 */

public class Info extends AppCompatActivity {
    TextView nombre, categoria, descripcion, puntuacion;
    Button descargar;
    String name, category, description, LinkDescarga;
    int scores;
    FirebaseStorage fs;
    StorageReference http;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_info);
        nombre = (TextView) findViewById(R.id.PonerNombre);
        categoria = (TextView) findViewById(R.id.PonerCategoria);
        descripcion = (TextView) findViewById(R.id.PonerDescripcion);
        descargar = (Button) findViewById(R.id.Descargar);
        puntuacion = (TextView) findViewById(R.id.PonerPuntuacion);
        fs = FirebaseStorage.getInstance();


        name = (String) getIntent().getExtras().get("Nombre");
        category = (String) getIntent().getExtras().get("Categoria");
        description = (String) getIntent().getExtras().get("Descripcion");
        LinkDescarga = (String) getIntent().getExtras().get("Url");

        scores = (int) getIntent().getExtras().get("Puntuacion");
        nombre.setText(name);
        categoria.setText(category);
        descripcion.setText(description);
        puntuacion.setText(""+scores);
        http = fs.getReferenceFromUrl(LinkDescarga);

        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Descarga();
            }
        });
    }

    private void Descarga() {
        File localFile = null;
        try {
            localFile = File.createTempFile("appllication", name, getExternalFilesDir(null));

            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle(getResources().getString(R.string.Texto4));
            pd.show();
            http.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    System.out.println(",,,,,,Descarga hecha,,,");
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Toast5),Toast.LENGTH_SHORT).show(); }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    double porcentaje = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    pd.setMessage(((int)porcentaje)+ getResources().getString(R.string.Texto5));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("--------  "+localFile.getAbsolutePath());
    }
}


