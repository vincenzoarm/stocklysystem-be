package it.academy.servlet.category;

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
import it.academy.util.DbOperation;

/**
 * Servlet implementation class CategoriesServlet
 */
@WebServlet("/api/category/getCategories")
public class CategoriesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CategoriesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// SETTO IL CONSENSO DI RICHIESTE FETCH
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		
		DbOperation operation = new DbOperation(getServletContext());
		
		List<Categoria> categories = operation.getCategories();
		JSONArray jsonArray = new JSONArray();
		
		for(Categoria category : categories) {
			JSONObject obj = new JSONObject();
			obj.put("category_id", category.getId());
			obj.put("name", category.getNome());
			
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

		response.setStatus(HttpServletResponse.SC_OK);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
