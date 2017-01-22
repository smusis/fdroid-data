package androidApps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetNameNEmails {
	public static void main(String args[]) throws Exception
	{
		getNamesEmails();
	}

	public static void getNamesEmails() throws IOException{
		final BufferedReader br=new BufferedReader(new FileReader("E:/Research Projects/ISSRE 2014 project/NameNEmails1Commits.txt"));
		TreeMap<String, String> nameNEmails=new TreeMap<String, String>();
		String inputLine="";
		String EMAIL_PATTERN = 
				"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
						+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		while ((inputLine = br.readLine()) != null)
		{
			String[] split=inputLine.split(";;;");

			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			Matcher matcher = pattern.matcher(split[1].toLowerCase().trim());
			if(matcher.matches()){
				if(!nameNEmails.containsKey(split[1].toLowerCase().trim())||
						!nameNEmails.containsValue(split[1].toLowerCase().trim())){
					nameNEmails.put(split[1].toLowerCase().trim(), split[0].trim());
				}
			}
		}

		TreeMap<String, String> uniqueNameNUniqueEmails=new TreeMap<String, String>();

		PrintWriter writer=new PrintWriter(new File("E:/Research Projects/ISSRE 2014 project/UniqueNameNEmails1Commits.txt"));
		for (Map.Entry<String, String> entry : nameNEmails.entrySet()) {
			uniqueNameNUniqueEmails.put(entry.getValue(), entry.getKey());
		}

		for (Map.Entry<String, String> entry : uniqueNameNUniqueEmails.entrySet()) {
			//if(entry.getKey().matches("\\w+")){
				writer.write(entry.getKey()+";;"+entry.getValue()+"\n");
			//}
		}

		writer.close();
	}
}
