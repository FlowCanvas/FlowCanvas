package com.flowcanvas.kanban.view;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import com.flowcanvas.kanban.dao.ProjectsDao;
import com.flowcanvas.kanban.model.dto.ProjectsDto;
import com.flowcanvas.kanban.model.form.ProjectsMergeForm;

import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JTabbedPane;


public class KanbanBoard {

	private JFrame frame;
	private JLabel lbl_user_email;
	private JLabel lbl_nick_name;
	private JTextField txt_project_name;
	private DefaultListModel<ProjectsDto> projectsModel;
	private JList<ProjectsDto> list_project;
	
	private ProjectsDao projectDao;
	private int userId;

	// 칸반 컬럼 (수정)
	private JPanel[] kanbanColumns;
	private JPanel kanban_panel;
	private List<Integer> checkTabCanban;
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KanbanBoard window = new KanbanBoard(61, "admin", "관리자");
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public KanbanBoard(int userId, String email, String nickName) {
		
		this.lbl_user_email = new JLabel("이메일\t : " + email);
		this.lbl_nick_name = new JLabel("닉네임\t : " + nickName);
		
		initialize();
		
		this.checkTabCanban = new ArrayList<>();
		this.projectDao = new ProjectsDao();
		this.userId = userId;
		
		selProjects();
	}


	private void initialize() {

		/*
		 * ======================================== 
		 * 컴포넌트 생성
		 * ========================================
		 */
		frame = new JFrame();
		frame.setBounds(50, 120, 1600, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		JPanel pnl_main_top = new JPanel();
		frame.getContentPane().add(pnl_main_top, BorderLayout.NORTH);
		pnl_main_top.setPreferredSize(new Dimension(frame.getWidth(), 50));
		pnl_main_top.setLayout(new BorderLayout(0, 0));

		
		JPanel pnl_user = new JPanel();
		pnl_main_top.add(pnl_user, BorderLayout.WEST);
		pnl_user.setLayout(new GridLayout(0, 1, 0, 0));
		pnl_user.setPreferredSize(new Dimension(200, pnl_main_top.getHeight()));

		
		pnl_user.add(lbl_user_email);
		lbl_user_email.setFont(new Font("굴림", Font.BOLD, 14));
		lbl_user_email.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

		
		pnl_user.add(lbl_nick_name);
		lbl_nick_name.setFont(new Font("굴림", Font.BOLD, 14));
		lbl_nick_name.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

		
		JSplitPane spnl_mains_body = new JSplitPane();
		frame.getContentPane().add(spnl_mains_body, BorderLayout.CENTER);

		
		JPanel pnl_project_back = new JPanel();
		spnl_mains_body.setLeftComponent(pnl_project_back);
		pnl_project_back.setPreferredSize(new Dimension(250, frame.getHeight()));
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
		list_project.setCellRenderer(new ButtonListRenderer());
		list_project.setModel(projectsModel);
		
		
		JTabbedPane tabpnl_kanban = new JTabbedPane(JTabbedPane.TOP);
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
				
				int mousePoint = list_project.locationToIndex(e.getPoint());
				Rectangle mouseClick = list_project.getCellBounds(mousePoint, mousePoint);
				Rectangle buttonSize = new Rectangle(mouseClick.x + mouseClick.width - 50, mouseClick.y, 45, mouseClick.height);
				
				if (buttonSize.contains(e.getPoint())) {
					openProjectMenu(e.getX(), e.getY(), mousePoint, projectsModel.get(mousePoint));
				}
				
			
				// 칸반 보드 open (수정)
				if (e.getClickCount() == 2) {
					
					int projectId = list_project.getSelectedValue().getProjectId();
					
					// 프로제트 탭 확인
					if (checkTabCanban.contains(projectId)) {
						return;
					}
					checkTabCanban.add(projectId);
					
					
					String projectTitle = list_project.getSelectedValue().getProjectName();
					kanban_panel = new JPanel();
					kanban_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
					tabpnl_kanban.addTab(projectTitle, null, kanban_panel, null);
					tabpnl_kanban.setSelectedIndex(tabpnl_kanban.indexOfTab(projectTitle));

					kanbanColumns = new JPanel[3];
					
					for (int i = 0; i < 3; i++) {
						JPanel kanban_column_panel = new JPanel();
						kanban_panel.add(kanban_column_panel);
						kanban_column_panel.setName("MainPanel" + i);
						kanban_column_panel.setBorder(new LineBorder(Color.BLACK, 1, true));
						kanban_column_panel.setBackground(Color.WHITE);
						kanban_column_panel.setPreferredSize(new Dimension((tabpnl_kanban.getWidth() - 30) / 3,
								tabpnl_kanban.getHeight() - 40));

						kanbanColumns[i] = kanban_column_panel;
						
						for (int j = 0; j < 3; j++) {
							JButton btnTest = new JButton("카드" + (i + j + 1));
							kanban_column_panel.add(btnTest);
							btnTest.setName("Button" + i + "_" + j);
							btnTest.setPreferredSize(new Dimension((tabpnl_kanban.getWidth() - 30) / 3, 50));
							setupButtonEvents(btnTest, kanban_column_panel);
						}
					}
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
		
		projectDao.delProject(projectsDto.getProjectId());
		
		// 프로젝트 리스트 조회
		selProjects();
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
	
	
	// 칸반 카드 드로그 앤 드롭 (수정)
	private void setupButtonEvents(JButton button, JPanel kanban_panel1) {
		
		
		button.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				for (int i = 0; i < kanbanColumns.length; i++) {
					System.out.println("Panel " + i + " location: " + kanbanColumns[i].getLocation());
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {

				Container parent = button.getParent();
				parent.remove(button);

				Component deepestComponent = SwingUtilities.getDeepestComponentAt(kanban_panel, e.getComponent().getX(),
						e.getComponent().getY());
				System.out.println("상대적 좌표 : " + e.getComponent().getX() + ", " + e.getComponent().getY());

				if (deepestComponent == null || !(deepestComponent instanceof JPanel)
						|| deepestComponent.getName() == null) {
					kanban_panel1.add(button);
					kanban_panel1.revalidate();
					kanban_panel1.repaint();
					return;
				}

				System.out.println(deepestComponent.getName());

				for (int i = 0; i < kanbanColumns.length; i++) {

					if (deepestComponent.getName().equals(kanbanColumns[i].getName())) {
						kanban_panel1.remove(button);
						kanban_panel1.revalidate();
						kanban_panel1.repaint();
						kanbanColumns[i].add(button);
						kanbanColumns[i].revalidate();
						kanbanColumns[i].repaint();
						break;
					}
				}
			}
		});

		
		button.addMouseMotionListener(new MouseMotionAdapter() {
			private int x;
			private int y;
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// 버튼 클릭 위치 맞추기
				int tem_x = e.getX();
				int tem_y = e.getY();

				x += tem_x;
				y += tem_y;

				button.setBounds(x, y, 200, 30);
				// 판넬
				kanban_panel1.getParent().setComponentZOrder(button, 0);
			}
		});
	}
}
