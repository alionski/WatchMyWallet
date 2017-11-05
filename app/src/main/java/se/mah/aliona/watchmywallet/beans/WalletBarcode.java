package se.mah.aliona.watchmywallet.beans;

import java.io.Serializable;

/**
 * Bean representing a barcode. Used for the db.
 * Created by aliona on 2017-10-12.
 */

public class WalletBarcode implements Serializable {
    private int barcodeID;
    private long barcodeNumber;
    private String productName;
    private double initialPrice;

    public int getBarcodeID() {
        return barcodeID;
    }

    public void setBarcodeID(int barcodeID) {
        this.barcodeID = barcodeID;
    }

    public long getBarcodeNumber() {
        return barcodeNumber;
    }

    public void setBarcodeNumber(long barcodeNumber) {
        this.barcodeNumber = barcodeNumber;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(double initialPrice) {
        this.initialPrice = initialPrice;
    }
}
