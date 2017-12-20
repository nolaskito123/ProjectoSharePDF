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
 * Created by Alex on 07/12/2017.
 */

public class ListaPDF  extends ArrayAdapter<PDF>{

    private Activity context;
    private List<PDF> listaPDF;

    public ListaPDF(Activity context, List<PDF> listaPDF){
        super(context,R.layout.lista_contenido,listaPDF);
        this.context=context;
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
        archivo_val.setText(""+pdf.getPuntuacion());

        return listViewItem;

    }
}