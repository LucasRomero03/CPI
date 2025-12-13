import com.sap.gateway.ip.core.customdev.util.Message;
import groovy.json.JsonSlurper;

/* {
  "pedidoId": "P-2025-001",
  "cliente": {
    "id": 789,
    "nome": "Lucas Romero",
    "contato": {
      "email": "lucas@example.com",
      "telefone": "+55 81 99999-0000"
    }
  },
  "enderecoEntrega": {
    "rua": "Rua das Acácias",
    "numero": 120,
    "cidade": "Recife",
    "estado": "PE",
    "cep": "50000-000"
  },
  "pagamento": {
    "metodo": "CARTAO",
    "parcelas": 3,
    "cartao": {
      "bandeira": "VISA",
      "ultimosDigitos": "1234"
    }
  },
  "itens": [
    {
      "itemId": 1,
      "descricao": "Notebook Gamer",
      "qtd": 1,
      "preco": 4500.00,
      "acessorios": [
        { "nome": "Capa protetora", "preco": 80.00 },
        { "nome": "Mouse Gamer", "preco": 120.00 }
      ]
    },
    {
      "itemId": 2,
      "descricao": "Monitor 144Hz",
      "qtd": 2,
      "preco": 1200.00,
      "acessorios": []
    },
    {
      "itemId": 3,
      "descricao": "Teclado Mecânico RGB",
      "qtd": 1,
      "preco": 350.00,
      "acessorios": [
        {
          "nome": "Keycaps extras",
          "cores": ["vermelho", "azul", "preto"]
        }
      ]
    }
  ],
  "status": "EM_PROCESSAMENTO",
  "observacoes": [
    "Cliente pediu embalagem de presente",
    "Entregar no período da manhã"
  ]
}
 */

def Message processData(Message message){

    def body = message.getBody(String);
    def jsonParsed = new JsonSlurper().parseText(body);
    def texto = ""
    def faturamentoPedido = 0.0;

    jsonParsed.itens.each{ item -> 

        faturamentoPedido += item.qtd * item.preco;
        if(item.acessorios && item.acessorios.size() > 0){
            item.acessorios.each{ acessorio ->
                faturamentoPedido += acessorio.preco ?: 0.0;
            }
        }
    }
     if(faturamentoPedido > 5000){
        texto = "Faturamento válido: R\$ ${String.format('%.3f', faturamentoPedido)}"
     }else{
        texto = "Faturamento abaixo do esperado: R\$ ${String.format('%.2f', faturamentoPedido)}"
        //throw new RuntimeException("faturamento abaixo do minimo esperado ") // isso aqui para a execução do fluxo e para dando um 500 la
     }


    

    //message.setBody(faturamentoPedido.toString());
    //message.setBody("faturamento  ${String.format(faturamentoPedido)}");
    //def texto = "Faturamento válido: R\$ ${String.format('%.2f', faturamentoPedido)}" G string por isso precisa meio que fazer essa gambiardda de tranfosmar em to string 
    message.setBody(texto.toString())

    //message.setBody("faturamento " +  faturamentoPedido.toString());
    return message;




}