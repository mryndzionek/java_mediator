package com.mr.mediator.engine.core.ifaces;

public interface Mediator<U,T> {

	public T process(Command<U,T> cmd) throws InterruptedException;
	public Command<U,T> transfer() throws InterruptedException;
	public T computeResult();
	public void stopProducing();
	public boolean isProducing();
}
