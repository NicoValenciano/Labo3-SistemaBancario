package ar.edu.utn.frbb.tup.controller;

public class MovimientoRespuesta {

        private String estado;
        private String mensaje;

        public MovimientoRespuesta(String estado, String mensaje) {
            this.estado = estado;
            this.mensaje = mensaje;
        }

    public MovimientoRespuesta() {

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
