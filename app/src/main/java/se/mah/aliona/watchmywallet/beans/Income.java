package se.mah.aliona.watchmywallet.beans;

import java.io.Serializable;

/**
 * Created by aliona on 2017-09-11.
 */

public class Income implements Serializable {
    private int incID;
    private String incTitle;
    private double incAmount;
    private long incDate;
    private int incCatId;
    private String incCat;

    public Income() {}

    public int getIncID() {
        return incID;
    }

    public void setIncID(int incID) {
        this.incID = incID;
    }

    public String getIncTitle() {
        return incTitle;
    }

    public void setIncTitle(String incTitle) {
        this.incTitle = incTitle;
    }

    public double getIncAmount() {
        return incAmount;
    }

    public void setIncAmount(double incAmount) {
        this.incAmount = incAmount;
    }

    public long getIncDate() {
        return incDate;
    }

    public void setIncDate(long incDate) {
        this.incDate = incDate;
    }

    public int getIncCatId() {
        return incCatId;
    }

    public void setIncCatId(int incCatId) {
        this.incCatId = incCatId;
    }


    public String getIncCat() {
        return incCat;
    }

    public void setIncCat(String incCat) {
        this.incCat = incCat;
    }
}
