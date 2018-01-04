package principal;

/**
 * Created by SharePDF
 * Clase PDF es una clase para poder subir y descargar archivos de la base de datos de Firebase
 */

public class PDF {
    private String fechaPublicacion, categoria, descripcion, email, idUsuario;
    private int puntuacion;
    private String titulo, url;

    public PDF() {

    }

    /**
     * Constructor de la clase PDF, recibe 8 parámetros
     * @param fechaPublicacion Fecha de subida del archivo PDF.
     * @param categoria Indica la categoría del archivo.
     * @param descripcion Descripción del archivo.
     * @param email Email del usuario que sube el archivo.
     * @param idUsuario Identiificación única del usuario que sube el archivo
     * @param puntuacion Puntuación del archivo subido, esto tiene valor diferente de 0 cuando está listo para descargar.
     * @param titulo Título del archivo a subir.
     * @param url Dirección url del archivo para descargar.
     */
    public PDF(String fechaPublicacion, String categoria, String descripcion, String email, String idUsuario, int puntuacion, String titulo, String url) {
        this.fechaPublicacion = fechaPublicacion;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.email = email;
        this.idUsuario = idUsuario;
        this.puntuacion = puntuacion;
        this.titulo = titulo;
        this.url = url;
    }

    /**
     * Devuelve la fecha de subida del archivo.
     * @return fecha con el formato dd/mm/yy.
     */
    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    /**
     * Devuelve la categoría del archivo subido
     * @return categoría
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Devuelve la descripción del archivo subido
     * @return descripción
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Devuelve el email del usuario que ha subido el archivo
     * @return email
     */
    public String getEmail(){return email;}

    /**
     * Devuelve el id de usuario que ha subido el archivo
     * @return id de usuario
     */
    public  String getIdUsuario(){return idUsuario;}
    /**
     * Devuelve la puntuación del archivo para descargar
     * @return puntuación
     */
    public int getPuntuacion() {
        return puntuacion;
    }

    /**
     * Devuelve el título del archivo
     * @return título
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Devuelve la dirección url del archivo para descargar
     * @return url
     */
    public String getUrl() {
        return url;
    }

}