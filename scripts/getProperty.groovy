import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    

    def status = message.getHeader("CamelHttpResponseCode",String)
    def logid = message.getHeader("SAP_MessageProcessingLogID",String)
    message.setProperty("datetime", new Date().toString())
    message.setProperty("logid", logid)
    message.setProperty("status", status)
    return message
}



