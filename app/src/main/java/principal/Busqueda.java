package principal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.alex.sharepdf.R;
import com.github.clans.fab.FloatingActionMenu;

/**
 * Created by gtc5 on 29/11/2017.
 */

public class Busqueda extends AppCompatActivity {

    FloatingActionMenu MenuAccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_busqueda);
        MenuAccion = findViewById(R.id.Principal);
        MenuAccion.setClosedOnTouchOutside(true);
        MenuAccion.setIconAnimated(false);
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
