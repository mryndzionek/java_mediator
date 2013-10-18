package com.mr.mediator.engine.core;

import java.util.concurrent.CyclicBarrier;

import com.mr.mediator.engine.core.ifaces.Command;
import com.mr.mediator.engine.core.ifaces.Mediator;

public abstract class AbstractConsumer<U,T> implements Runnable {

	protected Mediator<U,T> mediator;
	private String id;
	protected CyclicBarrier barrier;

	public AbstractConsumer(String name, Mediator<U,T> mediator, CyclicBarrier barrier)
	{
		this.id = name;
		this.mediator = mediator;
		this.barrier = barrier;
	}

	public Command<U,T> pull() throws InterruptedException
	{
		Command<U,T> result = this.mediator.transfer();
		return result;
	}

	public String getID()
	{
		return this.id;
	}
}
