package com.samtech.ajax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cometd.Bayeux;
import org.cometd.Client;
import org.cometd.RemoveListener;
import org.cometd.server.BayeuxService;

public class ChatService extends BayeuxService {
	private static List<String> members=new ArrayList<String>(2);
	public ChatService(Bayeux bayeux, String name) {
		super(bayeux, name);
		subscribe("/chat/*", "trackmsg");
		subscribe("/service/members", "trackmembers");
	}
	
	public void trackmembers(Client from,String channel,Map<String,Object> data,String id){
		final String object = (String)data.get("user");///service/members
		if(object!=null){
			if(!members.contains(object.toString())){
				members.add(object.toString());
				from.addListener(new RemoveListener() {
					
					public void removed(String clientId, boolean timeout) {
						members.remove(object.toString());
					}
				});
				Map<String,Object> m=new HashMap<String, Object>(1);
				m.put("data", members);
				Client client = this.getClient();
				this.getBayeux().getChannel("/chat/members", false).publish(client, m, "0");
				//this.send(, "/chat/members", m, id);
			}
		}
	}
	public void trackmsg(Client from,String channel,Map<String,Object> data,String id){
		Object object = data.get("user");///service/members
		Object chat = data.get("chat");
		if(object!=null && chat!=null){
			System.out.println("from "+object+" say: "+chat);
		}
	}
	public ChatService(Bayeux bayeux, String name, int maxThreads,
			boolean synchronous) {
		super(bayeux, name, maxThreads, synchronous);
	}

	public ChatService(Bayeux bayeux, String name, int maxThreads) {
		super(bayeux, name, maxThreads);
	}
	@Override
	protected void send(Client toClient, String onChannel, Object data,
			String id) {
		StringBuffer buf=new StringBuffer(20);
		if(toClient!=null){
			buf.append("clientId="+toClient.getId());
		}
		buf.append("channel="+onChannel);
		buf.append("data="+(data!=null?data.toString():""));
		buf.append(";id="+id);
		System.out.println(buf.toString());
		super.send(toClient, onChannel, data, id);
	}
	@Override
	protected void subscribe(String channelId, String methodName) {
		System.out.println("channelId="+methodName+";methodName="+methodName);
		super.subscribe(channelId, methodName);
	}
}
