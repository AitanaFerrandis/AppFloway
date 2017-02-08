package paisdeyann.floway.Objetos;

import java.sql.Blob;

/**
 * Created by caboc on 14/01/2017.
 */
public class Usuario {


    int id_usuario;
    String nombre;
    String apellidos;
    String usuario;
    String password;
    String poblacion;
    String cp;
    String horario;
    int puntuacion;
    String foto;


    public Usuario(int id, String nombre, String apellidos, String usuario, String password, String poblacion, String cp, String horario, int puntuacion, String blob) {
        this.id_usuario = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.usuario = usuario;
        this.password = password;
        this.poblacion = poblacion;
        this.cp = cp;
        this.horario = horario;
        this.puntuacion = puntuacion;
        this.foto = blob;

    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
