package com.flowcanvas.kanban.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.MatteBorder;

import com.flowcanvas.kanban.dao.KanbanColumnDao;
import com.flowcanvas.kanban.model.dto.KanbanColumnDto;

public class KanbanPanelSetting extends JPanel {

	private int projectListUserId;
	private int loginUserId;
	private int projectId;
	
	private JPanel[] kanbanColumns;
	private JPanel kanban_panel;
	
	private KanbanColumnDao kanbanColumnDao;
	
	private JPanel kanban_view_panel;
	private Dimension parentsSize;
	
	
	/**
	 * Create the panel.
	 */
	public KanbanPanelSetting(int projectListUserId, int loginUserId
							 , int projectId, Dimension parentsSize) {
		setBorder(new MatteBorder(3, 3, 3, 3, (Color) new Color(200, 221, 242)));
		
		this.projectListUserId = projectListUserId;
		this.loginUserId = loginUserId;
		this.projectId = projectId;
		this.kanbanColumnDao = new KanbanColumnDao();
		this.parentsSize = parentsSize;
		
		// 화면 GUI
		initGUI();
		
		// 조회
		getKanbanColList(projectId, "main");
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
						new KanbanColSettingDialog(projectId, new KanbanColSettingDialog.Callback() {
							
							@Override
							public void onColumnsUpdated() {
								getKanbanColList(projectId, "main");
							}
						});
				
				colSettingDialog.setVisible(true);
				colSettingDialog.setLocationRelativeTo(null);
			}

			// 사용자 초대
			private void usersInvite() {
				
			}
		});
		
		pnl_buttons.add(btn_setting);
		btn_setting.setVisible(projectListUserId == loginUserId);
		
		JPanel pnl_kanban_colonm = new JPanel();
		add(pnl_kanban_colonm, BorderLayout.CENTER);
		pnl_kanban_colonm.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pnl_kanban_colonm.add(scrollPane);
	
		kanban_view_panel = new JPanel();
		kanban_view_panel.setBorder(null);
		scrollPane.setViewportView(kanban_view_panel);
		kanban_view_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
	}
	
	// 칸반 컬럼 조회
	private void getKanbanColList(int projectId, String uiSelected) {
		// kanban_view_panel 위에 올려져있는 컨트롤 삭제
		kanban_view_panel.removeAll();
		
		List<KanbanColumnDto> kanbanColumnDtoList =
        		kanbanColumnDao.selKanbancolumnData(projectId, uiSelected);
		
		int len = kanbanColumnDtoList.size();
		for (KanbanColumnDto dto : kanbanColumnDtoList) {
			
			KanbanColumnPart kanbanColumnPart =
					new KanbanColumnPart(kanban_view_panel, parentsSize, len,
							dto.getKanbanColumnId(), dto.getKanbanColumnName(), loginUserId);
		
			
			JPanel kanban_column_panel = new JPanel();
			kanban_view_panel.add(kanbanColumnPart);

		}
		
		kanban_view_panel.revalidate();
		kanban_view_panel.repaint();
	}

}
