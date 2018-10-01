package presentation;

import data.Bottom;
import data.Cupcake;
import data.Invoice;
import data.LineItem;
import data.ShoppingCart;
import data.Topping;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.Controller;

@WebServlet(name = "Control", urlPatterns = {"/Control"})
public class Control extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
 ctrl.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");

        Controller ctrl = new Controller();

        String origin = request.getParameter("origin");
        if(origin == null){
            origin = "login";
        }

        ShoppingCart shoppingCart = null;
        shoppingCart = (ShoppingCart) request.getSession().getAttribute("shoppingCart");
        if (shoppingCart == null) {
            shoppingCart = ctrl.createShoppingCart();
        }

        switch (origin) {
            case "login":
                request.getRequestDispatcher("userLogin.jsp").forward(request, response);
                break;
            case "loginUser":
                String l_username = request.getParameter("login_user");
                String l_password = request.getParameter("login_pwd");
                double balance = ctrl.userLoggedInBalance(l_username);
                List<Topping> listOfToppings = ctrl.getAllToppings();
                List<Bottom> listOfBottoms = ctrl.getAllBottoms();
                if (ctrl.checkPassword(l_username, l_password)) {
                    request.getSession().setAttribute("username", l_username);
                    request.getSession().setAttribute("balance", balance);
                    request.getSession().setAttribute("toppings", listOfToppings);
                    request.getSession().setAttribute("bottoms", listOfBottoms);
                    if(ctrl.isAdmin(l_username)){
                        List<Integer> listOfInvoices = ctrl.getAllInvoicesFromDB();
                        request.setAttribute("invoices", listOfInvoices);
                        request.getSession().setAttribute("isAdmin", true);
                        request.getRequestDispatcher("showInvoices.jsp").forward(request, response);
                    } else {
                        request.getSession().setAttribute("isAdmin", false);
                        request.getRequestDispatcher("index.jsp").forward(request, response);
                    }
                } else {
                    request.getRequestDispatcher("userLoginError.jsp").forward(request, response);
                }
                break;
            case "create":
                request.getRequestDispatcher("userCreate.jsp").forward(request, response);
                break;
            case "createUser":
                String n_username = request.getParameter("new_username");
                String n_password = request.getParameter("new_pwd");
                String n_email = request.getParameter("new_email");
                double n_balance = Double.parseDouble(request.getParameter("new_balance"));
                if (ctrl.checkUser(n_username, n_password, n_email, n_balance)) {
                    ctrl.addUser(n_username, n_password, n_email, n_balance);
                    request.getRequestDispatcher("userCreateSuccessfully.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("userCreateError.jsp").forward(request, response);
                }
                break;
            case "addItemToCart":
                int qty = Integer.parseInt(request.getParameter("addItemQuantity"));
                String topping = request.getParameter("addItemTopping");
                String bottom = request.getParameter("addItemBottom");
                double bottomPrice = ctrl.getBottomPrice((List) request.getSession().getAttribute("bottoms"), request.getParameter("addItemBottom"));
                double toppingPrice = ctrl.getToppingPrice((List) request.getSession().getAttribute("toppings"), request.getParameter("addItemTopping"));
                double price = bottomPrice + toppingPrice;
                ctrl.addToCart(shoppingCart, new LineItem(qty, new Cupcake(price, topping, bottom)));
                request.getSession().setAttribute("shoppingCart", ctrl.getShoppingCart(shoppingCart));
                request.getRequestDispatcher("index.jsp").forward(request, response);
                break;
            case "removeItemFromCart":
                shoppingCart = (ShoppingCart) request.getSession().getAttribute("shoppingCart");
                ctrl.removeFromCart(shoppingCart, Integer.parseInt(request.getParameter("lineId")));
                request.getSession().setAttribute("shoppingCart", ctrl.checkEmptyShoppingCart(shoppingCart));
                request.getRequestDispatcher("index.jsp").forward(request, response);
                break;
            case "placeOrder":
                String po_username = (String) request.getSession().getAttribute("username");
                double po_balance = (double) request.getSession().getAttribute("balance");
                if (ctrl.checkoutCheckBalance((ShoppingCart) request.getSession().getAttribute("shoppingCart"), po_balance)) {
                    po_balance = ctrl.withdrawFromBalance((ShoppingCart) request.getSession().getAttribute("shoppingCart"), po_balance);
                    ctrl.createOrder((ShoppingCart) request.getSession().getAttribute("shoppingCart"), po_username, po_balance);
                    request.getSession().setAttribute("shoppingCart", null);
                    request.getSession().setAttribute("balance", po_balance);
                    request.getRequestDispatcher("placeOrderSuccessfully.jsp").forward(request, response);
                } else {
                    request.getRequestDispatcher("placeOrderError.jsp").forward(request, response);
                }
                break;
            case "showInvoices":
                String i_username = (String) request.getSession().getAttribute("username");
                List<Integer> listOfInvoices;
                if((boolean) request.getSession().getAttribute("isAdmin")){
                    listOfInvoices = ctrl.getAllInvoicesFromDB();
                } else {
                    listOfInvoices = ctrl.getInvoicesFromDB(i_username);
                }
                request.setAttribute("invoices", listOfInvoices);
                request.getRequestDispatcher("showInvoices.jsp").forward(request, response);
                break;
            case "showInvoiceDetails":
                int id_id = Integer.parseInt(request.getParameter("invoiceId"));
                Invoice invoice = ctrl.getInvoiceDetailsFromDB(id_id);
                request.setAttribute("invoiceDetails", invoice);
                request.getRequestDispatcher("showInvoiceDetails.jsp").forward(request, response);
                break;
            case "index":
                request.getRequestDispatcher("index.jsp").forward(request, response);
                break;
            case "logout":
                request.getSession().invalidate();
                request.getRequestDispatcher("userLogin.jsp").forward(request, response);
                break;
            default:
                request.getRequestDispatcher("userLogin.jsp").forward(request, response);
                break;
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet ctrl. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
