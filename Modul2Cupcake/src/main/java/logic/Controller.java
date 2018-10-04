package logic;

import data.Bottom;
import data.DataMapper;
import data.Invoice;
import data.LineItem;
import data.ShoppingCart;
import data.Topping;
import data.User;
import java.util.List;

public class Controller {

    DataMapper dataMapper = new DataMapper();
 

    public boolean checkPassword(String username, String password) {
        User user = dataMapper.getUserInformation(username);
        return password.equals(user.getPassword());
    }
    
    public boolean isAdmin(String username) {
        String admin = "admin";
        User user = dataMapper.getUserInformation(username);
        return admin.equalsIgnoreCase(user.getRole());
    }

    public boolean checkUser(String username, String password, String email, double balance) {

        if ((username == null) || (username.isEmpty())) {
            return false;
        }
        if ((password == null) || (password.isEmpty())) {
            return false;
        }
        if ((email == null) || (email.isEmpty())) {
            return false;
        }
        if ((balance < 0)) {
            return false;
        }

        return true;

    }

    public void addUser(String username, String password, String email, double balance) {
        dataMapper.addUser(new User(username, password, email, balance));
    }
    
    public double userLoggedInBalance(String username) {
        User user = dataMapper.getUserInformation(username);
        return user.getBalance();
    }
    
        /**
     * This method makes a list of all bottoms from the database
     * @return all bottoms
     */
    public List<Bottom> getAllBottoms() {
        List<Bottom> allBottoms = dataMapper.getBottoms();
        return allBottoms;
    }
    
    /**
     * This method makes a list of all toppings from the database
     * @return all toppings
     */
    public List<Topping> getAllToppings() {
        List<Topping> allToppings = dataMapper.getToppings();
        return allToppings;
    }
    
    /**
     * 
     * This method returns the price of a cupcake buttom. It loops through the list finding the matching buttomname.
     * @param listBottoms is a list of buttoms taken from the database.
     * @param bottomname is the name of the buttom cupcake.
     * 
     * @return 
     */
    public double getBottomPrice(List<Bottom> listBottoms, String bottomname){
        for (Bottom bottom : listBottoms) {
            if(bottomname.equals(bottom.getName())) return bottom.getPrice();
        }
        return 0.0;
    }
    
        /**
     * 
     * This method returns the price of a cupcake buttom. It loops through the list finding the matching buttomname.
     * @param listBottoms is a list of buttoms taken from the database.
     * @param bottomname is the name of the buttom cupcake.
     * 
     * @return 
     */
    public double getToppingPrice(List<Topping> listToppings, String toppingname){
        for (Topping topping : listToppings) {
            if(toppingname.equals(topping.getName())) return topping.getPrice();
        }
        return 0.0;
    }
    
    public ShoppingCart createShoppingCart() {
        return new ShoppingCart();
    }
    
    public void addToCart(ShoppingCart shoppingCart, LineItem lineItem) {
        shoppingCart.updateShoppingCart(lineItem);
        shoppingCart.setTotalpriceForShoppingCart();
    }
    
    public ShoppingCart removeFromCart(ShoppingCart shoppingCart, int removeItemID) {
        for (int i = 0; i < shoppingCart.getArrLineItems().size(); i++) {
            LineItem lineItem = shoppingCart.getArrLineItems().get(i);
            if(lineItem.getId() == removeItemID){
                shoppingCart.getArrLineItems().remove(i);
            }
        }
        shoppingCart.setTotalpriceForShoppingCart();
        return shoppingCart;
    }
    
    public ShoppingCart checkEmptyShoppingCart(ShoppingCart shoppingCart) {
        if(shoppingCart.getArrLineItems().isEmpty()){
            shoppingCart = null;
        }
        return shoppingCart;
    }

    public ShoppingCart getShoppingCart(ShoppingCart shoppingCart) {
        return shoppingCart;
    }
    
    public boolean checkoutCheckBalance(ShoppingCart shoppingCart, double balance) {
        if (shoppingCart.getTotalpriceForShoppingCart() <= balance) {
            return true;
        }
        return false;
    }

    public double withdrawFromBalance(ShoppingCart shoppingCart, double balance) {
        return balance - shoppingCart.getTotalpriceForShoppingCart();
    }

    public void createOrder(ShoppingCart shoppingCart, String username, double balance) throws Exception {
        dataMapper.createOrder(shoppingCart, username, balance);
    }
    
    public List<Integer> getInvoicesFromDB(String username) {
        List<Integer> listInvoices = dataMapper.getInvoiceId(username);
        return listInvoices;
    }
    
     public List<Integer> getAllInvoicesFromDB() {
        List<Integer> listAllInvoices = dataMapper.getAllInvoiceId();
        return listAllInvoices;
    }
    
    public Invoice getInvoiceDetailsFromDB(int id) {
        Invoice invoice = dataMapper.getInvoiceDetails(id);
        return invoice;
    }
}
