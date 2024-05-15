package com.flowcanvas.auth.view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.flowcanvas.auth.dao.UsersDao;
import com.flowcanvas.auth.model.dto.UsersDto;
import com.flowcanvas.auth.model.form.LoginForm;
import com.flowcanvas.auth.utill.EmailValidator;
import com.flowcanvas.common.encrypt.Encrypt;
import com.flowcanvas.common.socket.client.ClientServer;
import com.flowcanvas.kanban.view.KanbanBoard;

public class Login extends JFrame {
	
	private JTextField txt_login_email;

	private UsersDao usersDao;
	private JPasswordField txt_login_password;
	private UsersDto usersDto;

	
	private ClientServer clientServer;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { 
	        public void run() {
	            try {
	                Login login = new Login();
	                login.setVisible(true);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    });
	}

	// 초기화
	public Login() {
		this.usersDao = new UsersDao();
		
		
		initialize();
	}

	
	private void initialize() {
		setFont(new Font("D2Coding", Font.PLAIN, 12));
		setTitle("로그인");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 480, 161);

		// 화면 가운데 위치 설정
		setLocationRelativeTo(null);

		JLabel lbl_email = new JLabel("이메일");
		lbl_email.setBounds(12, 21, 106, 20);
		lbl_email.setFont(new Font("D2Coding", Font.PLAIN, 12));
		lbl_email.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_email.setPreferredSize(new Dimension(95, 20));

		JLabel lbl_password = new JLabel("비밀번호");
		lbl_password.setBounds(12, 47, 106, 20);
		lbl_password.setFont(new Font("D2Coding", Font.PLAIN, 12));
		lbl_password.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_password.setPreferredSize(new Dimension(95, 20));
		
		txt_login_password = new JPasswordField();
		txt_login_password.setBounds(131, 47, 286, 20);

		txt_login_email = new JTextField();
		txt_login_email.setBounds(131, 20, 286, 20);
		txt_login_email.setFont(new Font("D2Coding", Font.PLAIN, 12));
		// 크기 설정
		txt_login_email.setPreferredSize(new Dimension(300, 20));
		txt_login_email.setColumns(10);

		JButton btn_login = new JButton("로그인");
		btn_login.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				login();
			}
		});
		
		// 버튼에 액션 리스너 추가
        btn_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        // 엔터 키 이벤트를 버튼의 액션과 연결
        txt_login_email.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btn_login.doClick();
            }
        });
        
        txt_login_password.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btn_login.doClick();
            }
        });
		
		
		btn_login.setFont(new Font("D2Coding", Font.PLAIN, 12));
		btn_login.setBounds(137, 77, 129, 23);

		JButton btn_join = new JButton("회원 가입");
		btn_join.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				RegisterUser registerUser = new RegisterUser();
				// 위치를 화면 가운데로 설정
				registerUser.setLocationRelativeTo(null);
				registerUser.setVisible(true);
			}
		});
		
		
		btn_join.setFont(new Font("D2Coding", Font.PLAIN, 12));
		btn_join.setBounds(278, 77, 129, 23);
		getContentPane().setLayout(null);
		getContentPane().add(lbl_email);
		getContentPane().add(txt_login_email);
		getContentPane().add(btn_login);
		getContentPane().add(btn_join);
		getContentPane().add(lbl_password);
		getContentPane().add(txt_login_password);
		
	}
	
	
	// 로그인
	private void login() {
		
		// 이메일, 비밀번호 입력 확인
		if (txt_login_email.getText().trim().equals("") || txt_login_password.getPassword().length == 0) {
			JOptionPane.showMessageDialog(this, "이메일과 패스워드를 모두 입력해주세요.", "입력 오류",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		// 이메일 유효성 검사
		if (!EmailValidator.isValidEmail(txt_login_email.getText())) {
			JOptionPane.showMessageDialog(this, "이메일 형식을 확인해 주세요.", "입력 오류",
					JOptionPane.WARNING_MESSAGE);
			return;
        }

		// 패스워드 암호화
		String passwordHash = Encrypt.generateHash(txt_login_password.getPassword());
		LoginForm loginForm = LoginForm.builder()
				.email(txt_login_email.getText())
				.password(passwordHash)
				.build();

		// db 연동 이메일, 비밀번호 확인 후 로그인
		usersDto = usersDao.verifyLoginUser(loginForm);
		if(usersDto != null) {
			
			// 사용자 ID를 클라이언트 서버 연결 메소드에 전달
			clientServerConnection(usersDto.getUserId());
		
			KanbanBoard kanbanBoard = new KanbanBoard(usersDto, clientServer);
			kanbanBoard.setLocationRelativeTo(null);
			kanbanBoard.setVisible(true);
			
			dispose();
		
		}
	}
	
	
	// 클라이언트 서버 연결
	private void clientServerConnection(int userId) {
	    try {
	        clientServer = new ClientServer("localhost", 5000);
	        
	        // sendLoginDetails 메소드가 성공적으로 로그인 처리를 확인한 경우
	        clientServer.sendLoginDetails("loginUser", userId);

	    } catch (IOException e) {
	        JOptionPane.showMessageDialog(this, "서버 연결 실패: " + e.getMessage(), "연결 오류", JOptionPane.ERROR_MESSAGE);
	    }
	}
}