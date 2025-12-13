import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    
    // Ler o body
    def body = message.getBody(String)
    
    // Ler headers
    def headerValue = message.getHeader("HeaderName")
    
    // Ler properties
    def propertyValue = message.getProperty("PropertyName")
    
    // Fazer o processamento desejado aqui
    // ------------------------------------

    // Atualizar o body
    message.setBody(body)

    // Atualizar headers
    message.setHeader("HeaderName", "NewValue")

    // Atualizar properties
    message.setProperty("PropertyName", "NewValue")

    return message
}
