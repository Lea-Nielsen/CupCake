<%@page import="data.InvoiceDetails"%>
<%@page import="data.Invoice"%>
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
        <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
        <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <link rel="stylesheet" type="text/css" href="mycss2.css">
        <title>Invoice Details</title>

    </head>
    <body>
        <% String username = (String) request.getSession().getAttribute("username"); %>
        <% Invoice invoice = (Invoice) request.getAttribute("invoiceDetails");%>
        <h1><%= "Username: " + username%></h1>
        <form>
            <input type="button" value="Back to shop" onclick="window.location.href = 'Control?origin=index'" />
            <input type="button" value="Back to invoices" onclick="window.location.href = 'Control?origin=showInvoices'" />
        </form>
    </body>
</html>

<div class="container">
    <div class="row">
        <div class="col-12">
            <div class="card">
                <div class="card-body p-0">




                    <div class="row pb-5 p-5">
                        <div class="col-md-6">
                            <div id="logo">
                                <img src="logo2.png">
                            </div>
                            <p class="font-weight-bold mb-4">Client Information</p>
                            <p class="mb-1"><span class="text-muted">Username: </span><%= username%></p>
                        </div>

                        <div class="col-md-6 text-right">
                            <p class="font-weight-bold mb-4">Payment Details</p>
                            <p class="mb-1"><span class="text-muted">Invoice ID: </span><%= invoice.getId()%></p>
                            <p class="mb-1"><span class="text-muted">Invoice Date: </span><%= invoice.getDate()%></p>
                        </div>
                    </div>

                    <div class="row p-5">
                        <div class="col-md-12">
                            <table class="table">
                                <thead>
                                    <tr>
                                        <th class="border-0 text-uppercase small font-weight-bold">Quantity</th>
                                        <th class="border-0 text-uppercase small font-weight-bold">Name</th>
                                        <th class="border-0 text-uppercase small font-weight-bold">Price</th>
                                        <th class="border-0 text-uppercase small font-weight-bold">Price Total</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        for (InvoiceDetails invoiceDetails : invoice.getListInvoiceDetails()) {%>
                                    <tr>
                                        <td><%= invoiceDetails.getQty()%></td>
                                        <td><%= invoiceDetails.getName()%></td>
                                        <td><%= invoiceDetails.getPrice()%></td>
                                        <td><%= invoiceDetails.getTotalprice()%></td>
                                    </tr>
                                    <%}%>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="d-flex flex-row-reverse bg-dark text-white p-4">
                        <div class="py-3 px-5 text-right">
                            <div class="mb-2">Total order price</div>
                            <div class="h2 font-weight-light"><%= invoice.getOrdertotalprice()%></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>