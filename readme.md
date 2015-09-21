#Como consumir un servicio RestFul en Android.

En este ejemplo se presenta como consumir un servicio RestFul sencillo utilizando el API [**OpneWeatherMap**](http://openweathermap.org/current). 

###Requerimientos de Funcionamiento
1. Debe conectarse al API [**OpneWeatherMap**](http://openweathermap.org/current) y traer la información de clima para la ciudad cuyo nombre se ingresa a través de un formulario.
2. La información del clima es presentada en pantalla.
3. Debe verificar el estado de la conexión de red.

###Resumen de los pasos necesarios para consumir un servicio RestFul en Android
1. Verificar que exista conexión de red a través del **ConectivityManager**.
2. Crear un objeto **URL** (java.net.URL) que apunte al end-point del servicio RestFul.
3. Crear un objeto **URLConnection** (java.net.URLConnection) invocando el método `openConnection()` del objeto creado en el punto anterior.
4. Crear un objeto **InputStream** (java.io.InputStream) invocando el método `getInputStream()` del objeto **URLConnection** creado en el paso anterior.
5. Leer un array de bytes (buffer) a través del **InputStrean**.
6. Construir un **String** con el array de bytes leido en el paso anterior `String respuestaServicio=new String(buffer,0,cantidadLeidos);`, donde **cantidadLeidos** es la cantidad de bytes leidos en el **InputStream**.
7. Traducir los campos del **String** obtenido en el paso anterior a un objeto **Java**, esto se puede hacer por ejemplo utilizando la biblioteca [GSON](https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html).  

###Desarrollo
Crear un proyecto __ClienteClima__ en __Android Studio__ con nombre de paquete __com.prueba.clienteclima__ y declarar los siguientes permisos en el archivo **AndroidManifest.xml**
```xml
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
``` 
 En primer lugar debe conocerse la firma del servicio, es decir, los parámetros requeridos para realizar la solicitud y la estructura de la respuesta, [en el ejemplo de **OpenWeather**](http://api.openweathermap.org/data/2.5/weather?q=London,uk) se obtiene la siguiente estructura de una cadena en formato **JSON**
```json
{"coord":{"lon":-0.13,"lat":51.51},
 "weather":[{"id":701,"main":"Mist","description":"mist","icon":"50n"}],
 "base":"cmc stations",
 "main":{"temp":285.25,"pressure":1021,"humidity":87,"temp_min":284.15,"temp_max":287.15},
 "wind":{"speed":2.6,"deg":230},
 "clouds":{"all":76},
 "dt":1442790606,
 "sys":{"type":1,"id":5091,"message":0.007,"country":"GB","sunrise":1442727857,"sunset":1442772130},
 "id":2643743,
 "name":"London",
 "cod":200}
 ```
 Este ejemplo se enfoca en los objetos identificados con las claves __coord__ (coordenadas), __main__ (información del clima) y __name__ (nombre de la ciudad). La cadena de texto __JSON__ que representa la respuesta del servicio debe ser deserializada y convertida a un objeto __java__ para utilizar sus atributos como datos en la aplicación, para este fin se utiliza la biblioteca [GSON](https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html).
 
 Se crea una clase llamada **DatosClima** cuyos atributos son los campos que nos interesan de la cadena __JSON__, los objetos se declaran como clases anidadas (main y coord) y el nombre de los atributos debe coincidir con el de las claves de la cadena __JSON__.

```java
package com.prueba.clienteclima;

public class DatosClima {

	private String name;
	private Main main;
	private Coordenadas coord;
	private int cod;

	public Main getMain() {return main;}
	public String getName() {return name;}
	public Coordenadas getCoord() {return coord;}
	public int getCod(){return cod;}
	public class Main{
		private double temp;
		private double temp_min;
		private double temp_max;
		private double pressure;
		private double humidity;
		public double getTemp() {return temp;}
		public double getTemp_min() {return temp_min;}
		public double getTemp_max() {return temp_max;}
		public double getPressure() {return pressure;}
		public double getHumidity() {return humidity;}
	}
	
	public class Coordenadas{
		private double lat;
		private double lon;
		public double getLat(){return lat;}
		public double getLon(){return lon;}
		
	}
}
```

####Configuración de GSON

Para trabajar con [GSON](https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html). debe incluirse las [**dependencias GSON para gradle**](http://mvnrepository.com/artifact/com.google.code.gson/gson/2.3.1) en el archivo **/app/build.gradle**.

![Modificacion de dependencias](/capturas/gradle_dependencias.png). 

####Layout
Crear un __layout__ de nombre __layout_cliente_clima.xml__
```xml
<?xml version="1.0" encoding="utf-8"?>
<TableLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:stretchColumns="3">
    <TableRow>
        <TextView
            android:textSize="25sp"
            android:textColor="#0000FF"
            android:text="Ciudad"
            android:padding="3dip"
            android:layout_column="1"/>
        <TextView
            android:textSize="25sp"
            android:textColor="#FF0000"
            android:id="@+id/tvCiudad"
            android:text="Caracas"
            android:gravity="center"
            android:padding="3dip"
            android:layout_column="3"/>
    </TableRow>
    <TableRow>
        <TextView
            android:textSize="25sp"
            android:textColor="#0000FF"
            android:text="Temperatura"
            android:padding="3dip"
            android:layout_column="1"/>
        <TextView
            android:id="@+id/tvTemperatura"
            android:textSize="25sp"
            android:textColor="#FF0000"
            android:text="00ºC"
            android:gravity="center"
            android:padding="3dip"
            android:layout_column="3"/>
    </TableRow>
    <TableRow>
        <TextView
            android:textSize="25sp"
            android:textColor="#0000FF"
            android:layout_column="1"
            android:text="Presión"
            android:padding="3dip" />
        <TextView
            android:textSize="25sp"
            android:textColor="#FF0000"
            android:id="@+id/tvPresion"
            android:text="00mmHg"
            android:gravity="center"
            android:padding="3dip"
            android:layout_column="3"/>
    </TableRow>
    <TableRow>
        <TextView
            android:textSize="25sp"
            android:textColor="#0000FF"
            android:layout_column="1"
            android:text="Humedad"
            android:padding="3dip" />
        <TextView
            android:textSize="25sp"
            android:textColor="#FF0000"
            android:id="@+id/tvHumedad"
            android:text="00"
            android:gravity="center"
            android:padding="3dip"
            android:layout_column="3"/>
    </TableRow>
    <TableRow
        android:layout_width="match_parent"
        android:layout_height="match_parent">
                <Button
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:gravity="center"
                    android:layout_span="3"
                    android:layout_column="1"
                    android:text="Solicitar Clima"
                    android:id="@+id/button"
                    android:onClick="accionBotonSolicitar"/>
    </TableRow>
</TableLayout>
```

![inicio](/capturas/inicial.png)

Crear un layout para el formulario en el que se solicita el nombre de la ciudad __layout_registro_ciudad.xml__
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical" >

    <TextView
        android:id="@+id/text_nombre"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Nombre de la Ciudad" />

    <EditText
        android:id="@+id/edit_nombre"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:ems="10"
        android:inputType="text" >

        <requestFocus />
    </EditText>

</LinearLayout>
```

###Menu de Opciones
Crear un nuevo recurso del tipo **menu** con el nombre __menu_cliente_clima.xml__

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:id="@+id/accion_select_ciudad" android:title="@string/accion_ciudad"/>
</menu>
```
Crear el recurso **string** para la acción del __menu__ en el archivo **/app/src/res/values/strings.xml**
```xml
<resources>
    <string name="app_name">Cliente Clima</string>
    <string name="accion_ciudad">Seleccionar Ciudad</string>
</resources>
```

### Activity

Crear un **Activity** de nombre __ClienteClimaActivity__. Crear un String con la dirección del servicio
```java
    //Ruta al API OpenWeatherMap Inc. que provee información del clima.
    public static final String urlServicio="http://api.openweathermap.org/data/2.5/weather?q=";
```

Asignar la vista al activity en el método **onCreate** `setContentView(R.layout.layout_cliente_clima);`, obtener instancias de los **TextView** con texto en color rojo de __layout_cliente_clima.xml__ utilizando el método `findViewById`.

```java
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
```

En el método **onStart** de __ClienteClimaActivity__ verificar la conexión de red através de un **ConnectivityManager**

```java
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
```
Si está deconectado de la red entonces finaliza la aplicación.

Crear en __ClienteClimaActivity__ un método `ingresarCiudad()` que despliegue un diálogo solicitando el nombre de una ciudad y al hacer click sobre el botón __Aceptar__ cambie el nombre de la ciudad en la vista de __ClienteClimaActivity__.

```java
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
```

Implementar los métodos **onCreateOptionsMenu** y **onOptionsItemSelected(MenuItem item)** de __ClienteClimaActivity__ e invocar desde este último el método `ingresarCiudad()`

```java
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
```
 
Crear el método **solicitarClima** en __ClienteClimaActivity__ que reciba como parámetro el nombre de la ciudad, se conecte al servicio RestFul, y retorne un objeto del **DatosClima**.

```java
    //Conecta al servicio openWeatherMap y solicita el clima para el nombre de ciudad que se pasa
    // como parámetro
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
```  

Crear una subclase de [**AsyncTask**](https://github.com/dgonzalez870/AsyncTaskDemo) anidada en __ClienteClimaActivity__ que ejecute en segundo plano el método `solicitarClima(String ciudad)`

```java
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
```

Crear el método de acción del botón de __layout_cliente_clima.xml__ que inicie la ejecución de **TareaSolicitarClima**

```java
    //Acción del botón
    public void accionBotonSolicitar(View v){
        TareaSolicitarClima tarea=new TareaSolicitarClima();
        tarea.execute(tvCiudad.getText().toString());
    }
```

![inicio](/capturas/inicial.png) ![Registro Ciudad](/capturas/ciudad.png) ![progreso](/capturas/solicitando.png)