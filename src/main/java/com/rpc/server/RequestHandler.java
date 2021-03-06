package com.rpc.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.rpc.common.protocol.Request;
import com.rpc.server.register.ServiceObject;
import com.rpc.server.register.ServiceRegister;
import com.rpc.common.protocol.MessageProtocol;
import com.rpc.common.protocol.Response;
import com.rpc.common.protocol.Status;

public class RequestHandler {
	private MessageProtocol protocol;

	private ServiceRegister serviceRegister;

	public RequestHandler(MessageProtocol protocol, ServiceRegister serviceRegister) {
		super();
		this.protocol = protocol;
		this.serviceRegister = serviceRegister;
	}

	/**
	 * 入参为网络io传入的数据流，返回response编组之后的字节
	 */
	public byte[] handleRequest(byte[] data) throws Exception {
		// 1、解组消息
		Request req = this.protocol.unmarshallingRequest(data);

		// 2、查找服务对象
		ServiceObject so = this.serviceRegister.getServiceObject(req.getServiceName());

		Response rsp = null;

		if (so == null) {
			rsp = new Response(Status.NOT_FOUND);
		} else {
			// 3、反射调用对应的过程方法
			try {
				Method m = so.getInterf().getMethod(req.getMethod(), req.getPrameterTypes());
				Object returnValue = m.invoke(so.getObj(), req.getParameters());
				rsp = new Response(Status.SUCCESS);
				rsp.setReturnValue(returnValue);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				rsp = new Response(Status.ERROR);
				rsp.setException(e);
				System.out.println(e);
			}
		}

		// 4、编组响应消息
		return this.protocol.marshallingResponse(rsp);
	}

	public MessageProtocol getProtocol() {
		return protocol;
	}

	public void setProtocol(MessageProtocol protocol) {
		this.protocol = protocol;
	}

	public ServiceRegister getServiceRegister() {
		return serviceRegister;
	}

	public void setServiceRegister(ServiceRegister serviceRegister) {
		this.serviceRegister = serviceRegister;
	}

}
