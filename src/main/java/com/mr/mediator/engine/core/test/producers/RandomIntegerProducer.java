package com.mr.mediator.engine.core.test.producers;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

import com.mr.mediator.engine.core.AbstractProducer;
import com.mr.mediator.engine.core.ifaces.Command;
import com.mr.mediator.engine.core.ifaces.Mediator;
import com.mr.mediator.engine.core.test.commands.ConcreteCommand;


public class RandomIntegerProducer<U,T> extends AbstractProducer<Integer,Integer> {

	private int len;

	public RandomIntegerProducer(String id, Mediator<Integer,Integer> mediator, CyclicBarrier barrier, int len) {
		super(id, mediator, barrier);
		this.len = len;
	}

	@Override
	public com.mr.mediator.engine.core.AbstractProducer.EXIT_STATUS call()
	throws Exception {

		try
		{
			Random rand = new Random();
			for (Integer i = 1; i < len+1; ++i)
			{
				Integer v = rand.nextInt(100);
				Command<Integer, Integer> cmd = new ConcreteCommand<Integer,Integer>(getID(), "dest", v);
				this.mediator.process(cmd);
				System.out.println("Producer "+getID()+" produced integer: "+v);
			}

			this.mediator.stopProducing();
			System.out.println("Producer "+getID()+" finished its job; terminating.");
		}
		catch (InterruptedException ex)
		{
			ex.printStackTrace();
		}

		return EXIT_STATUS.S_OK;
		
	}

}
