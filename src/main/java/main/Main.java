package main;

import org.testng.TestListenerAdapter;
import org.testng.TestNG;

import configuration.DriverConfiguration;
import first_test.FirstTest;

/**
 * The Main class. This class will be run once the .jar file is packaged.
 * It should be programmed to run all the test cases. While to run individual
 * TC's one should write up SUT (System Under Test) XML files. In case you have
 * trouble writing SUT files, simply right click on a test class and select
 * convert to TestNG class it will generate a appropriate SUT file for the given
 * class.
 * 
 * @implSpec
 * TODO: Please note that this for now only is set up to work with the ChromeDriver 
 * and not FirefoxDriver. FirefoxDriver will be added in the near future.
 * 
 * @author karlomijaljevic
 */
public class Main
{
	public static final String HELP_FLAG = "--help";

	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			printNoArgsStartMessage();
		}
		else if (args.length == 1 && args[0].equals(HELP_FLAG))
		{
			printHelp();
			System.exit(0);
		}
		else
		{
			setConfigurations(args);
		}

		DriverConfiguration.getInstance().setRunFromMainClass(true);

		TestListenerAdapter testListenerAdapter = new TestListenerAdapter();
		TestNG testng = new TestNG();

		testng.setTestClasses(new Class[] {FirstTest.class});
		testng.addListener(testListenerAdapter);
		testng.run();
	}

	/**
	 * Message to be printed when no arguments are passed to the program.
	 */
	private static void printNoArgsStartMessage()
	{
		StringBuilder output = new StringBuilder();

		output.append("=======================================================================================================");
		output.append("\n");
		output.append("\n").append("The tests will run in normal mode (windows will pop up). Please not that the");
		output.append("\n").append("application is setup for only ChromeDriver, in future releases we will add the");
		output.append("\n").append("FirefoxDriver setup as well.");
		output.append("\n").append("If you wish to run them in headless mode, then pass these arguments:");
		output.append("\n").append("--headless --window-size=1920,1200");
		output.append("\n");
		output.append("=======================================================================================================");

		System.out.println(output.toString());
	}

	/**
	 * Method prints the '--help' output.
	 */
	private static void printHelp()
	{
		StringBuilder output = new StringBuilder();

		output.append("=======================================================================================================");
		output.append("\n");
		output.append("\n").append("Any command line arguments you pass will behave as command line args");
		output.append("\n").append("passed to the chromedriver itself. For example if you pass '--headless'");
		output.append("\n").append("the chrome driver will run in headless mode (note that for the chromedriver)");
		output.append("\n").append("to run propperly in headless mode you need to also set the '--window-size'");
		output.append("\n").append("flag e.g. '--window-size=1920,1200'");
		output.append("\n");
		output.append("\n").append("--------------------- IMPORTANT ---------------------");
		output.append("\n").append("Please not that the application is setup for only ChromeDriver, in future releases");
		output.append("\n").append("we will add the FirefoxDriver setup as well.");
		output.append("\n");
		output.append("=======================================================================================================");

		System.out.println(output.toString());
		System.out.println();
	}

	/**
	 * Method sets the {@link DriverConfiguration} singleton webDriverOptions with appropriate
	 * parameters that have been passed down via the command line or the eclipse 
	 * JVM parameters list.
	 * 
	 * @param args -> A array of {@link String} object's that contains the parameters passed
	 * to the program.
	 */
	private static void setConfigurations(String[] args)
	{
		for (String parameter : args)
		{
			String parameterName = null;
			String parameterValue = null;

			if (parameter.contains("="))
			{
				parameterName = parameter.split("=")[0];
				parameterValue = parameter.split("=")[1];
			}
			else
			{
				parameterName = parameter;
			}

			DriverConfiguration.getInstance().getWebDriverOptions().put(parameterName, parameterValue);
		}
	}
}