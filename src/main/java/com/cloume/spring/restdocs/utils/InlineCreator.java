package com.cloume.spring.restdocs.utils;

import java.util.function.Function;

public class InlineCreator {

	static public <T> T create(T emptyBean, Function<T, T> initializer) {
		return initializer.apply(emptyBean);
	}
}
