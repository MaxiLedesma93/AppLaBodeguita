package com.ledesmalillo.labodeguitaapp.ui.pagos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ledesmalillo.labodeguitaapp.Modelos.PagoDetalle;
import com.ledesmalillo.labodeguitaapp.request.ApiClient;
import com.ledesmalillo.labodeguitaapp.utils.Constantes;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PagosViewModel extends AndroidViewModel {
    private Context context;
    private MutableLiveData<Double> mutableTotalMercadoPago = new MutableLiveData<>();
    private MutableLiveData<Double> mutableTotalEfectivo = new MutableLiveData<>();
    private MutableLiveData<Double> mutableTotalGeneral = new MutableLiveData<>();
    private MutableLiveData<List<PagoDetalle>> mutListaPagoDetalle = new MutableLiveData<>();

    public PagosViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }
    public LiveData<Double> getTotalMercadoPago() {
        return mutableTotalMercadoPago;
    }
    public LiveData<Double> getTotalEfectivo() {
        return mutableTotalEfectivo;
    }
    public LiveData<Double> getTotalGeneral() {
        return mutableTotalGeneral;
    }
    public LiveData<List<PagoDetalle>> getListaPagoDetalle() {
        return mutListaPagoDetalle;
    }
    public void cargarCierreCaja(String fecha){
        SharedPreferences sp = ApiClient.conectar(context);
        String token = sp.getString("token", "no token");
        ApiClient.MisEndPoints api = ApiClient.getEndPoints();
        //Date fecha = new Date(fechaSinFormato);
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        //LocalDate fecha = LocalDate.parse(fechaSinFormato, formatter);
        Call<List<PagoDetalle>> callPagos = api.obtenerTotalFacturado(token, fecha);
        callPagos.enqueue(new Callback<List<PagoDetalle>>() {
            @Override
            public void onResponse(Call<List<PagoDetalle>> call, Response<List<PagoDetalle>> response) {
                if(response.isSuccessful()){
                    mutListaPagoDetalle.setValue(response.body());
                    calcularTotales();
                }
            }
            @Override
            public void onFailure(Call<List<PagoDetalle>> call, Throwable t) {}
        });
    }
    public void calcularTotales(){
        List<PagoDetalle> listaPagos = mutListaPagoDetalle.getValue();
        double totalMercadoPago = 0;
        double totalEfectivo = 0;
        if (listaPagos != null) {
            for (PagoDetalle pago : listaPagos) {
                if(pago.getMetodoDePago().equals(Constantes.METODO_PAGO_MERCADO_PAGO)){
                    totalMercadoPago += pago.getImporte();
                }else {
                    totalEfectivo += pago.getImporte();
                }
            }
        }
        mutableTotalMercadoPago.setValue(totalMercadoPago);
        mutableTotalEfectivo.setValue(totalEfectivo);
        mutableTotalGeneral.setValue(totalMercadoPago + totalEfectivo);
    }

}