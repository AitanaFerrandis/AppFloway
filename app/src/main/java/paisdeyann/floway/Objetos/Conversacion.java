package paisdeyann.floway.Objetos;

/**
 * Created by caboc on 16/02/2017.
 */

public class Conversacion {
    int id1;
    int id2;
    String chat;
    String fecha;

    public Conversacion(){

    }



    public Conversacion(int id1, int id2, String chat, String fecha) {
        this.id1 = id1;
        this.id2 = id2;
        this.chat = chat;
        this.fecha = fecha;

    }

    public int getId1() {

        return id1;
    }

    public void setId1(int id1) {
        this.id1 = id1;
    }

    public int getId2() {
        return id2;
    }

    public void setId2(int id2) {
        this.id2 = id2;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
