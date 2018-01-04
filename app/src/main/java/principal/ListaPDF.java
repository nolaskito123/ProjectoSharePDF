package principal;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.alex.sharepdf.R;

import java.util.List;

/**
 * Created by SharePDF developers
 *
 * Clase ListaPDF, es una clase para imprimir por pantalla toda la lista de archivos disponible para descargar.
 */

public class ListaPDF extends ArrayAdapter<PDF> {

    private Activity context;
    private List<PDF> listaPDF;

    /**
     * Constructor de la clase ListaPDF, recibe 2 parámetros
     * @param context Contexto del activity donde se imprimirá las lista de archivos.
     * @param listaPDF Lista de archivos PDF para imprimir por pantalla.
     */
    public ListaPDF(Activity context, List<PDF> listaPDF) {
        super(context, R.layout.lista_contenido, listaPDF);
        this.context = context;
        this.listaPDF = listaPDF;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.lista_contenido, null, true);

        TextView archivo_nombre = (TextView) listViewItem.findViewById(R.id.archivo_nombre);
        TextView archivo_categoria = (TextView) listViewItem.findViewById(R.id.archivo_categoria);
        TextView archivo_val = (TextView) listViewItem.findViewById(R.id.archivo_val);

        PDF pdf = listaPDF.get(position);

        archivo_nombre.setText(pdf.getTitulo());
        archivo_categoria.setText(pdf.getCategoria());
        archivo_val.setText("" + pdf.getPuntuacion());

        return listViewItem;

    }
}