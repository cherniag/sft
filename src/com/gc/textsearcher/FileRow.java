package com.gc.textsearcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FileRow {
	private File file;
	private List<StringResult> stringResults = new ArrayList<FileRow.StringResult>();

	public List<StringResult> getStringResults() {
		return stringResults;
	}

	public FileRow(File file) throws IOException {
		this.file = file;
	}

	public boolean search(String toFind)
			throws FileNotFoundException, IOException {
		stringResults.clear();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		int i = 0;
		while((line=reader.readLine())!=null){
			i++;
			if(line.contains(toFind)){
				//System.out.println("FileRow.FileRow()"+line+":"+toFind);
				stringResults.add(new StringResult(i, line));
			}
		}
		reader.close();
		return !stringResults.isEmpty();
	}
	
	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}
	
	public class StringResult{
		public StringResult(int lineNumber, String lineValue) {
			this.lineNumber = lineNumber;
			this.lineValue = lineValue;
		}
		private int lineNumber;
		private String lineValue;
		
		public int getLineNumber() {
			return lineNumber;
		}
		public String getLineValue() {
			return lineValue;
		}
	}
}
