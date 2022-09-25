package configuration;

import java.util.HashMap;

import javax.inject.Singleton;

import org.openqa.selenium.WebDriver;

/**
 * Singleton class that contains all {@link WebDriver} configuration.
 * This configuration is set up when calling the Main class.
 * 
 * @author karlomijaljevic
 */
@Singleton
public class DriverConfiguration
{
	private static DriverConfiguration instance;

	private HashMap<String, String> webDriverOptions;
	private HashMap<String, Object> webDriverPreferences;
	private boolean runFromMainClass = false;

	private DriverConfiguration()
	{
		setWebDriverOptions(new HashMap<String, String>());
		setWebDriverPreferences(new HashMap<String, Object>());
		loadDefaultPreferences();
	}

	public static DriverConfiguration getInstance()
	{
		if (instance == null)
		{
			instance = new DriverConfiguration();
		}

		return instance;
	}

	/**
	 * Method sets the {@link DriverConfiguration} singleton webDriverOptions with appropriate
	 * parameters. Those parameters are set as default for a given {@link WebDriver}. The
	 * configurations are loaded from the ENUM class {@link ChromeDriverPreferences}.
	 */
	private void loadDefaultPreferences()
	{
		for (ChromeDriverPreferences cdp : ChromeDriverPreferences.values())
		{
			String key = cdp.getParameterName();
			Object value = cdp.getParameterValue();

			getWebDriverPreferences().put(key, value);
		}
	}

	public HashMap<String, String> getWebDriverOptions()
	{
		return webDriverOptions;
	}

	private void setWebDriverOptions(HashMap<String, String> webDriverOptions)
	{
		this.webDriverOptions = webDriverOptions;
	}

	public HashMap<String, Object> getWebDriverPreferences()
	{
		return webDriverPreferences;
	}

	public void setWebDriverPreferences(HashMap<String, Object> webDriverPreferences)
	{
		this.webDriverPreferences = webDriverPreferences;
	}

	public boolean isRunFromMainClass()
	{
		return runFromMainClass;
	}

	public void setRunFromMainClass(boolean runFromMainClass)
	{
		this.runFromMainClass = runFromMainClass;
	}
}