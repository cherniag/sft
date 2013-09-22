package com.gc.textsearcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilesFinder {
	private List<File> fileRows = new ArrayList<File>();
	private MatcherBuilder matcherBuilder;

	public FilesFinder(MatcherBuilder matcherBuilder) {
		this.matcherBuilder = matcherBuilder;
	}

	public List<File> findFiles(File root) throws IOException {
		fileRows.clear();
		findFilesRecursive(root);
		return fileRows;
	}

	private void findFilesRecursive(File root) throws IOException {
		for (File f : root.listFiles()) {
			if (f.isDirectory() && f.listFiles().length > 0) {
				findFilesRecursive(f);
			} else {
				if (matcherBuilder.match(f.getAbsolutePath())) {
					fileRows.add(f);
				}
			}
		}
	}

}
