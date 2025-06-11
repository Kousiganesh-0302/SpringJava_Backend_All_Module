package com.saasant.customerServiceSpring.vo;



import org.springframework.stereotype.Component;

@Component
public class CustomerDetails
{

	String customerId;
    String customerName;
    String mobileNumber;
    String customerLocation;

    public String getCustomerLocation() {
		return customerLocation;
	}

	public void setCustomerLocation(String customerLocation) {
		this.customerLocation = customerLocation;
	}
	
    public CustomerDetails() {
		super();
	}

	public CustomerDetails(String customerId, String customerName, String mobileNumber, String customerLocation)
    {

        this.customerId = customerId;
        this.customerName = customerName;
        this.mobileNumber = mobileNumber;
        this.customerLocation = customerLocation;

    }
	

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
