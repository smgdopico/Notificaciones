package com.example.notificaciones;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MostrarNotifyBroadcastReceiver extends BroadcastReceiver {
    //creamos un numero de identificacion del servicio
    public final static int CODIGO_SERVICIO=123;



    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("x","entrando en el metodo");
        Intent i = new Intent(context, MostrarNotificacionService.class);
        //en el intent mandamos el nombre de la constante de sonido y la ruta
        i.putExtra(MostrarNotificacionService.RUTA_SONIDO,intent.getStringExtra(MostrarNotificacionService.RUTA_SONIDO));
        // y en el context iniciamos el servicio mandando el intent
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//banderita de android, Si se establece, esta actividad se convertirá en el inicio de una nueva tarea en esta pila de historial
        context.startService(i);
    }

}
