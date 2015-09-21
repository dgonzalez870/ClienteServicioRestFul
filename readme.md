#Como consumir un servicio RestFul en Android.

En este ejemplo se presenta como consumir un servicio RestFul sencillo utilizando el API [**OpneWeatherMap**](http://openweathermap.org/current). 

###Requerimientos de Funcionamiento
1. Debe conectarse al API [**OpneWeatherMap**](http://openweathermap.org/current) y traer la información de clima para la ciudad cuyo nombre se ingresa a través de un formulario.
2. La información del clima es presentada en pantalla.
3. Debe verificar el estado de la conexión de red.

###Desarrollo

 En primer lugar debe conocerse la firma del servicio, es decir, los prámetros requeridos para realizar la solicitud y la estructura de la respuesta, [en el ejemplo de **OpenWeather**](http://api.openweathermap.org/data/2.5/weather?q=London,uk) se obtiene la siguiente estructura de una cadena en formato **JSON**
```json
{**"coord":{"lon":-0.13,"lat":51.51},
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
 Este ejemplo se enfoca en los objetos identificados con las claves _coord_ (coordenadas), _main_ (información del clima) y _name_ (nombre de la ciudad). La cadena de texto _JSON_ que representa la respuesta del servicio debe ser deserializada y convertida a un objeto _java_ para utilizar sus atributos como datos en la aplicación, para este fin se utiliza la biblioteca [GSON](https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html).
 
 Se crea una clase llamada **DatosClima** cuyos atributos son los campos que nos interesan de la cadena _JSON_, los objetos se declaran como clases anidadas (main y coord) y el nombre de los atributos debe coincidir con el de las claves de la cadena _JSON_.

```java
package com.prueba.clienteclima;

public class DatosClima {

	private String name;
	private Main main;
	private Coordenadas coord;
	private int cod;

	public Main getMain() {return main;}
	public String getName() {return name;}
	public Coordenadas getCoord() {
		return coord;
	}
	public int getCod(){return cod;}
	public class Main{
		private double temp;
		private double temp_min;
		private double temp_max;
		private double pressure;
		private double humidity;
		public double getTemp() {
			return temp;
		}
		public double getTemp_min() {
			return temp_min;
		}
		public double getTemp_max() {
			return temp_max;
		}
		public double getPressure() {
			return pressure;
		}
		public double getHumidity() {
			return humidity;
		}
	}
	
	public class Coordenadas{
		private double lat;
		private double lon;
		public double getLat() {
			return lat;
		}
		public double getLon() {
			return lon;
		}
		
	}
}
``` 
####Configuración de GSON

Para trabajar con [GSON](https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html). debe incluirse las [**dependencias GSON para gradle**](http://mvnrepository.com/artifact/com.google.code.gson/gson/2.3.1) en el archivo **/app/build.gradle**.

![Modificacion de dependencias](/capturas/gradle_dependencias.png). 