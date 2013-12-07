Mediator design pattern implemented in Java
===========================================

Introduction
------------

The Medaitor design pattern is like a two-way Observer. This implementation is aimed at solving the **producer-consumer problem** in simple yet efficient manner using `java.util.concurrent` package.

Examples
--------

This is an simple multithreaded TCP echo server. It supports graceful shutdown and producers end statuses retrieval using Java Futures.

```java
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
```


Create Eclipse project files using Gradle
-----------------------------------------

To build Eclipse project files you need [Gradle](http://www.gradle.org/). 
Use the command:

```sh
gradle eclipse
```

You must also set the GRADLE_USER_HOME variable in Eclipse (Window->Preferences->Java->Build Path->Classpath Variable). Set it to the path of the .gradle folder in your home directory.

Contact
-------
If you have questions, contact Mariusz Ryndzionek at:

<mryndzionek@gmail.com>
