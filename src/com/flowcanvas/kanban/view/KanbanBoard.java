package com.flowcanvas.kanban.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;

import com.flowcanvas.auth.model.dto.UsersDto;
import com.flowcanvas.kanban.dao.ProjectsDao;
import com.flowcanvas.kanban.model.dto.ProjectsDto;
import com.flowcanvas.kanban.model.form.ProjectsMergeForm;

public class KanbanBoard extends JFrame {

	private JTextField txt_project_name;
	private JTabbedPane tabpnl_kanban;
	private DefaultListModel<ProjectsDto> projectsModel;
	private JList<ProjectsDto> list_project;
	
	private ProjectsDao projectDao;
	private int userId;
	
	// 로그인 유저
	private UsersDto usersDto;
	private List<String> checkTabCanban;

	
	public KanbanBoard(UsersDto usersDto) {
		this.usersDto = usersDto;
		this.projectDao = new ProjectsDao();
		this.userId = usersDto.getUserId();
		this.checkTabCanban = new ArrayList<>();
		
		// UI
		initialize();
		
		// 프로젝트 조회
		selProjects();
	}


	private void initialize() {
		
		/*
		* ======================================== 
		* 컴포넌트 생성
		* ========================================
		*/
		setTitle("FlowCanvas");
		setBounds(50, 120, 1600, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ToolTipManager.sharedInstance().setEnabled(false);

		JPanel pnl_main_top = new JPanel();
		getContentPane().add(pnl_main_top, BorderLayout.NORTH);
		pnl_main_top.setPreferredSize(new Dimension(getWidth(), 50));
		pnl_main_top.setLayout(new BorderLayout(0, 0));
	
		
		JPanel pnl_user = new JPanel();
		pnl_main_top.add(pnl_user, BorderLayout.WEST);
		pnl_user.setLayout(new GridLayout(0, 1, 0, 0));
		pnl_user.setPreferredSize(new Dimension(200, pnl_main_top.getHeight()));
	
		
		JLabel lbl_user_email = new JLabel("이메일\t : " + usersDto.getEmail());
		pnl_user.add(lbl_user_email);
		lbl_user_email.setFont(new Font("굴림", Font.BOLD, 14));
		lbl_user_email.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
	
		
		JLabel lbl_nick_name = new JLabel("닉네임\t : " + usersDto.getNickName());
		pnl_user.add(lbl_nick_name);
		lbl_nick_name.setFont(new Font("굴림", Font.BOLD, 14));
		lbl_nick_name.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
	
		
		JSplitPane spnl_mains_body = new JSplitPane();
		getContentPane().add(spnl_mains_body, BorderLayout.CENTER);
	
		
		JPanel pnl_project_back = new JPanel();
		spnl_mains_body.setLeftComponent(pnl_project_back);
		pnl_project_back.setPreferredSize(new Dimension(250, getHeight()));
		pnl_project_back.setLayout(new BorderLayout(0, 0));
	
		
		JPanel pnl_project_back_top = new JPanel();
		pnl_project_back.add(pnl_project_back_top, BorderLayout.NORTH);
		pnl_project_back_top.setLayout(new BorderLayout(0, 0));
		pnl_project_back_top.setPreferredSize(new Dimension(pnl_project_back.getWidth(), 50));
	
		
		JLabel lbl_project_list = new JLabel("프로젝트 리스트");
		pnl_project_back_top.add(lbl_project_list, BorderLayout.CENTER);
		lbl_project_list.setFont(new Font("굴림", Font.BOLD, 16));
		
		
		JButton btn_project_add = new JButton("프로젝트 추가 +");
		pnl_project_back_top.add(btn_project_add, BorderLayout.SOUTH);
	
		
		JScrollPane scpnl_project = new JScrollPane();
		pnl_project_back.add(scpnl_project, BorderLayout.CENTER);
	
	
		JPanel pnl_project = new JPanel();
		scpnl_project.setViewportView(pnl_project);
		pnl_project.setLayout(new BorderLayout(0, 0));
	
		
		txt_project_name = new JTextField();
		pnl_project.add(txt_project_name, BorderLayout.NORTH);
		txt_project_name.setVisible(false);
		
		
		projectsModel = new DefaultListModel<>();
		list_project = new JList<>();
		pnl_project.add(list_project, BorderLayout.CENTER);
		list_project.setCellRenderer(new ButtonListRenderer(userId));
		list_project.setModel(projectsModel);
		
	
		tabpnl_kanban = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
		spnl_mains_body.setRightComponent(tabpnl_kanban);

		
		/*
		 * ======================================== 
		 * 이벤트
		 * ========================================
		 */
	
		// 프로젝트 추가 이벤트
		btn_project_add.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if (txt_project_name.isVisible()) {
					txt_project_name.setVisible(false);
					txt_project_name.setText("");
				} else {
					txt_project_name.setVisible(true);
					// 포커스 요청
					txt_project_name.requestFocusInWindow();
				}
				pnl_project.revalidate();
			}
		});
	
		// 프로젝트 명 입력 이벤트
		txt_project_name.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str_project_name = txt_project_name.getText();
	
				if (!str_project_name.trim().isEmpty()) {
	
					// 프로젝트 저장
					mergeProject(-1, str_project_name);
					
					// 프로젝트 조회
					selProjects();
					
					txt_project_name.setVisible(false);
					txt_project_name.setText("");
					pnl_project.revalidate();
				}
			}
		});
		
		
		// 리스트 마우스클릭
		list_project.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if (projectsModel.isEmpty()) {
					return;
				}
				
				int mousePoint = list_project.locationToIndex(e.getPoint());
				Rectangle mouseClick = list_project.getCellBounds(mousePoint, mousePoint);
				Rectangle buttonSize = new Rectangle(mouseClick.x + mouseClick.width - 50, mouseClick.y, 45, mouseClick.height);
			
				if (buttonSize.contains(e.getPoint()) && list_project.getSelectedValue().getUserId() == userId) {
					openProjectMenu(e.getX(), e.getY(), mousePoint, projectsModel.get(mousePoint));
				}
				
				// 프로젝트 목록 더블 클릭 시
				// 칸반 보드 open (수정)
				if (e.getClickCount() == 2) {
					
					String projectId = String.valueOf(list_project.getSelectedValue().getProjectId());
					
					// 프로젝트 탭 확인
					if (checkTabCanban.contains(projectId)) {
						return;
					}
					checkTabCanban.add(projectId);
					
					String projectTitle = list_project.getSelectedValue().getProjectName();
					
					// 이미 생성한 칸반 Panel
			        KanbanPanelSetting pnl_total_kanban =
			        		new KanbanPanelSetting(list_project.getSelectedValue().getUserId()
			                  , usersDto.getUserId()
			                  , Integer.parseInt(projectId)
			                  , spnl_mains_body.getRightComponent().getSize());
					
					tabpnl_kanban.addTab(projectTitle, null, pnl_total_kanban, projectId);
					tabpnl_kanban.setSelectedIndex(tabpnl_kanban.indexOfTab(projectTitle));			
					
					// 탭 닫기 버튼 추가
					addCloseTabButton(projectTitle);
	
				}
			}
		});
	}
	
	
	// 프로젝트 조회
	private void selProjects() {

        List<ProjectsDto> projectsList = projectDao.selProjets(userId);
        
        projectsModel.clear();
		for (ProjectsDto projectsDto : projectsList) {
			projectsModel.addElement(projectsDto);
		}
	}
	
	
	// 프로젝트 저장 수정
	private void mergeProject(int projectId, String projectName) {
		
		ProjectsMergeForm pmf = ProjectsMergeForm.builder()
				.projectId(projectId)
				.userId(userId)
				.projectName(projectName)
				.build();

		projectDao.mergeProject(pmf);
		
		// 프로젝트 리스트 조회
		selProjects();
	}
	
	
	// 프로젝트 삭제
	private void delProject(ProjectsDto projectsDto) {
		
	    int delCheckMessage = 
	    		JOptionPane.showConfirmDialog(this, "프로젝트를 삭제하시겠습니까?", "삭제 확인", 
	    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

	    if (delCheckMessage == JOptionPane.YES_OPTION) {
	    	
	        projectDao.delProject(projectsDto.getProjectId());
	        // 프로젝트 리스트 조회
	        selProjects();
	        
	        JOptionPane.showMessageDialog(this, "프로젝트가 삭제되었습니다.", "삭제 완료", JOptionPane.INFORMATION_MESSAGE);
	    }
	}
	
	
  // 리스트 버튼 팝업
	private void openProjectMenu(int x, int y, int mousePoint, ProjectsDto projectDto) {
		
		JPopupMenu buttonMenu = new JPopupMenu();
		
		JMenuItem editItem = new JMenuItem("수정");
		editItem.addActionListener(e -> modProject(projectDto));
		
		JMenuItem deleteItem = new JMenuItem("삭제");
		deleteItem.addActionListener(e -> delProject(projectDto));
		
		buttonMenu.add(editItem);
		buttonMenu.add(deleteItem);
		buttonMenu.show(list_project, x, y);
	}
	
	
	// 프로젝트 수정 팝업
	private void modProject(ProjectsDto projectsDto) {
		
		String newProjectName = "";
		
		do {
			newProjectName = 
					JOptionPane.showInputDialog("변경할 프로젝트명을 입력하세요.", (projectsDto.getProjectName()));
			
			if (newProjectName == null) {
				return;
			}
		} while (newProjectName.trim().isEmpty()); 
		
		mergeProject(projectsDto.getProjectId(), newProjectName);
	}
	
	
	// 탭 닫기 버튼
    private void addCloseTabButton(String title) {
    	
        tabpnl_kanban.setTabComponentAt(
        		tabpnl_kanban.indexOfTab(title), new ButtonTabClosed(title, tabpnl_kanban, this::onTabClosed));
    }

    
    // 탭 닫기 버튼 이벤트 실행 후 
	private void onTabClosed(int tabIndex) {
		
	    checkTabCanban.remove(tabpnl_kanban.getToolTipTextAt(tabIndex));
	}
	
	
	// 칸반 카드 드로그 앤 드롭 (수정)
//	private void setupButtonEvents(JButton button, JPanel kanban_panel1) {
//		
//		button.addMouseListener(new MouseAdapter() {
//
//			@Override
//			public void mousePressed(MouseEvent e) {
//				
//				for (int i = 0; i < kanbanColumns.length; i++) {
//					System.out.println("Panel " + i + " location: " + kanbanColumns[i].getLocation());
//				}
//			}
//
//			
//			@Override
//			public void mouseReleased(MouseEvent e) {
//
//				Container parent = button.getParent();
//				parent.remove(button);
//
//				Component deepestComponent = SwingUtilities.getDeepestComponentAt(kanban_panel, e.getComponent().getX(),
//						e.getComponent().getY());
//				System.out.println("상대적 좌표 : " + e.getComponent().getX() + ", " + e.getComponent().getY());
//
//				if (deepestComponent == null || !(deepestComponent instanceof JPanel)
//						|| deepestComponent.getName() == null) {
//					kanban_panel1.add(button);
//					kanban_panel1.revalidate();
//					kanban_panel1.repaint();
//					return;
//				}
//
//				System.out.println(deepestComponent.getName());
//
//				for (int i = 0; i < kanbanColumns.length; i++) {
//
//					if (deepestComponent.getName().equals(kanbanColumns[i].getName())) {
//					  kanban_panel1.remove(button);
//						kanban_panel1.revalidate();
//						kanban_panel1.repaint();
//						kanbanColumns[i].add(button);
//						kanbanColumns[i].revalidate();
//						kanbanColumns[i].repaint();
//						break;
//					}
//				}
//			}
//		});
//
//		
//		button.addMouseMotionListener(new MouseMotionAdapter() {
//			private int x;
//			private int y;
//			
//			@Override
//			public void mouseDragged(MouseEvent e) {
//				// 버튼 클릭 위치 맞추기
//				int tem_x = e.getX();
//				int tem_y = e.getY();
//
//				x += tem_x;
//				y += tem_y;
//
//				button.setBounds(x, y, 200, 30);
//				// 판넬
//				kanban_panel1.getParent().setComponentZOrder(button, 0);
//			}
//		});
//	}
}
