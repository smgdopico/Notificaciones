package com.example.notificaciones;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //codigo que manda poner Angel
    public final static boolean DEBUG = true;       // Está activada a depuración. Na versión release se ten que poñer a false
    public final static String DEBUG_TAG="notificaciones";
    public static void amosarMensaxeDebug(String mensaxe){
        if (DEBUG){
            Log.d(DEBUG_TAG,mensaxe);
        }
    }

    // variable pending
    private PendingIntent pendingIntent;
    //hay que crear el nombre del canal y la id del canal
    public final static String CHANNEL_ID = "notificacion";
    public final static int NOTIFICACION_ID = 1;
    //variables de fechas
    private int dia1, mes1, año1, hora1, minutos1;
    private String fechaString="";
    private Calendar fecha = Calendar.getInstance();
    // variable de alarma
    public AlarmManager alarmManager;
    //todo variables de la grabacion
    public MediaRecorder grabacion; //OBJETO
    public String rutaSalida = null; //RUTA DE SALIDA
    //private int grabacionContador = 1;//VARIABLE PARA QUE LAS GRABACIONES NO SE SOBREECRIBAn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //icono en el action bar porque queda bonito
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // necesita estas lineas de codigo para trabajar con los permisos de grabar guardar etc si no no funciona y lo ponemos EN el main pq si no los permite
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            //todo esto pide escribir y grabar
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, 1000);
        }

        gestion();
        //AlarmManager alarma manager despierta mi app y me muestra mi notificacion coger el sonido como se pueda y oirlo
        //SharedPreferences para guarda  valor que el nombre que invento y ruta sonido

        //codigo que manda poner Angel
        // Exemplo de uso
        // Poñemos no texto a Activity e o método onde nos atopamos xunto coa variable que queremos saber o seu valor
        amosarMensaxeDebug("MainActivity => onCreate => " + getApplicationInfo().loadLabel(getPackageManager()));
    }

    //TODO/////////////////////////////////// METODO HECHO  GESTION DE BOTONES ETC////////////////////////////////////////////////////////
    private void gestion() {

        Button bAlarma = findViewById(R.id.bAlarma);
        bAlarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pasamos el string de la fecha la hora y minutos y el calendar fecha
                crearAlarma(fechaString, hora1, minutos1, fecha);
            }
        });

        //TODO/////////////////////////////////// METODO HECHO  ////////////////////////////////////////////////////////
        //boton de grabar audio
        final Button bRec = findViewById(R.id.bRec);
        bRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (grabacion == null) {                  //contador++ o los milisegundos del sistema
                    rutaSalida = getFilesDir() + "/grabacion" + System.currentTimeMillis()+ ".mp3";//creamos un archivo de salida de audio
                    //rutaSalida=Environment.getDataDirectory().getAbsolutePath();
                    //  rutaSalida = Environment.getExternalStorageDirectory().getAbsolutePath() + "/grabacion.mp3";
                    grabacion = new MediaRecorder();
                    grabacion.setAudioSource(MediaRecorder.AudioSource.MIC); //usar el micro
                    grabacion.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);//captura y sale con three_gpp
                    grabacion.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);//tranforma da igual el formato
                    grabacion.setOutputFile(rutaSalida);//archivo de salida de la grabacion
                    try {
                        grabacion.prepare();//tenemos que pasar antes por el prepare o da error
                        grabacion.start();// e iniciamos
                    } catch (IOException e) {
                    }
                    bRec.setBackgroundResource(R.drawable.rec);// cambiar el color del boton cuando grabamos para que quede bonito
                    Toast.makeText(getApplicationContext(), "Grabando", Toast.LENGTH_SHORT).show();
                } else if (grabacion != null) {
                    //si es diferente de nulo necesitamos para la grabacion pasar por el release(lanzamiento) e igualarla a nula
                    grabacion.stop();
                    grabacion.release();
                    grabacion = null;
                    bRec.setBackgroundResource(R.drawable.stop_rec);//cambia el fondo del color al parar de grabar
                    Toast.makeText(getApplicationContext(), "Grabacion finalizada ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //TODO/////////////////////////////////// METODO HECHO  ////////////////////////////////////////////////////////
        //CREACION DE VARIABLES DONDE RECOGEMOS DE UN DATAPICKER LA FECHA Y LA HORA Y LAS COLOCAMOS EN UNAS CAJAS DE TEXTO NO EDITABLES
        final EditText txtF = findViewById(R.id.txtF);
        final EditText txtH = findViewById(R.id.txtH);

        ///////////////////////////////////BOTON HORA////////////////////////////////////////////////////////////
        final Button f = findViewById(R.id.bHora);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hacemos lo mismo que en fecha creamo sun calendario para recoger los datos de hora del sistema creamos un datapicker y guardamos los datos
                Calendar x = Calendar.getInstance();
                hora1 = x.get(Calendar.HOUR_OF_DAY);
                minutos1 = x.get(Calendar.MINUTE);
                TimePickerDialog dataPickerHora = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int horaDia, int minDia) {
                        txtH.setText(horaDia + " : " + minDia);
                        fecha.set(Calendar.HOUR_OF_DAY, horaDia);
                        fecha.set(Calendar.MINUTE, minDia);
                    }
                }, hora1, minutos1, true);
                dataPickerHora.show();//muestra el dialogo que contiene la hora en este caso
            }
        });

        ///////////////////////////////////BOTON FECHA////////////////////////////////////////////////////////////
        Button h = findViewById(R.id.bFecha);
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //HACEMOS UN CALENDARIO Y REOCGEMOS DEL SISTEMA LOS DATOS
                Calendar c = Calendar.getInstance();
                dia1 = c.get(Calendar.DAY_OF_MONTH);
                mes1 = c.get(Calendar.MONTH);
                año1 = c.get(Calendar.YEAR);
                //CREAMOS EL DATAPICKER PARA QUE SALGA EL CALENDARIO Y EL RELOJ (OJO AL CONTEXTO SE PASA ASI PQ SI NO ROMPE) Y SE CREA UN DATAPICKER...
                DatePickerDialog dataPickerFecha = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    //CAMBIAMOS LOS NOMBRES DE LOS ENTEROS PARA SABER QUE RECOGE
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        txtF.setText(String.valueOf(day) + " " + String.valueOf(month+1) + " " + String.valueOf(year));
                        fecha.set(year, month, day);//SE CUARDA EN UN CALENDAR NORMAL
                        fechaString = day + "/" + month + "/" + year;// TAMBIEN LOS METEMOS EN UN STRING FECHA
                    }
                }, año1, mes1, dia1);
                dataPickerFecha.show();
            }
        });

