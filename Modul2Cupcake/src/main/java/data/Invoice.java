
package data;

import java.util.ArrayList;

public class Invoice {
    
    private int id;
    private double ordertotalprice;
    private String date;
    private ArrayList<InvoiceDetails> listInvoiceDetails = new ArrayList();

    public Invoice(int id, double ordertotalprice, String date) {
        this.id = id;
        this.ordertotalprice = ordertotalprice;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getOrdertotalprice() {
        return ordertotalprice;
    }

    public void setOrdertotalprice(double ordertotalprice) {
        this.ordertotalprice = ordertotalprice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<InvoiceDetails> getListInvoiceDetails() {
        return listInvoiceDetails;
    }

    public void setListInvoiceDetails(ArrayList<InvoiceDetails> listInvoiceDetails) {
        this.listInvoiceDetails = listInvoiceDetails;
    }
    
    public void updateInvoiceList(InvoiceDetails invoiceDetails){
        listInvoiceDetails.add(invoiceDetails);
    }
}
