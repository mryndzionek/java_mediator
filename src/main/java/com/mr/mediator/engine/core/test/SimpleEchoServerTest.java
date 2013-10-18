package com.mr.mediator.engine.core.test;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.mr.mediator.engine.core.AbstractProducer.EXIT_STATUS;
import com.mr.mediator.engine.core.ifaces.Mediator;
import com.mr.mediator.engine.core.test.consumers.SimpleSocketConsumer;
import com.mr.mediator.engine.core.test.mediators.SocketMediator;
import com.mr.mediator.engine.core.test.producers.SimpleSocketProducer;

public class SimpleEchoServerTest {

	public static final int CONSUMERS = 10;
	public static final int PRODUCERS = 1;
	public static final int QUEUE_SIZE = 1000;

	private Mediator<Socket,Integer> mediator;
	private ExecutorService threadPool;
	private CyclicBarrier csBarrier;
	private CyclicBarrier prBarrier;

	private ArrayList<Future<EXIT_STATUS>>  producerStatuses = new ArrayList<Future<EXIT_STATUS>>();

	public SimpleEchoServerTest()
	{
		mediator = new SocketMediator<Socket,Integer>(QUEUE_SIZE);
		threadPool = Executors.newFixedThreadPool(CONSUMERS+PRODUCERS);
		csBarrier = new CyclicBarrier(CONSUMERS+1);	
		prBarrier = new CyclicBarrier(PRODUCERS);
	}

	public void start(int port)
	{
		for(int i=1; i<=CONSUMERS;i++)
			threadPool.execute(new SimpleSocketConsumer<Socket,Integer>("Consumer_"+i, mediator, csBarrier));

		producerStatuses.add(threadPool.submit(new SimpleSocketProducer<Socket,Integer>
		("ConnectionProducer",mediator,prBarrier, port)));
	}

	public void stop()
	{
		mediator.stopProducing();

		for(Future<EXIT_STATUS> f: producerStatuses)
		{
			try {
				f.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		threadPool.shutdown();
		try {
			csBarrier.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		SimpleEchoServerTest server = new SimpleEchoServerTest();

		long start = System.currentTimeMillis();

		server.start(4444);

		try {
			Thread.sleep(5000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			server.stop();
		}

		long stop = System.currentTimeMillis();

		System.out.println("Grinding took:"+(stop - start)+"ms");

	}

}
