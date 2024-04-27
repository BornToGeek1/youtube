package com.borntogeek.word;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class WordWriter {

	private File wordFile;
	private XWPFDocument document;

	public WordWriter(File wordFile) {
		this.wordFile = wordFile;
		this.document = new XWPFDocument();
	}

	public static void main(String[] args) throws Exception {
		File wordFile = Paths.get("").resolve("word.docx").toFile();
		WordWriter writer = new WordWriter(wordFile);
		writer.writeToWordFile();
		System.out.println("Word file created");
	}

	private void writeToWordFile() throws Exception {
		createTitle("Title");
		createParagraph("This is the first paragraph.");
		createParagraph("This is the second paragraph which is a longer than first one.");
		createImage("word_logo.png");

		writeDocumentToWordFile();
	}

	private void createTitle(String title) {
		XWPFParagraph titleParagraph = document.createParagraph();
		titleParagraph.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun titleRun = titleParagraph.createRun();
		titleRun.setText(title);
		titleRun.setColor("0000FF");
		titleRun.setBold(true);
		titleRun.setFontFamily("Calibri");
		titleRun.setFontSize(20);
	}

	private void createParagraph(String content) {
		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.LEFT);

		XWPFRun paragraphRun = paragraph.createRun();
		paragraphRun.setText(content);
		paragraphRun.setColor("000000");
		paragraphRun.setFontFamily("Times News Roman");
		paragraphRun.setFontSize(15);
	}

	private void createImage(String fileName) throws Exception {
		XWPFParagraph image = document.createParagraph();
		image.setAlignment(ParagraphAlignment.CENTER);
		
		XWPFRun imageRun = image.createRun();
		
		Path imagePath = Paths.get("").resolve(fileName);
		imageRun.addPicture(Files.newInputStream(imagePath), XWPFDocument.PICTURE_TYPE_PNG,
				imagePath.getFileName().toString(), Units.toEMU(100), Units.toEMU(100));
	}

	private void writeDocumentToWordFile() throws IOException {
		FileOutputStream outputStream = new FileOutputStream(wordFile);
		document.write(outputStream);
		outputStream.close();
		document.close();
	}
}
