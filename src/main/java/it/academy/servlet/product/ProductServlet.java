package it.academy.servlet.product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import it.academy.model.Prodotto;
import it.academy.util.DbOperation;
import it.academy.util.Logging;

/**
 * Servlet implementation class ProductServlet
 */
@WebServlet("/api/product")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductServlet() {
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

		Prodotto product = operation.getProduct(Integer.parseInt(request.getParameter("id")));

		JSONObject obj = new JSONObject();
		obj.put("product_id", product.getId());
		obj.put("name", product.getNome());
		obj.put("description", product.getDescrizione());
		obj.put("price", product.getPrezzo());
		obj.put("category", product.getCategoria());

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

		Prodotto product = new Prodotto();

		product.setNome(jsonObject.getString("name"));
		product.setDescrizione(jsonObject.getString("description"));
		product.setPrezzo(jsonObject.getDouble("price"));
		product.setCategoria(jsonObject.getInt("category"));

		operation.createProduct(product);

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
		int idParam = Integer.parseInt(request.getParameter("id"));

		DbOperation operation = new DbOperation(getServletContext());

		Logging logger = new Logging(getServletContext());
		logger.logToFile("DELETE", operation.getProduct(idParam), null);

		operation.deleteProduct(idParam);

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
		DbOperation operation = new DbOperation(getServletContext());

		// Leggere il corpo della richiesta come stringa JSON
		StringBuilder jsonBody = new StringBuilder();
		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				jsonBody.append(line);
			}
		}

		// Analizzare il JSON ricevuto
		JSONObject jsonObject = new JSONObject(jsonBody.toString());

		Prodotto product = new Prodotto();

		product.setId(jsonObject.getInt("id"));
		product.setNome(jsonObject.getString("name"));
		product.setDescrizione(jsonObject.getString("description"));
		product.setPrezzo(jsonObject.getDouble("price"));
		product.setCategoria(jsonObject.getInt("category"));

		Logging logger = new Logging(getServletContext());
		logger.logToFile("UPDATE", operation.getProduct(jsonObject.getInt("id")), product);

		operation.updateProduct(product);

		if (operation.closeConnection()) {
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write("Prodotto aggiornato con successo.");
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
//		resp.setContentType("application/json; charset=UTF-8");

		super.doOptions(req, resp);
	}

}
