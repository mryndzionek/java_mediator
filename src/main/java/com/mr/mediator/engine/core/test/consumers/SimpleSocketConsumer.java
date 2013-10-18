package com.mr.mediator.engine.core.test.consumers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import com.mr.mediator.engine.core.AbstractConsumer;
import com.mr.mediator.engine.core.ifaces.Command;
import com.mr.mediator.engine.core.ifaces.Mediator;
import com.mr.mediator.engine.core.test.commands.NewConnectionCommand;
import com.mr.mediator.engine.core.test.mediators.SocketMediator;


public class SimpleSocketConsumer<U,T> extends AbstractConsumer<Socket,Integer> {

	private int timeout = 0;

	public SimpleSocketConsumer(String name,
			Mediator<Socket, Integer> mediator, CyclicBarrier barrier) {
		super(name, mediator, barrier);
	}

	public SimpleSocketConsumer(String name,
			Mediator<Socket, Integer> mediator, CyclicBarrier barrier, int timeout) {
		super(name, mediator, barrier);
		this.timeout  = timeout;
	}

	@Override
	public void run() {

		try
		{
			Command<Socket, Integer> cmd = mediator.transfer();

			while (mediator.isProducing() || cmd != null)
			{
				if(cmd!=null)
				{
					if(cmd instanceof NewConnectionCommand)
					{
						System.out.println("Consumer " + getID() + " received NewConnection command: " + cmd.getValue()+ " from: "+cmd.getSource());
						Socket clientSocket = ((NewConnectionCommand) cmd).getValue();
						try {
							if(timeout>0)
								clientSocket.setSoTimeout(timeout);


							BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
							PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

							try {
								out.println("Hello! ....");
								out.println("Enter BYE to exit.");
								out.flush();

								while (true) {
									String str = in.readLine();
									if (str == null) {
										System.out.println("Connection "+clientSocket+" closed by client");
										break; // client closed connection
									} else {
										out.println("Echo: " + str);
										out.flush();
										if (str.trim().equals("BYE"))
											break;
									}
								}

							} catch (SocketTimeoutException e){
								// Ignore the socket timeout - it is normal
							} catch (SocketException e){
								//This is normal - graceful shutdown
							} catch (IOException e) {
								e.printStackTrace();
							} finally {
								try {
									out.close();
									in.close();
									clientSocket.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}

						} catch (SocketException e){
							//This is normal - graceful shutdown
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try {
								clientSocket.close();
							} catch (IOException e) {
								e.printStackTrace();
							} finally {
								((SocketMediator<Socket, Integer>) mediator).removeSocket(clientSocket);
							}
						}

					}
					else
						System.err.println("Consumer " + getID()+" received invalid command");
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
