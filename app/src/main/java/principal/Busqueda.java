package principal;

import android.content.Intent;
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
 * Created by gtc5 on 29/11/2017.
 */

public class Busqueda extends AppCompatActivity {

    private DatabaseReference mDatabase;
    FloatingActionMenu MenuAccion;
    ListView lista_pdf;
    Spinner Lista_Categoria;
    String[] items;
    List<PDF> listaPDF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_busqueda);
        mDatabase = FirebaseDatabase.getInstance().getReference("PDF");
        listaPDF = new ArrayList<>();
        lista_pdf = (ListView) findViewById(R.id.lista_pdf);
        Lista_Categoria = (Spinner) findViewById(R.id.lista_categoria);
        items = getResources().getStringArray(R.array.Spinner_Categoria);
        ArrayAdapter<String> eleccion = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, items);
        eleccion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Lista_Categoria.setAdapter(eleccion);
        MenuAccion = findViewById(R.id.Principal);
        MenuAccion.setClosedOnTouchOutside(true);
        MenuAccion.setIconAnimated(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaPDF.clear();
                for(DataSnapshot PDFSnapshot : dataSnapshot.getChildren()){
                    PDF pdf = PDFSnapshot.getValue(PDF.class);
                    listaPDF.add(pdf);
                }
                ListaPDF adapter = new ListaPDF(Busqueda.this, listaPDF);
                lista_pdf.setAdapter((ListAdapter) adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        lista_pdf.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                PDF seleccionado = (PDF) adapter.getItemAtPosition(position);
                Intent i = new Intent(Busqueda.this, Info.class);
                i.putExtra("Nombre", seleccionado.getNombre());
                i.putExtra("Categoria", seleccionado.getCategoría());
                i.putExtra("Descripcion", seleccionado.getDescripcion());
                i.putExtra("Url", seleccionado.getUrl());
                startActivity(i);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row
            }
        });
    }

    public void ClickSubir(View view) {
        Intent i = new Intent(this, Subir.class);
        startActivity(i);
    }

    public void ClickMiCuenta(View view) {
        Intent i = new Intent(this, MiCuenta.class);
        startActivity(i);
    }

    public void ClickArchivos(View view) {
        Intent i = new Intent(this, Archivos.class);
        startActivity(i);
    }

    public void ClickAjustes(View view) {
        Intent i = new Intent(this, Ajustes.class);
        startActivity(i);
    }
}
