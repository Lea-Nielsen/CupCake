
package data;

public class InvoiceDetails {
    
    private int qty;
    private String name;
    private double price, totalprice;

    public InvoiceDetails(int qty, String name, double price, double totalprice) {
        this.qty = qty;
        this.name = name;
        this.price = price;
        this.totalprice = totalprice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(double totalprice) {
        this.totalprice = totalprice;
    }
    
    
}
