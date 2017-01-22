package androidApps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class DownloadApps {
	public static void main(String args[]) throws Exception
	{
		//getAllAppsInfo();
		//getIndividualApps();
		//getGitHubURLs();
		getGitHubProjects();
	}

	//Get all the pages from F-droid
	public static void getAllAppsInfo() throws NoSuchAlgorithmException, KeyManagementException, IOException{
		for(int i=1;i<=37;i++){

			URL url=new URL("https://f-droid.org/repository/browse/?fdpage="+i);
			SSLContext sslctx = SSLContext.getInstance("TLS");
			TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
				public X509Certificate[] getAcceptedIssuers(){return null;}
				public void checkClientTrusted(X509Certificate[] certs, String authType){}
				public void checkServerTrusted(X509Certificate[] certs, String authType){}
			}};
			sslctx.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sslctx.getSocketFactory());
			//HttpsURLConnection yc = (HttpsURLConnection)url.openConnection();
			URLConnection yc = url.openConnection();
			BufferedReader in = null;
			try{
				in = new BufferedReader(
						new InputStreamReader(
								yc.getInputStream()));
			}
			catch(Exception e) {
				continue;
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("E:/Research Projects/ISSRE 2014 project/Data/AllAppsInfo/"+i+".html")));

			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				bw.write(inputLine+"\n");
			}

			in.close();
			bw.close();
		}
	}

	//Store the URLs of all apps
	public static void getIndividualApps() throws Exception {
		try {

			ArrayList<String> allMashups=new ArrayList<String>();
			final File folder = new File("E:/Research Projects/ISSRE 2014 project/Data/AllAppsInfo");
			final List<File> fileList = Arrays.asList(folder.listFiles());
			ArrayList<String> allURLs=new ArrayList<String>();
			//Get names of all mashups
			for(int i=0;i<fileList.size();i++)
			{
				final BufferedReader br=new BufferedReader(new FileReader(fileList.get(i)));
				String line="";
				StringBuilder builder=new StringBuilder();
				while((line=br.readLine())!=null)
				{
					if(line.startsWith("<a href=\"")){
						String url=line.substring(line.indexOf("<a href=\""),line.indexOf("><div",line.indexOf("<a href=\""))).
								replace("<a href=\"","").replace("\"","");
						System.out.println(url);
						allURLs.add(url);
					}
				}
			}

			getIndividualAppsData(allURLs);
		}catch (Exception e) {

			throw e;
		}
	}

	//Get the Individual pages of apps & store them
	public static void getIndividualAppsData(ArrayList<String> allURLs) throws KeyManagementException, IOException, NoSuchAlgorithmException {
		for(String str:allURLs){

			URL url=new URL(str);
			System.out.println(str);
			SSLContext sslctx = SSLContext.getInstance("TLS");
			TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
				public X509Certificate[] getAcceptedIssuers(){return null;}
				public void checkClientTrusted(X509Certificate[] certs, String authType){}
				public void checkServerTrusted(X509Certificate[] certs, String authType){}
			}};
			sslctx.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sslctx.getSocketFactory());
			//HttpsURLConnection yc = (HttpsURLConnection)url.openConnection();
			URLConnection yc = url.openConnection();
			BufferedReader in = null;
			try{
				in = new BufferedReader(
						new InputStreamReader(
								yc.getInputStream()));
			}
			catch(Exception e) {
				continue;
			}

			System.out.println(str);
			String name=str.substring(str.indexOf("fdid="),str.length()).replace("fdid=","");
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("E:/Research Projects/ISSRE 2014 project/Data/AllAppsData/"+name+".html")));

			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				bw.write(inputLine+"\n");
			}

			in.close();
			bw.close();
		}
	}

	//Get projects which are store on GitHub
	public static void getGitHubURLs() throws Exception {
		try {

			ArrayList<String> allMashups=new ArrayList<String>();
			final File folder = new File("E:/Research Projects/ISSRE 2014 project/Data/AllAppsData");
			final List<File> fileList = Arrays.asList(folder.listFiles());
			ArrayList<String> allURLs=new ArrayList<String>();
			PrintWriter bw = new PrintWriter(new FileWriter(new File("E:/Research Projects/ISSRE 2014 project/Data/GitHubURLs.txt")));
			//Get names of all mashups
			int count=0;

			for(int i=0;i<fileList.size();i++)
			{
				System.out.println(fileList.get(i));
				final BufferedReader br=new BufferedReader(new FileReader(fileList.get(i)));
				String line="";
				StringBuilder builder=new StringBuilder();
				while((line=br.readLine())!=null)
				{
					builder.append(line);
				}


				if(builder.toString().contains("Source Code:")){
					String temp=builder.substring(builder.indexOf("Source Code:"),builder.indexOf("\">",builder.indexOf("Source Code:")));
					if(temp.contains("https://github.com/")){
						String url=temp.replace("Source Code:</b> <a href=\"","");
						System.out.println(url);
						bw.write(url+"\n");
						count++;

						int start=builder.indexOf("</p></div>");
						int end=builder.indexOf("</p><p><b>License:",builder.indexOf("</p></div>"));
						if(end>start){
							String des=builder.substring(builder.indexOf("</p></div>"),builder.indexOf("</p><p><b>License:",builder.indexOf("</p></div>")));
							String description=des.replace("</p></div>","").replaceAll("</p>","").replaceAll("<p>","").replaceAll("</a>","")
									.replaceAll("<a>","").replaceAll("</ul>","").replaceAll("<ul>","").replaceAll("</li>","").replaceAll("<li>","");

							PrintWriter bw1=null;
							String[] split=url.split("/");
							if(split.length>4){
								bw1 = new PrintWriter(new FileWriter(new File("E:/Research Projects/ISSRE 2014 project/Data/AllAppsDesciption/"+split[3]+"_"+split[4]+".txt")));
							}
							else{
								bw1 = new PrintWriter(new FileWriter(new File("E:/Research Projects/ISSRE 2014 project/Data/AllAppsDesciption/"+split[3]+"_"+split[3]+".txt")));
							}
							bw1.write(description);
							bw1.close();
						}
					}
				}

			}
			bw.close();
			System.out.println(count);

		}catch (Exception e) {

			throw e;
		}
	}

	//Download GitHub Projects
	public static void getGitHubProjects() throws Exception {
		try {
			final BufferedReader br=new BufferedReader(new FileReader("E:/Research Projects/ISSRE 2014 project/Data/GitHubURLs.txt"));
			String line="";

			while((line=br.readLine())!=null)
			{

				URL url=new URL(line);
				System.out.println(line);
				SSLContext sslctx = SSLContext.getInstance("TLS");
				TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
					public X509Certificate[] getAcceptedIssuers(){return null;}
					public void checkClientTrusted(X509Certificate[] certs, String authType){}
					public void checkServerTrusted(X509Certificate[] certs, String authType){}
				}};
				sslctx.init(null, trustAllCerts, new SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sslctx.getSocketFactory());
				//HttpsURLConnection yc = (HttpsURLConnection)url.openConnection();
				URLConnection yc = url.openConnection();
				BufferedReader in = null;
				try{
					in = new BufferedReader(
							new InputStreamReader(
									yc.getInputStream()));
				}

				catch(Exception e) {
					continue;
				}

				boolean contains=false;
				String inputLine;
				while ((inputLine = in.readLine()) != null)
				{
					//if(inputLine.contains("build.xml")){
						contains=true;
					//}
				}


				if(contains){
					System.out.println("Yes Cloning");
					String[] command =
						{
							"cmd",
						};
					Process p = Runtime.getRuntime().exec(command);
					new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
					new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
					PrintWriter stdin = new PrintWriter(p.getOutputStream());

					stdin.println("E:");
					stdin.println("cd E:/Research Projects/ISSRE 2014 project/Data");
					String[] split=line.split("/");
					if(split.length>4){
						stdin.println("git clone "+line+" "+split[3]+"_"+split[4]);
					}
					else{
						stdin.println("git clone "+line+" "+split[3]+"_"+split[3]);
					}
					// write any other commands you want here
					stdin.close();
					int returnCode = p.waitFor();
					System.out.println("Return code = " + returnCode);
				}

			}
		}catch (Exception e) {

			throw e;
		}
	}
	
	//Get email of authors from log files
	public static void getAuthorEmails() throws IOException, InterruptedException{
		final File folder = new File("E:/Projects/ISSRE 2014/Repos");
		final List<File> fileList = Arrays.asList(folder.listFiles());
		
		for(int i=0;i<fileList.size();i++)
		{
			final BufferedReader br = new BufferedReader(new FileReader(fileList.get(i))); 
			
			String[] command =
				{
					"cmd",
				};
			Process p = Runtime.getRuntime().exec(command);
			new Thread(new SyncPipe(p.getErrorStream(), System.err)).start();
			new Thread(new SyncPipe(p.getInputStream(), System.out)).start();
			PrintWriter stdin = new PrintWriter(p.getOutputStream());

			stdin.println("E:");
			stdin.println("cd "+fileList.get(i));
			stdin.println("git log --pretty=format:\"%ae\" | sort | uniq");
			
			// write any other commands you want here
			stdin.close();
			int returnCode = p.waitFor();
			System.out.println("Return code = " + returnCode);
			
		}
	}
	
	public static void convertEmailToLowerCase() throws IOException{
		final BufferedReader br = new BufferedReader(new FileReader("E:/Projects/ISSRE 2014/Emails.txt")); 
		String inputLine;
		ArrayList<String> eMails=new ArrayList<String>();
		while ((inputLine = br.readLine()) != null)
		{
			if(!eMails.contains(inputLine.toLowerCase())){
				eMails.add(inputLine.toLowerCase());
				System.out.println(inputLine.toLowerCase());
			}
		}
	}
}
class SyncPipe implements Runnable
{
	public SyncPipe(InputStream istrm, OutputStream ostrm) {
		istrm_ = istrm;
		ostrm_ = ostrm;
	}
	public void run() {
		try
		{
			final byte[] buffer = new byte[1024];
			for (int length = 0; (length = istrm_.read(buffer)) != -1; )
			{
				ostrm_.write(buffer, 0, length);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	private final OutputStream ostrm_;
	private final InputStream istrm_;
}
