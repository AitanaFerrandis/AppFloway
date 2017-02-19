package paisdeyann.floway.Objetos;

import android.util.Log;

import java.io.Serializable;
import java.sql.Blob;

/**
 * Created by caboc on 14/01/2017.
 */
public class Usuario implements Serializable{


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
    int conductor;
    int conectado;
    double longitud;
    double latitud;


    public Usuario(int id, String nombre, String apellidos, String usuario, String password, String poblacion, String cp, String horario, int puntuacion, String blob, int conductor, int conectado, double longitud, double latitud) {
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
        this.conductor = conductor;
        this.conectado = conectado;
        this.longitud = longitud;
        this.latitud = latitud;

    }

    public int getConductor() {
        return conductor;
    }

    public void setConductor(int conductor) {
        this.conductor = conductor;
    }

    public int getConectado() {
        return conectado;
    }

    public void setConectado(int conectado) {
        this.conectado = conectado;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
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

    public void imprimir(){
        Log.d("prueba",id_usuario+" "+nombre+" "+apellidos+" "+usuario+" "+password+" "+poblacion+" "+cp+" "+horario+" "+puntuacion+" "+foto+" "+conectado+" "+conductor+" "+longitud+" "+latitud);
    }
}
