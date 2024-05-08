package com.flowcanvas.kanban.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class KanbanPanelSetting extends JPanel {

	private int projectListUserId;
	private int loginUserId;
	private int projectId;
	
	
	/**
	 * Create the panel.
	 */
	public KanbanPanelSetting(int projectListUserId, int loginUserId, int projectId) {
		
		this.projectListUserId = projectListUserId;
		this.loginUserId = loginUserId;
		this.projectId = projectId;
		
		// 화면 GUI
		initGUI();
	}
	
	// 화면 GUI
	private void initGUI() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel pnl_kanban_button = new JPanel();
		add(pnl_kanban_button, BorderLayout.NORTH);
		pnl_kanban_button.setLayout(new BorderLayout(0, 0));
		
		JPanel pnl_buttons = new JPanel();
		pnl_kanban_button.add(pnl_buttons, BorderLayout.EAST);
		pnl_buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btn_setting = new JButton("···");
		btn_setting.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JPopupMenu buttonMenu = new JPopupMenu();
				
				JMenuItem usersInvite = new JMenuItem("사용자 초대");
				usersInvite.addActionListener(event -> usersInvite());
				
				JMenuItem settingKanbancol = new JMenuItem("칸반 컬럼 설정");
				settingKanbancol.addActionListener(event -> settingKanbanCol());
				
				buttonMenu.add(usersInvite);
				buttonMenu.add(settingKanbancol);
				buttonMenu.show(btn_setting, e.getX(), e.getY());
			}
			
			// 칸반 컬럼 설정
			
			private void settingKanbanCol() {
				KanbanColSettingDialog colSettingDialog =
						new KanbanColSettingDialog(projectId);
				
				colSettingDialog.setVisible(true);
				colSettingDialog.setPreferredSize(null);
			}

			// 사용자 초대
			private void usersInvite() {
				
			}
		});
		pnl_buttons.add(btn_setting);
		btn_setting.setVisible(projectListUserId == loginUserId);
		
		JPanel pnl_kanban_colonm = new JPanel();
		add(pnl_kanban_colonm, BorderLayout.CENTER);
	}

}
