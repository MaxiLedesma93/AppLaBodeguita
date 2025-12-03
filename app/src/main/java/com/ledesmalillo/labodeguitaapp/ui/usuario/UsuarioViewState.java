package com.ledesmalillo.labodeguitaapp.ui.usuario;

import com.ledesmalillo.labodeguitaapp.Modelos.Usuario;

import java.io.Serializable;

public class UsuarioViewState implements Serializable {
    private  Usuario usuario;
    private  boolean camposHabilitados;
    public int  visibilidadBotonEditar;
    public int visibilidadBotonGuardar;

    public UsuarioViewState() {
    }

    public UsuarioViewState(Usuario usuario, boolean camposHabilitados, int visibilidadBotonEditar, int visibilidadBotonGuardar) {
        this.usuario = usuario;
        this.camposHabilitados = camposHabilitados;
        this.visibilidadBotonEditar = visibilidadBotonEditar;
        this.visibilidadBotonGuardar = visibilidadBotonGuardar;
    }


    // Getters para todos los campos...
    public Usuario getUsuario() {
        if (usuario == null) {
            usuario = new Usuario();
        }
        return usuario; }
    public boolean isCamposHabilitados() { return camposHabilitados; }
    public int getVisibilidadBotonEditar() { return visibilidadBotonEditar; }
    public int getVisibilidadBotonGuardar() { return visibilidadBotonGuardar; }
}
