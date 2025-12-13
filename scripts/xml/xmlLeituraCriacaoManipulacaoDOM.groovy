import com.sap.gateway.ip.core.customdev.util.Message; // Importa a classe Message do SAP Gateway IP Core Custom Development Utility
import javax.xml.parsers.DocumentBuilderFactory; // mporta a fábrica responsável por criar objetos capazes de montar e interpretar documentos XML (DOM parser).
import org.xml.sax.InputSource; // Permite transformar um texto (String) em algo que o parser XML consiga ler.
import groovy.xml.XmlUtil; // Utilitário do Groovy para serializar (transformar objeto DOM em texto XML).

/* <OrderMessage>
    <Header>
        <OrderId>789</OrderId>
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
            <Price>10,5</Price>
        </Item>
        <Item>
            <Code>XYZ</Code>
            <Qty>1</Qty>
            <Price>22.0</Price>
        </Item>
    </Items>
</OrderMessage>



<?xml version="1.0" encoding="UTF-8"?>
<Order>
  <OrderHeader>
    <ID>789</ID>
    <CustomerName>Lucas Romero</CustomerName>
    <CustomerEmail>lucas@example.com</CustomerEmail>
    <Date>2025-12-05</Date>
  </OrderHeader>
  <OrderItems>
    <Item>
      <Material>ABC</Material>
      <Quantity>2</Quantity>
      <UnitPrice>10.5</UnitPrice>
      <Total>21.0</Total>
    </Item>
    <Item>
      <Material>XYZ</Material>
      <Quantity>1</Quantity>
      <UnitPrice>22.0</UnitPrice>
      <Total>22.0</Total>
    </Item>
  </OrderItems>
</Order>


 */

def Message processData(Message message){

    def body = message.getBody(String)

    // =========================================================
    // DOCUMENTOS / DOM
    // =========================================================

    def factory = DocumentBuilderFactory.newInstance()
    def builder = factory.newDocumentBuilder()

    def docIn  = builder.parse(new InputSource(new StringReader(body))) // XML de entrada
    def docOut = builder.newDocument()                                // XML de saída


    // =========================================================
    // HEADER DE ENTRADA
    // =========================================================

    def header = docIn.getElementsByTagName("Header").item(0)

    def orderId       = header.getElementsByTagName("OrderId").item(0).getTextContent()
    def customerName  = header.getElementsByTagName("Customer").item(0).getElementsByTagName("Name").item(0).getTextContent()
    def customerEmail = header.getElementsByTagName("Customer").item(0).getElementsByTagName("Email").item(0).getTextContent()
    def orderDate     = header.getElementsByTagName("OrderDate").item(0).getTextContent()


    // =========================================================
    // CRIAÇÃO DO XML DE SAÍDA (DOM MANUAL)
    // =========================================================

    // <Order>
    def order = docOut.createElement("Order")
    docOut.appendChild(order)

    // <OrderHeader>
    def orderHeader = docOut.createElement("OrderHeader")
    order.appendChild(orderHeader)

    // <ID>
    def idElem = docOut.createElement("ID")
    idElem.setTextContent(orderId)
    orderHeader.appendChild(idElem)

    // <CustomerName>
    def cnElem = docOut.createElement("CustomerName")
    cnElem.setTextContent(customerName)
    orderHeader.appendChild(cnElem)

    // <CustomerEmail>
    def ceElem = docOut.createElement("CustomerEmail")
    ceElem.setTextContent(customerEmail)
    orderHeader.appendChild(ceElem)

    // <Date>
    def dtElem = docOut.createElement("Date")
    dtElem.setTextContent(orderDate)
    orderHeader.appendChild(dtElem)


    // =========================================================
    // ITENS
    // =========================================================

    def items    = docIn.getElementsByTagName("Items").item(0)
    def itemList = items.getElementsByTagName("Item")

    // <OrderItems>
    def orderItems = docOut.createElement("OrderItems")
    order.appendChild(orderItems)

    for (int i = 0; i < itemList.getLength(); i++) {

        def itemNode = itemList.item(i)

        def code  = itemNode.getElementsByTagName("Code").item(0).getTextContent()
        def qty   = itemNode.getElementsByTagName("Qty").item(0).getTextContent().toBigDecimal()
        def price = itemNode.getElementsByTagName("Price").item(0).getTextContent().replace(",",".").toBigDecimal()  //replace para caso tenha virgula ja tratamos

        def total = qty * price

        // <Item>
        def itemOut = docOut.createElement("Item")
        orderItems.appendChild(itemOut)

        def mat = docOut.createElement("Material")
        mat.setTextContent(code)
        itemOut.appendChild(mat)

        def qt = docOut.createElement("Quantity")
        qt.setTextContent(qty.toString())              // precismos passar o to string pois esse método espera um tipo STRING e antrs o qty esta para Bigdecimal
        itemOut.appendChild(qt)

        def up = docOut.createElement("UnitPrice")
        up.setTextContent(price.toString())
        itemOut.appendChild(up)

        def tot = docOut.createElement("Total")
        tot.setTextContent(total.toString())
        itemOut.appendChild(tot)
    }


    // =========================================================
    // SERIALIZAÇÃO SIMPLES (XmlUtil exige o Element raiz)
    // =========================================================

    def xmlFinal = XmlUtil.serialize(docOut.getDocumentElement())
    message.setBody(xmlFinal)

    return message
}
