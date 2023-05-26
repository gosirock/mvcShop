package com.javalec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.javalec.dto.o_userDto;

public class o_userDao {

	// Field
	DataSource dataSource;
	
	public o_userDao() {
		// TODO Auto-generated constructor stub
		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/mvcshop");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<o_userDto> searchUserInfo(String queryName, String queryContent){
		ArrayList<o_userDto> dtos = new ArrayList<>();

		if(queryName == null){ // 첫 화면인 경우
			queryName = "username";
			queryContent = "";
		}
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try {
			connection = dataSource.getConnection();
			String query = "select userid, username, usertel, useremail, useraddress, insertdate, deletedate from user";
			String where = " where " + queryName + " like '%" + queryContent + "%';";
			preparedStatement = connection.prepareStatement(query + where);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				String userid = resultSet.getString(1);
				String username = resultSet.getString(2);
				String usertel = resultSet.getString(3);
				String useremail = resultSet.getString(4);
				String useraddress = resultSet.getString(5);
				Timestamp insertdate = resultSet.getTimestamp(6);
				Timestamp deletedate = resultSet.getTimestamp(7);
				
				
				o_userDto dto = new o_userDto(userid, username, usertel, useremail, useraddress, insertdate, deletedate);
				dtos.add(dto);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				// 생성한 순서의 역순대로 닫아준다! -> 퍼포먼스가 좋아짐.
				if(resultSet != null) resultSet.close();
				if(preparedStatement != null) preparedStatement.close();
				if(connection != null) connection.close();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dtos;
		
	} // SearchUserInfo
	
} // End
