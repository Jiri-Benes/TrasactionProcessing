package prog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TransactionData {
	int originalOrder;
	String trasation;
	String provider;
	String customerNumber;
	String dateTime;
	LocalDateTime dateTimedate;
	int trasactionOrderPerProvider;
	
	public void converDate() {
	    // 2015-09-01 12:00:00
	    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    try {
	    	dateTimedate = LocalDateTime.parse(dateTime, myFormatObj);
	    } catch (Exception e) {
			// TODO: handle exception
	    	dateTimedate = LocalDateTime.now();
	    	System.err.println("Cannot convert date "+dateTime+ " on line " +originalOrder);
		} 
	}
}
