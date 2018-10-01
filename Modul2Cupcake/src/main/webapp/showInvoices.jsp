<%@page import="data.LineItem"%>
<%@page import="data.ShoppingCart"%>
<%@page import="java.util.List"%>
<%@page import="data.Topping"%>
<%@page import="data.Bottom"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Invoices</title>
    </head>
    <body>
        <% String username = (String) request.getSession().getAttribute("username"); %>
        <% List<Integer> listOfInvoices = (List) request.getAttribute("invoices");%>
        <h1><%= "Username: " + username%></h1>
        <br><br>
        <%
            for (Integer r : listOfInvoices) {
                out.println("<li><a href=\"Control?origin=showInvoiceDetails&invoiceId=" + r + "\">InvoiceID: " + r + "</li><br>");
            }
        %>
    </body>
</html>
