#Como consumir un servicio RestFul en Android.

En este ejemplo se presenta como consumir un servicio RestFul sencillo utilizando el API [**OpneWeatherMap**](http://openweathermap.org/current). 

###Requerimientos de Funcionamiento
1. Debe conectarse al API [**OpneWeatherMap**](http://openweathermap.org/current) y traer la información de clima para la ciudad cuyo nombre se ingresa a través de un formulario.
2. La información del clima es presentada en pantalla.
3. Debe verificar el estado de la conexión de red.

###Desarrollo

1. En primer lugar debe conocerse la firma del servicio, es decir, los prámetros requeridos para realizar la solicitud y la estructura de la respuesta, [en el ejemplo de **OpenWeather**](http://api.openweathermap.org/data/2.5/weather?q=London,uk) se obtiene la siguiente estructura de una cadena en formato **JSON**
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