import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {

    // Ler body atual
    def body = message.getBody(String)
    // Adicionar texto
    body = body + "\nProcessado pelo Groovy Script"
    
    // Ler header existente
    def method = message.getHeader("CamelHttpMethod", String)
    body = body + "\n" + "méotdo usado:"+ method 
    
    // Criar novo header
    message.setHeader("ScriptInfo", "Executado com sucesso")

    // Criar property
    message.setProperty("ExecutionTime", new Date().toString())
    message.setBody(body)

    return message
}

