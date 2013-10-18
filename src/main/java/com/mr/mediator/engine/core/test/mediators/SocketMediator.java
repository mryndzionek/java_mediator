package com.mr.mediator.engine.core.test.mediators;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import com.mr.mediator.engine.core.BaseMediator;
import com.mr.mediator.engine.core.MediatorDecorator;
import com.mr.mediator.engine.core.ifaces.Command;


public class SocketMediator<U,T> extends MediatorDecorator<Socket,Integer> {

	private Vector<Socket> conList = new Vector<Socket>();
	private Vector<ServerSocket> srvList = new Vector<ServerSocket>();

	public SocketMediator(int queueSize) {
		super(new BaseMediator<Socket, Integer>(queueSize));
	}

	@Override
	public Integer process(Command<Socket,Integer> cmd) throws InterruptedException
	{
		conList.add(cmd.getValue());
		return decoratedMediator.process(cmd);
	}

	@Override
	public Integer computeResult() {
		return decoratedMediator.computeResult();
	}

	@Override
	public synchronized void stopProducing() {
		decoratedMediator.stopProducing();
		//Close all client sockets
		synchronized(conList)
		{
			for(Socket s:conList)
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		synchronized(srvList)
		{
			for(ServerSocket s:srvList)
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public void removeSocket(Socket socket)
	{
		conList.remove(socket);
	}

	public void addSocket(ServerSocket serverSocket) 
	{
		srvList.add(serverSocket);
	}

}
