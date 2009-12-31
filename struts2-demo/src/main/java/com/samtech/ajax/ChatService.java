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
	//private static List<String> members=new ArrayList<String>(2);
	private Map<String,List<MemberInfo>> room_members=new HashMap<String, List<MemberInfo>>(2);
	private String default_room="default_room";
	public ChatService(Bayeux bayeux, String name) {
		super(bayeux, name);
		subscribe("/chat/*", "trackmsg");
		subscribe("/service/members", "trackmembers");
		subscribe("/service/privatechat", "trackprivatemsg");
	}
	
	public void trackmembers(Client from,String channel,Map<String,Object> data,String id){
		final String object = (String)data.get("user");///service/members
		if(object!=null){
			
		 final Object object2 = data.get("room");
		 List<MemberInfo> def_members=null;
		if(object2!=null){
			def_members = room_members.get(object2.toString());
			if(def_members==null){
				
				def_members=new  ArrayList<MemberInfo>(2);
				room_members.put(object2.toString(), def_members);
			}
		}else if(def_members==null){
				def_members = room_members.get(default_room);
				if(def_members==null){
					def_members=new  ArrayList<MemberInfo>(2);
					room_members.put(default_room, def_members);
				}
			}
			
			if(!def_members.contains(new MemberInfo(from.getId(),object.toString()))){
				def_members.add(new MemberInfo(from.getId(),object.toString()));
				final List<MemberInfo> members=def_members;
				final Bayeux fbayeux=this.getBayeux();
				from.addListener(new RemoveListener() {
					
					public void removed(String clientId, boolean timeout) {
						System.out.println("removing clientid="+clientId);
						members.remove(new MemberInfo(clientId,object.toString()));
						Client client = fbayeux.getClient(clientId);
						List<String> users=new ArrayList<String>(members.size());
						Map<String,Object> m=new HashMap<String, Object>(1);
						for(int i=0;i<members.size();i++){
							 users.add(members.get(i).getUsername());	
							}
						m.put("data", users);
						fbayeux.getChannel("/chat/members", false).publish(client, m, "0");
					}
				});
				Map<String,Object> m=new HashMap<String, Object>(1);
				List<String> users=new ArrayList<String>(def_members.size());
				for(int i=0;i<def_members.size();i++){
				 users.add(def_members.get(i).getUsername());	
				}
				m.put("data", users);
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
	
	public void trackprivatemsg(Client from,String channel,Map<String,Object> data,String id){
		Object object = data.get("user");///service/members
		Object chat = data.get("chat");
		final Object room = data.get("room");
		if(object!=null && chat!=null){
			System.out.println("from "+object+" say: "+chat);
		}
		Object object2 = data.get("peer");
		List<MemberInfo> members = this.room_members.get(room.toString());
		Client toClient=null;
		if(object2!=null){
			 boolean found=false;
			 for(int i=0;i<members.size();i++){
				 if(members.get(i).getUsername().equals(object2.toString())){
					 found=true;
					 toClient=this.getBayeux().getClient(members.get(i).getClientId());
					 break;
				 }
			 }
			if(found && toClient!=null){
				data.remove("peer");
				this.send(toClient, "/chat/demo", data, id);
				
				this.send(from, "/chat/demo", data, id);
			}else{
				data.remove("peer");
				
				this.send(from, "/chat/demo", data, id);
			}
		}else this.send(from, "/chat/demo", data, id);
		
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
	
	protected static class MemberInfo{
		private String clientId;
		private String username;

		protected MemberInfo(String clientId,String username){
			this.clientId=clientId;
			this.username=username;
		}

		public String getClientId() {
			return clientId;
		}

		public String getUsername() {
			return username;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((clientId == null) ? 0 : clientId.hashCode());
			result = prime * result
					+ ((username == null) ? 0 : username.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MemberInfo other = (MemberInfo) obj;
			if (clientId == null) {
				if (other.clientId != null)
					return false;
			} else if (!clientId.equals(other.clientId))
				return false;
			if (username == null) {
				if (other.username != null)
					return false;
			} else if (!username.equals(other.username))
				return false;
			return true;
		}
		
		
	}
}
