import com.sap.gateway.ip.core.customdev.util.Message
import javax.xml.parsers.DocumentBuilderFactory
import org.xml.sax.InputSource
import groovy.xml.XmlSlurper
import groovy.xml.XmlParser
import groovy.xml.MarkupBuilder
import groovy.xml.XmlUtil
import java.io.StringReader


def Message processData(Message message){

    def body = message.getBody(String)

    // =====================================================================
    // XML DE ENTRADA
    // =====================================================================

    // <OrderMessage>
    //     <Header>
    //         <OrderId>789</OrderId>
    //         <Customer>
    //             <Name>Lucas Romero</Name>
    //             <Email>lucas@example.com</Email>
    //         </Customer>
    //         <OrderDate>2025-12-05</OrderDate>
    //     </Header>
    //     <Items>
    //         <Item>
    //             <Code>ABC</Code>
    //             <Qty>2</Qty>
    //             <Price>10,5</Price>
    //         </Item>
    //         <Item>
    //             <Code>XYZ</Code>
    //             <Qty>1</Qty>
    //             <Price>22.0</Price>
    //         </Item>
    //     </Items>
    // </OrderMessage>
    // =====================================================================


    // #####################################################################
    // #####################################################################
    // ###########            BLOCO 1 — DOM (W3C)                  ##########
    // #####################################################################
    // #####################################################################

    /*
    // ===============================================================
    // 1A — LEITURA COM DOM
    // ===============================================================
    def factory = DocumentBuilderFactory.newInstance()
    def builder = factory.newDocumentBuilder()
    def docDom  = builder.parse(new InputSource(new StringReader(body)))

    // ===============================================================
    // 1B — ACESSAR CAMPOS COM DOM
    // ===============================================================
    def headerDom = docDom.getElementsByTagName("Header").item(0)

    def idDom       = headerDom.getElementsByTagName("OrderId").item(0).getTextContent()
    def nameDom     = headerDom.getElementsByTagName("Customer").item(0).getElementsByTagName("Name").item(0).getTextContent()
    def emailDom    = headerDom.getElementsByTagName("Customer").item(0).getElementsByTagName("Email").item(0).getTextContent()
    def dateDom     = headerDom.getElementsByTagName("OrderDate").item(0).getTextContent()

    // ===============================================================
    // 1C — MANIPULAÇÃO COM DOM (adicionar novo nó)
    // ===============================================================
    def status = docDom.createElement("Status")
    status.setTextContent("APROVADO")
    headerDom.appendChild(status)

    // ===============================================================
    // 1D — CRIAÇÃO DE NOVO XML COM DOM
    // ===============================================================
    def docOut = builder.newDocument()

    def orderOut = docOut.createElement("Order")
    docOut.appendChild(orderOut)

    def hOut = docOut.createElement("OrderHeader")
    orderOut.appendChild(hOut)

    def idOut = docOut.createElement("ID")
    idOut.setTextContent(idDom)
    hOut.appendChild(idOut)

    def xmlFinalDom = XmlUtil.serialize(docOut.getDocumentElement())

    message.setBody(xmlFinalDom)
    return message
    */


    // #####################################################################
    // #####################################################################
    // ###########            BLOCO 2 — XmlSlurper                ##########
    // #####################################################################
    // #####################################################################

    /*
    // ===============================================================
    // 2A — LEITURA COM SLURPER
    // ===============================================================
    def xmlS = new XmlSlurper().parseText(body)

    // ===============================================================
    // 2B — ACESSAR CAMPOS COM SLURPER
    // ===============================================================
    def idS       = xmlS.Header.OrderId.text()
    def nameS     = xmlS.Header.Customer.Name.text()
    def emailS    = xmlS.Header.Customer.Email.text()
    def dateS     = xmlS.Header.OrderDate.text()

    // Lista de itens
    def codesS = xmlS.Items.Item.collect { it.Code.text() }     // ["ABC","XYZ"]

    // ===============================================================
    // 2C — "MANIPULAÇÃO" COM SLURPER (somente lógica, não estrutura)
    // ===============================================================
    def totalS = xmlS.Items.Item.collect {
        it.Qty.text().toBigDecimal() * it.Price.text().replace(",",".").toBigDecimal()
    }.sum()

    // ===============================================================
    // 2D — CRIAR NOVO XML USANDO STRINGBUILDER
    // ===============================================================
    def sb = new StringBuilder()

    sb.append("<Order>")
    sb.append("<OrderHeader>")
    sb.append("<ID>${idS}</ID>")
    sb.append("<CustomerName>${nameS}</CustomerName>")
    sb.append("<CustomerEmail>${emailS}</CustomerEmail>")
    sb.append("<Date>${dateS}</Date>")
    sb.append("</OrderHeader>")

    sb.append("<OrderItems>")
    xmlS.Items.Item.each { item ->
        def code  = item.Code.text()
        def qty   = item.Qty.text()
        def price = item.Price.text()
        def total = qty.toBigDecimal() * price.replace(",",".").toBigDecimal()

        sb.append("<Item>")
        sb.append("<Material>${code}</Material>")
        sb.append("<Quantity>${qty}</Quantity>")
        sb.append("<UnitPrice>${price}</UnitPrice>")
        sb.append("<Total>${total}</Total>")
        sb.append("</Item>")
    }
    sb.append("</OrderItems>")
    sb.append("</Order>")

    message.setBody(sb.toString())
    return message
    */


    // #####################################################################
    // #####################################################################
    // ###########            BLOCO 3 — XmlParser                 ##########
    // #####################################################################
    // #####################################################################

    /*
    // ===============================================================
    // 3A — LEITURA COM XmlParser
    // ===============================================================
    def xmlP = new XmlParser().parseText(body)

    // ===============================================================
    // 3B — ACESSAR CAMPOS COM PARSER
    // ===============================================================
    def idP    = xmlP.Header[0].OrderId[0].text()
    def nameP  = xmlP.Header[0].Customer[0].Name[0].text()
    def emailP = xmlP.Header[0].Customer[0].Email[0].text()
    def dateP  = xmlP.Header[0].OrderDate[0].text()

    // ===============================================================
    // 3C — MANIPULAR XML COM PARSER (permite editar)
    // ===============================================================
    xmlP.Header[0].appendNode("Status", "APROVADO")

    // ===============================================================
    // 3D — CRIAR NOVO XML COM MarkupBuilder
    // ===============================================================
    def writer = new StringWriter()
    def mk = new MarkupBuilder(writer)

    mk.Order {
        OrderHeader {
            ID(idP)
            CustomerName(nameP)
            CustomerEmail(emailP)
            Date(dateP)
        }
        OrderItems {
            xmlP.Items[0].Item.each { item ->
                def code = item.Code[0].text()
                def qty  = item.Qty[0].text()
                def price = item.Price[0].text()

                Item {
                    Material(code)
                    Quantity(qty)
                    UnitPrice(price)
                }
            }
        }
    }

    message.setBody(writer.toString())
    return message
    */


    // #####################################################################
    // #####################################################################
    // ####  FIM DO SCRIPT — ESCOLHA O BLOCO E DESCOMENTE PARA TESTAR  ####
    // #####################################################################
    // #####################################################################

    message.setBody("Nenhuma abordagem habilitada. Descomente um bloco.")
    return message
}
