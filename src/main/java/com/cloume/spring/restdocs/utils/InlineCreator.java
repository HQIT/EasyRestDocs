package com.cloume.spring.restdocs.utils;

import java.util.function.Consumer;
import java.util.function.Function;

public class InlineCreator {

	static public <T> T create(T emptyBean, Function<T, T> initializer) {
		return initializer.apply(emptyBean);
	}
	
	static public <T> T create(T emptyBean, Consumer<T> initializer) {
		initializer.accept(emptyBean);
		return emptyBean;
	}
}
