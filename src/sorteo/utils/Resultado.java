/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sorteo.utils;

/**
 *
 * @author esanchez
 */
public class Resultado<E> {

    TipoResultado resultado;
    String mensaje;
    E object;

    public Resultado() {
        setResultado(TipoResultado.SUCCESS);
    }

    public TipoResultado getResultado() {
        return resultado;
    }

    public void setResultado(TipoResultado resultado) {
        this.resultado = resultado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public E get() {
        return object;
    }

    public void set(E object) {
        this.object = object;
    }
}
