package com.mr.mediator.engine.core;

import java.util.UUID;

import com.mr.mediator.engine.core.ifaces.Command;

public abstract class AbstractCommand<U,T> implements Command<U,T> {

	private String src;
	private String dst;
	private long timestamp;
	private UUID uuid;

	public AbstractCommand(String source, String destinantion)
	{
		this.src = source;
		this.dst = destinantion;
		this.timestamp = System.currentTimeMillis();
		this.uuid = UUID.randomUUID();
	}

	public String getSource()
	{
		return this.src;
	}

	public String getDestination()
	{
		return this.dst;
	}

	public long getTimestamp()
	{
		return this.timestamp;
	}
	
	public UUID getUUID()
	{
		return this.uuid;
	}

}
