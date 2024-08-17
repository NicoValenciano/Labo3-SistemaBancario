package ar.edu.utn.frbb.tup.controller;

public class OperacionRespuesta {

    private String estado;
    private String mensaje;

    public OperacionRespuesta(String estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }

    public OperacionRespuesta() {
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
