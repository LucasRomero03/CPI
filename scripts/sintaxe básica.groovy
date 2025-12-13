import com.sap.gateway.ip.core.customdev.util.Message              
import java.util.HashMap                                           

// Importa a classe Message (API do CPI) para ler/alterar body, headers e properties.
// Importa HashMap do Java (útil para estruturas chave-valor; aqui não é usado diretamente).


def Message processData(Message message) {                          // Função padrão que o CPI executa; recebe e devolve um objeto Message.

    // 1. Ler body
    def body = message.getBody(String)                              // Lê o payload (conteúdo principal) da mensagem como String.

    // 2. Ler header
    def id = message.getHeader("OrderID",String)                    // Lê o header "OrderID" e tenta convertê-lo para String.

    // 3. Ler property
    def origem = message.getProperty("origemSistema")               // Lê a property "origemSistema" (dado de contexto do iFlow).

    // 4. Montar novo conteúdo
    def novoBody = "Pedido "+ id +" vindo de"+origem+".\nConteúdo original:"+ body
                                                                     // Concatena textos e variáveis para formar um novo body; "\n" quebra a linha.

    // 5. Atualizar body, header e property
    message.setBody(novoBody)                                       // Substitui o payload da mensagem pelo conteúdo montado em novoBody.
    message.setHeader("StatusProcessamento", "OK")                   // Cria/atualiza o header "StatusProcessamento" com o valor "OK".
    message.setProperty("ultimaExecucao", new Date().toString())    // Grava a property "ultimaExecucao" com a data/hora atuais (em String).
    
    return message                                                  // Retorna a mensagem modificada para as próximas etapas do iFlow.
}
