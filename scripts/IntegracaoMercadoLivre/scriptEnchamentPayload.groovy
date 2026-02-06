import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonSlurper;
import groovy.json.JsonOutput;


def Message processData(Message message){
    def body = message.getBody(String);
    def categorias = new JsonSlurper().parseText(body);

    categorias.each { categoria -> 
        categoria.moto = (categoria.name.equalsIgnoreCase("motos")) ? "SIM" : "NAO";

    }
    message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(categorias)));
    return message;

}