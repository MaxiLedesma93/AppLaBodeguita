package com.ledesmalillo.labodeguitaapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.ledesmalillo.labodeguitaapp.request.ApiClient;

public class SessionManager {

    private static final String KEY_ROL = "rol";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_DIRECCION = "direccion";
    private static final String KEY_ID_CLIENTE = "idCliente";

    public static String getRol(Context context) {
        SharedPreferences sp = ApiClient.conectar(context);
        return sp.getString(KEY_ROL, "");
    }

    public static boolean esRecepcionista(Context context) {
        return getRol(context).equalsIgnoreCase("Recepcionista");
    }

    public static String getToken(Context context) {
        SharedPreferences sp = ApiClient.conectar(context);
        return sp.getString(KEY_TOKEN, "");
    }

    public static String getDireccion(Context context) {
        SharedPreferences sp = ApiClient.conectar(context);
        return sp.getString(KEY_DIRECCION, "");
    }

    public static String getIdCliente(Context context) {
        SharedPreferences sp = ApiClient.conectar(context);
        return sp.getString(KEY_ID_CLIENTE, "");
    }

    public static void cerrarSesion(Context context) {
        SharedPreferences sp = ApiClient.conectar(context);
        sp.edit().clear().apply();
    }
}
