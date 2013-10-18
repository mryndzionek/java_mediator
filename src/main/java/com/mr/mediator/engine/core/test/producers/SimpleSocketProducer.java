package com.mr.mediator.engine.core.test.producers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CyclicBarrier;

import com.mr.mediator.engine.core.AbstractProducer;
import com.mr.mediator.engine.core.ifaces.Command;
import com.mr.mediator.engine.core.ifaces.Mediator;
import com.mr.mediator.engine.core.test.commands.NewConnectionCommand;
import com.mr.mediator.engine.core.test.mediators.SocketMediator;

public class SimpleSocketProducer<U,T> extends AbstractProducer<Socket,Integer> {

	private int port;
	
	public SimpleSocketProducer(String id, Mediator<Socket, Integer> mediator,
			CyclicBarrier barrier, int port) {
		super(id, mediator, barrier);
		this.port = port;
	}

	@Override
	public EXIT_STATUS call()
	throws Exception {

		final ServerSocket serverSocket = new ServerSocket(port);
		((SocketMediator<Socket, Integer>) mediator).addSocket(serverSocket);
		try {
			while(true)
			{
				final Socket clientSocket = serverSocket.accept();

				Command<Socket,Integer> cmd = new NewConnectionCommand(getID(), "CONNECTION_CONSUMER", clientSocket);
				this.mediator.process(cmd);
			}

		}
		catch (SocketException ex)
		{
			//This is normal - graceful shutdown
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		catch (InterruptedException ex)
		{
			ex.printStackTrace();
		} finally {
			serverSocket.close();
			barrier.await();
			if(this.mediator.isProducing())
				this.mediator.stopProducing();
			System.out.println("Cycle producer "+getID()+" finished its job; terminating.");
		}

		return EXIT_STATUS.S_OK;
	}

}
