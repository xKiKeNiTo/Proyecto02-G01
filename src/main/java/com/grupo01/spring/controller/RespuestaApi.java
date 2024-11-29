package com.grupo01.spring.controller;

public class RespuestaApi<T> {
    private String mensaje;
    private int status;
    private T data;

    public RespuestaApi(String mensaje, int status, T data) {
        this.mensaje = mensaje;
        this.status = status;
        this.data = data;
    }

    //Getters y Setters
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
