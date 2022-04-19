/**
 * 
 */
package prog;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author locadmin
 * class MarkTheOrder { public String markTheOrder (String fileLocation); }
 *
 */
public class MarkTheOrder {
	
	private static final String COMMA_DELIMITER = ",";
	private static final String PIPE_DELIMITER = "|";
	
	// transaction,provider/customer number,transaction datetime newline
	private static final int TRANSACTION_COLUMN = 0;
	private static final int PROVIDER_COLUMN = 1;
	private static final int DATETIME_COLUMN = 2;
	
	static Path inputFilePath; 
	static ArrayList<TransactionData> allTrasaction = new ArrayList<TransactionData>();
	static HashMap<String, LocalDateTime> datesPerProvider = new HashMap<String, LocalDateTime>();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String firstArg = "";
		String mainFile;
		if(args.length>0) {
			firstArg = args[0];
		}
		if(firstArg.isEmpty()) {
			mainFile = "./input.txt";
		} else {
			mainFile = firstArg;
		}		
		MarkTheOrder mto = new MarkTheOrder();
		String out = mto.markTheOrder(mainFile);
		System.out.print(out);
	}
	
	public String markTheOrder(String fileLocation) {
		
		
		inputFilePath = Paths.get(fileLocation);
		
		//HashMap<String, Integer> countryMap = new HashMap<String, Integer>();
		//HashMap<String, Integer> countryOrderMap = new HashMap<String, Integer>();
				 
		
		readTrasaction();
		generateOrder();
		return writeOutput();
		
	}

	private static void readTrasaction() {
		//Charset charset = Charset.forName(StandardCharsets.UTF_8);
		try {
		    String line = null;
		    Integer i = 0;			
			BufferedReader reader = Files.newBufferedReader(inputFilePath, StandardCharsets.UTF_8); 
		    while ((line = reader.readLine()) != null) {
		    	i++;
		        //System.out.println(line);
		        String[] values = line.split(COMMA_DELIMITER);
		        if(values.length < 3) {
		        	System.err.println("Wrong format of line("+i+"):"+line);
		        	continue;
		        }
		        String trasation = values[TRANSACTION_COLUMN];
		        trasation = trasation.trim();
		        String provider = values[PROVIDER_COLUMN];
		        provider = provider.trim();
		        String customerNumber="";
		        String[] values2 = provider.split("/");
		        if(values2.length>=2) {
		        	provider = values2[0].trim();
		        	customerNumber = values2[1].trim();
		        }		        
		        String dateTime = values[DATETIME_COLUMN];
		        dateTime = dateTime.trim();
		        System.out.println(trasation+ ":" +provider+ ":" +customerNumber+ ":" +dateTime);
		        TransactionData td = new TransactionData();
		        td.order = i;
		        td.dateTime = dateTime;
		        td.provider = provider;
		        td.trasation = trasation;
		        td.converDate();		        
		        allTrasaction.add(td);
		        //System.out.println(td);
		        //System.out.println(td.dateTimedate);
		    }
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}			
	
	}
	
	private static void generateOrder() {
	    for (TransactionData oneTrasaction : allTrasaction) {
	        //System.out.println(i);
	        datesPerProvider.put(oneTrasaction.provider, oneTrasaction.dateTimedate);
	    }
		
	}
	
	private static String writeOutput() {
		System.out.println();
		String out ="";
	    for (TransactionData oneTrasaction : allTrasaction) {
	        datesPerProvider.put(oneTrasaction.provider, oneTrasaction.dateTimedate);
	        String order = "0"; 
	        //System.out.println(oneTrasaction.provider +PIPE_DELIMITER+ order +PIPE_DELIMITER+ oneTrasaction.trasation);
	        out = out + oneTrasaction.provider +PIPE_DELIMITER+ order +PIPE_DELIMITER+ oneTrasaction.trasation+"\n";
	    }		
	    return out;
	}	
}


