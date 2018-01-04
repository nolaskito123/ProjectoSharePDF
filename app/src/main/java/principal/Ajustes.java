package principal;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.alex.sharepdf.R;
import com.github.clans.fab.FloatingActionMenu;

import java.util.Locale;

import auth.ResetPasswordActivity;

/**
 * Created by gtc5 on 29/11/2017.
 */

/**
 * Clase Ajustes, clase utilizada para cambiar de idioma, mostrar información de la empresa  y
 * también se podrá escribir directamente a atención al cliente de sharePDF.
 */
public class Ajustes extends AppCompatActivity {
    FloatingActionMenu MenuAccion;
    private Button idioma, acerca, enviar;
    private EditText TextoEnviar;
    private Locale locale;
    private Configuration config = new Configuration();
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_ajustes);
        idioma = (Button) findViewById(R.id.cambiar_idioma);
        acerca = (Button) findViewById(R.id.acerca);
        enviar = (Button) findViewById(R.id.enviar);
        TextoEnviar = (EditText) findViewById(R.id.TextoEnviar);
        mp = MediaPlayer.create(this, R.raw.click);


        MenuAccion = findViewById(R.id.Principal);
        MenuAccion.setClosedOnTouchOutside(true);
        MenuAccion.setIconAnimated(false);

        idioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CambioIdioma();
            }
        });
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                EnviarCorreo();
            }
        });
        acerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Acerca();
            }
        });
    }

    /**
     * Método que sirve para poder enviar un correo a atención al cliente de sharePDF.
     */
    private void EnviarCorreo() {

        Intent itSend = new Intent(android.content.Intent.ACTION_SEND);


        itSend.setType("plain/text");
        String email = "project.sharepdf@gmail.com";
        String etSubject = "Clientes";

        //colocamos los datos para el envío
        itSend.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ email});
        itSend.putExtra(android.content.Intent.EXTRA_SUBJECT, etSubject);
        itSend.putExtra(android.content.Intent.EXTRA_TEXT, TextoEnviar.getText());

        //iniciamos la actividad
        startActivity(itSend);
    }

    @Override
    public void onBackPressed() {
        mp.start();
        super.onBackPressed();
        Intent i = new Intent(Ajustes.this, MiCuenta.class);
        startActivity(i);
        this.finish();
    }

    /**
     * Método que sirve para poder cambiar de idioma(español, catalán o inglés)
     */
    private void CambioIdioma() {
        mp.start();
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getResources().getString(R.string.Texto6));
        String[] idiomas = getResources().getStringArray(R.array.idiomas);
        b.setItems(idiomas, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (which) {
                    case 0:
                        locale = new Locale("es");
                        config.locale = locale;
                        break;
                    case 1:
                        locale = new Locale("ca");
                        config.locale = locale;
                        break;
                    case 2:
                        locale = new Locale("en");
                        config.locale = locale;
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

    /**
     * Método que sirve para mostrar al usuario información basica de la empresa sharePDF
     */
    private void Acerca() {
        mp.start();
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setIcon(R.mipmap.ic_launcher);
        b.setTitle(getResources().getString(R.string.app_name));
        b.setMessage("SharePDF app © Todos los derechos reservados \n"+"Palma de Mallorca(I.Baleares, España)\n"+"Tel:(+34) 692069699 \n"+"Correo: project.sharepdf@gmail.com");
        b.show();
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
     * método para ir a la clase Busqueda
     * @param view : opción elegida por el usuario.
     */
    public void ClickBusqueda(View view) {
        mp.start();
        Intent i = new Intent(this, Busqueda.class);
        startActivity(i);
        finish();
    }
}