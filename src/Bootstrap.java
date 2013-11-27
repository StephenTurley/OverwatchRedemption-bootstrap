import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;



public class Bootstrap {


	private static String OS = System.getProperty("os.name").toLowerCase();
	private static String installRoot;
	private final static String GET_DOWN = "getdown-1.3.1.jar";
	
 
	public static void main(String[] args) {
 
		System.out.println(OS);
 
		if (isWindows()) {
			System.out.println("This is Windows");
			installRoot = System.getenv("AppData") +File.separator+ ".overwatch" + File.separator ;
		} else if (isMac()) {
			installRoot = System.clearProperty("user.home");
			System.out.println("This is Mac");
		} else if (isUnix()) {
			installRoot = System.clearProperty("user.home") +File.separator+ ".overwatch" + File.separator ; //not dry
			System.out.println("This is Unix or Linux");
		} else if (isSolaris()) {
			System.out.println("This is Solaris");
		} else {
			System.out.println("Your OS is not support!!");
		}
		
		//check java and have user install if missing
		if(installRoot != null)
		{
			try
			{
				checkAndLaunch();
			}
			catch(Exception e)
			{
				System.err.println(e.getMessage());
			}
		}
		else
		{
			System.exit(-1);
		}
	}
	private static void checkAndLaunch()throws Exception
	{
		File file = new File(installRoot+ GET_DOWN);
		if(file.exists())
		{
			launch();
		}
		else
		{
			file.getParentFile().mkdirs();

			InputStream getDownStream = Bootstrap.class.getResourceAsStream(GET_DOWN);
			if(getDownStream == null){
				throw new Exception("Can't locate getDown in jar");
			}
			
			InputStream getDownTxtStream = Bootstrap.class.getResourceAsStream("getdown.txt");
			if(getDownTxtStream == null){
				throw new Exception("Can't locate getDown.txt in jar");
			}
			
			OutputStream getDownJar = new FileOutputStream(file);
			OutputStream getDownTxt = new FileOutputStream(file.getParentFile()+"/getdown.txt");
			
			int readBytes;
			byte[] buffer = new byte[4096];
			
			try
			{	  
		        while ((readBytes = getDownStream.read(buffer)) > 0) {
		            getDownJar.write(buffer, 0, readBytes);
		        }
		        
		        readBytes = 0;
		        buffer = new byte[4096];
		        
		        while ((readBytes = getDownTxtStream.read(buffer)) > 0) {
		            getDownTxt.write(buffer, 0, readBytes);
		        }
			}
			catch(Exception e)
			{
				System.err.println(e.getMessage());
			}
			finally 
			{
		        getDownStream.close();
		        getDownTxtStream.close();
		        
		        getDownJar.close();
		        getDownTxt.close();
		        
		        launch();
		    }
		}
	}
	
	private static void launch()
	{
		try
		{
			String[] cmd = {"java","-jar", installRoot+ GET_DOWN, installRoot};
			Runtime r = Runtime.getRuntime();
			r.exec(cmd);
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
 
	public static boolean isWindows() {
 
		return (OS.indexOf("win") >= 0);
 
	}
 
	public static boolean isMac() {
 
		return (OS.indexOf("mac") >= 0);
 
	}
 
	public static boolean isUnix() {
 
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
 
	}
 
	public static boolean isSolaris() {
 
		return (OS.indexOf("sunos") >= 0);
 
	}
	
	
}
