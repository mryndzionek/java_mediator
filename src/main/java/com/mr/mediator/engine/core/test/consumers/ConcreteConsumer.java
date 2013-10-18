package com.mr.mediator.engine.core.test.consumers;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.mr.mediator.engine.core.AbstractConsumer;
import com.mr.mediator.engine.core.ifaces.Command;
import com.mr.mediator.engine.core.ifaces.Mediator;


public class ConcreteConsumer<U,T> extends AbstractConsumer<U,T> {

	public ConcreteConsumer(String name, Mediator<U,T> mediator, CyclicBarrier barrier) {
		super(name, mediator, barrier);
	}

	@Override
	public void run() {
		try
		{
			Command<U,T> cmd = mediator.transfer();

			while (mediator.isProducing() || cmd != null)
			{
				if(cmd!=null)
				{
					System.out.println("Consumer " + getID() + " processed data : " + cmd.getValue()+ " from: "+cmd.getSource());
				}
				cmd = mediator.transfer();
			}

			System.out.println("Consumer " + getID() + " finished its job; terminating.");
			barrier.await();
		}
		catch (InterruptedException ex)
		{
			ex.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}

	}

}
