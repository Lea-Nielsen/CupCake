package data;

import java.util.ArrayList;

public class ShoppingCart {

    private double totalprice;
    private ArrayList<LineItem> arrLineItems = new ArrayList();
    
    public void updateShoppingCart(LineItem lineItem) {
        arrLineItems.add(lineItem);
    }

    public double getTotalpriceForShoppingCart() {
        return totalprice;
    }

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
