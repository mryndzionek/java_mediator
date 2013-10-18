package com.mr.mediator.engine.core;

import com.mr.mediator.engine.core.ifaces.Command;
import com.mr.mediator.engine.core.ifaces.Mediator;


public abstract class MediatorDecorator<U,T> implements Mediator<U,T> {

	protected Mediator<U,T> decoratedMediator;

	public MediatorDecorator(Mediator<U,T> mediator)
	{
		this.decoratedMediator = mediator;
	}

	@Override
	public T process(Command<U, T> cmd) throws InterruptedException {
		return decoratedMediator.process(cmd);
	}

	@Override
	public Command<U, T> transfer() throws InterruptedException {
		return decoratedMediator.transfer();
	}

	@Override
	public T computeResult() {
		return decoratedMediator.computeResult();
	}

	@Override
	public void stopProducing() {
		decoratedMediator.stopProducing();
	}

	@Override
	public boolean isProducing() {
		return decoratedMediator.isProducing();
	}

}
