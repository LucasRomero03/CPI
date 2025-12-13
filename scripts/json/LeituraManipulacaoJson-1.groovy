import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper; // json slurper 

/* json que foi usado
{
    "vendas": [
        {
            "produto": "mero",
            "quantidade": 1
        },
        {
            "produto": "mero1",
            "quantidade": 2
        },
        {
            "produto": "mero3",
            "quantidade": 3
        }
    
    
    
    ]
}
*/


def Message processData(Message message){

    def body = message.getBody(String);
    def parser = new JsonSlurper();
    def json = parser.parseText(body);

    def total = 0;

    //função ja pronta para iterar sobre item do array e atribuit a "item" 
    json.vendas.each { item -> 
        total += item.quantidade;

    }

    message.setBody("Total de pedidos="+total);

    return message;


}


// import com.sap.gateway.ip.core.customdev.util.Message
// import groovy.json.JsonSlurper; // json slurper 




// def Message processData(Message message){

//     def body = message.getBody(String);
//     def parser = new JsonSlurper();
//     def json = parser.parseText(body);

//     def total = 0;

//     json.pedidos.each { pedido -> 
//         pedido.itens.each { item ->
//             total+= item.qtd;
//         }

//     }

//     message.setBody("Total de pedidos="+total);

//     return message;


// }



// {
//     "meta": {
//         "versao": "1.0",
//         "geradoEm": "2025-11-12T16:40:00Z"
//     },
//     "clientes": [
//         {
//             "id": 101,
//             "nome": "Lucas Romero",
//             "tags": [
//                 "vip",
//                 "recorrente"
//             ],
//             "enderecos": [
//                 {
//                     "tipo": "residencial",
//                     "linhas": [
//                         "Rua das Palmeiras, 120",
//                         "Bloco B, Apt 402"
//                     ],
//                     "cidade": "Recife",
//                     "estado": "PE"
//                 },
//                 {
//                     "tipo": "comercial",
//                     "linhas": [
//                         "Av. Boa Viagem, 5000"
//                     ],
//                     "cidade": "Recife",
//                     "estado": "PE"
//                 }
//             ]
//         },
//         {
//             "id": 102,
//             "nome": "Ana Souza",
//             "tags": [],
//             "enderecos": []
//         }
//     ],
//     "catalogo": [
//         {
//             "itemId": "MOU-001",
//             "nome": "Mouse",
//             "preco": 120.0,
//             "categorias": [
//                 "Periféricos",
//                 "USB"
//             ]
//         },
//         {
//             "itemId": "TECL-002",
//             "nome": "Teclado Mecânico",
//             "preco": 250.5,
//             "categorias": [
//                 "Periféricos"
//             ]
//         },
//         {
//             "itemId": "KIT-900",
//             "nome": "Combo Gamer",
//             "preco": 340.0,
//             "categorias": [
//                 "Kit"
//             ],
//             "componentes": [
//                 "MOU-001",
//                 "TECL-002"
//             ]
//         }
//     ],
//     "pedidos": [
//         {
//             "orderId": "A-1001",
//             "clienteId": 101,
//             "canal": "Loja Online",
//             "parcelas": [
//                 120.0,
//                 250.5,
//                 10.0
//             ],
//             "itens": [
//                 {
//                     "itemId": "MOU-001",
//                     "qtd": 1
//                 },
//                 {
//                     "itemId": "TECL-002",
//                     "qtd": 1
//                 }
//             ],
//             "descontos": {
//                 "cupons": [
//                     "BFRIDAY10",
//                     "VIP5"
//                 ],
//                 "matrizDescontos": [
//                     [
//                         5,
//                         0,
//                         0
//                     ],
//                     [
//                         0,
//                         3,
//                         2
//                     ]
//                 ]
//             },
//             "entregas": [
//                 {
//                     "entregaId": "E-01",
//                     "volumes": [
//                         {
//                             "volumeId": "V-01",
//                             "pesoKg": 1.2,
//                             "itens": [
//                                 {
//                                     "itemId": "MOU-001",
//                                     "qtd": 1
//                                 }
//                             ]
//                         },
//                         {
//                             "volumeId": "V-02",
//                             "pesoKg": 1.8,
//                             "itens": [
//                                 {
//                                     "itemId": "TECL-002",
//                                     "qtd": 1
//                                 }
//                             ]
//                         }
//                     ],
//                     "rotas": [
//                         {
//                             "dia": "2025-11-13",
//                             "checkpoints": [
//                                 "Centro de Distribuição",
//                                 "HUB Recife",
//                                 "Em rota",
//                                 "Entregue"
//                             ]
//                         }
//                     ]
//                 }
//             ],
//             "recomendacoes": [
//                 [
//                     "MOU-001",
//                     "TECL-002"
//                 ], 
//                 [
//                     "KIT-900"
//                 ] 
//             ]
//         },
//         {
//             "orderId": "A-1002",
//             "clienteId": 102,
//             "canal": "Marketplace",
//             "parcelas": [],
//             "itens": [
//                 {
//                     "itemId": "KIT-900",
//                     "qtd": 1
//                 }
//             ],
//             "entregas": [
//                 {
//                     "entregaId": "E-02",
//                     "volumes": [
//                         {
//                             "volumeId": "V-03",
//                             "pesoKg": 3.1,
//                             "itens": [
//                                 {
//                                     "itemId": "KIT-900",
//                                     "qtd": 1
//                                 }
//                             ]
//                         }
//                     ],
//                     "rotas": [
//                         {
//                             "dia": "2025-11-14",
//                             "checkpoints": [
//                                 "CD Sul",
//                                 "HUB Recife",
//                                 "Aguardando retirada"
//                             ]
//                         }
//                     ]
//                 }
//             ],
//             "recomendacoes": []
//         }
//     ]
// }


