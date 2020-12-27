package com.example.notificaciones;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

// esto es un service una actuivity sin interfaz
public class MostrarNotificacionService extends IntentService {
//estas son la variables y los metodos que hemos creado en el mein los hemos traido a esta clase que obliga a externder el intent service
    public final static String CHANNEL_ID = "notificacion";
    public final static int NOTIFICACION_ID = 1;

    public MostrarNotificacionService() {
        super("MostrarNotificacionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //código para mostrar la notificación
        //llamamos al metodo que crea la notificacion
        creacionCANALNotificacionSuperiorOreo();
        creaNotificacion();

    }

    //TODO/////////////////////////////////// METODO HECHO  ////////////////////////////////////////////////////////

    //CREAR LAS ALERTAS DEL MOVIL
    private PendingIntent abreCtivityEscuhar() {

        // creamos un intent para ir a nuestra segunda pantalla cuando pulsemos en el icono
        Intent intent = new Intent(getApplicationContext(), OirNotificacionActivity.class);
        // hacemos que si el usuario le da atras vaya a nuestro main o se saldra de app
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        // le decimos que pantalla queremos que lo haga
        taskStackBuilder.addParentStack(OirNotificacionActivity.class);
        // le pasamos el intent
        taskStackBuilder.addNextIntent(intent);
        // y luego a nuestra variable pending le decimos que coja el taskt y le pasamos esos dos parametros
        //pendingIntent= taskStackBuilder.getPendingIntent(1,PendingIntent.FLAG_UPDATE_CURRENT);
        //devuelvo el pending intent que acabo de crear
        return taskStackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    // LE DAMOS FORMA A LA NOTIFICACION COLORES Y DE MAS HISTORIAS, SI LA VERSION ES ANTERIOR A OREO SE EJECUTARA NORMAL SI NO NO FUNCIONARA
    private void creaNotificacion() {
        //T CREAMOS LA NOTIFICACION Y LE PASAMOS EL NANAL
        NotificationCompat.Builder contructor = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        // CREAMOS EL ICONO QUE APARECERA
        contructor.setSmallIcon(R.drawable.ic_baseline_hearing_24);
        // CREAMOS EL TITUTLO Y EL CONTENIDO QUE QUEREMOS QUE APAREZCAN
        contructor.setContentTitle("Nota de audio");
        contructor.setContentText("Se borrara despues de oirlo");
        // LE PONEMOS UN COLOR PARA QUE QUEDE BONITO
        contructor.setColor(Color.RED);
        // LE DAMOS UNA PRIORIDAD, AUNQUE NO ES NECESARIO
        contructor.setPriority(NotificationCompat.PRIORITY_MAX);
        //LE PONEMOS UNA LUCECITA DE ALERTA PASANDO EL CORLOR Y LOS SEGUNDOS
        contructor.setLights(Color.RED, 1000, 1000);
        //VIVRACION aunq en el emulador no se ve queda mas pofesiona
        contructor.setVibrate(new long[]{1000, 1000, 1000});
        // ESTO VALE PARA PONER UN SONIDO POR DEFECTO O CREANDO UNA CARPETA CON SONIDOS LE PASAMOS EL NUESTRO
        // QUIZAS PUEDA HACER QUE AL LLEGAR LA NOTIFICACION SE ESCUCHE DIRECTAMENTE CON ESTE PARAMETRO.
        contructor.setDefaults(Notification.DEFAULT_SOUND);

        //esto hay que ponerlo para cuando le demos a la notificacion abra nuestra activity 2
        // al contructor le ponemos el content inten y nuestra variable pending
        //contructor.setContentIntent(pendingIntent);
        //llamando directamente el pending intent que devolvemos de arriba
        contructor.setContentIntent(abreCtivityEscuhar());
        // AHOPRA CREMOS LA VARIABLE
        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationCompat.notify(NOTIFICACION_ID, contructor.build());
    }
    private void creacionCANALNotificacionSuperiorOreo() {
        //SI LA VERSION DEL SDK ES SUPERIOR O IGUAL A O HACES LO SIGUIENTE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //HAY QUE CREAR UN NOMBRE PARA EL CANAL
            CharSequence nombre = "Notificacion";
            // y aqui le pasamos nuestro canal nuestro nombre que acabamos de crear y la importancia de las notificaciones en mi caso alto que vale 4segundos
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, nombre, NotificationManager.IMPORTANCE_HIGH);
            // creamos el manager lo casteamos y cogemos el recurso del sistema
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // creamos el canal con nuestra notificacion
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}

