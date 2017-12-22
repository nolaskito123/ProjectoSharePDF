package principal;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.alex.sharepdf.R;
import com.github.clans.fab.FloatingActionMenu;

import java.util.Locale;

import auth.Login;

/**
 * Created by gtc5 on 29/11/2017.
 */

public class Ajustes extends AppCompatActivity {
    FloatingActionMenu MenuAccion;
    private Button contra, idioma;
    private Locale locale;
    private Configuration config = new Configuration();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_ajustes);
        contra = (Button) findViewById(R.id.cambiar_pass);
        idioma = (Button) findViewById(R.id.cambiar_idioma);
        MenuAccion = findViewById(R.id.Principal);
        MenuAccion.setClosedOnTouchOutside(true);
        MenuAccion.setIconAnimated(false);

        contra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent actulizar_usuario = new Intent(Ajustes.this, ResetPasswordActivity.class);
                startActivity(actulizar_usuario);

            }
        });
        idioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CambioIdioma();
            }
        });
    }

    private void CambioIdioma(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getResources().getString(R.string.Texto6));
        String[] idiomas = getResources().getStringArray(R.array.idiomas);
        b.setItems(idiomas, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch(which){
                    case 0:
                        locale = new Locale("es");
                        config.locale =locale;
                        break;
                    case 1:
                        locale = new Locale("ca");
                        config.locale =locale;
                        break;
                    case 2:
                        locale = new Locale("en");
                        config.locale =locale;
                        break;
                }
                getResources().updateConfiguration(config, null);
                Intent refresh = new Intent(Ajustes.this, Ajustes.class);
                startActivity(refresh);
                finish();
            }

        });

        b.show();
    }

    public void ClickMiCuenta(View view){
        Intent i = new Intent(this, MiCuenta.class);
        startActivity(i);
    }
    public void ClickSubir(View view){
        Intent i = new Intent(this, Subir.class);
        startActivity(i);
    }
    public void ClickBusqueda(View view){
        Intent i = new Intent(this, Busqueda.class);
        startActivity(i);
    }
}