package com.flowcanvas.auth.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.JOptionPane;

import com.flowcanvas.auth.model.dto.UsersDto;
import com.flowcanvas.auth.model.form.LoginForm;
import com.flowcanvas.auth.model.form.RegistForm;
import com.flowcanvas.common.db.DBConnection;

import oracle.jdbc.OracleTypes;

public class UsersDao {

	private ResultSet rs;

	// 저장
	public String insUsers(RegistForm users) {
		// try-with-resources
		try (Connection conn = DBConnection.getConnection();
				CallableStatement cs = conn.prepareCall("{call USERS_CRUD_PACK.INS_USERS(?, ?, ?)}")) {

			cs.setString(1, users.getEmail());
			cs.setString(2, users.getPassword());
			cs.setString(3, users.getNickName());

			cs.executeUpdate();
			return "회원가입 되었습니다.";

		} catch (SQLException e) {
			// 에러 코드에 따라 적절한 메시지 반환
			switch (e.getErrorCode()) {
			case 20001:
				return "존재하는 이메일입니다."; // 이메일 중복
			case 20002:
				return "존재하는 닉네임입니다."; // 닉네임 중복
			default:
				e.printStackTrace();
				return "회원가입에 실패했습니다.";
			}
		}
	}

	// 이메일, 비밀번호 확인 후 로그인
	public UsersDto verifyLoginUser(LoginForm loginForm) {
		try (Connection conn = DBConnection.getConnection();
				CallableStatement cs = conn.prepareCall("{call USERS_CRUD_PACK.VERIFY_LOGIN_USER(?, ?, ?, ?)}")) {

			cs.setString(1, loginForm.getEmail());
			cs.setString(2, loginForm.getPassword());
			cs.registerOutParameter(3, OracleTypes.CURSOR); // SYS_REFCURSOR
			cs.registerOutParameter(4, Types.VARCHAR);

			cs.execute();
			rs = (ResultSet) cs.getObject(3);
			String resultMessage = cs.getString(4);

			if ("SUCCESS".equals(resultMessage)) {
				if (rs.next()) {
					return UsersDto.builder().userId(rs.getInt("user_id")).email(rs.getString("email"))
							.nickName(rs.getString("nick_name")).build();
				}
			} else {
				// FAILURE: Message
				String[] msg = resultMessage.split(":");
				if ("FAILURE".equals(msg[0].trim())) {
					JOptionPane.showMessageDialog(null, msg[1].trim(), "입력 오류", JOptionPane.ERROR_MESSAGE);

					return null;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	// 프로젝트 초대 추가 시 이메일 확인
	public UsersDto chkEmail(int projectId, String email) {

		try (Connection conn = DBConnection.getConnection();
				CallableStatement cs = conn.prepareCall("{call USERS_CRUD_PACK.CHK_USER_EMAIL(?, ?, ?)}")) {

			cs.setInt(1, projectId);
			cs.setString(2, email);
			cs.registerOutParameter(3, OracleTypes.CURSOR); // SYS_REFCURSOR

			cs.execute();

			rs = (ResultSet) cs.getObject(3);

			if (rs.next()) {
				return UsersDto.builder()
						.userId(rs.getInt("user_id"))
						.email(rs.getString("email"))
						.nickName(rs.getString("nick_name")).build();
			}

			return null;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}