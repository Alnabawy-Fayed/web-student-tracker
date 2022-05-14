package web_student_tracker.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	private DataSource dataSource;
	public StudentDbUtil(DataSource myDataSource) {
		this.dataSource = myDataSource;
	}
	public List<Student> getStudents() throws Exception {
		List<Student> list = new ArrayList<>();
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			String sql = "select * from student order by last_name";
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			while(resultSet.next()) {
				int id = resultSet.getInt("id");
				String firstName= resultSet.getString("first_name");
				String lastName =  resultSet.getString("last_name");
				String email  = resultSet.getString("email");
				Student tempStudent = new Student(id,firstName,lastName,email);
				list.add(tempStudent);				
			}
		}
		finally {
			close(connection,statement,resultSet);
			
		}
		return list;
		
	}
	private void close(Connection con, Statement stm, ResultSet rset) {
		try {
			if(con != null) {
				con.close();
			}
			if(stm != null) {
				stm.close();
			}
			if(rset != null) {
				rset.close();
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	public void addStudent(Student student) throws SQLException {
		Connection connection = null;
		PreparedStatement statement = null ;
		try {
			connection = dataSource.getConnection();
			String sql = " insert into student (first_name,last_name,email) values (?,?,?)";
			statement = connection.prepareStatement(sql);
			statement.setString(1,student.getFirstName());
			statement.setString(2,student.getLastName());
			statement.setString(3,student.getEmail());
			statement.execute();
		}
		finally {
			close(connection,statement,null);
		}
		
	}
	public Student getStudent(String id) throws Exception {
		int studentId ;
		Student student ;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		try {
			studentId = Integer.parseInt(id);
			connection = dataSource.getConnection();
			String sql = "select * from student where id=?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, studentId);
			result = statement.executeQuery();
			if(result.next()) {
				String firstName = result.getString("first_name");
				String lastName = result.getString("last_name");
				String email = result.getString("email");
				student = new Student(studentId,firstName,lastName,email);
			}
			else {
				throw new Exception("could not find studentId" + id);
			}
			return student;
		}
		finally {
			close(connection,statement,result);
		}
		
	}
	public void updateStudent(Student student) throws Exception {
		Connection connection = null;
		PreparedStatement statement =null;
		try {
			connection = dataSource.getConnection();
			String sql = "update student "
					+ "set first_name=?, last_name=?, email=? "
					+ "where id=?";
			statement = connection.prepareStatement(sql);
			statement.setString(1, student.getFirstName());
			statement.setString(2, student.getLastName());
			statement.setString(3, student.getEmail());
			statement.setInt(4, student.getId());
			statement.execute();
		}
		finally{
			close(connection,statement,null);
		}
		
	}
	public void deleteStudent(int id) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = dataSource.getConnection();
			String sql = "delete from student where id = ?";
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			statement.execute();
		}
		finally {
			close(connection,statement,null);
		}
		
	}

}
