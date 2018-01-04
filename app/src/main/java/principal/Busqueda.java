package principal;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.alex.sharepdf.R;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SharePDF developers
 * Clase Busqueda, es la clase del activity Busqueda el cual muestra por pantalla toda la lista de archivos que se pueden
 * descargar de la base de datos. Cada item de la lista muetra el título, la categoría y la puntuación del archivo.
 * Al seleccionar un archivo pasamos al activity 'Info' para ver más detalles del archivo y descargarlo.
 */

public class Busqueda extends AppCompatActivity {

    private DatabaseReference mDatabase;
    FloatingActionMenu MenuAccion;
    ListView lista_pdf;
    Spinner Lista_Categoria;
    String[] items;
    List<PDF> listaPDF, listaPDF_R, listaPDF_E, listaPDF_P, listaPDF_M, listaPDF_F, listaPDF_Q, listaPDF_O;
    ListaPDF adapter;
    boolean leido = false;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_busqueda);
        mDatabase = FirebaseDatabase.getInstance().getReference("PDFREV");
        listaPDF = new ArrayList<>();
        listaPDF_R = new ArrayList<>();
        listaPDF_E = new ArrayList<>();
        listaPDF_P = new ArrayList<>();
        listaPDF_M = new ArrayList<>();
        listaPDF_F = new ArrayList<>();
        listaPDF_Q = new ArrayList<>();
        listaPDF_O = new ArrayList<>();
        lista_pdf = (ListView) findViewById(R.id.lista_pdf);
        Lista_Categoria = (Spinner) findViewById(R.id.lista_categoria);
        items = getResources().getStringArray(R.array.Spinner_Categoria);
        mp = MediaPlayer.create(this, R.raw.click);

        ArrayAdapter<String> eleccion = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        eleccion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Lista_Categoria.setAdapter(eleccion);
        MenuAccion = findViewById(R.id.Principal);
        MenuAccion.setClosedOnTouchOutside(true);
        MenuAccion.setIconAnimated(false);
    }
    @Override
    public void onBackPressed() {
        mp.start();
        super.onBackPressed();
        Intent i = new Intent(Busqueda.this, MiCuenta.class);
        startActivity(i);
        this.finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Limpiar();
                for (DataSnapshot PDFSnapshot : dataSnapshot.getChildren()) {
                    PDF pdf = PDFSnapshot.getValue(PDF.class);
                    listaPDF.add(pdf);
                    if (pdf.getCategoria().equals("Redes")) {
                        listaPDF_R.add(pdf);
                    } else if (pdf.getCategoria().equals("Electrónica")) {
                        listaPDF_E.add(pdf);
                    } else if (pdf.getCategoria().equals("Programación")) {
                        listaPDF_P.add(pdf);
                    } else if (pdf.getCategoria().equals("Matemáticas")) {
                        listaPDF_M.add(pdf);
                    } else if (pdf.getCategoria().equals("Física")) {
                        listaPDF_F.add(pdf);
                    } else if (pdf.getCategoria().equals("Química")) {
                        listaPDF_Q.add(pdf);
                    } else {
                        listaPDF_O.add(pdf);
                    }
                }
                adapter = new ListaPDF(Busqueda.this, listaPDF);
                lista_pdf.setAdapter(adapter);
                leido = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        lista_pdf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {

                PDF seleccionado = (PDF) adapter.getItemAtPosition(position);
                Intent i = new Intent(Busqueda.this, Info.class);
                i.putExtra("Nombre", seleccionado.getTitulo());
                i.putExtra("Categoria", seleccionado.getCategoria());
                i.putExtra("Fecha", seleccionado.getFechaPublicacion());
                i.putExtra("Puntuacion", seleccionado.getPuntuacion());
                i.putExtra("Descripcion", seleccionado.getDescripcion());
                i.putExtra("Url", seleccionado.getUrl());
                startActivity(i);
                finish();
            }
        });

        Lista_Categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (leido) {
                    switch (i) {
                        case 1:
                            adapter = new ListaPDF(Busqueda.this, listaPDF_R);
                            break;
                        case 2:
                            adapter = new ListaPDF(Busqueda.this, listaPDF_E);
                            break;
                        case 3:
                            adapter = new ListaPDF(Busqueda.this, listaPDF_P);
                            break;
                        case 4:
                            adapter = new ListaPDF(Busqueda.this, listaPDF_M);
                            break;
                        case 5:
                            adapter = new ListaPDF(Busqueda.this, listaPDF_F);
                            break;
                        case 6:
                            adapter = new ListaPDF(Busqueda.this, listaPDF_Q);
                            break;
                        case 7:
                            adapter = new ListaPDF(Busqueda.this, listaPDF_O);
                            break;
                        default:
                            adapter = new ListaPDF(Busqueda.this, listaPDF);
                            break;
                    }
                    lista_pdf.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    /**
     * Método que se usa para limpiar las listas de los archivos PDF antes de leer de base de datos y así evitar repetición de archivos.
     */
    public void Limpiar() {
        listaPDF.clear();
        listaPDF_R.clear();
        listaPDF_E.clear();
        listaPDF_P.clear();
        listaPDF_M.clear();
        listaPDF_F.clear();
        listaPDF_Q.clear();
        listaPDF_O.clear();
    }
    /**
     * método para ir a la clase Subir
     * @param view : opción elegida por el usuario.
     */
    public void ClickSubir(View view) {
        mp.start();
        Intent i = new Intent(this, Subir.class);
        startActivity(i);
        finish();
    }
    /**
     * método para ir a la clase MiCuenta
     * @param view : opción elegida por el usuario.
     */
    public void ClickMiCuenta(View view) {
        mp.start();
        Intent i = new Intent(this, MiCuenta.class);
        startActivity(i);
        finish();
    }
    /**
     * método para ir a la clase Ajustes
     * @param view : opción elegida por el usuario.
     */
    public void ClickAjustes(View view) {
        mp.start();
        Intent i = new Intent(this, Ajustes.class);
        startActivity(i);
        finish();
    }
}