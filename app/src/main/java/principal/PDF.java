package principal;

/**
 * Created by gtc5 on 29/11/2017.
 */

public class PDF {
    private String fecha, categoria, descripcion, email, idUsuario;
    private int puntuacion;
    private String titulo, url;

    public PDF() {

    }

    public PDF(String fecha, String categoria, String descripcion, String email, String idUsuario, int puntuacion, String titulo, String url) {
        this.fecha = fecha;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.email = email;
        this.idUsuario = idUsuario;
        this.puntuacion = puntuacion;
        this.titulo = titulo;
        this.url = url;
    }

    public String getFecha() {
        return fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEmail() {
        return email;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getUrl() {
        return url;
    }

}