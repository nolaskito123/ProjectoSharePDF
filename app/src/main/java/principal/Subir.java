package principal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alex.sharepdf.R;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by gtc5 on 29/11/2017.
 */

public class Subir extends AppCompatActivity implements View.OnClickListener{
    FloatingActionMenu MenuAccion;
    EditText Name, Descrip;
    Spinner ListaItems;
    final String[] Items = {"Redes", "Electrónica", "Programación", "Matemáticas", "Física", "Química", "Otros"};
    private Button BotonEscoger, BotonSubir;
    final static int PICK_PDF_CODE = 2342;
    private String rutaString="";
    private Uri rutaUri;
    private String Categoria;
    private StorageReference sr;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_subir);
        MenuAccion = findViewById(R.id.Principal);
        MenuAccion.setClosedOnTouchOutside(true);
        MenuAccion.setIconAnimated(false);
        sr = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference("PDF");
        Name = (EditText) findViewById(R.id.pdf);
        Descrip = (EditText) findViewById(R.id.CampoDescripcion);
        BotonEscoger = (Button) findViewById(R.id.BotonEscoger);
        BotonSubir = (Button) findViewById(R.id.BotonSubir);
        ListaItems = (Spinner) findViewById(R.id.EscogerCategoria);
        BotonEscoger.setOnClickListener(this);
        BotonSubir.setOnClickListener(this);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Items);
        ListaItems.setAdapter(adapter);
        ListaItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                Categoria =Items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
    }


    @Override
    public void onClick(View view) {
        if(view == BotonEscoger){ //Escoger archivos para subir
            getPDF();
        }else if(view == BotonSubir){ //Subir archivo a la nube
            if(rutaString.length()!=0 && !Name.getText().toString().isEmpty() && !Descrip.getText().toString().isEmpty() ){
                SubirArchivo(rutaUri);
            } else if(rutaString.length()==0){
                Toast.makeText(this, "Selecciona archivo",Toast.LENGTH_SHORT).show();
            }else if(Name.getText().toString().isEmpty()){
                Toast.makeText(this, "Pon nombre al archivo",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Pon una descripción",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona archivo"), PICK_PDF_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            rutaString = data.getDataString();
            rutaUri = data.getData();
            Name.setText(rutaUri.getLastPathSegment());
        }
    }

    private void SubirArchivo(Uri data){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Subiendo Archivo");
        pd.show();
        StorageReference riversRef = sr.child(Categoria +"/"+Name.getText().toString()+".pdf");
        riversRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri p =taskSnapshot.getDownloadUrl();
                        ActualizarDB(p);
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Archivo subido con éxito",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double porcentaje = (100.0*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                        pd.setMessage(((int)porcentaje)+ "%  Subido");
                    }
                });
    }

    public void ActualizarDB(Uri Url){
        String id = mDatabase.push().getKey();
        PDF pdfsubir = new PDF(Categoria, Descrip.getText().toString(), "gary@hola.com", id, Name.getText().toString()+".pdf", false, Url.toString());
        mDatabase.child(id).setValue(pdfsubir);
        Name.setText("");
        Descrip.setText("");
        rutaString="";
    }

    public void ClickMiCuenta(View view){
        Intent i = new Intent(this, MiCuenta.class);
        startActivity(i);
    }
    public void ClickBusqueda(View view){
        Intent i = new Intent(this, Busqueda.class);
        startActivity(i);
    }
    public void ClickArchivos(View view){
        Intent i = new Intent(this, Archivos.class);
        startActivity(i);
    }
    public void ClickAjustes(View view){
        Intent i = new Intent(this, Ajustes.class);
        startActivity(i);
    }
}
