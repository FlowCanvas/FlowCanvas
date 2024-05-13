package com.flowcanvas.common.socket.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.flowcanvas.kanban.dao.ProjectJoinDao;
import com.flowcanvas.kanban.view.KanbanBoard;
import com.flowcanvas.kanban.view.KanbanColumnPart;
import com.flowcanvas.kanban.view.KanbanPanelSetting;

public class ClientServer {

	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;

	private KanbanBoard kanbanBoard;
	private KanbanPanelSetting kanbanPanelSetting;
	private KanbanColumnPart kanbanColumnPart;
	private int projectId;
	private List<Integer> kanbanColumnIds;

	private ProjectJoinDao projectJoinDao = new ProjectJoinDao();

	public ClientServer(String serverAddress, int serverPort) throws IOException {

		socket = new Socket(serverAddress, serverPort);
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(socket.getInputStream());

		System.out.println("클라이언트 연결 -" + serverAddress + ":" + serverPort);

		listenToServerMessages(); // 메시지 수신 스레드 시작
	}

	// 로그인 시
	public void sendLoginDetails(String prefix, int userId) {
		try {
			// prefix: 메시지구분자 / userId: 메시지 보낼 데이터
			sendMessage(prefix + ";" + userId);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 서버로부터 응답 받기
	private String responseMessage() throws IOException {
		String response = in.readUTF();
		return response;
	}

	// 다른 메시지
	private void listenToServerMessages() {
		new Thread(() -> {
			try {
				while (!socket.isClosed()) {

					System.out.println("다른 메시지 온다 ?");

					String response = responseMessage(); // 서버로부터 메시지 수신

					if (response.startsWith("invited:")) {
						SwingUtilities.invokeLater(() -> {
							int response1 = JOptionPane.showConfirmDialog(kanbanBoard, "프로젝트에 참여하시겠습니까?", "프로젝트 초대",
									JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

							if (response1 == JOptionPane.YES_OPTION) {
								try {
									sendMessage("프로젝트 참여 수락");

									// 최종 응답 메시지 -> message + ":" + projectId + ":" + userId
									String[] arrString = response.split(":");

									// 데이터베이스에 userIds 저장
									projectJoinDao.insProjectJoin(Integer.parseInt(arrString[1]),
											Integer.parseInt(arrString[2]));

									kanbanBoard.selProjects();

								} catch (IOException e) {
									e.printStackTrace();
								}

							} else {

								try {
									sendMessage("프로젝트 참여 거절");
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						});
					}
					
					
					if (response.equals("selKanbanColumn")) {
						
						kanbanPanelSetting.getKanbanColList(projectId, "main");
	                }
					

					// 핑퐁 이외의 메시지 처리
					if (!response.startsWith("Heartbeat:")) {
						sendMessage(response);
					}
				}

			} catch (IOException e) {
				System.err.println("Server message listening error: " + e.getMessage());
				closeConnection();
			}

		}, "MessageListenerThread").start();
	}

	// 소켓 닫기
	public void closeConnection() {
		try {
			if (socket != null && !socket.isClosed()) {
				socket.close();
				System.out.println("Connection closed.");
			}

		} catch (IOException e) {
			System.err.println("Error closing connection: " + e.getMessage());
		}
	}

	// 서버에 보내기
	public void sendMessage(String message) throws IOException {
		System.out.println("서버에 메시지 보내기 : " + message);
		out.writeUTF(message);
	}

	// UI 설정
	public void setting(KanbanBoard kanbanBoard) {
		this.kanbanBoard = kanbanBoard;
	}

	public void kanbanColumnSetting(KanbanPanelSetting kanbanPanelSetting, int projectId) {

		this.kanbanPanelSetting = kanbanPanelSetting;
		this.projectId = projectId;
	}

//	public void setting2(KanbanColumnPart kanbanColumnPart, int kanbanColumnId) {
//		this.kanbanColumnPart = kanbanColumnPart;
//		kanbanColumnIds.add(kanbanColumnId);
//	}
}