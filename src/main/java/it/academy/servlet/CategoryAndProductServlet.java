package it.academy.servlet;

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

import it.academy.model.CategoryAndProduct;
import it.academy.util.DbOperation;

/**
 * Servlet implementation class CategoryAndProductServlet
 */
@WebServlet("/api/getCategoryAndProduct")
public class CategoryAndProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CategoryAndProductServlet() {
		super();
		// TODO Auto-generated constructor stub
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

		List<CategoryAndProduct> elements = operation.getAllCategoryAndProduct();

		JSONArray array = new JSONArray();
		
		for(CategoryAndProduct elem : elements) {
			JSONObject obj = new JSONObject();
			
			obj.put("product_id", elem.getProdotto().getId());
			obj.put("product_name", elem.getProdotto().getNome());
			obj.put("product_description", elem.getProdotto().getDescrizione());
			obj.put("product_price", elem.getProdotto().getPrezzo());
			obj.put("product_category", elem.getProdotto().getCategoria());
			obj.put("category_id", elem.getCategoria().getId());
			obj.put("category_name", elem.getCategoria().getNome());
			
			array.put(obj);

		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		// SCRIVIAMO LA RISPOSTA JSON
		PrintWriter out = response.getWriter();
		out.print(array.toString());
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
