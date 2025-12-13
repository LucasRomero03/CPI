import com.sap.gateway.ip.core.customdev.util.Message;
import javax.xml.parsers.*;
import org.xml.sax.InputSource;

def Message processData(Message message){
    def body = message.getBody(String);
    def factory = DocumentBuilderFactory.newInstance();
    def builder = factory.newDocumentBuilder();
    def doc = builder.parse(new InputSource(new StringReader(body)));

    def nodes = doc.getElementsByTagName("faturamento");
    def total = 0.0;

    for (int i = 0; i < nodes.getLength(); i++){
        def node = nodes.item(i);
        def valor = node.getElementsByTagName("valor").item(0).getTextContent().toBigdecimal();
        total += valor;
    }

    message.setBody("Total de faturamento="+total);
    return message;
}