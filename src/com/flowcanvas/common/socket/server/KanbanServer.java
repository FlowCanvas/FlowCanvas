package com.flowcanvas.common.socket.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class KanbanServer {

	private ServerSocket serverSocket;
	private ConcurrentHashMap<Integer, ClientHandler> clientHandlers = new ConcurrentHashMap<>();
	private ExecutorService executor = Executors.newCachedThreadPool();

	ConcurrentHashMap<Integer, List<Integer>> projectJoinUsers = new ConcurrentHashMap<>();

	
	public KanbanServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		System.out.println("서버 시작 / 포트번호 :" + port);
	}

	// 서버 시작
	public void startServer() {
		try {
			while (!serverSocket.isClosed()) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("클라이언트 주소 : " + clientSocket.getInetAddress());
				ClientHandler handler = new ClientHandler(clientSocket, this);
				executor.execute(handler);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 서버에 연결한 클라이언트 서버 등록
	public void registerClient(int clientId, ClientHandler handler) {
		clientHandlers.put(clientId, handler);
		System.out.println("클라이언트 서버 등록 : " + clientId);
	}

	// 서버에 연결된 클라이언트 서버 삭제
	public void removeClient(int clientId) {
		clientHandlers.remove(clientId);
		System.out.println("클라이언트 서버 삭제 : " + clientId);
	}

	
	// 특정 사용자에게만 메시지 전달
	public void sendMessageToSpecificUsers(String message, List<Integer> userIds) {
		clientHandlers.forEach((id, handler) -> {
			if (userIds.contains(id)) {
				handler.sendMessage(message);
			}
		});
	}
	
	
	// 초대 시 
	public void sendProjectInvite(String message, List<Integer> userIds, String projectId) {
		for (int userId : userIds) {
			if (clientHandlers.containsKey(userId)) {
				clientHandlers.get(userId).sendMessage(message + ":" + projectId + ":" + userId);
			}
		}
	}
	

	// main
	public static void main(String[] args) {
		try {
			KanbanServer server = new KanbanServer(5000);
			server.startServer();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

// 클라이언트 Handler
class ClientHandler implements Runnable {
	private Socket socket;
	private KanbanServer server;
	private DataInputStream in;
	private DataOutputStream out;
	private int clientId;

	public ClientHandler(Socket socket, KanbanServer server) {
		this.socket = socket;
		this.server = server;
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			String loginInfo = in.readUTF(); // "loginUser;userId" 형식의 로그인 정보 받기

			String[] parts = loginInfo.split(";");
			String command = parts[0];
			clientId = Integer.parseInt(parts[1]); // userId 파싱
			System.out.println(clientId);

			if ("loginUser".equals(command)) {

				server.registerClient(clientId, this);
				// 로그인 성공 응답
//	        	sendMessage("로그인 성공");

			} else {
				// 로그인 실패 응답
				sendMessage("로그인 실패");
				closeResources();
				return;
			}

			while (socket.isConnected()) {
				String message = in.readUTF();
				String strCommand = message.contains(":") ? message.substring(0, message.indexOf(':')) : message;

				System.out.println(strCommand);

				switch (strCommand) {
				case "invite":

					// message(=list) + ":" + projectId + "/" + userId
					String listAndProjectId = message.substring(message.indexOf(':') + 1, message.length());
					String[] arrListAndProjectId = listAndProjectId.split("/");

					handleInvite(arrListAndProjectId[0], arrListAndProjectId[1]);
					break;

				case "projectJoin":
					System.out.println("=========");

					String[] ids = message.substring(message.indexOf(':') + 1).split("/");
					int userId = Integer.parseInt(ids[0]);
					int projectId = Integer.parseInt(ids[1]);

					// ConcurrentHashMap에 userId 추가
					server.projectJoinUsers.compute(projectId, (key, userList) -> {
						if (userList == null) {
							userList = new ArrayList<>();
						}
						if (!userList.contains(userId)) {
							userList.add(userId);
						}
						return userList;
					});

					// 결과 출력
					System.out.println(projectId + ": " + server.projectJoinUsers.get(projectId));
					System.out.println("=========");

					break;
					
				case "kanbanColumn":
                    selProjectInUser("selKanbanColumn", message.substring(message.indexOf(':') + 1));
                    
                    break;
                    
                 case "cardplus":
                     selProjectInUser("selKanbanColumn", message.substring(message.indexOf(':') + 1));
                     
                     break; 

				case "Heartbeat":
					System.out.println("Heartbeat ping: " + clientId);
					sendMessage("Heartbeat:pong!");
					break;

				default:
					System.out.println("Received: " + message);
					// 기타 메시지 처리
					break;
				}
			}

		} catch (IOException e) {
			System.out.println("Client disconnected: " + clientId);
			server.removeClient(clientId);
			closeResources();
		}
	}

	// 초대 시
	private void handleInvite(String substring, String projectId) {
		// 문자열 형태의 사용자 ID를 리스트로 변환 (예: "[1, 2, 3]" 형식 가정)
		List<Integer> userIds = Arrays.stream(substring.replaceAll("[\\[\\]]", "").split(",")).map(String::trim)
				.map(Integer::parseInt).collect(Collectors.toList());

		// 특정 사용자들에게 초대 메시지 전송
		server.sendProjectInvite("invited", userIds, projectId);

		sendMessage("프로젝트에 초대하였습니다.");
	}

	// 응답 메시지 처리
	public void sendMessage(String message) {
		try {
			out.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 연결 닫기
	private void closeResources() {
		try {
			in.close();
			out.close();
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void selProjectInUser(String messageTitle, String projectId) {

		int project = Integer.parseInt(projectId);
		server.sendMessageToSpecificUsers(messageTitle, server.projectJoinUsers.get(project));
	}
}
