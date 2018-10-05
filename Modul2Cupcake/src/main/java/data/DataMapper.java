package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataMapper {

    private final static String GET_INVOICE_DETAILS = "SELECT orders.order_id, u_username, qty, cupcakename, cupcakeprice, totalprice, ordertotalprice, orderDate FROM orders JOIN orderdetails ON orders.order_id = orderdetails.order_id JOIN cupcake ON cupcake.id = orderdetails.c_id WHERE orders.order_id = ? ORDER BY qty ASC, cupcakename ASC";
    private final static String INSERT_ORDER_CREATE_ORDER = "INSERT INTO orders (u_username, ordertotalprice, orderdate) VALUES (?, ?, ?)";
    private final static String INSERT_CUPCAKE_CREATE_ORDER = "INSERT INTO cupcake (cupcakename, cupcakeprice, t_toppingname, b_bottomname) VALUES (?, ?, ?, ?)";
    private final static String INSERT_ORDER_DETAILS_CREATE_ORDER = "INSERT INTO orderdetails (order_id, c_id, qty, totalprice) VALUES (?, ?, ?, ?)";
    private final static String UPDATE_BALANCE_CREATE_ORDER = "UPDATE `user`SET balance = ? WHERE username = ?";
    private final static String GET_USER_INFORMATION = "SELECT * FROM `user`WHERE username = ?";
    private final static String GET_INVOICE_ID = "SELECT order_id FROM orders WHERE u_username = ?";
    private final static String GET_ALL_INVOICE_ID = "SELECT order_id FROM orders ORDER BY order_id";
    private final static String ADD_USER = "INSERT INTO `user` (username, password, email, balance) VALUES(?,?,?,?)";
    private final static String GET_CUPCAKE_INFORMATION = "SELECT * FROM `cupcake`WHERE id = ?";
    private final static String GET_BOTTOMS = "SELECT * FROM `bottoms`";
    private final static String GET_TOPPINGS = "SELECT * FROM `toppings`";

    String dateFormat = "yyyy-MM-dd";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    String orderDate = simpleDateFormat.format(new Date());

    /**
     * Through a connection to the database, it executes a query which will
     * return a resultSet which includes details for a specific order.
     *
     * @param id Used to specify the order.
     * @returns a resultSet of invoice details.
     */
    public Invoice getInvoiceDetails(int id) {
        Invoice invoice = null;
        try {
            Connection conn = new DBConnector().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(GET_INVOICE_DETAILS);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int i_id = rs.getInt("order_id");
            int i_qty = rs.getInt("qty");
            String i_name = rs.getString("cupcakename");
            double i_price = rs.getDouble("cupcakeprice");
            double i_totalprice = rs.getDouble("totalprice");
            double i_ordertotalprice = rs.getDouble("ordertotalprice");
            String i_date = rs.getString("orderDate");
            invoice = new Invoice(i_id, i_ordertotalprice, i_date);
            invoice.updateInvoiceList(new InvoiceDetails(i_qty, i_name, i_price, i_totalprice));
            while (rs.next()) {
                i_qty = rs.getInt("qty");
                i_name = rs.getString("cupcakename");
                i_price = rs.getDouble("cupcakeprice");
                i_totalprice = rs.getDouble("totalprice");
                invoice.updateInvoiceList(new InvoiceDetails(i_qty, i_name, i_price, i_totalprice));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return invoice;
    }

    /**
     * Through a database connection, it executes several queries in order to
     * create an order in the database. To ensure a full order always will be
     * inserted in the database, all queries have to be successfully runned,
     * otherwise it rolls back.
     *
     * @param shoppingCart Holds information about the lineItems, which includes
     * name, price, top, bottom that are needed for an order.
     * @param username Belongs to the user who made the order.
     * @param balance Belongs to the user who made the order.
     */
    public void createOrder(ShoppingCart shoppingCart, String username, double balance) {
        try {
            Connection conn = new DBConnector().getConnection();
            PreparedStatement orderPstmt = conn.prepareStatement(INSERT_ORDER_CREATE_ORDER, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement cupcakePstmt = conn.prepareStatement(INSERT_CUPCAKE_CREATE_ORDER, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement orderDetailsPstmt = conn.prepareStatement(INSERT_ORDER_DETAILS_CREATE_ORDER);
            PreparedStatement updateBalancePstmt = conn.prepareStatement(UPDATE_BALANCE_CREATE_ORDER);
            ResultSet rs = null;
            int orderId = 0;
            try {
                orderPstmt.setString(1, username);
                orderPstmt.setDouble(2, shoppingCart.getTotalpriceForShoppingCart());
                orderPstmt.setString(3, orderDate);
                //To create a transaction we need to not have automatic commit after each statement.
                conn.setAutoCommit(false);
                int resultOrder = orderPstmt.executeUpdate();
                rs = orderPstmt.getGeneratedKeys();
                rs.next();
                orderId = rs.getInt(1);
                if (resultOrder == 1) {
                    ResultSet rsCupcake = null;
                    int cupcakeId = 0;
                    for (LineItem lineItem : shoppingCart.getArrLineItems()) {
                        cupcakePstmt.setString(1, lineItem.getCupcake().getName());
                        cupcakePstmt.setDouble(2, lineItem.getCupcake().getPrice());
                        cupcakePstmt.setString(3, lineItem.getCupcake().getTop());
                        cupcakePstmt.setString(4, lineItem.getCupcake().getBottom());
                        int resultCupcake = cupcakePstmt.executeUpdate();
                        rsCupcake = cupcakePstmt.getGeneratedKeys();
                        rsCupcake.next();
                        cupcakeId = rsCupcake.getInt(1);
                        if (resultCupcake == 1) {
                            orderDetailsPstmt.setInt(1, orderId);
                            orderDetailsPstmt.setInt(2, cupcakeId);
                            orderDetailsPstmt.setInt(3, lineItem.getQty());
                            orderDetailsPstmt.setDouble(4, lineItem.getTotalprice());
                            orderDetailsPstmt.executeUpdate();
                            updateBalancePstmt.setDouble(1, balance);
                            updateBalancePstmt.setString(2, username);
                            updateBalancePstmt.executeUpdate();
                            conn.commit();
                        } else {
                            conn.rollback();
                        }
                    }
                    conn.commit();
                } else {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace(); //This should go in the log file.
                // roll back the transaction
                if (conn != null) {
                    conn.rollback();
                }
            } finally {
                conn.setAutoCommit(true);
                if (orderPstmt != null) {
                    orderPstmt.close();
                }
                if (cupcakePstmt != null) {
                    cupcakePstmt.close();
                }
                if (orderDetailsPstmt != null) {
                    orderDetailsPstmt.close();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Through a connection to the database, it executes a query which will
     * return a resultSet which includes details for a specific user.
     *
     * @param username Is used to match the result from the database.
     * Username(s) are always unique in the database.
     * @return User object
     */
    public User getUserInformation(String username) {
        User user = null;
        try {
            Connection conn = new DBConnector().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(GET_USER_INFORMATION);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String u_username = rs.getString("username");
                String u_password = rs.getString("password");
                double u_balance = rs.getDouble("balance");
                String u_email = rs.getString("email");
                String u_role = rs.getString("role");
                user = new User(u_username, u_password, u_email, u_balance, u_role);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return user;
    }

    /**
     * Through a connection to the database, it executes a query which will
     * return a resultSet which includes all invoices for a user.
     *
     * @param username Is used to specify what invoices should be extracted, as
     * only the ones linked to the user should be returned.
     * @return List of invoice id's
     */
    public ArrayList<Integer> getInvoiceId(String username) {
        ArrayList<Integer> listInvoiceId = new ArrayList();
        try {
            Connection conn = new DBConnector().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(GET_INVOICE_ID);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int invoiceId = rs.getInt("order_id");
                listInvoiceId.add(invoiceId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return listInvoiceId;
    }

    /**
     * Through a connection to the database, it executes a query which will return a resultSet which includes all invoice id's. (Admin feature)
     * 
     * @return List of all invoice id's.
     */
    public ArrayList<Integer> getAllInvoiceId() {
        ArrayList<Integer> listInvoiceId = new ArrayList();
        try {
            Connection conn = new DBConnector().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(GET_ALL_INVOICE_ID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int invoiceId = rs.getInt("order_id");
                listInvoiceId.add(invoiceId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return listInvoiceId;
    }
    /**
     * Through a connection to the database, it add's a user to the database.
     * 
     * @param user Holds all information about the user object that will be inserted in the database.
     */
    public void addUser(User user) {
        try {
            Connection conn = new DBConnector().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(ADD_USER);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setDouble(4, user.getBalance());
            pstmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * Through a connection to the database, it executes a query which will
     * return a resultSet which includes all information for a cupcake, based on a id.
     * 
     * @param id Information on a specific cupcake based on this id.
     * @return Cupcake object
     */
    public Cupcake getCupcakeInformation(int id) {
        Cupcake cupcake = null;
        try {
            Connection conn = new DBConnector().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(GET_CUPCAKE_INFORMATION);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int c_id = rs.getInt("id");
                String c_name = rs.getString("cupcakename");
                double c_price = rs.getDouble("cupcakeprice");
                String c_top = rs.getString("t_topname");
                String c_bottom = rs.getString("b_bottomname");
                cupcake = new Cupcake(c_id, c_name, c_price, c_top, c_bottom);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return cupcake;
    }
    /**
     * Through a connection to the database, it executes a query which will
     * return a resultSet which includes all information for cupcake bottoms.
     * 
     * @return List of all cupcake bottoms.
     */
    public List<Bottom> getBottoms() {
        List<Bottom> getAllBottoms = new ArrayList();
        try {
            Connection conn = new DBConnector().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(GET_BOTTOMS);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String b_name = rs.getString("bottomname");
                double b_price = rs.getDouble("bottomprice");
                getAllBottoms.add(new Bottom(b_name, b_price));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return getAllBottoms;
    }
    
    /**
     * Through a connection to the database, it executes a query which will
     * return a resultSet which includes all information for cupcake toppings.
     * 
     * @return List of all cupcake toppings
     */

    public List<Topping> getToppings() {
        List<Topping> getAllToppings = new ArrayList();
        try {
            Connection conn = new DBConnector().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(GET_TOPPINGS);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String t_name = rs.getString("toppingname");
                double t_price = rs.getDouble("toppingprice");
                getAllToppings.add(new Topping(t_name, t_price));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return getAllToppings;
    }

}
