import com.sap.gateway.ip.core.customdev.util.Message

// PARTE 1 – LEITURA / PARSE
import javax.xml.parsers.DocumentBuilderFactory
import org.xml.sax.InputSource
import java.io.StringReader

// PARTE 2 – Slurper / Parser / Builders
import groovy.xml.XmlSlurper
import groovy.xml.XmlParser
import groovy.xml.MarkupBuilder
import groovy.xml.XmlUtil

def Message processData(Message message) {

    // =====================================================================
    // XML DE EXEMPLO (vem no body do CPI)
    // =====================================================================
    //
    // <?xml version="1.0" encoding="UTF-8"?>
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
    //
    // =====================================================================

    // ---------------------------------------------------------------------
    // 1) PEGAR O BODY DE ENTRADA (STRING XML)
    // ---------------------------------------------------------------------
    def body = message.getBody(String)

    // =====================================================================
    // BLOCO A – LEITURA / PARSE DO XML
    // =====================================================================

    // A1) LEITURA COM DOM (DocumentBuilder / org.w3c.dom.Document)
    //     - Cria uma árvore DOM padrão W3C
    //     - Permite manipulação estrutural completa
    def factory = DocumentBuilderFactory.newInstance()
    def builder = factory.newDocumentBuilder()
    def docDom  = builder.parse(new InputSource(new StringReader(body)))  // doc DOM (entrada)

    // A2) LEITURA COM XmlSlurper (GPathResult – leitura "tipo JSON")
    //     - Ideal para leitura e navegação
    //     - Não altera a estrutura original
    def xmlSlurper = new XmlSlurper().parseText(body)

    // A3) LEITURA COM XmlParser (Node)
    //     - Também cria árvore
    //     - Permite alterações de estrutura (diferente do Slurper)
    def xmlParser = new XmlParser().parseText(body)

    // =====================================================================
    // BLOCO B – ACESSO A ATRIBUTOS / ELEMENTOS
    // =====================================================================

    // --------------------------------------------------
    // B1) ACESSO COM DOM
    // --------------------------------------------------
    def headerDom = docDom.getElementsByTagName("Header").item(0)

    def orderIdDom = headerDom.getElementsByTagName("OrderId").item(0).getTextContent()
    def customerNameDom = headerDom.getElementsByTagName("Customer")
                                   .item(0)
                                   .getElementsByTagName("Name")
                                   .item(0)
                                   .getTextContent()
    def customerEmailDom = headerDom.getElementsByTagName("Customer")
                                    .item(0)
                                    .getElementsByTagName("Email")
                                    .item(0)
                                    .getTextContent()
    def orderDateDom = headerDom.getElementsByTagName("OrderDate")
                                .item(0)
                                .getTextContent()

    // Lista de itens com DOM
    def itemsDom    = docDom.getElementsByTagName("Items").item(0)
    def itemListDom = itemsDom.getElementsByTagName("Item")
    def firstItemCodeDom  = itemListDom.item(0).getElementsByTagName("Code").item(0).getTextContent()
    def firstItemQtyDom   = itemListDom.item(0).getElementsByTagName("Qty").item(0).getTextContent()
    def firstItemPriceDom = itemListDom.item(0).getElementsByTagName("Price").item(0).getTextContent()

    // --------------------------------------------------
    // B2) ACESSO COM XmlSlurper
    // --------------------------------------------------
    def orderIdSlurper      = xmlSlurper.Header.OrderId.text()
    def customerNameSlurper = xmlSlurper.Header.Customer.Name.text()
    def customerEmailSlurper= xmlSlurper.Header.Customer.Email.text()
    def orderDateSlurper    = xmlSlurper.Header.OrderDate.text()

    // Lista de itens com Slurper
    def firstItemCodeSlurper  = xmlSlurper.Items.Item[0].Code.text()
    def firstItemQtySlurper   = xmlSlurper.Items.Item[0].Qty.text()
    def firstItemPriceSlurper = xmlSlurper.Items.Item[0].Price.text()

    // Exemplo: pegar todos os códigos numa lista
    def codigosSlurper = xmlSlurper.Items.Item.collect { it.Code.text() }     // ["ABC","XYZ"]

    // --------------------------------------------------
    // B3) ACESSO COM XmlParser
    // --------------------------------------------------
    def orderIdParser      = xmlParser.Header[0].OrderId[0].text()
    def customerNameParser = xmlParser.Header[0].Customer[0].Name[0].text()
    def customerEmailParser= xmlParser.Header[0].Customer[0].Email[0].text()
    def orderDateParser    = xmlParser.Header[0].OrderDate[0].text()

    def firstItemCodeParser  = xmlParser.Items[0].Item[0].Code[0].text()
    def firstItemQtyParser   = xmlParser.Items[0].Item[0].Qty[0].text()
    def firstItemPriceParser = xmlParser.Items[0].Item[0].Price[0].text()

    // =====================================================================
    // BLOCO C – MANIPULAÇÃO DO XML (EM MEMÓRIA)
    // =====================================================================

    // Observação:
    // - XmlSlurper → leitura apenas (imutável para estrutura)
    // - XmlParser   → permite alteração de estrutura
    // - DOM         → permite alteração de estrutura

    // --------------------------------------------------
    // C1) MANIPULAÇÃO COM DOM – Exemplo:
    //     - Adicionar <Status>APROVADO</Status> em <Header>
    //     - Alterar OrderId para "999"
    // --------------------------------------------------
    def headerDomForEdit = docDom.getElementsByTagName("Header").item(0)

    // Trocar valor do OrderId
    headerDomForEdit.getElementsByTagName("OrderId")
                    .item(0)
                    .setTextContent("999")

    // Adicionar um novo elemento <Status>APROVADO</Status>
    def statusElem = docDom.createElement("Status")
    statusElem.setTextContent("APROVADO")
    headerDomForEdit.appendChild(statusElem)

    // --------------------------------------------------
    // C2) MANIPULAÇÃO COM XmlParser – Exemplo:
    //     - Adicionar <Status>APROVADO</Status> em <Header>
    // --------------------------------------------------
    // Em XmlParser, cada tag é um Node e tem uma lista de filhos
    def headerParserNode = xmlParser.Header[0]
    headerParserNode.appendNode("Status", "APROVADO")

    // Agora xmlParser.Header[0].Status[0].text() == "APROVADO"

    // --------------------------------------------------
    // C3) "MANIPULAÇÃO" COM Slurper – APENAS LÓGICA, NÃO ESTRUTURA
    //     Exemplo: calcular um total de faturamento
    // --------------------------------------------------
    def totalSlurper = xmlSlurper.Items.Item.collect { item ->
        def qty   = item.Qty.text().toBigDecimal()
        def price = item.Price.text().replace(",", ".").toBigDecimal()
        qty * price
    }.sum()   // soma todas as multiplicações

    // Aqui você não muda o XML, só gera um valor calculado.

    // =====================================================================
    // BLOCO D – CRIAÇÃO DE UM NOVO XML DE SAÍDA
    // =====================================================================

    // Vamos assumir que queremos um XML de saída com esta cara:
    //
    // <Order>
    //   <OrderHeader>
    //     <ID>...</ID>
    //     <CustomerName>...</CustomerName>
    //     <CustomerEmail>...</CustomerEmail>
    //     <Date>...</Date>
    //   </OrderHeader>
    //   <OrderItems>
    //     <Item>
    //       <Material>...</Material>
    //       <Quantity>...</Quantity>
    //       <UnitPrice>...</UnitPrice>
    //       <Total>...</Total>
    //     </Item>
    //   </OrderItems>
    // </Order>

    // ---------------------------------------------------------------------
    // D1) CRIANDO XML NOVO COM StringBuilder (mais usado no CPI)
    //     - Ler com Slurper
    //     - Construir XML como texto
    // ---------------------------------------------------------------------

    def sb = new StringBuilder()

    // Header do XML de saída
    sb.append("<Order>")
    sb.append("<OrderHeader>")
    sb.append("<ID>${orderIdSlurper}</ID>")
    sb.append("<CustomerName>${customerNameSlurper}</CustomerName>")
    sb.append("<CustomerEmail>${customerEmailSlurper}</CustomerEmail>")
    sb.append("<Date>${orderDateSlurper}</Date>")
    sb.append("</OrderHeader>")

    // Itens
    sb.append("<OrderItems>")
    xmlSlurper.Items.Item.each { item ->
        def code  = item.Code.text()
        def qty   = item.Qty.text().toBigDecimal()
        def price = item.Price.text().replace(",", ".").toBigDecimal()
        def total = qty * price

        sb.append("<Item>")
        sb.append("<Material>${code}</Material>")
        sb.append("<Quantity>${qty}</Quantity>")
        sb.append("<UnitPrice>${price}</UnitPrice>")
        sb.append("<Total>${total}</Total>")
        sb.append("</Item>")
    }
    sb.append("</OrderItems>")
    sb.append("</Order>")

    def novoXmlStringBuilder = sb.toString()


    // ---------------------------------------------------------------------
    // D2) CRIANDO XML NOVO COM MarkupBuilder (XML "bonito")
    //     - Também usando Slurper para leitura
    // ---------------------------------------------------------------------

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


    // ---------------------------------------------------------------------
    // D3) CRIANDO XML NOVO COM DOM + XmlUtil (árvore W3C)
    //     - Ler com Slurper, construir DOM de saída
    // ---------------------------------------------------------------------

    def docOut = builder.newDocument()

    def orderOut = docOut.createElement("Order")
    docOut.appendChild(orderOut)

    def orderHeaderOut = docOut.createElement("OrderHeader")
    orderOut.appendChild(orderHeaderOut)

    def idOut = docOut.createElement("ID")
    idOut.setTextContent(orderIdSlurper)
    orderHeaderOut.appendChild(idOut)

    def custNameOut = docOut.createElement("CustomerName")
    custNameOut.setTextContent(customerNameSlurper)
    orderHeaderOut.appendChild(custNameOut)

    def custEmailOut = docOut.createElement("CustomerEmail")
    custEmailOut.setTextContent(customerEmailSlurper)
    orderHeaderOut.appendChild(custEmailOut)

    def dateOut = docOut.createElement("Date")
    dateOut.setTextContent(orderDateSlurper)
    orderHeaderOut.appendChild(dateOut)

    def orderItemsOut = docOut.createElement("OrderItems")
    orderOut.appendChild(orderItemsOut)

    xmlSlurper.Items.Item.each { item ->
        def code  = item.Code.text()
        def qty   = item.Qty.text().toBigDecimal()
        def price = item.Price.text().replace(",", ".").toBigDecimal()
        def total = qty * price

        def itemOut = docOut.createElement("Item")
        orderItemsOut.appendChild(itemOut)

        def materialOut = docOut.createElement("Material")
        materialOut.setTextContent(code)
        itemOut.appendChild(materialOut)

        def qtyOut = docOut.createElement("Quantity")
        qtyOut.setTextContent(qty.toString())
        itemOut.appendChild(qtyOut)

        def priceOut = docOut.createElement("UnitPrice")
        priceOut.setTextContent(price.toString())
        itemOut.appendChild(priceOut)

        def totalOut = docOut.createElement("Total")
        totalOut.setTextContent(total.toString())
        itemOut.appendChild(totalOut)
    }

    def novoXmlDom = XmlUtil.serialize(docOut.getDocumentElement())

    // =====================================================================
    // QUAL XML VAI SAIR NO BODY?
    // Aqui você escolhe qual saída quer testar.
    // =====================================================================

    // Exemplo: usar o StringBuilder
    // message.setBody(novoXmlStringBuilder)

    // Exemplo: usar o MarkupBuilder
    // message.setBody(novoXmlMarkupBuilder)

    // Exemplo: usar o DOM + XmlUtil
    message.setBody(novoXmlDom)

    return message
}
