package lamngo.financialtool.Transaction;

/**
 * Created by lamngo on 5.1.2018.
 */

public class TransactionEvent {
    private int transactionId;
    private long date;
    private String productName;
    private Double price;

    public TransactionEvent(int transactionId, long date, String productName, Double price) {
        this.transactionId = transactionId;
        this.date = date;
        this.productName = productName;
        this.price = price;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "TransactionEvent{" +
                "transactionId=" + transactionId +
                ", date=" + date +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                '}';
    }
}
