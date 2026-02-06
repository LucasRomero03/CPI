import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

Message processData(Message message) {

    def body = message.getBody(String)
    def bodyParsed = new JsonSlurper().parseText(body)
    def html = new StringBuilder()

    html << """
    <html>
    <head>
        <meta charset="UTF-8">
        <style>
            table {
                width: 100%;
                border-collapse: collapse;
                font-family: Arial, sans-serif;
            }
            th, td {
                border: 1px solid #cccccc;
                text-align: left;
                padding: 8px;
            }
            th {
                background-color: #f2f2f2;
            }
            td.sim {
                background-color: #c7f7c1;
            }
        </style>
    </head>
    <body>
    <table>
        <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Total</th>
            <th>Moto</th>
        </tr>
    """

    bodyParsed.each { item ->

        def classeMoto = item.moto == "SIM" ? "sim" : ""

        html << """
        <tr>
            <td>${item.id}</td>
            <td>${item.name}</td>
            <td>${item.total_items_in_this_category}</td>
            <td class="${classeMoto}">${item.moto}</td>
        </tr>
        """
    }

    html << """
    </table>
    </body>
    </html>
    """

    message.setBody(html.toString())
    return message
}
