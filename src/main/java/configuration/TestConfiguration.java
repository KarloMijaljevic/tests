package configuration;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import driver.Driver;
import helper.FileHelper;

/**
 * Class used to help with configuration/execution of the {@link BeforeClass},
 * {@link BeforeTest}, {@link AfterClass} and {@link AfterTest} methods.
 * 
 * @author karlomijaljevic
 */
public class TestConfiguration
{
	/**
	 * This method should be called in every {@link BeforeTest} or
	 * {@link BeforeClass} method inside a test class.
	 * 
	 * @return Returns a valid {@link WebDriver} instance on success or NULL
	 * on failure.
	 */
	public static WebDriver configureWebDriver()
	{
		if (!DriverConfiguration.getInstance().isRunFromMainClass())
		{
			DriverConfiguration.getInstance().getWebDriverOptions().put("--headless", null);
			DriverConfiguration.getInstance().getWebDriverOptions().put("--window-size", "1920,1200");
			DriverConfiguration.getInstance().getWebDriverOptions().put("--incognito", null);
		}

		return Driver.getInstance().getWebDriver();
	}

	/**
	 * Use in {@link BeforeTest} or in {@link BeforeClass} methods when you have test cases
	 * that need to download files.
	 * 
	 * @return Returns true in case of success, and false otherwise.
	 */
	public static boolean configureDowloadDirectory()
	{
		return FileHelper.setupDownloadDirectory();
	}

	/**
	 * Method is used to delete any and all support folders that were created for the 
	 * test cases.
	 *  
	 * @return Returns true on success and false otherwise.
	 */
	public static boolean cleanSupprotFolders()
	{
		return Driver.getInstance().deleteDriver() && FileHelper.deleteDownloadDirectory();
	}
}