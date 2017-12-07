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
    TextView nombre, categoria, descripcion;
    Button descargar;
    String name, category, description, LinkDescarga;
    Uri url = Uri.parse("/storage/0/Download");
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
        fs = FirebaseStorage.getInstance();

        name = (String) getIntent().getExtras().get("Nombre");
        category = (String) getIntent().getExtras().get("Categoria");
        description = (String) getIntent().getExtras().get("Descripcion");
        LinkDescarga = (String) getIntent().getExtras().get("Url");
        //url = Uri.parse(LinkDescarga);
        nombre.setText(name);
        categoria.setText(category);
        descripcion.setText(description);
        http = fs.getReferenceFromUrl(LinkDescarga);

        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Descarga();
                //downloadFile();
            }
        });
    }

    private void Descarga() {
        //File localFile = new File(getFilesDir(), name);
        //localFile.setReadable(true, false);
        File localFile = null;
        try {
            localFile = File.createTempFile("appllication", name, getExternalFilesDir(null));

            final ProgressDialog pd = new ProgressDialog(this);
            pd.setTitle("Descargando Archivo");
            pd.show();
            http.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    System.out.println(",,,,,,Descarga hecha,,,");
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Archivo descargado con éxito",Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    double porcentaje = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    pd.setMessage(((int)porcentaje)+ "%  Descargado");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("--------  "+localFile.getAbsolutePath());

    }

    private void downloadFile() {
        //FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference  islandRef = storageRef.child("/"+category+"/"+name);
        //final ProgressDialog pd = new ProgressDialog(this);
        //pd.setTitle("Descargando Archivo");
        //pd.show();
        final FileOutputStream[] outputStream = new FileOutputStream[1];

        final long ONE_MEGABYTE = 3*1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                try {
                    outputStream[0] = openFileOutput(name, MODE_WORLD_READABLE);
                    outputStream[0].write(bytes);
                    outputStream[0].close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "Archivo descargado con éxito",Toast.LENGTH_SHORT).show();
            }
        });

    }

}


