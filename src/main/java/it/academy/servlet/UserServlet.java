package it.academy.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import it.academy.model.Utente;
import it.academy.util.DbOperation;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/api/login")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().println("NOT SUPPORTED");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}

		String jsonString = sb.toString();
		JSONObject jsonObject = new JSONObject(jsonString);

		DbOperation operation = new DbOperation(getServletContext());

		Utente user = operation.login(jsonObject.getString("email"));
		if(jsonObject.getString("password").equals(user.getPassword())) {
			JSONObject obj = new JSONObject();
			obj.put("id_utente", user.getId());
			obj.put("username", user.getUsername());
			obj.put("password", user.getPassword());
			obj.put("nome", user.getNome());
			obj.put("cognome", user.getCognome());
			obj.put("email", user.getEmail());
			obj.put("ruolo", user.getRuolo());

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			
			PrintWriter out = response.getWriter();
			out.print(obj.toString());
			out.flush();
		}else {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			PrintWriter out = response.getWriter();
			out.print("Password errata!");
		}
		response.setStatus(HttpServletResponse.SC_OK);
		
		if (operation.closeConnection()) {
			
			System.out.println("Connessione chiusa correttamente");
		} else
			System.out.println("Errore nella chiusura della connessione");
	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// SETTO IL CONSENSO DI RICHIESTE FETCH
		resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

		super.doOptions(req, resp);
	}

}
