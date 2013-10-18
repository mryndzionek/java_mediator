package com.mr.mediator.engine.core;

import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

import com.mr.mediator.engine.core.ifaces.Command;
import com.mr.mediator.engine.core.ifaces.Mediator;

public abstract class AbstractProducer<U,T> implements Callable<AbstractProducer.EXIT_STATUS> {

	public enum EXIT_STATUS {
		S_OK, S_FAILED	
	}

	protected Mediator<U,T> mediator;
	private String id;
	protected CyclicBarrier barrier;

	public AbstractProducer(String id, Mediator<U,T> mediator, CyclicBarrier barrier)
	{
		this.mediator = mediator;
		this.id = id;
		this.barrier = barrier;
	}

	public T push(Command<U,T> cmd) throws InterruptedException
	{
		T result = this.mediator.process(cmd);
		return result;
	}

	public String getID()
	{
		return this.id;
	}

}
