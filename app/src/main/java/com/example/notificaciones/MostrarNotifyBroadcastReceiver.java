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
     //   Toast.makeText(MainActivity.this,"entrando en el recibe del broad",Toast.LENGTH_LONG);
        Log.i(MainActivity.DEBUG_TAG, "BOOT Complete received by Client !");
        Log.d(MainActivity.DEBUG_TAG,"****************");

        Intent i = new Intent(context, MostrarNotificacionService.class);
        //   Intent i = new Intent(MainActivity.this, ServiceNotificacion.class);
        //en el intente mandamos el nombre de la constante de sonido y la ruta
        i.putExtra(MostrarNotificacionService.RUTA_SONIDO,intent.getStringExtra(MostrarNotificacionService.RUTA_SONIDO));
        // y en el contextr iniciamos el servicio mandando el intent
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(i);
        goAsync();

    }

    /* Esto puede ser llamado por una aplicación onReceive(Context, Intent)para permitirle mantener la transmisión activa después de regresar de esa función.
    public final void goAsync(){ }


Recupere los datos de resultados actuales, según lo establecido por el receptor anterior. A menudo, esto es nulo.
        public final String getResultData (){
return   "nulo;"
    }
    */

}
