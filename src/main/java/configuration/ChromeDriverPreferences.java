package configuration;

import java.io.File;

import org.openqa.selenium.WebDriver;

/**
 * ChromeDriver options for the {@link WebDriver} instance. These are default
 * options needed for the {@link WebDriver} to function properly on any OS and
 * with CI/CD software e.g. Jenkins.
 * 
 * @implSpec
 * TODO: Maybe in future releases we can extract these configurations to a
 * property file or a JSON file.
 * 
 * @author karlomijaljevic
 */
public enum ChromeDriverPreferences
{
	PROFILE_DEFAULT_CONTENT_SETTINGS_POPUP("profile.default_content_settings.popups", 0),
	DOWNLOAD_PROPMPT_FOR_DOWNLOAD("download.prompt_for_download", "false"),
	DOWNLOAD_DEFAULT_DIRECTORY("download.default_directory", System.getProperty("user.dir") + File.separator + "Downloads");

	private String parameterName;
	private Object parameterValue;

	private ChromeDriverPreferences(String parameterName, Object parameterValue)
	{
		this.parameterName = parameterName;
		this.parameterValue = parameterValue;
	}

	public String getParameterName()
	{
		return this.parameterName;
	}

	public Object getParameterValue()
	{
		return this.parameterValue;
	}
}