package com.gc.textsearch;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.gc.textsearcher.MatcherBuilder;
import com.gc.textsearcher.NegateStringRegexMatcher;
import com.gc.textsearcher.StringRegexMatcher;

public class MatcherBuilderTest {
	@Test
	public void testMatcherBuilder() {
		MatcherBuilder matcherBuilder = new MatcherBuilder()
			.addOrMatcher(new StringRegexMatcher(".*doc"))
			.addOrMatcher(new StringRegexMatcher(".*txt"))
			.addAndMatcher(new NegateStringRegexMatcher(".*\\.git.*"))
			.addAndMatcher(new NegateStringRegexMatcher(".*target.*"))
			.build();
		assertTrue(matcherBuilder.match("C:\\dev\\test.doc"));
		assertTrue(matcherBuilder.match("C:\\dev\\test.txt"));
		assertFalse(matcherBuilder.match("C:\\.git\\test.doc"));
		assertFalse(matcherBuilder.match("C:\\.git\\test.txt"));
		assertFalse(matcherBuilder.match("C:\\test\\target\\test.doc"));
		assertFalse(matcherBuilder.match("C:\\test\\target\\test.txt"));
	}
}
