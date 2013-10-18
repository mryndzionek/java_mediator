package com.mr.mediator.engine.core;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.mr.mediator.engine.core.ifaces.Command;
import com.mr.mediator.engine.core.ifaces.Mediator;


public class BaseMediator<U,T> implements Mediator<U,T> {

	private ArrayBlockingQueue<Command<U,T>> queue = null;
	private volatile Boolean continueProducing = Boolean.TRUE;
	
	public BaseMediator(int queueSize)
	{
		this.queue = new ArrayBlockingQueue<Command<U,T>>(queueSize);
	}

	public T process(Command<U,T> cmd) throws InterruptedException
	{
		cmd.setResult(this.computeResult());
		this.queue.put(cmd);
		return cmd.getResult();
	}

	public Command<U,T> transfer() throws InterruptedException {
		return this.queue.poll(1, TimeUnit.SECONDS);
	}
	
	@Override
	public void stopProducing() {
		this.continueProducing = Boolean.FALSE;
	}

	@Override
	public boolean isProducing() {
		return this.continueProducing;
	}

	@Override
	public T computeResult() {
		return null;
	}
}
