package com.mr.mediator.engine.core.test;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.mr.mediator.engine.core.AbstractProducer.EXIT_STATUS;
import com.mr.mediator.engine.core.ifaces.Mediator;
import com.mr.mediator.engine.core.test.consumers.ConcreteConsumer;
import com.mr.mediator.engine.core.test.mediators.ConcreteMediator;
import com.mr.mediator.engine.core.test.producers.RandomIntegerProducer;


public class IntegerMediatorTest {

	public static final int POOL_SIZE = 10;
	public static final int QUEUE_SIZE = 100;

	public static void main(String[] args) {

		try {
			Mediator<Integer, Integer> mediator = new ConcreteMediator<Integer,Integer>(QUEUE_SIZE );

			ExecutorService threadPool = Executors.newFixedThreadPool(POOL_SIZE);
			
			CyclicBarrier csBarrier = new CyclicBarrier(10);
			for(int i=1; i<POOL_SIZE;i++)
				threadPool.execute(new ConcreteConsumer<Integer,Integer>("Consumer_"+i, mediator, csBarrier));

			CyclicBarrier prBarrier = new CyclicBarrier(1);
			Future<EXIT_STATUS> producerStatus = threadPool.submit(new RandomIntegerProducer<Integer,Integer>("Producer_1",mediator,prBarrier,10));

			producerStatus.get();
			threadPool.shutdown();
			csBarrier.await();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
