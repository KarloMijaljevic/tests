package driver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Singleton;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;

import configuration.DriverConfiguration;

/**
 * Singleton class to help with the {@link WebDriver} setup. Looks for a driver
 * in the resources/drivers/ folder. Checks the {@link DriverConfiguration} singleton
 * for any driver configurations e.g. if it is headless or not.
 * 
 * @implNote
 * Note that this class will create a directory called 'Driver' next to the .jar
 * file or inside the Development folder in which it will place the ChromeDriver
 * instance. Please delete that instance in the main {@link AfterTest} method using
 * the method deleteDriver() after of course quitting it.
 * 
 * @author karlomijaljevic
 */
@Singleton
public class Driver
{
	private static final String SUPPRT_FOLDER = "Driver";
	private static final String DRIVERS_FOLDER = "drivers/";

	private static Driver instance;

	private WebDriver webDriver;

	private Driver()
	{
		setupDriver();
	}

	public static Driver getInstance()
	{
		if (instance == null)
		{
			instance = new Driver();
		}

		return instance;
	}

	/**
	 * Method extracts the driver from the resources/drivers/ directory. And sets it up
	 * as a default driver for tests.
	 * 
	 * @implNote
	 * The {@link WebDriver} will be set to NULL in case there is a {@link IOException}.
	 */
	private void setupDriver()
	{
		final String chromeDriverName = getChromeDriverName();

		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(DRIVERS_FOLDER + chromeDriverName);

		Path path = Paths.get(SUPPRT_FOLDER);
		if (!Files.exists(path))
		{
			File file = new File(SUPPRT_FOLDER);
			file.mkdirs();
		}

		String chromeSupprotDriverPath = SUPPRT_FOLDER + File.separator + chromeDriverName;
		path = Paths.get(chromeSupprotDriverPath);
		File chromeDriver = new File(chromeSupprotDriverPath);

		if (!Files.exists(path))
		{
			try
			{
				chromeDriver.createNewFile();
				FileUtils.copyURLToFile(resource, chromeDriver);
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
				setWebDriver(null);
				return;
			}
		}

		if (!chromeDriverName.contains("exe"))
		{
			Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
			perms.add(PosixFilePermission.OWNER_READ);
			perms.add(PosixFilePermission.OWNER_WRITE);
			perms.add(PosixFilePermission.OWNER_EXECUTE);
			perms.add(PosixFilePermission.GROUP_EXECUTE);
			perms.add(PosixFilePermission.GROUP_WRITE);
			perms.add(PosixFilePermission.GROUP_READ);
			perms.add(PosixFilePermission.OTHERS_READ);
			perms.add(PosixFilePermission.OTHERS_WRITE);
			perms.add(PosixFilePermission.OTHERS_EXECUTE);
			try
			{
				Files.setPosixFilePermissions(chromeDriver.toPath(), perms);
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
				setWebDriver(null);
				return;
			}
		}

		System.setProperty("webdriver.chrome.driver", chromeDriver.getAbsolutePath());

		HashMap<String, Object> chromePrefs = DriverConfiguration.getInstance().getWebDriverPreferences();

		ChromeOptions options = configureChromeOptions();

		options.setExperimentalOption("prefs", chromePrefs);

		setWebDriver(new ChromeDriver(options));
	}

	/**
	 * Method used to configure {@link ChromeOptions} for the {@link WebDriver}.
	 * 
	 * @return Returns a {@link ChromeOptions} object.
	 */
	private ChromeOptions configureChromeOptions()
	{
		ChromeOptions options = new ChromeOptions();
		List<String> optionsList = new ArrayList<String>();

		DriverConfiguration.getInstance().getWebDriverOptions().entrySet().forEach(entry -> {
			if (entry.getValue() == null)
			{
				optionsList.add(entry.getKey());
			}
			else
			{
				optionsList.add(entry.getKey() + "=" + entry.getValue());
			}
		});

		return options.addArguments(optionsList);
	}

	/**
	 * Method simply deletes the driver created from the resources/drivers/ instance
	 * which it had placed either next to a executable .jar or inside the development
	 * directory.
	 * 
	 * @return Returns true if the file was successfully deleted and false otherwise. Please
	 * use this boolean as a assertion in the last {@link AfterTest} so that we know that the
	 * file was cleaned.
	 */
	public boolean deleteDriver()
	{
		String chromeSupprotDriverPath = SUPPRT_FOLDER + File.separator + getChromeDriverName();

		if (Files.exists(Paths.get(chromeSupprotDriverPath)))
		{
			File chromeDriver = new File(chromeSupprotDriverPath);

			if (chromeDriver.delete())
			{
				File supportFolder = new File(SUPPRT_FOLDER);

				return supportFolder.delete();
			}
			else
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Method simply returns ChromeDriver name depending on the OS name. 
	 * Used to determine if the ChromeDriver tool has a .exe suffix or not.
	 * 
	 * @return Returns a ChromeDriver name as a {@link String} object.
	 */
	private String getChromeDriverName()
	{
		String osName = System.getProperty("os.name");

		if ("Linux".equals(osName))
		{
			return "chromedriver";
		}
		else if (osName.contains("Windows"))
		{
			return "chromedriver.exe";
		}
		else
		{
			return null;
		}
	}

	public WebDriver getWebDriver()
	{
		return webDriver;
	}

	private void setWebDriver(WebDriver webDriver)
	{
		this.webDriver = webDriver;
	}
}