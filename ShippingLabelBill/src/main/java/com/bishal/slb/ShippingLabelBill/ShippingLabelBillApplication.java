package com.bishal.slb.ShippingLabelBill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShippingLabelBillApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShippingLabelBillApplication.class, args);
	}

}
