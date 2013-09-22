package com.gc.textsearcher;

public class StringRegexMatcher implements Matcher {

	private String pattern;
	
	public StringRegexMatcher(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public boolean match(Object value) {
		return ((String) value).matches(pattern);
	}

	@Override
	public String toString() {
		return "StringRegexMatcher [pattern=" + pattern + "]";
	}
	
}
