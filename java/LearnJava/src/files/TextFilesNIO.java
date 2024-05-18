package files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TextFilesNIO {
	
	private static final String SMALL_FILE_PATH = "C:\\subdirectory\\small_file.txt";
	private static final String BIG_FILE_PATH = "C:\\subdirectory\\big_file.txt";
	
	public static void main(String[] args) throws IOException {
		// readFromSmallTxtFile();
		// readFromBigTxtFile();
		// writeToSmallTxtFile();
		// writeToBigTxtFile();
	}
	
	private static void readFromSmallTxtFile() throws IOException {
		Path path = Paths.get(SMALL_FILE_PATH);
		
		List<String> lines = Files.readAllLines(path);
		for (String line : lines) {
			System.out.println(line);
		}
	}
	
	private static void readFromBigTxtFile() throws IOException {
		Path path = Paths.get(BIG_FILE_PATH);
		
		try(BufferedReader reader = Files.newBufferedReader(path)) {
			String line;
			while((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		}
	}
	
	private static void writeToSmallTxtFile() throws IOException {
		Path path = Paths.get(SMALL_FILE_PATH);
		
		// For appending to existing text use third parameter: StandardOpenOption.APPEND
		Files.writeString(path, "New line\n");
	}
	
	private static void writeToBigTxtFile() throws IOException {
		Path path = Paths.get(BIG_FILE_PATH);
		
		// For appending to existing text use third parameter: StandardOpenOption.APPEND
		try(BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write("New line\n");
		}
	}
}
