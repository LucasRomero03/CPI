import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.XmlUtil;
import groovy.xml.MarkupBuilder
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

    def xmlSlurper = new XmlSlurper().parseText(body);

    def orderIdSlurper      = xmlSlurper.Header.OrderId.text()
    def customerNameSlurper = xmlSlurper.Header.Customer.Name.text()
    def customerEmailSlurper= xmlSlurper.Header.Customer.Email.text()
    def orderDateSlurper    = xmlSlurper.Header.OrderDate.text()

    // Lista de itens com Slurper
    def firstItemCodeSlurper  = xmlSlurper.Items.Item[0].Code.text()
    def firstItemQtySlurper   = xmlSlurper.Items.Item[0].Qty.text()
    def firstItemPriceSlurper = xmlSlurper.Items.Item[0].Price.text()
  
    def writer = new StringWriter()
    def mb = new MarkupBuilder(writer)

    mb.Order {
        OrderHeader {
            ID(orderIdSlurper)
            CustomerName(customerNameSlurper)
            CustomerEmail(customerEmailSlurper)
            Date(orderDateSlurper)
        }
        OrderItems {
            xmlSlurper.Items.Item.each { item ->
                def code  = item.Code.text()
                def qty   = item.Qty.text().toBigDecimal()
                def price = item.Price.text().replace(",", ".").toBigDecimal()
                def total = qty * price

                Item {
                    Material(code)
                    Quantity(qty.toString())
                    UnitPrice(price.toString())
                    Total(total.toString())
                }
            }
        }
    }

    def novoXmlMarkupBuilder = writer.toString()
    
    //def xmlFinal = XmlUtil.serialize(new XmlParser().parseText(sb.toString()))
    message.setBody(novoXmlMarkupBuilder)
    //message.setBody(sb.toString())

    return message
}
