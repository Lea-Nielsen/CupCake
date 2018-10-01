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
        <title>Shop page</title>

        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">

        <!-- Bootstrap core CSS -->
        <link href="vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

        <!-- Custom fonts for this template -->
        <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Varela+Round" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

        <!-- Custom styles for this template -->
        <link href="css/grayscale.min.css" rel="stylesheet">

    </head>
    <body id="page-top">

        <!-- Navigation -->
        <nav class="navbar navbar-expand-lg navbar-light fixed-top" id="mainNav">
            <div class="container">
                <a class="navbar-brand js-scroll-trigger" href="#page-top">Cupcake Shop</a>
                <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
                    Menu
                    <i class="fas fa-bars"></i>
                </button>
                <div class="collapse navbar-collapse" id="navbarResponsive">
                    <ul class="navbar-nav ml-auto">
                        <li class="nav-item">
                            <a class="nav-link js-scroll-trigger" href="#about">Order Cupcake</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link js-scroll-trigger" href="#projects">Info</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link js-scroll-trigger" href="#signup">Contact</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="Control?origin=logout">Logout</a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>

        <!-- Header -->
        <header class="masthead">
            <div class="container d-flex h-100 align-items-center">
                <div class="mx-auto text-center">
                    <h1 class="text-white-50 mx-auto my-0 text-uppercase">Cupcake Shop</h1>
                    <h2 class="text-white-50 mx-auto mt-2 mb-5">Buy the best cupcakes!</h2>
                    <a href="#about" class="btn btn-primary js-scroll-trigger">Order Cupcakes</a>
                </div>
            </div>
        </header>

        <!-- About Section -->
        <section id="about" class="about-section text-center">
            <div class="container">
                <div class="row">
                    <div class="col-lg-8 mx-auto">

                        <% String username = (String) request.getSession().getAttribute("username"); %>
                        <% double balance = (double) request.getSession().getAttribute("balance"); %>
                        <% List<Bottom> listOfBottomsSelect = (List) request.getSession().getAttribute("bottoms"); %>
                        <% List<Topping> listOfToppingsSelect = (List) request.getSession().getAttribute("toppings");%>
                        <% ShoppingCart shoppingCart = (ShoppingCart) request.getSession().getAttribute("shoppingCart");%>
                        <h2 class="text-white mb-4"><%= "Username: " + username%><%= " <br> "%><%= "Balance: " + balance%><%= " <br><br> "%><a href="Control?origin=showInvoices">Show invoices</a></h2>
                        <br><br>
                        <form method="POST" action="Control">
                            <h3 class="text-white mb-4">Topping</h3>
                            <select name="addItemTopping">
                                <%
                                    for (Topping t : listOfToppingsSelect) {
                                        out.println("<option value=\"" + t.getName() + "\">" + t.getName() + " " + t.getPrice() + "</option>");
                                                
                                    }
                                %>
                            </select>
                            <br><br>
                            <h3 class="text-white mb-4">Bottom</h3>
                            <select name="addItemBottom">
                                <%
                                    for (Bottom b : listOfBottomsSelect) {
                                        out.println("<option value=\"" + b.getName() + "\">" + b.getName() + " " + b.getPrice() + "</option>");
                                    }
                                %>
                            </select>
                            <br><br>
                            <h3 class="text-white mb-4">Quantity</h3>
                            <input type="text" name="addItemQuantity" value="1"><br><br>
                            <input type="hidden" name="origin" value="addItemToCart" ><br><br>
                            <input type="submit" value="Add to cart">
                        </form>
                        <br>
                        <h3 class="text-white-50">
                            <%
                                if (shoppingCart != null) {
                                    out.println("<table><thead><tr><th>Name</th><th>Quantity</th><th>Price</th><th>Total</th></tr></thead><tbody>");

                                    for (LineItem lineItem : shoppingCart.getArrLineItems()) {
                                        out.println("<tr><td>" + lineItem.getCupcake().getName()
                                                + "</td><td>" + lineItem.getQty()
                                                + "</td><td>" + lineItem.getCupcake().getPrice()
                                                + "</td><td>" + lineItem.getTotalprice()
                                                + "</td><td><form method=\"POST\" action=\"Control\">"
                                                + "<input type=\"hidden\" name=\"origin\" value=\"removeItemFromCart\" >"
                                                + "<input type=\"hidden\" name=\"lineId\" value=\"" + lineItem.getId() + "\" >"
                                                + "<input type=\"submit\" value=\"Remove from cart\"></form>"
                                                + "</tr>");
                                    }
                                    out.println("<tr><td>" + shoppingCart.getTotalpriceForShoppingCart()
                                            + "</td></tr>");

                                    out.println("</tbody></table>");
                            %>
                            <%}
                            %>
                        </h3>
                        <form method="POST" action="Control">
                            <input type="hidden" name="origin" value="placeOrder"><br><br>
                            <input type="submit" value="Place order">
                        </form>
                        <br><br><br>

                        <!--                        <h2 class="text-white mb-4">Built with Bootstrap 4</h2>
                                                <p class="text-white-50">Grayscale is a free Bootstrap theme created by Start Bootstrap. It can be yours right now, simply download the template on
                                                    <a href="http://startbootstrap.com/template-overviews/grayscale/">the preview page</a>. The theme is open source, and you can use it for any purpose, personal or commercial.</p>
                        -->
                    </div>
                </div>

            </div>
        </section>

        <!-- Projects Section -->
        <section id="projects" class="projects-section bg-light">
            <div class="container">

                <!-- Featured Project Row -->
                <div class="row align-items-center no-gutters mb-4 mb-lg-5">
                    <div class="col-xl-8 col-lg-7">
                        <img class="img-fluid mb-3 mb-lg-0" src="cup.jpg" alt="">
                    </div>
                    <div class="col-xl-4 col-lg-5">
                        <div class="featured-text text-center text-lg-left">
                            <h4>The CupCake factory</h4>
                            <p class="text-black-50 mb-0">The CupCake factory is one of the largest CupCake shops i the world!</p>
                        </div>
                    </div>
                </div>

                <!-- Project One Row -->
                <!--              <div class="row justify-content-center no-gutters mb-5 mb-lg-0">
                                    <div class="col-lg-6">
                                        <img class="img-fluid" src="img/demo-image-01.jpg" alt="">
                                    </div>
                                    <div class="col-lg-6">
                                        <div class="bg-black text-center h-100 project">
                                            <div class="d-flex h-100">
                                                <div class="project-text w-100 my-auto text-center text-lg-left">
                                                    <h4 class="text-white">Misty</h4>
                                                    <p class="mb-0 text-white-50">An example of where you can put an image of a project, or anything else, along with a description.</p>
                                                    <hr class="d-none d-lg-block mb-0 ml-0">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                
                
                                <div class="row justify-content-center no-gutters">
                                    <div class="col-lg-6">
                                        <img class="img-fluid" src="img/demo-image-02.jpg" alt="">
                                    </div>
                                    <div class="col-lg-6 order-lg-first">
                                        <div class="bg-black text-center h-100 project">
                                            <div class="d-flex h-100">
                                                <div class="project-text w-100 my-auto text-center text-lg-right">
                                                    <h4 class="text-white">Mountains</h4>
                                                    <p class="mb-0 text-white-50">Another example of a project with its respective description. These sections work well responsively as well, try this theme on a small screen!</p>
                                                    <hr class="d-none d-lg-block mb-0 mr-0">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                
                            </div>
                -->        </section>


        <!-- Signup Section -->
        <section id="signup" class="signup-section">
            <div class="container">
                <div class="row">
                    <div class="col-md-10 col-lg-8 mx-auto text-center">

                        <i class="far fa-paper-plane fa-2x mb-2 text-white"></i>
                        <h2 class="text-white mb-5">Subscribe to receive updates!</h2>

                        <form class="form-inline d-flex">
                            <input type="email" class="form-control flex-fill mr-0 mr-sm-2 mb-3 mb-sm-0" id="inputEmail" placeholder="Enter email address...">
                            <button type="submit" class="btn btn-primary mx-auto">Subscribe</button>
                        </form>

                    </div>
                </div>
            </div>
        </section>

        <!-- Contact Section -->
        <section class="contact-section bg-black">
            <div class="container">

                <div class="row">

                    <div class="col-md-4 mb-3 mb-md-0">
                        <div class="card py-4 h-100">
                            <div class="card-body text-center">
                                <i class="fas fa-map-marked-alt text-primary mb-2"></i>
                                <h4 class="text-uppercase m-0">Address</h4>
                                <hr class="my-4">
                                <div class="small text-black-50">4923 Market Street, Orlando FL</div>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-4 mb-3 mb-md-0">
                        <div class="card py-4 h-100">
                            <div class="card-body text-center">
                                <i class="fas fa-envelope text-primary mb-2"></i>
                                <h4 class="text-uppercase m-0">Email</h4>
                                <hr class="my-4">
                                <div class="small text-black-50">
                                    <a href="#">hello@yourdomain.com</a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-4 mb-3 mb-md-0">
                        <div class="card py-4 h-100">
                            <div class="card-body text-center">
                                <i class="fas fa-mobile-alt text-primary mb-2"></i>
                                <h4 class="text-uppercase m-0">Phone</h4>
                                <hr class="my-4">
                                <div class="small text-black-50">+1 (555) 902-8832</div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="social d-flex justify-content-center">
                    <a href="#" class="mx-2">
                        <i class="fab fa-twitter"></i>
                    </a>
                    <a href="#" class="mx-2">
                        <i class="fab fa-facebook-f"></i>
                    </a>
                    <a href="#" class="mx-2">
                        <i class="fab fa-github"></i>
                    </a>
                </div>

            </div>
        </section>

        <!-- Footer -->
        <footer class="bg-black small text-center text-white-50">
            <div class="container">
                Copyright &copy; Your Website 2018
            </div>
        </footer>

        <!-- Bootstrap core JavaScript -->
        <script src="vendor/jquery/jquery.min.js"></script>
        <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

        <!-- Plugin JavaScript -->
        <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

        <!-- Custom scripts for this template -->
        <script src="js/grayscale.min.js"></script>

    </body>

</html>