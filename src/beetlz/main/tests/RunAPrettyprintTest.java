import main.Beetlz;


public class RunAPrettyprintTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String current = System.getProperty("java.class.path");
		String[] parts = current.split("[:;]");
		
		String path = parts[0];
		if(path.endsWith("/bin")) {
			path = path.substring(0, path.length()-3);
			
			{
				String[] my_args = {
						"-source", "java", 
	                    //"-pureBON",
	                    //"-verbose",
	                    "-skeleton", 
	                    path + "tests/skeleton",
	                    //"-skeletonOneFile",
	                    //"-noNullCheck",
		                
		                "-userSettings", path + "tests/zoo/custom_zoo.txt",
		                "-files", path + "tests/zoo"
		                };
				
				final Beetlz checker = new Beetlz(my_args, System.err, System.out);
			    checker.run();
			}
			
			
			{
				String[] my_args = {
						"-source", "bon", 
	                    //"-pureBON",
	                    //"-verbose",
	                    "-skeleton", 
	                    path + "tests/skeleton",
	                    //"-skeletonOneFile",
	                    //"-noNullCheck",
		                
		                "-userSettings", path + "tests/zoo/custom_zoo.txt",
		                "-files", path + "tests/zoo"
		                };
				
				final Beetlz checker = new Beetlz(my_args, System.err, System.out);
			    checker.run();
			}
			
		} else {
			System.err.println("Error: classpath has wrong format.");
		}
		

	}

}
