package lamngo.financialtool.Transaction;

/**
 * Created by lamngo on 5.1.2018.
 */

public class TransactionEvent {
    /** TransactionActivity ID */
    public int transactionId;

    /** TransactionActivity date in milliseconds*/
    public long date;

    /** TransactionActivity product name */
    public String productName;


    public TransactionEvent(int transactionId, long date, String productName) {
        this.transactionId = transactionId;
        this.date = date;
        this.productName = productName;
    }

    public long getTransactionId() {
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

    @Override
    public String toString() {
        return "TransactionEvent{" +
                "transactionId=" + transactionId +
                ", date=" + date +
                ", productName='" + productName + '\'' +
                '}';
    }
}
