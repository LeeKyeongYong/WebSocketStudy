package com.wstudy.websocket;

import java.io.IOException;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/chat")
public class MyWebSocket {

	@OnOpen
	public void onOpen(Session session) {
		try {
			//세션이 오픈되면 자신의 세션에는 환영 메세지 표시
			session.getBasicRemote().sendText("["+session.getId()+"]님 어서오세요");
			
			//다른 세션에는 입장 메세지표시
			for(Session s:session.getOpenSessions()) {
				s.getBasicRemote().sendText("["+session.getId()+"]님이 입장하셨습니다.");
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@OnClose
	public void onClose(Session session) {
		try {
			//세션이 닫히면 다른 세션에게 퇴장 메세지 표시
			for(Session s:session.getOpenSessions()) {
				s.getBasicRemote().sendText("["+session.getId()+"]님이 퇴장하셨습니다.");
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	@OnMessage
	public void onMessage(Session session,String message) {
	
		try {
			if(session.isOpen()) {
				for(Session s:session.getOpenSessions()) {
					if(s.getId().equals(session.getId())) {
						//자신이 보낸 메세지 창에 표시
						s.getBasicRemote().sendText("["+session.getId()+": 나] "+message);
					} else {
						//자신이 보낸 메세지를 다른 사용자의 창에 표시
						s.getBasicRemote().sendText("["+session.getId()+"] "+message);
						
					}
				}
			}
		}catch(IOException e) {
			try {
				session.close();
			} catch (IOException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
			e.printStackTrace();
		}
		
	}
	
	@OnError
	public void onError(Session session,Throwable throwable) {}
}
