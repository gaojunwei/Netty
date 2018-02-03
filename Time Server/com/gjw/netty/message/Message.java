package com.gjw.netty.message;

import java.nio.charset.Charset;

public class Message {
	private byte[] startFlage = new byte[2];
	private int bodyLength = 0;
	byte[] bodyData = null;
	private byte[] endFlage = new byte[2];

	public Message(String data) {
		this.startFlage[0] = (byte) 0xaa;
		this.startFlage[1] = (byte) 0xaa;
		
		this.bodyData = data.getBytes(Charset.forName("utf-8"));
		this.bodyLength = this.bodyData.length;
		
		this.endFlage[0] = (byte) 0x0a;//'\r'
		this.endFlage[1] = (byte) 0x0d;//'\n'
	}

	public byte[] getStartFlage() {
		return startFlage;
	}

	public int getBodyLength() {
		return bodyLength;
	}

	public byte[] getBodyData() {
		return bodyData;
	}

	public byte[] getEndFlage() {
		return endFlage;
	}
}
