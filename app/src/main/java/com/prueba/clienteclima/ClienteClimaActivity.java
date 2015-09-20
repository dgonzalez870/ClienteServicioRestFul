package com.prueba.clienteclima;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ClienteClimaActivity extends Activity {

    //Ruta al API OpenWeatherMap Inc. que provee información del clima.
    public static final String urlServicio="http://api.openweathermap.org/data/2.5/weather?q=";
    private TextView tvCiudad;
    private TextView tvTemperatura;
    private TextView tvHumedad;
    private TextView tvPresion;

    private ProgressDialog progreso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cliente_clima);
        tvCiudad= (TextView) findViewById(R.id.tvCiudad);
        tvTemperatura= (TextView) findViewById(R.id.tvTemperatura);
        tvHumedad= (TextView) findViewById(R.id.tvHumedad);
        tvPresion= (TextView) findViewById(R.id.tvPresion);
        progreso=new ProgressDialog(this);
        progreso.setTitle("Solicitud de Clima");
        progreso.setMessage("Solicitando clima");
    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectivityManager conectivityManager= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo informacion=conectivityManager.getActiveNetworkInfo();
        NetworkInfo.State estado=informacion.getState();
        if(NetworkInfo.State.DISCONNECTED.equals(estado)){
            Toast.makeText(this,"No se ha encontrado conexión de red",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cliente_clima, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ingresarCiudad();
        return super.onOptionsItemSelected(item);
    }

    //Despliega un diálogo solicitando el nombre de la ciudad para la cual se desea consultar el
    // clima
    public void ingresarCiudad(){
        final Context contexto=this;
        View formulario = View.inflate(this, R.layout.layout_registro_ciudad, null);
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle("Ingresar Ciudad");
        alerta.setView(formulario);
        //obtiene una referencia al EditText
        final EditText edit_nombre = (EditText) formulario
                .findViewById(R.id.edit_nombre);
        alerta.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        tvCiudad.setText(edit_nombre.getText().toString());
                    }
                });
        alerta.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialogo = alerta.create();
        dialogo.show();
    }

    //Conecta al servicio openWeatherMap y solicita el clima para el nombre de ciudad que se pasa
    // como parametro
    public DatosClima solicitarClima(String ciudad){
        DatosClima datosClima=null;
        try {
            URL url=new URL(urlServicio+ciudad+"&units=metric");
            URLConnection conexion=url.openConnection();
            InputStream inputStream=conexion.getInputStream();
            //buffer en el que se guardan los datos leidos del inputStream
            byte[] buffer=new byte[1024];
            //lee los datos del inputStream
            int cantidadLeidos=inputStream.read(buffer);
            //cierra el inputStream
            inputStream.close();
            //Construye una cadena de texto JSON a partir de los bytes leidos
            String respuestaServicio=new String(buffer,0,cantidadLeidos);
            //Obtiene un objeto DatosClima utilizando la librería GSON de Google
            datosClima=(new Gson()).fromJson(respuestaServicio, DatosClima.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datosClima;
    }

    //Realiza las tareas de conexión y solicitud de clima en segundo plano
    public class TareaSolicitarClima extends AsyncTask<String, Void, DatosClima>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progreso.show();
        }

        @Override
        protected DatosClima doInBackground(String... strings) {
            return solicitarClima(strings[0]);
        }

        @Override
        protected void onPostExecute(DatosClima datosClima) {
            super.onPostExecute(datosClima);
            progreso.dismiss();
            //si la consulta no es exitosa sale del método
            if(datosClima.getCod()!=200){
                Toast.makeText(getApplicationContext(),"La consulta no ha sido exitosa",Toast.LENGTH_LONG).show();
                return;
            }
            tvCiudad.setText(datosClima.getName());
            tvTemperatura.setText(datosClima.getMain().getTemp() + " ºC");
            tvPresion.setText(datosClima.getMain().getPressure() + " mmHg");
            tvHumedad.setText(datosClima.getMain().getHumidity()+" %");
        }
    }

    //Acción del botón
    public void accionBotonSolicitar(View v){
        TareaSolicitarClima tarea=new TareaSolicitarClima();
        tarea.execute(tvCiudad.getText().toString());
    }
}
