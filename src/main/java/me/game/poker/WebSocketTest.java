package me.game.poker;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 * 使用springboot的唯一区别是要@Component声明下，而使用独立容器是由容器自己管理websocket的，但在springboot中连容器都是spring管理的。
 * 虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
 */
@ServerEndpoint("/websocket/{userName}")
@Component
public class WebSocketTest {
	/**
	 * 存活的session集合（使用线程安全的map保存）
	 */
	private static Map<String, Session> livingSessions = new ConcurrentHashMap<>();


	/**
	 * 建立连接的回调方法
	 * @param session 与客户端的WebSocket连接会话
	 */
	@OnOpen
	public void onOpen(Session session,@PathParam("userName") String userName) {
		livingSessions.put(session.getId(), session);
		System.out.println("1111111");
		sendMessageToAll(userName+  " 加入聊天室");
	}

	/**
	 * 收到客户端消息的回调方法
	 * @param message 客户端传过来的消息
	 * @param session 对应的session
	 */
	@OnMessage
	public void onMessage(String message, Session session, @PathParam("userName") String userName) {
		sendMessageToAll(userName + " : " + message);
	}
	/**
	 *
	 * 发生错误的回调方法
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		System.out.println("发生错误");
		error.printStackTrace();
	}

	/**
	 *  关闭连接的回调方法
	 */
	@OnClose
	public void onClose(Session session, @PathParam("userName") String userName) {
		livingSessions.remove(session.getId());
		sendMessageToAll(userName + " 退出聊天室");
	}
	/**
	 * 单独发送消息
	 * @param session
	 * @param message
	 */
	public void sendMessage(Session session, String message) {
		try {
			session.getBasicRemote().sendText(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 群发消息
	 * @param message
	 */
	public void sendMessageToAll(String message) {
		livingSessions.forEach((sessionId, session) -> {
			sendMessage(session, message);
		});
	}







}
