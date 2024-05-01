package com.flowcanvas.common.db;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBConnectionTestMain {

	public static void main(String[] args) {
		// 연결 테스트
		try (Connection conn = DBConnection.getConnection()) {
			if (conn != null && !conn.isClosed()) {
				System.out.println("데이터베이스 연동 성공");

			} else {
				System.out.println("데이터베이스 연동 실패");
			}

		} catch (SQLException e) {
			log.error("데이터베이스 연결 중 오류 발생");
			e.printStackTrace();
		}
	}
}
