package com.gc.textsearcher;

import java.util.ArrayList;

public class MatcherBuilder {
	private ArrayList<Matcher> orMatchers = new ArrayList<Matcher>();
	private ArrayList<Matcher> andMatchers = new ArrayList<Matcher>();
	private Matcher[][] matrix;

	public MatcherBuilder addOrMatcher(Matcher macther) {
		orMatchers.add(macther);
		return this;
	}

	public MatcherBuilder addAndMatcher(Matcher macther) {
		andMatchers.add(macther);
		return this;
	}

	public MatcherBuilder build() {
		int rowSize = orMatchers.size();
		int columnSize = andMatchers.size() + 1;
		if (rowSize == 0 && columnSize == 1) {
			return this;
		}
		if (rowSize == 0) {
			rowSize = 1;
		}
		matrix = new Matcher[rowSize][columnSize];
		if (orMatchers.isEmpty()) {
			matrix[0] = (Matcher[]) andMatchers.toArray();
		}
		for (int i = 0; i < orMatchers.size(); i++) {
			matrix[i][0] = orMatchers.get(i);
			for (int j = 0; j < andMatchers.size(); j++) {
				matrix[i][j + 1] = andMatchers.get(j);
			}
		}
		return this;
	}

	public boolean match(Object value) {
		boolean match = false;
		for (Matcher[] row : matrix) {
			match = true;
			for (Matcher matcher : row) {
				if(!matcher.match(value)){
					match = false;
					break;
				}
			}
			if(match){
				break;
			}
		}
		return match;
	}
}
