package principal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.alex.sharepdf.R;
import com.google.firebase.auth.FirebaseAuth;

import auth.Login;

/**
 * Created by Alex on 12/11/2017.
 */

public class Principal extends AppCompatActivity implements View.OnClickListener {
    Button principal_cuenta, principal_bajar, principal_subir, principal_cerrar_sesion,
            principa_comprar;
    TextView principal_creditos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.layout_principal);

        principal_cuenta = (Button)findViewById(R.id.principal_cuenta);
        principal_bajar= (Button)findViewById(R.id.principal_bajar);
        principal_subir = (Button)findViewById(R.id.principal_subir);
        principal_cerrar_sesion = (Button)findViewById(R.id.principal_cerrar_sesion);
        principa_comprar = (Button)findViewById(R.id.principal_compar);
        principal_creditos = (TextView)findViewById(R.id.principal_creditos);

        principal_cerrar_sesion.setOnClickListener(this);
        principal_cuenta.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.principal_cerrar_sesion:
                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(Principal.this, Login.class);
                startActivity(login);
                break;
            case R.id.principal_cuenta:
                Intent cuenta = new Intent(Principal.this, MiCuenta.class);
                startActivity(cuenta);
                break;

        }

    }

}
