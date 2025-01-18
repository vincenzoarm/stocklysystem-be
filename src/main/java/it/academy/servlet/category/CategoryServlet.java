package it.academy.servlet.category;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import it.academy.model.Categoria;
import it.academy.util.DbOperation;
import it.academy.util.Logging;

/**
 * Servlet implementation class CategoryServlet
 */
@WebServlet("/api/category")
public class CategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CategoryServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

		DbOperation operation = new DbOperation(getServletContext());

		Categoria category = operation.getCategory(Integer.parseInt(request.getParameter("id")));

		JSONObject obj = new JSONObject();
		obj.put("category_id", category.getId());
		obj.put("name", category.getNome());

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		// SCRIVIAMO LA RISPOSTA JSON
		PrintWriter out = response.getWriter();
		out.print(obj.toString());
		out.flush();

		if (operation.closeConnection())
			System.out.println("Connessione chiusa correttamente");
		else
			System.out.println("Errore nella chiusura della connessione");

		response.setStatus(HttpServletResponse.SC_OK);
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

		Categoria category = new Categoria();
		category.setNome(jsonObject.getString("name"));

		operation.createCategory(category);

		if (operation.closeConnection())
			System.out.println("Connessione chiusa correttamente");
		else
			System.out.println("Errore nella chiusura della connessione");

		response.setStatus(HttpServletResponse.SC_OK);
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		DbOperation operation = new DbOperation(getServletContext());

		Logging logger = new Logging(getServletContext());
		logger.logToFile("DELETE", operation.getCategory(Integer.parseInt(request.getParameter("id"))), null);

		operation.deleteCategory(Integer.parseInt(request.getParameter("id")));

		if (operation.closeConnection())
			System.out.println("Connessione chiusa correttamente");
		else
			System.out.println("Errore nella chiusura della connessione");

		response.setStatus(HttpServletResponse.SC_OK);

	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		// Leggi il contenuto della richiesta
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}

		// Converti il corpo della richiesta in un oggetto JSON
		String jsonString = sb.toString();
		JSONObject jsonObject = new JSONObject(jsonString);

		DbOperation operation = new DbOperation(getServletContext());

		Categoria category = new Categoria();
		category.setId(jsonObject.getInt("id"));
		category.setNome(jsonObject.getString("name"));

		Logging logger = new Logging(getServletContext());
		logger.logToFile("UPDATE", operation.getCategory(jsonObject.getInt("id")), category);

		operation.updateCategory(category);

		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write("Dati ricevuti con successo");

		if (operation.closeConnection()) {
			System.out.println("Connessione chiusa correttamente");

		} else
			System.out.println("Errore nella chiusura della connessione");

	}

	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

		super.doOptions(request, response);
	}

}
