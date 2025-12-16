import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import java.net.URLEncoder
import java.net.HttpURLConnection
import java.net.URL


Message processData(Message message) {
    def body = message.getBody(String)
    def jsonSlurper = new JsonSlurper() 
    def bodyParsed  = jsonSlurper.parseText(body)
     

    // Subistitua "SUA_CHAVE" pela sua chave de APi do OpenWeather 
    def apiKey = "715e722a05416ed98e52a6a8381e73cb"
    def url;

    // Construir a url usando o nome codificado 

      if(bodyParsed.cidade){
        // Codificar o nome da cidade, trazer aquele padrão de url Rio+de+janeiro 
        def cidadeCodificada = URLEncoder.encode(bodyParsed.cidade,"UTF-8")
        //Construir a url
        url = new URL("https://api.openweathermap.org/data/2.5/weather?q=${cidadeCodificada}&appid=${apiKey}&units=metric&lang=pt_br")

    }else{
        def lat = bodyParsed.lat;
        def lon = bodyParsed.lon;
        //Construir a url
        url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${apiKey}")

    }
   
    // Realizar a chamada HTTP GET
    HttpURLConnection connection = (HttpURLConnection) url.openConnection()
    connection.setRequestMethod("GET")
    connection.setRequestProperty("Accept", "application/json")

    // Verificar o codigo de resposta 
    def responseCode = connection.responseCode
    def resposta = (responseCode == 200) ? connection.inputStream.text :  connection.errorStream?.text ?: {} ;
    
    def output = jsonSlurper.parseText(resposta)
    // Retornar a resposta formatada com JSON 
    message.setBody(JsonOutput.toJson(output))
   
    message.setHeader("Content-Type","application/json")
    return message

}