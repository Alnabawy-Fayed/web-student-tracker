package web_student_tracker.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StudentDbUtil studentDbUtil ;
	@Resource(name = "jdbc/web_student_tracker")
    private DataSource dataSource;
	
    
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		}
		catch(Exception ex) {
			throw new ServletException(ex);
		}
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String myCommand = request.getParameter("command");
			if(myCommand == null) {
				myCommand = "list";
			}
			switch(myCommand){
				case "list" : 
					listStudents(request, response);
				    break;
				case "ADD" : 
					addStudent(request, response);
					break;
				case "LOAD" :
					loadStudent(request, response);
					break;
				case "UPDATE" : 
					updateStudent(request, response);
					break;
				case "DELETE" :
					deletStudent(request, response);
					break;
					default :
					listStudents(request, response);
			}
			
		}
		catch(Exception ex) {
			throw new ServletException(ex);
		}
		
	}
	private void deletStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = Integer.parseInt(request.getParameter("studentId"));
		studentDbUtil.deleteStudent(id);
		listStudents(request,response);
		
	}


	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//int id = Integer.parseInt(request.getParameter("student_id"));
		String id = request.getParameter("student_id");
		int StudentId = Integer.parseInt(id);
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		Student student = new Student(StudentId,firstName,lastName,email);
		studentDbUtil.updateStudent(student);
		listStudents(request, response);
		
	}


	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception, IOException {
		String id = request.getParameter("studentId");
		Student myStudent = studentDbUtil.getStudent(id);
		request.setAttribute("the_student", myStudent);
		RequestDispatcher dispatcher = request.getRequestDispatcher("update-student-form.jsp");
		dispatcher.forward(request, response);
		
	}


	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		Student student = new Student(firstName,lastName,email);
		studentDbUtil.addStudent(student);
		listStudents(request,response);
		
	}


	public void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception{
		List<Student> student = studentDbUtil.getStudents();
		request.setAttribute("StudentsList",student);
		RequestDispatcher dispatcher = request.getRequestDispatcher("ListStudents.jsp");
		dispatcher.forward(request,response);
	}

}
