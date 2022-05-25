package com.github.jh3nd3rs0n.jargyle.client.internal.propertyspec.impl;

import com.github.jh3nd3rs0n.jargyle.client.Property;
import com.github.jh3nd3rs0n.jargyle.client.PropertySpec;
import com.github.jh3nd3rs0n.jargyle.common.text.Words;

public final class WordsPropertySpec extends PropertySpec<Words> {

	public WordsPropertySpec(final String s, final Words defaultVal) {
		super(s, Words.class, defaultVal);
	}

	@Override
	public Property<Words> newPropertyOfParsableValue(final String value) {
		return super.newProperty(Words.newInstance(value));
	}

}
