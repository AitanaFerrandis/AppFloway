package paisdeyann.floway.Objetos;

import android.util.Log;

/**
 * Created by caboc on 19/02/2017.
 */

public class Publicacion {

    int usuarioEmisor;
    int usuarioReceptor;
    String nombreUsuarioEmisor;
    String fecha;
    String mensaje;

    public Publicacion(){}

    public Publicacion(int usuarioEmisor, int usuarioReceptor, String nombreUsuarioEmisor, String fecha, String mensaje) {

        this.usuarioEmisor = usuarioEmisor;
        this.usuarioReceptor = usuarioReceptor;
        this.nombreUsuarioEmisor = nombreUsuarioEmisor;
        this.fecha = fecha;
        this.mensaje = mensaje;
    }

    public int getUsuarioEmisor() {
        return usuarioEmisor;
    }

    public void setUsuarioEmisor(int usuarioEmisor) {
        this.usuarioEmisor = usuarioEmisor;
    }

    public int getUsuarioReceptor() {
        return usuarioReceptor;
    }

    public void setUsuarioReceptor(int usuarioReceptor) {
        this.usuarioReceptor = usuarioReceptor;
    }

    public String getNombreUsuarioEmisor() {
        return nombreUsuarioEmisor;
    }

    public void setNombreUsuarioEmisor(String nombreUsuarioEmisor) {
        this.nombreUsuarioEmisor = nombreUsuarioEmisor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void imprimir(){

        Log.d("prueba","usuario emisor "+getNombreUsuarioEmisor()+" idemisor "+getUsuarioEmisor()+" idreceptor "+getUsuarioReceptor()+" fecha "+getFecha()+" mensaje "+getMensaje());

    }


}
