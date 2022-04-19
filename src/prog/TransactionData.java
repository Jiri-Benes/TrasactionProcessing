package prog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TransactionData {
	int order;
	String trasation;
	String provider;
	String customerNumber;
	String dateTime;
	LocalDateTime dateTimedate;
	
	public void converDate() {
	    // 2015-09-01 12:00:00
	    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    dateTimedate = LocalDateTime.parse(dateTime, myFormatObj);	    
	}
}
