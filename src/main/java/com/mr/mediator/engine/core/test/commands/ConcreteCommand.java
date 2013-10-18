package com.mr.mediator.engine.core.test.commands;

import com.mr.mediator.engine.core.AbstractCommand;

public class ConcreteCommand<U,T> extends AbstractCommand<U,T> {

	private U value = null;
	private T result = null;
	private boolean hasResult = false;

	public ConcreteCommand(String source, String destinantion, U initValue) {
		super(source, destinantion);
		this.value = initValue;
	}

	@Override
	public boolean hasResult() {
		return hasResult;
	}

	@Override
	public U getValue() {
		return this.value;
	}

	@Override
	public T getResult() {
		return this.result;
	}

	@Override
	public void setResult(T result) {
		this.result = result;
		
	}
}
