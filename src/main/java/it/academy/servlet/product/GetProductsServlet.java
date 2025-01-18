package it.academy.servlet.product;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import it.academy.model.Categoria;
import it.academy.model.Prodotto;
import it.academy.util.DbOperation;

/**
 * Servlet implementation class GetProductsServlet
 */
@WebServlet("/api/product/getProducts")
public class GetProductsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetProductsServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// SETTO IL CONSENSO DI RICHIESTE FETCH
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		
		DbOperation operation = new DbOperation(getServletContext());
		
		List<Prodotto> products = operation.getProducts();
		JSONArray jsonArray = new JSONArray();
		
		for(Prodotto product : products) {
			JSONObject obj = new JSONObject();
			obj.put("product_id", product.getId());
			obj.put("name", product.getNome());
			obj.put("description", product.getDescrizione());
			obj.put("price", product.getPrezzo());
			obj.put("category", product.getCategoria());
			
			jsonArray.put(obj);
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		// SCRIVIAMO LA RISPOSTA JSON
		PrintWriter out = response.getWriter();
		out.print(jsonArray.toString());
		out.flush();

		if (operation.closeConnection())
			System.out.println("Connessione chiusa correttamente");
		else
			System.out.println("Errore nella chiusura della connessione");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
