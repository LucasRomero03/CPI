import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonSlurper;
import groovy.json.JsonOutput;
import java.net.HttpURLConnection;
import java.net.URL;

def Message processData(Message message){

    def body = message. getBody(String);
    def bodyParsed = new JsonSlurper().parseText(body);
    def cep = bodyParsed.cep.replaceAll(/\D+/,""); // entre /regex/ para nao botar \\

    if(cep.length() != 8 ){
        throw new RuntimeException("Formato invalido do cep");
    }

    /* esses metodos aqui aceitam o uso de regex caso queira.
        procure regex cheat sheet se quiser umas colas kkk.
        quiser testar um regex pode usar o regex101
        replaceAll(regex, substituição)
        replaceFirst(regex, substituição)

     */

    //Criando a url 
    def url = new URL("https://viacep.com.br/ws/${cep}/json/");

    //Realizar a Chama HTTP 
    HttpURLConnection conn = (HttpURLConnection) url.openConnection()
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Accept","application/json")

    def responseCode = conn.responseCode;

    if(responseCode == 200){
        def resposta = conn.inputStream.text;
        def respostaJson = new JsonSlurper().parseText(resposta);

        //Serializar o json de volta 
        message.setBody(JsonOutput.toJson(respostaJson))
    }else{
        message.setBody("deu errado")
    }
    

    message.setHeader("Content-Type", "application/json")
    //message.setBody(cep);
    return message;
}