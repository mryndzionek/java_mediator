package com.mr.mediator.engine.core.ifaces;

import java.util.UUID;

public interface Command<U,T> {
	
	public T getResult();
	public void setResult(T result);
	public boolean hasResult();
	
	public U getValue();
	
	public String getSource();
	public String getDestination();
	public long getTimestamp();
	public UUID getUUID();

}
