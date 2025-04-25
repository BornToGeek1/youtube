package com.borntogeek.gmail_reader;

public class FileByteData {
	
    private final String filename;
    private final byte[] data;

    public FileByteData(String filename, byte[] data) {
        this.filename = filename;
        this.data = data;
    }

    public String getFilename() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }
}
