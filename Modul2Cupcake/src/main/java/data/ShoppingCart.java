package data;

import java.util.ArrayList;

public class ShoppingCart {

    private double totalprice;
    private ArrayList<LineItem> arrLineItems = new ArrayList();
    
    /**
     * 
     * @param lineItem - Adds a lineItem object to the shopping cart
     */
    public void updateShoppingCart(LineItem lineItem) {
        arrLineItems.add(lineItem);
    }

    /**
     * 
     * @return - return a total price for the shoppingcart 
     */
    public double getTotalpriceForShoppingCart() {
        return totalprice;
    }

    /**
     * For each lineItem that exists in the arrayList 'arrLineItems' it adds it to the totalPrice variable. Expected result will always be a total price of all the lineItems in the array.
     */
    public void setTotalpriceForShoppingCart() {
        totalprice = 0.00;
        
        for (LineItem lineItem : arrLineItems) {
            totalprice += lineItem.getTotalprice();
        }
    }

    public ArrayList<LineItem> getArrLineItems() {
        return arrLineItems;
    }
    
    public ShoppingCart getShoppingCart(){
        return this;
    }

}
