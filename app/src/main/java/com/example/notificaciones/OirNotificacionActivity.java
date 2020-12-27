package com.example.notificaciones;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.notificaciones.MainActivity.*;

import java.io.File;
import java.io.IOException;

import static com.example.notificaciones.MainActivity.NOTIFICACION_ID;
import static com.example.notificaciones.MainActivity.rutaSalida;

public class OirNotificacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oir_notificacion);

        //para que se quite la notificacion UNA VEZ QUE CLICKEMOS HAY QUE USAR ESTO
        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationCompat.cancel(NOTIFICACION_ID);//para que se cancele

        //TODO/////////////////////////////////// METODO HECHO ////////////////////////////////////////////////////////
        Button bReproducir = findViewById(R.id.bReproducir);
        bReproducir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    if(rutaSalida==null){
                        Toast.makeText(getApplicationContext(), "No hay ninguna grabacion", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                    mediaPlayer.setDataSource(rutaSalida);
                    mediaPlayer.prepare();//preparamos o no funcionada el metodo start
                         }
                } catch (IOException e){//capturamos la excepcion sin nada
                }
                mediaPlayer.start();//reproducimos
                Toast.makeText(getApplicationContext(), "Este audio se autodestruira al oirlo ", Toast.LENGTH_SHORT).show();
                File f = new File(rutaSalida);//borramos archivo
                f.delete();
            }
        });



    }
}

  /*  //todo prueba para reproducir audio PARA COMPROBAR QUE FUNCIONABA BIEN
    Button bAudio = findViewById(R.id.bAudio);
        bAudio.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View view){
        MediaPlayer m = MediaPlayer.create(getApplicationContext(), R.raw.little_boy);
        m.start();
    }
    });

}*/
