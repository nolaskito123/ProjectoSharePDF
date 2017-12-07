package principal;

/**
 * Created by gtc5 on 29/11/2017.
 */

public class PDF {
    private String categoría, descripcion, email, id, nombre, url;
    private boolean revisado;
    private float valoracion;
    private int descargas;
    public PDF(){
    }

    public PDF(String categoría, String descripcion, String email, String id, String nombre,
               boolean revisado, String url, float valoracion, int descargas  ) {
        this.categoría = categoría;
        this.descripcion = descripcion;
        this.email = email;
        this.id = id;
        this.nombre = nombre;
        this.revisado = revisado;
        this.url = url;
        this.valoracion = valoracion;
        this.descargas = descargas;
    }

    public String getCategoría() {
        return categoría;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }

    public boolean isRevisado() {
        return revisado;
    }

    public float getValoracion() {
        return valoracion;
    }

    public int getDescargas() {
        return descargas;
    }
}
