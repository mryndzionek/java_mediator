package com.mr.mediator.engine.core.test.helpers;

public class Value {

	private String id;
	private Integer value;

	public Value(String id, Integer value)
	{
		this.id = id;
		this.value = value;
	}

	public Integer getValue()
	{
		return this.value;
	}

	public String getID()
	{
		return this.id;
	}

}
