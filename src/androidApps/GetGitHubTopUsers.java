package androidApps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;

public class GetGitHubTopUsers {
	public static void main(String args[]) throws Exception
	{
		//getGitHubUsers();
		//checkEmails();
		testThread();
	}

	//Read the usernames of top contributors & get their names & emails.
	public static void getGitHubUsers() throws IOException{

		//GitHub github = GitHub.connectAnonymously();
		GitHub github = GitHub.connect("smusis", "d7a5a9a10e2d1f20e305e86ad205c4fe0ead3ed1");
		//github.connect("smusis2012", "d7a5a9a10e2d1f20e305e86ad205c4fe0ead3ed1");
		//System.out.println(github.getUser("smusis"));
		GHUser ghUser=new GHUser();
		FileWriter fw = new FileWriter("E:\\Research Projects\\ICSE 2016\\GitHubUsersFinal6.csv");
		BufferedWriter bw = new BufferedWriter(fw);

		ArrayList<String> list1Names = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader("E:\\Research Projects\\ICSE 2016\\List1.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] split=line.split(",");
				list1Names.add(split[1].trim());
			}
		}
		try (BufferedReader br = new BufferedReader(new FileReader("E:\\Research Projects\\ICSE 2016\\GitHubUsersFinal1.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] split=line.split(",");
				list1Names.add(split[1].trim());
			}
		}
		try (BufferedReader br = new BufferedReader(new FileReader("E:\\Research Projects\\ICSE 2016\\GitHubUsersFinal2.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] split=line.split(",");
				list1Names.add(split[1].trim());
			}
		}
		try (BufferedReader br = new BufferedReader(new FileReader("E:\\Research Projects\\ICSE 2016\\GitHubUsersFinal3.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] split=line.split(",");
				list1Names.add(split[1].trim());
			}
		}
		try (BufferedReader br = new BufferedReader(new FileReader("E:\\Research Projects\\ICSE 2016\\GitHubUsersFinal4.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] split=line.split(",");
				list1Names.add(split[1].trim());
			}
		}
		try (BufferedReader br = new BufferedReader(new FileReader("E:\\Research Projects\\ICSE 2016\\GitHubUsersFinal5.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] split=line.split(",");
				list1Names.add(split[1].trim());
			}
		}
		int count=0;
		int userCount=0;
		TreeMap<String, Integer> userNames=new TreeMap<String,Integer>();
		try (BufferedReader br = new BufferedReader(new FileReader("E:\\Research Projects\\ICSE 2016\\GitHubTopUsersAll.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				try{
					String [] split=line.split(",");
					if(split[0].equals("")||split[0] ==null){
						continue;
					}
					if(list1Names.contains(split[0].trim())){
						continue;
					}
					userNames.put(split[0],Integer.parseInt(split[1]));
					if(userCount==50000){
						break;
					}
					userCount++;
				}
				catch(Exception e){
					continue;
				}
			}
		}

		//Sort by value
		ValueComparator bvc =  new ValueComparator(userNames);
		TreeMap<String,Integer> sorted_map = new TreeMap<String,Integer>(bvc);
		sorted_map.putAll(userNames);

		boolean start=false;
		System.out.println("Done");
		for(Map.Entry<String,Integer> entry : sorted_map.entrySet()) {
			try{
				String user=entry.getKey();

				if(user.equals("ericclemmons")){
					start=true;
				}
				if(!start){
					continue;
				}
				Integer contri=entry.getValue();
				System.out.println(user);
				ghUser=github.getUser(user);
				//System.out.println(split[0]);
				Map<String, GHRepository> repos=ghUser.getRepositories();
				if(ghUser.getEmail()==null){
					continue;
				}
				if(ghUser.getEmail().equals("")){
					continue;
				}
				if(ghUser.getRepositories().size()<=5){
					continue;
				}
				if(ghUser.getFollowersCount()<=20){
					continue;
				}
				String name= ghUser.getName().replaceAll("[^\\x00-\\x7F]", "");
				if(name.contains("?")|| name.equals("")
						||name==null|| name.toLowerCase().contains("unofficial")){
					continue;
				}

				if(count==1000){
					break;
				}
				count++;
				System.out.println(ghUser.getName()+","+user+","+ghUser.getEmail()+","+contri);
				bw.write(ghUser.getName()+","+user+","+ghUser.getEmail()+","+contri);
				bw.write("\n");
			}

			catch(FileNotFoundException f){
				try {
					TimeUnit.MINUTES.sleep(10);                //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				continue;
			}
			//			catch(IOException n){
			//				try {
			//					TimeUnit.MINUTES.sleep(20);                //1000 milliseconds is one second.
			//				} catch(InterruptedException ex) {
			//					Thread.currentThread().interrupt();
			//				}
			//				continue;
			//			}

			catch(NullPointerException n){
				continue;
			}
			catch (Exception e) {
				try {
					TimeUnit.MINUTES.sleep(20);                //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				continue;
			}

		}
		bw.close();
		//ghUser=github.getUser("smusis");
		//System.out.println(ghUser.getEmail());
	}

	//To check if emails are proper or not
	public static void checkEmails() throws IOException{
		FileWriter fw = new FileWriter("E:\\Research Projects\\ICSE 2016\\FinalUsers5.csv");
		BufferedWriter bw = new BufferedWriter(fw);
		try (BufferedReader br = new BufferedReader(new FileReader("E:\\Research Projects\\ICSE 2016\\GitHubUsersFinal4.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] split=line.split(",");
				if(isValidEmailAddress(split[2])){
					bw.write(split[0]+","+split[1]+","+split[2]+","+split[3]);
					bw.write("\n");
				}
				else
				{
					System.out.println(split[2]);
				}
			}
		}
		bw.close();
	}

	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	public static void testThread() throws InterruptedException
	{
		System.out.println("Before Thread");
		//Thread.sleep(20000);
		System.out.println("After Thread");
		int i=0;
		while(i<10){
			try{
				System.out.println(i);
				i++;
				if(i==5){
					throw new FileNotFoundException();
				}
			}
			catch(Exception e){
				System.out.println("Exc");
				continue;
			}
		}
	}
}

class ValueComparator implements Comparator<String> {

	Map<String, Integer> base;
	public ValueComparator(Map<String, Integer> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with equals.    
	public int compare(String a, String b) {
		if (base.get(a) >= base.get(b)) {
			return -1;
		} else {
			return 1;
		} // returning 0 would merge keys
	}
}