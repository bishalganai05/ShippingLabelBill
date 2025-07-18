package com.bishal.slb.ShippingLabelBill.entity;

public class ShippingDetails {
    private String fromAddress;
    private String fromPIN;
    private String toAddress;
    private String toPIN;
    private String productID;
    private String productName;
    private String productType;
    private String emailID;

    public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public String getFromAddress() {
        return fromAddress;
    }
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }
    public String getFromPIN() {
        return fromPIN;
    }
    public void setFromPIN(String fromPIN) {
        this.fromPIN = fromPIN;
    }
    public String getToAddress() {
        return toAddress;
    }
    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }
    public String getToPIN() {
        return toPIN;
    }
    public void setToPIN(String toPIN) {
        this.toPIN = toPIN;
    }
    public String getProductID() {
        return productID;
    }
    public void setProductID(String productID) {
        this.productID = productID;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductType() {
        return productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }

    public ShippingDetails() {}

	public ShippingDetails(String fromAddress, String fromPIN, String toAddress, String toPIN, String productID,
			String productName, String productType, String emailID) {
		this.fromAddress = fromAddress;
		this.fromPIN = fromPIN;
		this.toAddress = toAddress;
		this.toPIN = toPIN;
		this.productID = productID;
		this.productName = productName;
		this.productType = productType;
		this.emailID = emailID;
	}
}

