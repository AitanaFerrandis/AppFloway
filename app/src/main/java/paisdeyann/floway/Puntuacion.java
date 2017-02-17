package paisdeyann.floway;

/**
 * Created by Aitana on 17/02/2017.
 */

public class Puntuacion {

    String nombre;
    String apellidos;
    String puntuacion;

    public Puntuacion(String nombre, String apellidos, String puntuacion){
        this.nombre = nombre;
        this.puntuacion = puntuacion;
        this.apellidos = apellidos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(String puntuacion) {
        this.puntuacion = puntuacion;
    }
}
