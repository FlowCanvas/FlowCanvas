package com.flowcanvas.auth.view;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.flowcanvas.auth.dao.UsersDao;
import com.flowcanvas.auth.form.LoginForm;
import com.flowcanvas.common.encrypt.Encrypt;

public class Login {

	private JFrame frmFlowkanban;
	private JTextField txt_login_email;
	
	private UsersDao usersDao;
	private JPasswordField txt_login_password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frmFlowkanban.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// 초기화
	public Login() {
		initialize();
	}

	private void initialize() {
		this.usersDao = new UsersDao();
		
		frmFlowkanban = new JFrame();
		frmFlowkanban.setFont(new Font("D2Coding", Font.PLAIN, 12));
		frmFlowkanban.setTitle("로그인");
		frmFlowkanban.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFlowkanban.setBounds(100, 100, 480, 161);

		// 화면 가운데 위치 설정
		frmFlowkanban.setLocationRelativeTo(null);

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
				
				// 이메일, 비밀번호 입력 확인
				if (txt_login_email.getText().equals("") || txt_login_password.getPassword().length == 0) {
					JOptionPane.showMessageDialog(null, "이메일과 패스워드를 모두 입력해주세요.", "입력 오류",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				// 패스워드 암호화
				String passwordHash = Encrypt.generateHash(txt_login_password.getPassword());
				LoginForm loginForm = LoginForm.builder()
						.email(txt_login_email.getText())
						.password(passwordHash)
						.build();
				
				// db 연동 이메일, 비밀번호 확인 후 로그인
				usersDao.verifyLoginUser(loginForm);
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
		frmFlowkanban.getContentPane().setLayout(null);
		frmFlowkanban.getContentPane().add(lbl_email);
		frmFlowkanban.getContentPane().add(txt_login_email);
		frmFlowkanban.getContentPane().add(btn_login);
		frmFlowkanban.getContentPane().add(btn_join);
		frmFlowkanban.getContentPane().add(lbl_password);
		
		txt_login_password = new JPasswordField();
		txt_login_password.setBounds(131, 47, 286, 20);
		frmFlowkanban.getContentPane().add(txt_login_password);
	}
}
