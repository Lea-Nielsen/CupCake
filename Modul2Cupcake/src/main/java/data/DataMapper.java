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

    public void createOrder(ShoppingCart shoppingCart, String username, double balance) throws Exception {
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
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

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
