package com.mr.mediator.engine.core.test.commands;

import java.net.Socket;


public class NewConnectionCommand extends ConcreteCommand<Socket,Integer>  {

	public NewConnectionCommand(String source, String destinantion,
			Socket initValue) {
		super(source, destinantion, initValue);
	}

}
