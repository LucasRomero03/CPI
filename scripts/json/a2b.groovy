import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonSlurper;
import groovy.json.JsonOutput;
import groovy.json.JsonBuilder;



def Message processData(Message message){

    def body = message.getBody(String);
    def parser = new JsonSlurper();
    def bodyParsed = parser.parseText(body);
    def jsonSaida = new JsonBuilder();
    def output = new JsonOutput();


    jsonSaida (
        atualizadoEm : bodyParsed.entry.updated,
        clientId : bodyParsed.entry.content.properties.EmployeeID.$,
        criadoEm : bodyParsed.entry.content.properties.OrderDate.$,
        status : "Em processamento"

        
    )
    
    //def saida = output.toJson(jsonSaida); usando output ele ciar o campo cmapo content antes de criar o json 
    //def jsonPretty = JsonOutput.prettyPrint(saida);
    def pretty = jsonSaida.toPrettyString()
    message.setBody(pretty)


    // retorno dos codigos abaixo 
    //     {
    //     "atualizadoEm": "2025-11-17T22:07:21Z",
    //     "clientId": "6",
    //     "criadoEm": "1996-07-05T00:00:00",
    //     "status": "Em processamento"
    //     }

    // def jsonSaida = [
    // atualizadoEm : bodyParsed.entry.updated,
    // clienteId    : bodyParsed.entry.content.properties.EmployeeID.$,
    // criadoEm     : bodyParsed.entry.content.properties.OrderDate.$,
    // status       : "Em processamento"
    // ]

    // def saida = output.toJson(jsonSaida);
    // def jsonPretty = JsonOutput.prettyPrint(saida);
    // message.setBody(jsonPretty);
    



    // jsonSaida ([
    //     atualizadoEm : bodyParsed.entry.updated,
    //     clientId : bodyParsed.entry.content.properties.EmployeeID.$,
    //     criadoEm : bodyParsed.entry.content.properties.OrderDate.$,
    //     status : "Em processamento"

        
    // ])

    // def saida = jsonSaida.toString()
    // def pretty = jsonSaida.toPrettyString()
    // message.setBody(pretty)



    

    


    // def builder = new JsonBuilder([
    // atualizadoEm : bodyParsed.entry.updated,
    // clienteId    : bodyParsed.entry.content.properties.EmployeeID.$,
    // criadoEm     : bodyParsed.entry.content.properties.OrderDate.$,
    // status       : "Em processamento"
    // ])

    // def saida = builder.toString()
    // def pretty = builder.toPrettyString()
    // message.setBody(pretty)



    
    message.setHeader("Content-Type", "application/json")
    return message;

}