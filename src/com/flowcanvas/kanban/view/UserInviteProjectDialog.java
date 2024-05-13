package com.flowcanvas.kanban.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.flowcanvas.auth.dao.UsersDao;
import com.flowcanvas.auth.model.dto.UsersDto;
import com.flowcanvas.common.socket.client.ClientServer;
import com.flowcanvas.kanban.dao.ProjectJoinDao;

public class UserInviteProjectDialog extends JDialog {

	private ClientServer clientServer;
	private DefaultTableModel tableModel;
	private JTable table;
	private JTextField emailField;

	private int projectId;
	private UsersDao usersDao;
	
	private ProjectJoinDao projectJoinDao;

	public UserInviteProjectDialog(int projectId, ClientServer clientServer) {
		
		this.projectId = projectId;
		this.clientServer = clientServer;
		this.usersDao = new UsersDao();
		this.projectJoinDao = new ProjectJoinDao();

		// UI
		initialize();
	}

	
	// UI
	private void initialize() {
		setTitle("사용자 초대");
		setSize(500, 300);
		getContentPane().setLayout(new BorderLayout());

		// 상단 패널 설정
		JPanel topPanel = new JPanel();
		JLabel emailLabel = new JLabel("이메일:");
		emailField = new JTextField(20);
		
		
		JButton addButton = new JButton("추가");
		

		topPanel.add(emailLabel);
		topPanel.add(emailField);
		topPanel.add(addButton);

		// 테이블 모델 설정
		tableModel = new DefaultTableModel(new Object[] { "UserID", "이메일", "삭제" }, 0) {
			public boolean isCellEditable(int row, int column) {
				// '삭제' 버튼만 수정 가능
				return column == 2;
			}
		};

		table = new JTable(tableModel);
		table.getColumn("UserID").setWidth(0);
		table.getColumn("UserID").setMinWidth(0);
		table.getColumn("UserID").setMaxWidth(0);
		table.getColumn("UserID").setPreferredWidth(0);

		// '삭제' 버튼 렌더러 및 에디터 설정
		JButton deleteButton = new JButton("삭제");
		
		
		table.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
		table.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox(), deleteButton));

		
		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(topPanel, BorderLayout.NORTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		
		// 하단 패널 설정
		JPanel bottomPanel = new JPanel();
		JButton inviteButton = new JButton("초대");
		JButton closeButton = new JButton("닫기");
		

		bottomPanel.add(inviteButton);
		bottomPanel.add(closeButton);
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
		
		
		
		// 삭제 버튼 이벤트
		deleteButton.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row >= 0) {
				tableModel.removeRow(row);
			}
		});
		
		
		// 사용자 추가 버튼 이벤트
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String email = emailField.getText().trim();
				if (!email.isEmpty()) {
					// 사용자 추가
					addUser(projectId, email);
				}
			}
		});
		
		
		// 초대 버튼 이벤트
		inviteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// 초대 된 사용자 PROJECT_JOIN에 저장
				List<Integer> inviteUserIdList = new ArrayList<>();
				
				// 테이블의 모든 행에서 UserId 추출
		        for (int i = 0; i < tableModel.getRowCount(); i++) {
		            int userId = Integer.parseInt(tableModel.getValueAt(i, 0).toString());
		            inviteUserIdList.add(userId);
		        }
		        
		        // 서버에게 전달
				try {
					clientServer.sendMessage("invite:" + inviteUserIdList.toString() +
							"/" + projectId);
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		
		// 닫기 버튼 이벤트
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 대화 상자 닫기
				UserInviteProjectDialog.this.dispose(); 
			}
		});
	}
	
	
	// 사용자 추가
	private void addUser(int projectId, String email) {
		// 이메일 확인
		UsersDto dto = usersDao.chkEmail(projectId, email);
		if (dto != null) {
			tableModel.addRow(new Object[] { dto.getUserId(), email, "삭제" });
			emailField.setText("");
			
		} else {
			JOptionPane.showMessageDialog(UserInviteProjectDialog.this, "해당 사용자가 없거나\n이미 프로젝트에 참여된 사용자입니다.", "메시지",
					JOptionPane.WARNING_MESSAGE);
			
			return;
		}
	}
	

	// 삭제 버튼 렌더링
	private class ButtonRenderer extends JButton implements TableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			setText((value == null) ? "" : value.toString());
			return this;
		}
	}

	
	// 삭제 버튼 이벤트
	private class ButtonEditor extends DefaultCellEditor {
		private JButton button;

		public ButtonEditor(JCheckBox checkBox, JButton button) {
			super(checkBox);
			this.button = button;
			this.button.addActionListener(e -> fireEditingStopped());
		}

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			button.setText((value == null) ? "" : value.toString());
			return button;
		}
	}
}
