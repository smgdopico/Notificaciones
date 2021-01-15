package com.example.notificaciones;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MostrarNotifyBroadcastReceiver extends BroadcastReceiver {
//creamos un numero de identificacion del servicio
    public final static int CODIGO_SERVICIO=123;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MostrarNotificacionService.class);
        //   Intent i = new Intent(MainActivity.this, ServiceNotificacion.class);
        //en el intente mandamos el nombre de la constante de sonido y la ruta
        i.putExtra(MostrarNotificacionService.RUTA_SONIDO,intent.getStringExtra(MostrarNotificacionService.RUTA_SONIDO));
        // y en el contextr iniciamos el servicio mandando el intent
        context.startService(i);

    }
}
