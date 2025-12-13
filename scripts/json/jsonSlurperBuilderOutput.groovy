import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonSlurper;
import groovy.json.JsonOutput;
import groovy.json.JsonBuilder;



def Message processData(Message message){
    
    def body = message.getBody(String);
    def parser = new JsonSlurper(); // serve para deserializar
    def bodyParsed = parser.parseText(body); // aqui transforma para um obejto com a esturtura do json 
    def jsonSaida = new JsonBuilder(); // serve para criar um objeto json
    def output = new JsonOutput(); // para serializar de voltar e dar um prettyprinter

    jsonSaida (
        pedidoId : bodyParsed.pedidoId,
        cliente  : bodyParsed.cliente,
        enderecoEntrega : bodyParsed.enderecoEntrega,
        itens : bodyParsed.itens.collect{it->           // collet para poder capturar e manipular criando um novo array diferentemente do each que só percorre
            [
                itemId :        it.itemId,
                descricao :     it.descricao,
                quantidade :    it.qtd,
                preco :         it.preco,
                valorTotal :    it.qtd * it.preco,
                acessorios  : it?.acessorios ?: "sem acessorios",
                porte : it.qtd > 1 ? "grande" : "Pequeno"
            ]
        },
        status : bodyParsed.status
    )


    def saida = output.toJson(jsonSaida);
    def jsonPretty = JsonOutput.prettyPrint(saida);

    message.setBody(jsonPretty);
    message.setHeader("Content-Type", "application/json")

    return message;

}