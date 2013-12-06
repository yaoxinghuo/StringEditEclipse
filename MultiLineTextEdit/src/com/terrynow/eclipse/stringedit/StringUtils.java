package com.terrynow.eclipse.stringedit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.UnicodeEscaper;

public class StringUtils {
	public String doc;
	public int cursorPosition;
	int start;
	int end;
	int length;
	private String expression;
	String prefix;
	String blank;
	String name;
	String text;

	public boolean proccess() {
		String pattern = "[^\\\"\\n]*\\s*\"(?:[^\\\\\"]+|\\\\.)*\"(\\s*\\+\\s*\"(?:[^\\\\\"]+|\\\\.)*\")*\\s*";

		Matcher matcher = Pattern.compile(pattern).matcher(this.doc);

		while (matcher.find()) {
			if ((matcher.start() <= this.cursorPosition)
					&& (matcher.end() >= this.cursorPosition)) {
				this.start = matcher.start();
				this.end = matcher.end();

				this.expression = matcher.group();
				String patternStart = "\\\"";
				Matcher findString = Pattern.compile(patternStart).matcher(
						this.expression);

				findString.find();

				this.prefix = this.expression.substring(0,
						findString.start() - 1);
				this.expression = this.expression
						.substring(findString.start() - 1);

				Matcher blankMatcher = Pattern.compile("\\s*").matcher(
						this.prefix);

				if (blankMatcher.find())
					this.blank = blankMatcher.group();
				else {
					this.blank = "";
				}
				this.length = (this.expression.length() + this.prefix.length());

				this.name = String.valueOf(this.prefix.hashCode());
				this.text = getText(this.expression);

				return true;
			}
		}

		return false;
	}

	private static String getText(String exp) {
		StringBuilder text = new StringBuilder();

		String pattern = "\"(?:[^\\\\\"]+|\\\\.)*\"";
		Matcher matcher = Pattern.compile(pattern).matcher(exp);

		while (matcher.find()) {
			String it = matcher.group();
			text.append(StringEscapeUtils.unescapeJava(it.substring(1,
					it.length() - 1)));
		}

		return text.toString();
	}

	public String getEscapedString(boolean unicode) {
		StringBuilder builder = new StringBuilder();

		builder.append("\"");
		builder.append(escapeJava(this.text, unicode).replaceAll("\\\\n",
				"\\\\n\" +\n" + this.blank + "\t\"").replaceAll("\\\\r\\\\n",
				"\\\\n"));
		builder.append("\"");

		return builder.toString();
	}

	private String escapeJava(String str, boolean unicode) {
		CharSequenceTranslator tr = new LookupTranslator(new String[][] {
				{ "\"", "\\\"" }, { "\\", "\\\\" } })
				.with(new CharSequenceTranslator[] { new LookupTranslator(
						EntityArrays.JAVA_CTRL_CHARS_ESCAPE()) });
		if (unicode)
			tr = tr.with(new CharSequenceTranslator[] { UnicodeEscaper
					.outsideOf(32, 127) });
		return tr.translate(str);
	}

	public String getNewExpression(boolean unicode) {
		StringBuilder builder = new StringBuilder();

		String pattern = "\\\"";

		Matcher findString = Pattern.compile(pattern).matcher(this.expression);

		findString.find();

		builder.append(this.prefix);
		builder.append(this.expression.substring(0, findString.start()));
		builder.append(getEscapedString(unicode));

		return builder.toString();
	}

	public String getDoc() {
		return this.doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public int getCursorPosition() {
		return this.cursorPosition;
	}

	public void setCursorPosition(int cursorPosition) {
		this.cursorPosition = cursorPosition;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getName() {
		return this.name;
	}

	public int getStart() {
		return this.start;
	}

	public int getLength() {
		return this.length;
	}

	public void setLength(int length) {
		this.length = length;
	}
}