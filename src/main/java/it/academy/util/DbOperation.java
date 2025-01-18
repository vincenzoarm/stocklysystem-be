package it.academy.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;

import it.academy.model.Categoria;
import it.academy.model.CategoryAndProduct;
import it.academy.model.Prodotto;
import it.academy.model.Utente;

public class DbOperation {
	private Properties config = new Properties();
	private Connection conn = null;
	Statement statement;

	public DbOperation(ServletContext servletContext) throws IOException {
		// implementa la logica per creare una connessione

		// Legge il parametro dal web.xml
		String configFilePath = servletContext.getInitParameter("configFilePath");

		InputStream in = servletContext.getResourceAsStream(configFilePath);
		if (in == null)
			throw new RuntimeException("File config.properties non trovato!");
		config.load(in);

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Recupera i parametri dal file di configurazione
			String url = config.getProperty("url");
			String user = config.getProperty("user");
			String password = config.getProperty("password");

			conn = DriverManager.getConnection(url, user, password);

			// Assegno allo statement la connessione
			statement = conn.createStatement();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		createDB();
		createTables();

	}

	public void createDB() {

		try {

			// CREIAMO IL DB SE NON ESISTE
			statement.executeUpdate("CREATE DATABASE IF NOT EXISTS stockly");

			statement.executeUpdate("USE stockly");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createTables() {

		try {
			if (statement.execute(
					"CREATE TABLE IF NOT EXISTS prodotto (id_prodotto INT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(50), descrizione VARCHAR(50), prezzo DOUBLE, categoria INT)")) {
				System.out.println("Tabella prodotto creata correttamente o già esistente!");
			} else {
				System.out.println("Tabella prodotto già esistente!");
			}

			if (statement.execute(
					"CREATE TABLE IF NOT EXISTS categoria (id_categoria INT AUTO_INCREMENT PRIMARY KEY, nome VARCHAR(50) UNIQUE)")) {
				System.out.println("Tabella categoria creata correttamente o già esistente!");
			} else {
				System.out.println("Tabella categoria già esistente!");
			}
			if (statement.execute(
					"CREATE TABLE IF NOT EXISTS utente (id_utente INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(50) UNIQUE, password VARCHAR(50), nome VARCHAR(50), cognome VARCHAR(50), email VARCHAR(50), ruolo VARCHAR(50))")) {
				System.out.println("Tabella utente creata correttamente o già esistente!");
			} else {
				System.out.println("Tabella utente già esistente!");
			}

		} catch (SQLException e) {
			System.out.println("Errore SQL nella creazione delle tabelle!");
			e.printStackTrace();
		}
	}

	public boolean closeConnection() {
		// implementa la logica per chiudere una connection
		try {
			this.conn.close();
			return true;
		} catch (Exception e) {
			System.out.println("Errore nella chiusura della connessione!");
			e.printStackTrace();
			return false;
		}
	}

	public void createProduct(Prodotto p) {
		String sql = "INSERT INTO prodotto(nome,descrizione,prezzo,categoria) VALUES(?,?,?,?)";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, p.getNome());
			pstmt.setString(2, p.getDescrizione());
			pstmt.setDouble(3, p.getPrezzo());
			pstmt.setInt(4, p.getCategoria());

			int rowsInserted = pstmt.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("Inserimento riuscito!");
			} else {
				System.out.println("Inserimento fallito.");
			}
		} catch (SQLException e) {
			System.out.println("Errore SQL nella creazione del prodotto!");
			e.printStackTrace();
		}

	}

	public void updateProduct(Prodotto p) {
		String sql = "UPDATE prodotto SET nome = ?,descrizione = ?,prezzo = ?,categoria = ? WHERE id_prodotto = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, p.getNome());
			pstmt.setString(2, p.getDescrizione());
			pstmt.setDouble(3, p.getPrezzo());
			pstmt.setInt(4, p.getCategoria());
			pstmt.setInt(5, p.getId());

			int rowsUpdated = pstmt.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Prodotto modificato correttamente!");
			} else {
				System.out.println("Modifica prodotto fallita!");
			}
		} catch (SQLException e) {
			System.out.println("Errore SQL nella modifica del prodotto!");
			e.printStackTrace();
		}
	}

	public Prodotto getProduct(int id) {
		String sql = "SELECT * FROM prodotto WHERE id_prodotto = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, id);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return new Prodotto(rs.getInt("id_prodotto"), rs.getString("nome"), rs.getString("descrizione"),
							rs.getDouble("prezzo"), rs.getInt("categoria"));
				} else {
					System.out.println("Nessun prodotto trovato.");
					return null;
				}
			}
		} catch (SQLException e) {
			System.out.println("Errore SQL nella ricerca del prodotto!");
			e.printStackTrace();
			return null;
		}
	}

	public void deleteProduct(int id) {
		String sql = "DELETE FROM prodotto WHERE id_prodotto = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, id);

			int rowsDeleted = pstmt.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("Eliminazione riuscita!");
			} else {
				System.out.println("Nessun prodotto eliminato.");
			}
		} catch (SQLException e) {
			System.out.println("Errore SQL nella cancellazione del prodotto");
			e.printStackTrace();
		}
	}

	public List<Prodotto> getProducts() {
		String sql = "SELECT * FROM prodotto";
		List<Prodotto> products = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {

				products.add(new Prodotto(rs.getInt("id_prodotto"), rs.getString("nome"), rs.getString("descrizione"),
						rs.getDouble("prezzo"), rs.getInt("categoria")));
			}
		} catch (SQLException e) {
			System.out.println("Errore nel recupero dei prodotti!");
			e.printStackTrace();
		}

		return products;
	}

	/* CATEGORIA */

	public void createCategory(Categoria c) {
		String sql = "INSERT INTO categoria(nome) VALUES(?)";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, c.getNome());

			int rowsInserted = pstmt.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("Inserimento riuscito!");
			} else {
				System.out.println("Inserimento fallito.");
			}
		} catch (SQLException e) {
			System.out.println("Errore SQL nella creazione della categoria!");
			e.printStackTrace();
		}

	}

	public void updateCategory(Categoria c) {
		String sql = "UPDATE categoria SET nome = ? WHERE id_categoria = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, c.getNome());
			pstmt.setInt(2, c.getId());

			int rowsUpdated = pstmt.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Categoria modificata correttamente!");
			} else {
				System.out.println("Modifica categoria fallita!");
			}
		} catch (SQLException e) {
			System.out.println("Errore SQL nella modifica della categoria!");
			e.printStackTrace();
		}
	}

	public Categoria getCategory(int id) {
		String sql = "SELECT * FROM categoria WHERE id_categoria = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, id);

			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return new Categoria(rs.getInt("id_categoria"), rs.getString("nome"));
				} else {
					System.out.println("Nessuna categoria trovata.");
					return null;
				}
			}
		} catch (SQLException e) {
			System.out.println("Errore SQL nella ricerca della categoria!");
			e.printStackTrace();
			return null;
		}
	}

	public void deleteCategory(int id) {
		String sql = "DELETE FROM categoria WHERE id_categoria = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, id);

			int rowsDeleted = pstmt.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("Eliminazione riuscita!");
			} else {
				System.out.println("Nessuna categoria eliminata.");
			}
		} catch (SQLException e) {
			System.out.println("Errore SQL nella cancellazione della categoria");
			e.printStackTrace();
		}

	}

	public List<Categoria> getCategories() {
		String sql = "SELECT * FROM categoria";
		List<Categoria> categories = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {

				categories.add(new Categoria(rs.getInt("id_categoria"), rs.getString("nome")));
			}
		} catch (SQLException e) {
			System.out.println("Errore nel recupero delle categorie!");
			e.printStackTrace();
		}

		return categories;
	}

	public List<CategoryAndProduct> getAllCategoryAndProduct() {
		String sql = "SELECT prodotto.id_prodotto, prodotto.nome AS product_name, prodotto.descrizione, prodotto.prezzo, prodotto.categoria, categoria.id_categoria, categoria.nome AS category_name FROM categoria, prodotto WHERE prodotto.categoria = categoria.id_categoria ORDER BY category_name, product_name";

		List<CategoryAndProduct> list = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(new CategoryAndProduct(
						new Prodotto(rs.getInt("id_prodotto"), rs.getString("product_name"),
								rs.getString("descrizione"), rs.getDouble("prezzo"), rs.getInt("categoria")),
						new Categoria(rs.getInt("id_categoria"), rs.getString("category_name"))));
			}
		} catch (SQLException e) {
			System.out.println("Errore nel recupero dei dati!");
			e.printStackTrace();
		}

		return list;
	}

	public Utente login(String email) {
		String sql = "SELECT * FROM utente WHERE email = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Utente user = new Utente(rs.getInt("id_utente"), rs.getString("username"), rs.getString("password"),
						rs.getString("nome"), rs.getString("cognome"), rs.getString("email"), rs.getString("ruolo"));
				return user;
			} else {
				System.out.println("L'utente non esiste!");
			}
		} catch (SQLException e) {
			System.out.println("Errore nel recupero dei dati!");
			e.printStackTrace();

		}
		return null;

	}

}
