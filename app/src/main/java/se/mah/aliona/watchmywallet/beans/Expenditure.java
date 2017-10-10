package se.mah.aliona.watchmywallet.beans;

import java.io.Serializable;

/**
 * Created by aliona on 2017-09-11.
 */

public class Expenditure implements Serializable {
    private int expID;
    private String expTitle;
    private double expCost;
    private long expDate;
    private int expCatId;
    private String expCat;

    public Expenditure() { }

    public int getExpID() {
        return expID;
    }

    public void setExpID(int id) {
        this.expID = id;
    }

    public String getExpTitle() {
        return expTitle;
    }

    public void setExpTitle(String expTitle) {
        this.expTitle = expTitle;
    }

    public double getExpCost() {
        return expCost;
    }

    public void setExpCost(double expCost) {
        this.expCost = expCost;
    }

    public long getExpDate() {
        return expDate;
    }

    public void setExpDate(long expDate) {
        this.expDate = expDate;
    }

    public int getExpCatId() {
        return expCatId;
    }

    public void setExpCatId(int expCatId) {
        this.expCatId = expCatId;
    }

    public String getExpCat() {
        return expCat;
    }

    public void setExpCat(String expCat) {
        this.expCat = expCat;
    }
}
