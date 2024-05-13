package com.flowcanvas.auth.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.flowcanvas.auth.dao.UsersDao;
import com.flowcanvas.auth.model.form.RegistForm;
import com.flowcanvas.auth.utill.EmailValidator;
import com.flowcanvas.common.encrypt.Encrypt;

import javax.swing.JPasswordField;

public class RegisterUser extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private JTextField txt_register_email;
	private JTextField txt_register_nickname;
	
	private final UsersDao usersDao;
	private JPasswordField txt_register_password;

	/**
	 * Create the dialog.
	 */
	public RegisterUser() {
		this.usersDao = new UsersDao(); 
		
		setFont(new Font("D2Coding", Font.PLAIN, 12));
		setTitle("회원 가입");
		setBounds(100, 100, 450, 205);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			txt_register_email = new JTextField();
			txt_register_email.setBounds(119, 24, 286, 20);
			txt_register_email.setPreferredSize(new Dimension(300, 20));
			txt_register_email.setFont(new Font("D2Coding", Font.PLAIN, 12));
			txt_register_email.setColumns(10);
			contentPanel.add(txt_register_email);
		}
		{
			JLabel lbl_registeruser_email = new JLabel("이메일");
			lbl_registeruser_email.setBounds(12, 24, 95, 20);
			lbl_registeruser_email.setPreferredSize(new Dimension(95, 20));
			lbl_registeruser_email.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_registeruser_email.setFont(new Font("D2Coding", Font.PLAIN, 12));
			contentPanel.add(lbl_registeruser_email);
		}
		{
			JLabel lbl_registeruser_password = new JLabel("비밀번호");
			lbl_registeruser_password.setBounds(12, 54, 95, 20);
			lbl_registeruser_password.setPreferredSize(new Dimension(95, 20));
			lbl_registeruser_password.setHorizontalAlignment(SwingConstants.RIGHT);
			lbl_registeruser_password.setFont(new Font("D2Coding", Font.PLAIN, 12));
			contentPanel.add(lbl_registeruser_password);
		}
		
		txt_register_nickname = new JTextField();
		txt_register_nickname.setPreferredSize(new Dimension(300, 20));
		txt_register_nickname.setFont(new Font("D2Coding", Font.PLAIN, 12));
		txt_register_nickname.setColumns(10);
		txt_register_nickname.setBounds(119, 82, 286, 20);
		contentPanel.add(txt_register_nickname);
		
		JLabel lbl_registeruser_nickname = new JLabel("닉네임");
		lbl_registeruser_nickname.setPreferredSize(new Dimension(95, 20));
		lbl_registeruser_nickname.setHorizontalAlignment(SwingConstants.RIGHT);
		lbl_registeruser_nickname.setFont(new Font("D2Coding", Font.PLAIN, 12));
		lbl_registeruser_nickname.setBounds(12, 82, 95, 20);
		contentPanel.add(lbl_registeruser_nickname);
		{
			txt_register_password = new JPasswordField();
			txt_register_password.setFont(new Font("D2Coding", Font.PLAIN, 12));
			txt_register_password.setBounds(119, 54, 286, 20);
			contentPanel.add(txt_register_password);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("등록");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
						// 이메일 유효성 검사
						if (!EmailValidator.isValidEmail(txt_register_email.getText())) {
							JOptionPane.showMessageDialog(RegisterUser.this, "이메일 형식을 확인해 주세요.", "입력 오류",
									JOptionPane.WARNING_MESSAGE);
							return;
				        }

						// 패스워드 암호화
						String passwordHash =
								Encrypt.generateHash(txt_register_password.getPassword());
						
						// 회원 저장
						RegistForm insUserDto = RegistForm.builder()
								.email(txt_register_email.getText())
								.password(passwordHash)
								.nickName(txt_register_nickname.getText())
								.build();
						
						String message = usersDao.insUsers(insUserDto);
						JOptionPane.showMessageDialog(null, message,
								"메시지", JOptionPane.WARNING_MESSAGE);
						
						if(message.equals("회원가입 되었습니다.")) {
							dispose();
						}
					}
				});
				okButton.setFont(new Font("D2Coding", Font.PLAIN, 12));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("취소");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// 대화상자 닫기
						dispose();
					}
				});
				cancelButton.setFont(new Font("D2Coding", Font.PLAIN, 12));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
