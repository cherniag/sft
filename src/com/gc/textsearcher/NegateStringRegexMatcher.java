package com.gc.textsearcher;

public class NegateStringRegexMatcher implements Matcher {
	private String pattern;
	
	public NegateStringRegexMatcher(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public boolean match(Object value) {
		return !((String) value).matches(pattern);
	}

	@Override
	public String toString() {
		return "NegateStringRegexMatcher [pattern=" + pattern + "]";
	}
}
