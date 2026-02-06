import com.sap.gateway.ip.core.customdev.util.Message;



def Message processData( Message message){
    // acho que poderia ter sido evitado o uso de um script usando um content modifier tbm daria no mesmo. 
    def headers = message.getHeaders();
    message.setHeader("Authorization","Bearer APP_USR-6719405149060515-020517-255759f594c29add367e9f8e985fe22c-3024253338")

    return message;
}