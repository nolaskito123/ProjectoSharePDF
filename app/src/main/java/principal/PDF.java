package principal;

/**
 * Created by gtc5 on 29/11/2017.
 */

public class PDF {
    public String categoría, descripcion, email, id, nombre;
    public boolean revisado;
    public String url;

    public PDF(){

    }

    public PDF(String categoría, String descripcion, String email, String id, String nombre, boolean revisado, String url) {
        this.categoría = categoría;
        this.descripcion = descripcion;
        this.email = email;
        this.id = id;
        this.nombre = nombre;
        this.revisado = revisado;
        this.url = url;
    }
}
