/*******************************************************************************
 * Copyright (c) 2009, 2011 Mountainminds GmbH & Co. KG and Contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *    
 *******************************************************************************/
package org.jboss.errai.gwtmaven.agent;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Matches strings against <code>?</code>/<code>*</code> wildcard expressions.
 * Multiple expressions can be separated with a colon (:). In this case the
 * expression matches if at least one part matches.
 * <p>
 * <b>WARNING: to keep this package both self-contained and compatible with
 * JaCoCo's own agent wildcardsm this class was copied from the
 * org.jacoco.agent.rt project. Don't modify this class.
 */
public class WildcardMatcher {

	private final Pattern pattern;

	/**
	 * Creates a new matcher with the given expression.
	 * 
	 * @param expression
	 *            wildcard expressions
	 */
	public WildcardMatcher(final String expression) {
		final String[] parts = expression.split("\\:");
		final StringBuilder regex = new StringBuilder(expression.length() * 2);
		boolean next = false;
		for (final String part : parts) {
			if (next) {
				regex.append('|');
			}
			regex.append('(').append(toRegex(part)).append(')');
			next = true;
		}
		pattern = Pattern.compile(regex.toString());
	}

	private static CharSequence toRegex(final String expression) {
		final StringBuilder regex = new StringBuilder(expression.length() * 2);
		final StringTokenizer st = new StringTokenizer(expression, "?*", true);
		while (st.hasMoreTokens()) {
			final String token = st.nextToken();
			if ("?".equals(token)) {
				regex.append(".?");
			} else if ("*".equals(token)) {
				regex.append(".*");
			} else {
				regex.append(Pattern.quote(token));
			}
		}
		System.out.println("Created regex for expression: " + regex);
		return regex;
	}

	/**
	 * Matches the given string against the expressions of this matcher.
	 * 
	 * @param s
	 *            string to test
	 * @return <code>true</code>, if the expression matches
	 */
	public boolean matches(final String s) {
		return pattern.matcher(s).matches();
	}

}
