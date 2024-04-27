package com.borntogeek.word;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class WordReader {

	private XWPFDocument document;

	private WordReader(File wordFile) throws FileNotFoundException, IOException {
       this.document = new XWPFDocument(new FileInputStream(wordFile));
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		File wordFile = Paths.get("").resolve("word.docx").toFile();
		WordReader reader = new WordReader(wordFile);
		reader.readFromWordFile();
	}
	
	private void readFromWordFile() throws IOException {
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		
		 for (XWPFParagraph paragraph : paragraphs) {
             System.out.println(paragraph.getText());
         }
		 
		 document.close();
	}

}
