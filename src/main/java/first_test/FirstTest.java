package first_test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import configuration.TestConfiguration;
import helper.FileHelper;

/**
 * Random test created to test Selenium setup.
 * 
 * @author karlomijaljevic
 */
public class FirstTest
{
	private static final String SIMPLE_TEST_URL = "https://www.javatpoint.com/";
	private static final String DOWNLOAD_TEST_URL = "https://sample-videos.com/download-sample-csv.php/";
	private static final String DOWNLOAD_PDF_TEST_URL = "https://file-examples.com/storage/feba2ccef1633048d9205d0/2017/10/file-sample_150kB.pdf";

	private WebDriver webDriver;

	/**
	 * The main {@link BeforeClass} annotation used to create the Downloads folder and 
	 * used to instantiate the {@link WebDriver} instance.
	 */
	@BeforeClass(description = "Main BeforeClass call. Creates the downloads folder and driver.")
	public void beforeTest()
	{
		assertTrue(FileHelper.setupDownloadDirectory(), "Failed to set up Downloads directory");

		webDriver = TestConfiguration.configureWebDriver();
		assertNotNull(webDriver, "ChromeDriver failed to initialize!");

		webDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));
		webDriver.manage().window().maximize();
	}

	@Test
	public void simpleSeleniumTest()
	{
		webDriver.get(SIMPLE_TEST_URL);

		String URL = webDriver.getCurrentUrl();
		System.out.print(URL);

		String title = webDriver.getTitle();
		System.out.println(title);
	}

	@Test
	public void fileDownloadTest()
	{
		webDriver.get(DOWNLOAD_TEST_URL);

		webDriver.findElement(By.partialLinkText("Click")).click();

		boolean isDownloaded = false;
		try
		{
			isDownloaded = FileHelper.isDownloadFilePresent("SampleCSVFile_2kb.csv", 10);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		assertEquals(isDownloaded, true);
	}

	@Test
	public void pdfFileDownloadAndCheck()
	{
		webDriver.get(DOWNLOAD_PDF_TEST_URL);

		boolean isDownloaded = false;
		try
		{
			isDownloaded = FileHelper.isDownloadFilePresent("file-sample_150kB.pdf", 10);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		assertEquals(isDownloaded, true);

		assertTrue(performPdfFileIntegrityCheck(), "PDF file not in proper order!");
	}

	/**
	 * Method to check generic PDF file that was downloaded by the pdfFileDownloadAndCheck()
	 * test method.
	 * 
	 * @return Return true if PDF file is in order, false otherwise.
	 */
	private boolean performPdfFileIntegrityCheck()
	{
		try
		{
			String pdfFileContent = FileHelper.readPdfFile("Downloads/file-sample_150kB.pdf");

			return pdfFileContent.contains("Vestibulum neque massa, scelerisque sit amet ligula eu, congue molestie mi.");
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * The main {@link AfterClass} annotation used to delete the support folders.
	 */
	@AfterClass(description = "Main AfterClass call. Deletes the support folders.")
	public void afterClass()
	{
		webDriver.quit();
		assertTrue(TestConfiguration.cleanSupprotFolders(), "Failed to delete the support folders!");
	}
}