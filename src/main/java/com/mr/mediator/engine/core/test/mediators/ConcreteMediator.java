package com.mr.mediator.engine.core.test.mediators;

import com.mr.mediator.engine.core.BaseMediator;
import com.mr.mediator.engine.core.MediatorDecorator;

public class ConcreteMediator<U,T> extends MediatorDecorator<U,T> {

	public ConcreteMediator(int queueSize) {
		super(new BaseMediator<U,T>(queueSize));
	}

	@Override
	public T computeResult() {
		return decoratedMediator.computeResult();
	}
}
