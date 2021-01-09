package com.example.notificaciones;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MostrarNotifyBroadcastReceiver extends BroadcastReceiver {

    public final static int CODIGO_SERVICIO=123;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MostrarNotificacionService.class);
        //   Intent i = new Intent(MainActivity.this, ServiceNotificacion.class);
        i.putExtra(MostrarNotificacionService.RUTA_SONIDO,intent.getStringExtra(MostrarNotificacionService.RUTA_SONIDO));
        context.startService(i);

    }
}