//FIN DE GESTION (DONDE ESTAN LOS BOTONES)
    }

    //TODO/////////////////////////////////// METODO HECHO  ////////////////////////////////////////////////////////
    //TODO CREAR LAS ALARMAS PARA LINKAR A LAS NOTIFICACIONES
    public void crearAlarma(final String fechaString, final int hora, int min, final Calendar fecha) {
            //COMPROBACIONES QUE LOS DATOS ESTEN CORRECTOS
        boolean banderita = true;
        if (fechaString.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No ha seleecionado fecha", Toast.LENGTH_SHORT).show();
            banderita = false;
        }
        if (hora1 == -1 || hora1 > 24) {
            Toast.makeText(getApplicationContext(), "No ha seleecionado una hora correcta", Toast.LENGTH_SHORT).show();
            banderita = false;
        }
        if (minutos1 == -1 || minutos1 > 59) {
            Toast.makeText(getApplicationContext(), "No ha seleecionado los minutos correctamente", Toast.LENGTH_SHORT).show();
            banderita = false;
        }

        //AQUI NOS MANDA A LA CLASE SERVICIO UNA ACTIVITY SIN INTERFAZ...
        if (banderita) {
            //se crea la alarma, se le pasa la app y se coge del sistema el alarm_service
            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
          /*  //esto es un intent y devuelve un penguing intent de esos  que hice en abreCtivityEscuhar();
            //tenemos que crear un servicio por eso creamos la clase mostrar notificaciones  y le pasamos este main
            //creamos un pending  y le mandamos main y nuestro intent, creamos la alarma al que le mandamos nuestra fecha que es clockInfo ese.. y nuestro pending intent y nos lleva
            a la clase que hicimos con los metodo que crean la notificacion*/
          //todo -------------------------------------------------------------------------------------------------------------
          //todo cambiamos este intent por el de la otra clase a ver si va
            Intent i = new Intent(MainActivity.this, MostrarNotificacionService.class);
         //   Intent i = new Intent(MainActivity.this, ServiceNotificacion.class);
            i.putExtra(MostrarNotificacionService.RUTA_SONIDO,rutaSalida);
            PendingIntent pi = PendingIntent.getService(MainActivity.this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(fecha.getTimeInMillis(), pi), pi);
            Toast.makeText(getApplicationContext(), "Alarma Guardada", Toast.LENGTH_LONG).show();
            //todo --------------------------------------------------------------------------------------------------------------
          /* FORMA PREVIA A LA SOLUCION ACTUAL
            //calendar recoge el TIEMPO REAL DESDE 1970 + 2000 QUE SON 2 SEGUNDOS Calendar.getInstance().getTime().getTime()+2000
            //\\\\\\\\\\\\\\\\\\\\ esto ya no es necerario pero no quiero borrarlo aun  porque las dos lineas de arriba hacen lo mismo que estas\\\\\\\\\\\\\\\\\\\\\\\\\\
            //  ((AlarmManager)getSystemService(ALARM_SERVICE)).set(AlarmManager.RTC_WAKEUP, Long.parseLong(fechaString+hora1+minutos1),abreCtivityEscuhar());
            //  crearAlarma(fechaString, hora1, minutos1);*/

        } else {
            Toast.makeText(getApplicationContext(), "faltan datos para establecer la alarma", Toast.LENGTH_SHORT).show();
        }
    }

}


