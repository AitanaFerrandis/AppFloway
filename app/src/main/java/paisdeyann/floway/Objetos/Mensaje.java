package paisdeyann.floway.Objetos;

/**
 * Created by caboc on 15/02/2017.
 */

public class Mensaje {

    String contenido;
    String fecha;
    int emisor;

    public Mensaje(){}

    public Mensaje(String contenido, String fecha, int emisor) {
        this.contenido = contenido;
        this.fecha = fecha;
        this.emisor = emisor;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getEmisor() {
        return emisor;
    }

    public void setEmisor(int emisor) {
        this.emisor = emisor;
    }
/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mensaje)) return false;

        Mensaje mensaje = (Mensaje) o;

        if (getEmisor() != mensaje.getEmisor()) return false;
        if (!getContenido().equals(mensaje.getContenido())) return false;
        return getFecha().equals(mensaje.getFecha());

    }

    @Override
    public int hashCode() {
        int result = getContenido().hashCode();
        result = 31 * result + getFecha().hashCode();
        result = 31 * result + getEmisor();
        return result;
    }
    */
}
