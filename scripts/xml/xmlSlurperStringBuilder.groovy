import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.XmlUtil;
/* <?xml version="1.0" encoding="UTF-8"?>
<OrderMessage>
    <Header>
        <OrderId>7829</OrderId>
        <Customer>
            <Name>Lucas Romero</Name>
            <Email>lucas@example.com</Email>
        </Customer>
        <OrderDate>2025-12-05</OrderDate>
    </Header>
    <Items>
        <Item>
            <Code>ABC</Code>
            <Qty>2</Qty>
            <Price>100,5</Price>
        </Item>
        <Item>
            <Code>XYZ</Code>
            <Qty>1</Qty>
            <Price>22.0</Price>
        </Item>
    </Items>
</OrderMessage> */

def Message processData(Message message){

    def body = message.getBody(String)
    def sb = new StringBuilder();

    def xml = new XmlSlurper().parseText(body);

    def orderId = xml.Header.OrderId.text();
    def orderId1 = xml.Header.OrderId;
    def name =  xml.Header.Customer.Name;
    //def items = xml.Items.Item[0].Code.text();
    //def items = xml.Items.Item.text();
    //def items = xml.Items.Item[0].text();

   /* xml.Items.Item.each { item -> 
        println item.Code
        println item.Qty
        println item.Price
        println item


    }
*/  
    //sb.append("<Order>${xml.Header.OrderId}</Order>");
    //sb.append("<CustomerName>${name}</CustomerName>");
  
  //USANDO STIRN BUUILDER 
    sb.append("<Order>")
        sb.append("<Items>")
        def items = xml.Items.Item.each{ it -> 
          sb.append("<Item>")
            sb.append("<Material>${it.Code}</Material>")
            sb.append("<Total>${it.Qty.text().replace(",",".").toBigDecimal() * it.Price.text().replace(",",".").toBigDecimal() }</Total>")
          sb.append("</Item>")
          
        }
        //sb.append(items)
        sb.append("</Items>")
    sb.append("</Order>")

    //println sb;
    //println orderId;
    //println orderId1;
    //println items;
    
    def xmlFinal = XmlUtil.serialize(new XmlParser().parseText(sb.toString()))
    message.setBody(xmlFinal)
    //message.setBody(sb.toString())

    return message
}
