package com.flowcanvas.common.db;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBConnection {

	private static HikariDataSource hds = null;

	static {
		initializeDataSource();
	}

	private static void initializeDataSource() {
		// 환경설정 파일을 읽어오기 위한 객체 생성
		Properties properties = new Properties();
		try (FileReader file = new FileReader("lib/oracle.properties")) {
			properties.load(file);

			HikariConfig config = new HikariConfig();
			config.setDriverClassName(properties.getProperty("driver"));
			config.setJdbcUrl(properties.getProperty("url"));
			config.setUsername(properties.getProperty("user"));
			config.setPassword(properties.getProperty("password"));

			if (hds != null) {
				hds.close();
			}

			hds = new HikariDataSource(config);

		} catch (NullPointerException e1) {
			log.error("예외: InputStream이 null 입니다. :" + e1.getMessage());
			System.out.println("예외: InputStream이 null 입니다. :" + e1.getMessage());
			e1.printStackTrace();
		} 
		catch (FileNotFoundException e2) {
			log.error("예외: 지정한 파일을 찾을수없습니다 :" + e2.getMessage());
			e2.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		if (hds == null) {
			initializeDataSource();
		}

		return hds.getConnection();
	}
}
