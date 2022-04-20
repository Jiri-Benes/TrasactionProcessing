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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Jiri Benes
 * @since 20.04.2022
 * class MarkTheOrder { public String markTheOrder (String fileLocation); }
 *
 */
public class MarkTheOrder {
	
	private static final char COMMA_DELIMITER = ',';
	private static final char PIPE_DELIMITER = '|';
	private static final char QUOTE_CHAR = '"';
	
	// transaction,provider/customer number,transaction datetime newline
	private static final int TRANSACTION_COLUMN = 0;
	private static final int PROVIDER_COLUMN = 1;
	private static final int DATETIME_COLUMN = 2;
	
	static Path inputFilePath; 
	static ArrayList<TransactionData> allTrasaction = new ArrayList<TransactionData>();
	static HashMap<String, ArrayList<LocalDateTime>> datesPerProvider = new HashMap<String, ArrayList<LocalDateTime>>();
	
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
		        //String[] values = line.split(COMMA_DELIMITER);
		        List<String> values = splitQuotedStr(line, COMMA_DELIMITER, QUOTE_CHAR, true);
		        if(values.size() < 3) {
		        	System.err.println("Wrong format of line("+i+"):"+line);
		        	continue;
		        }
		        String trasation = values.get(TRANSACTION_COLUMN);
		        trasation = trasation.trim();
		        String provider = values.get(PROVIDER_COLUMN);
		        provider = provider.trim();
		        String customerNumber="";
		        String[] values2 = provider.split("/");
		        if(values2.length>=2) {
		        	provider = values2[0].trim();
		        	customerNumber = values2[1].trim();
		        }		        
		        String dateTime = values.get(DATETIME_COLUMN);
		        dateTime = dateTime.trim();
		        //System.out.println(trasation+ ":" +provider+ ":" +customerNumber+ ":" +dateTime);
		        TransactionData td = new TransactionData();
		        td.originalOrder = i;
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
	    	if(datesPerProvider.containsKey(oneTrasaction.provider)) {
	    		ArrayList<LocalDateTime> forProvider = datesPerProvider.get(oneTrasaction.provider);
	    		forProvider.add(oneTrasaction.dateTimedate);
	    		datesPerProvider.put(oneTrasaction.provider, forProvider);
	    	} else {
	    		ArrayList<LocalDateTime> forProvider = new ArrayList<LocalDateTime>();
    			forProvider.add(oneTrasaction.dateTimedate);
    			datesPerProvider.put(oneTrasaction.provider, forProvider);	    			    		
	    	}
	    }
	    // sort per provider
	    for (String oneProvider : datesPerProvider.keySet()) {
	    	ArrayList<LocalDateTime> forOneProvider = datesPerProvider.get(oneProvider);
	    	Collections.sort(forOneProvider);
	    	datesPerProvider.put(oneProvider, forOneProvider);	    	
	    }		
	}
	
	private static String writeOutput() {
		//System.out.println();
		String out ="";
	    for (TransactionData oneTrasaction : allTrasaction) {
	    	// get transaction order
	        String order = "0"; 
	        ArrayList<LocalDateTime> forOneProvider = datesPerProvider.get(oneTrasaction.provider);
	        int i=1;
			for (LocalDateTime ldt : forOneProvider){
				if(ldt == oneTrasaction.dateTimedate) {
					order =  String.valueOf(i);
					oneTrasaction.trasactionOrderPerProvider = i;
 					break;
				}
				i++;
		    }		        
	        //System.out.println(oneTrasaction.provider +PIPE_DELIMITER+ order +PIPE_DELIMITER+ oneTrasaction.trasation);
	        out = out + oneTrasaction.provider +PIPE_DELIMITER+ order +PIPE_DELIMITER+ oneTrasaction.trasation+"\n";
	    }		
	    return out;
	}	

    public static List<String> splitQuotedStr(String str, char delimiter, char quote, boolean trim) {
        int startPos = 0;
        boolean insideQuotation = false;
        boolean wasQuoted = false;
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == quote) {
                insideQuotation = !insideQuotation;
                wasQuoted = true;
            }
            if (!insideQuotation && c == delimiter) {
            	String subStr;
            	if(wasQuoted) {
            		subStr = str.substring(startPos+1, i-1);
            	} else {
            		subStr = str.substring(startPos, i);
            	}
                if (trim) {
                    subStr = subStr.trim();
                }
                result.add(subStr);
                startPos = i + 1;
                wasQuoted = false;
                continue;
            }
        }
        if (startPos < str.length()) {
            String subStr = str.substring(startPos, str.length());
            if (trim) {
                subStr = subStr.trim();
            }
            result.add(subStr);
        }
        return result;
    }	
}

