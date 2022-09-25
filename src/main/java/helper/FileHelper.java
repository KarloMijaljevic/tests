package helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import configuration.ChromeDriverPreferences;

/**
 * Class used to help with file related operations for example
 * checking if a file is present in a given directory or creating
 * a directory.
 * 
 * @author karlomijaljevic
 */
public class FileHelper
{
	/**
	 * Setting up a Download directory is driver independent. Only
	 * depends on the OS the tests are running on.
	 * 
	 * @return Returns true on success and false on failure.
	 */
	public static boolean setupDownloadDirectory()
	{
		try
		{
			String downlaodPath = (String) ChromeDriverPreferences.DOWNLOAD_DEFAULT_DIRECTORY.getParameterValue();

			Path path = Paths.get(downlaodPath);

			if (!Files.exists(path))
			{
				File file = new File(downlaodPath);
				file.mkdirs();
			}

			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Method is used to delete the {@link ChromeDriverPreferences} download directory.
	 * 
	 * @return Returns true on success and false on failure.
	 */
	public static boolean deleteDownloadDirectory()
	{
		String downlaodPath = (String) ChromeDriverPreferences.DOWNLOAD_DEFAULT_DIRECTORY.getParameterValue();

		if (Files.exists(Paths.get(downlaodPath)))
		{
			File downloadsDirectory = new File(downlaodPath);

			for (File file : downloadsDirectory.listFiles())
			{
				file.delete();
			}

			return downloadsDirectory.delete();
		}

		return true;
	}

	/**
	 * Method is used to check the presence of a file in the {@link ChromeDriverPreferences} download
	 * directory.
	 * 
	 * @param fileName -> {@link String} object that contains the name of the file in question.
	 * @param iterations -> Number of iterations to go through while waiting for the file, every iteration
	 * pauses the thread for 5 seconds.
	 * 
	 * @return Returns true if file is present, false otherwise.
	 * 
	 * @throws InterruptedException in case the {@link Thread} got interrupted.
	 */
	public static boolean isDownloadFilePresent(String fileName, int iterations) throws InterruptedException
	{
		boolean isFilePresent = false;

		String downlaodPath = (String) ChromeDriverPreferences.DOWNLOAD_DEFAULT_DIRECTORY.getParameterValue();

		File dir = new File(downlaodPath);

		for (int waitForDownload = 0; waitForDownload < iterations; waitForDownload++)
		{
			for(File file : dir.listFiles())
			{
				if (file.getName().contains(fileName))
				{
					isFilePresent = true;
					break;
				}
			}

			if (isFilePresent)
			{
				break;
			}

			Thread.sleep(5000);
		}
		return isFilePresent;
	}

	/**
	 * Method uses the Apache PdfBox package to read PDF file contents.
	 * 
	 * @param filePath -> Path to the PDF file as {@link String}
	 * @return Returns the contents of the PDF file as a {@link String} object. In case
	 * it returned null that means that the PDF was encrypted.
	 * @throws IOException -> In case it could not read the PDF file.
	 */
	public static String readPdfFile(String filePath) throws IOException
	{
		String pdfFileInText = null;
		File pdfFile = new File(filePath);

		PDDocument document = Loader.loadPDF(pdfFile);

		document.getClass();

		if (!document.isEncrypted())
		{
			PDFTextStripperByArea stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(true);

			PDFTextStripper tStripper = new PDFTextStripper();

			pdfFileInText = tStripper.getText(document);
		}

		return pdfFileInText;
	}
}