//https://stackoverflow.com/questions/42126979/cannot-keep-android-service-alive-after-app-is-closed/64113820#64113820
//https://stackoverflow.com/questions/30525784/android-keep-service-running-when-app-is-killed
//https://developer.android.com/reference/android/service/dreams/DreamService
//https://developer.android.com/guide/components/activities/process-lifecycle
//https://es.stackoverflow.com/questions/373953/caused-by-android-view-inflateexception-binary-xml-file-line-8-error-inflati












//comentarios cosas a mejorar y metodos antiguos o modificaciones de cogigo que quedan aqui por si son necesarias de nuevo

//todo para mejorar la aplicacion crear una base de datos guardando la ruta de una carpeta con todos los audios
//todo estos metodos ya no hacen falta en el main pq los metimos en el servicio

 /*

         //INFO DE LA API EN CUESTION DE ALARMAS

       // void	setAlarmClock(AlarmManager.AlarmClockInfo info, PendingIntent operation)
        // Programe una alarma que represente un despertador, que se utilizará para notificar al usuario cuando suene.
        // void	setAndAllowWhileIdle(int type, long triggerAtMillis, PendingIntent operation)
        //Me gusta  set(int, long, android.app.PendingIntent), pero esta alarma podrá ejecutarse incluso cuando el sistema esté inactivo de baja potencia (también conocido como
        // void	setExact(int type, long triggerAtMillis, PendingIntent operation)
        // Programe una alarma para que se envíe con precisión a la hora indicada.
        //https://developer.android.com/reference/android/app/AlarmManager#setAlarmClock(android.app.AlarmManager.AlarmClockInfo,%20android.app.PendingIntent)
        // Este método es parecido setExact(int, long, android.app.PendingIntent), pero implica RTC_WAKEUP.
        // set(int, long, android.app.PendingIntent), pero esta alarma podrá ejecutarse incluso cuando el sistema esté en modo inactivo de bajo consumo

           ////////////METODO INSERVIBLE CREA LAS ALERTAS NADA MAS DARLE AL BOTON DE CREAR ///////////////////////////
       Button bCrear = findViewById(R.id.bCrear);
        bCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // creamos un metodo para que abra la 2 activity cuando pulsemos en la alerta
                // OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
                // crearAlarma(fechaString, hora1, minutos1, fecha);
                // OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
                //abreCtivityEscuhar();
                //TENEMOS QUE CREAR ESTE METODO PORQUE NUESTRA VERSION ES SUPERIOR A LA O Y SI NO NO HACE NADA
                //Y LA PONEMOS ANTES DEL METODO DE ABAJO PORQUE SI NO ES SUPERIOR NO ENTRARA EN EL IF DEL METODO Y SI NO ENTRA Y LUEGO EJECUTA TODO
                 creacionCANALNotificacionSuperiorOreo();
                creaNotificacion();
            }
        });
   //TODO/////////////////////////////////// METODO HECHO  ////////////////////////////////////////////////////////
    //CREACION PendingIntent Y DEVOLVER PendingIntent PARA QUE NOS ABRE NUESTRA ACTIVITY
    // CREAR LAS ALERTAS DEL MOVIL
    private PendingIntent abreCtivityEscuhar() {
          creacionCANALNotificacionSuperiorOreo(); creaNotificacion();
//creamos un intent para ir a nuestra segunda pantalla cuando pulsemos en el icono
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
//TODO/////////////////////////////////// METODO HECHO  ////////////////////////////////////////////////////////
//CREACION DE VARIABLE Y SUS PAREMTROS LISTA
//LE DAMOS FORMA A LA NOTIFICACION COLORES Y DE MAS HISTORIAS, SI LA VERSION ES ANTERIOR A OREO SE EJECUTARA NORMAL SI NO NO FUNCIONARA POR ESO SE HIZO EL METODO CREAR CANALdEnoTIFICACION
   private void creaNotificacion() {
        // CREAMOS LA NOTIFICACION Y LE PASAMOS EL CANAL
        NotificationCompat.Builder contructor = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        // CREAMOS EL ICONO QUE APARECERA
        contructor.setSmallIcon(R.drawable.ic_baseline_hearing_24);
        // CREAMOS EL TITUTLO Y EL CONTENIDO QUE QUEREMOS QUE APAREZCAN
        contructor.setContentTitle("Nota de audio");
        contructor.setContentText("Se borrara despues de oirlo");
        //LE PONEMOS UN COLOR PARA QUE QUEDE BONITO
        contructor.setColor(Color.RED);
        // LE DAMOS UNA PRIORIDAD, AUNQUE NO ES NECESARIO
        contructor.setPriority(NotificationCompat.PRIORITY_MAX);
        // LE PONEMOS UNA LUCECITA DE ALERTA PASANDO EL CORLOR Y LOS SEGUNDOS
        contructor.setLights(Color.RED, 1000, 1000);
        // VIVRACION aunq en el emulador no se ve queda mas pofesiona
        contructor.setVibrate(new long[]{1000, 1000, 1000});
        // ESTO VALE PARA PONER UN SONIDO POR DEFECTO O CREANDO UNA CARPETA CON SONIDOS LE PASAMOS EL NUESTRO
        //TODO VERTSION DE MEJORA QUIZAS SE PUDIERA HACER QUE AL LLEGAR LA NOTIFICACION SE ESCUCHE DIRECTAMENTE CON ESTE PARAMETRO.
        contructor.setDefaults(Notification.DEFAULT_SOUND);

// esto hay que ponerlo para cuando le demos a la notificacion abra nuestra activity 2
// al contructor le ponemos el content intent y nuestra variable pending
//contructor.setContentIntent(pendingIntent);
//llamando directamente el pending intent que devolvemos de arriba
       contructor.setContentIntent(abreCtivityEscuhar());
        // AHOPRA CREaMOS LA VARIABLE
        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationCompat.notify(NOTIFICACION_ID, contructor.build());

//  }
//TODO/////////////////////////////////// METODO HECHO  ////////////////////////////////////////////////////////
//***************************** METODO LISTO  CONTROL DEL SDK POR VERSION SUPEROPR A O *****************************
//hay que crear este metodo por culpa delas versiones
// SI SON VERSIONES ANTERIORES ESTO NO SE EJECUTA SI SON POSTERIORES A O(oreo) SI
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
  public static void setAlarm(int i, Long timestamp, Context ctx) {
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent;
        pendingIntent = PendingIntent.getBroadcast(ctx, i, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmManager.set(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
    }
//primera prueba de codigo al ultilizar el calendario por defecto en la interfaz normal
      final TextView txt = findViewById(R.id.txt);
        String x;
        CalendarView calendario = findViewById(R.id.calendario);
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                txt.setText(" " + year + "- " + month + "- " + dayOfMonth);
            }
        });
     // startService(new Intent(this, MostrarNotificacionService.class));
     //   y para detenerlo:
     //     stoptService(new Intent(this, ClaseParaEjecutarEnSegundoPlano.class));



//todo ********************informacion de audio por si acaso*****************************************
        // <uses-permission android:name="android.permission.RECORD_AUDIO"/>
    /*            fMediaRecorder= new MediaRecorder();
        fMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        fMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        fMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        fMediaRecorder.setAudioChannels(1);
        fMediaRecorder.setAudioSamplingRate(8000);

     //   fMediaRecorder.setOutputFile(fTmpFile.getAbsolutePath());
     //   fMediaRecorder.prepare();
        fMediaRecorder.start();*/
//todo **************************************************************************************

/**/