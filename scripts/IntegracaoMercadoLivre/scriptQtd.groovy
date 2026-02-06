import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonSlurper;
import groovy.json.JsonOutput;


def Message processData(Message message){
    def body = message.getBody(String);
    def bodyParsed = new JsonSlurper().parseText(body);

    //da para concatenar métodos.
    def filteredChildrens = bodyParsed.children_categories
    .findAll(){it.total_items_in_this_category < 10000}
    .sort{ it.total_items_in_this_category}

    //setar body de arrays e talz da merda, tem que transformar para um json com a lib do output 
    message.setBody(JsonOutput.prettyPrint(JsonOutput.toJson(filteredChildrens)));
    message.setHeader("Content-Type","application/json");
    return message;


}