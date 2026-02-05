import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import java.net.URLEncoder
import java.net.HttpURLConnection
import java.net.URL


Message processData(Message Message) {
    def body = message.getBody(String)
    def jsonSlurper = new JsonSlurper() 
    def input  = jsonSlurper.parseText(body)
    def cidade = input.cidade 

    // Subistitua "SUA_CHAVE" pela sua chave de APi do OpenWeather 
    def apiKey = "c505249a4f9ff095792fb433c7ad03b0"
    // Codificar o nome da cidade
    def cicadeCodificada = URLEncoder.encode(cidade,"UTF-8")
    // Construir a url usando o nome codificado 
    def url = new URL("https://api.openweathermap.org/data/2.5/weather?q=${cidade}&appid=${apiKey}&units=metric&lang=pt_br")
    def url = new URL("https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}")

   
    // Realizar a chamada HTTP GET
    HttpURLConnection connection = (HttpURLConnection) url.openConnection()
    connection.setRequestMethod("GET")
    connection.setRequestProperty("Accept", "application/json")

    // Verificar o codigo de resposta 
    def responseCode = connection.responseCode
    if (responseCode == 200) {
        // Ler a resposta da API 
        def responseStream = connection.inputStream.text
        def responseJson = new JsonSlurper().parseText(responseStream)

        // Retornar a resposta formatada com JSON 
        message.setBody(JsonOutput.toJson(responseJson))

    } else {
        message.setBody(JsonOutput.toJson([error: "Erro ao consultar a API OpenWeather"]))

    }

    return message

}