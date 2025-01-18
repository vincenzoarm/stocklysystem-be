# StocklySystem Backend

Questo è il backend del sistema di gestione del magazzino StocklySystem, sviluppato in Java utilizzando Java Servlet.

## Prerequisiti

- **Java Development Kit (JDK)**: Assicurati di avere installato JDK 11 o superiore. Puoi verificarlo eseguendo:
  ```bash
  java -version
- **Apache Tomcat**: Necessario per eseguire il progetto. Scaricalo da Tomcat Downloads.
- **Database**: Configura un database MySQL o un altro DBMS compatibile.
- **Dipendenze Esterne**: Le dipendenze utilizzate sono state gestite manualmente nel build path del progetto e sono presenti nella cartella build/classes

## Configurazione

1. Clona il repository:
      git clone https://github.com/vincenzoarm/stocklysystem-be.git
2. Aggiungi le librerie necessarie:
      Aggiungili al build path del progetto tramite il tuo IDE (ad esempio Eclipse o IntelliJ IDEA):
3. Configura il database:
      Crea un database denominato stocklysystem.
4. Crea il file **config.properties** e aggiungi le credenziali del database:
      db.url=jdbc:mysql://localhost:3306/stocklysystem
      db.username=tuo_username
      db.password=tuo_password
5. Imposta tomcat come server nel tuo ide ed importa il progetto

6. Avvia il server Tomcat ed accedi al backend tramite http://localhost:8080/stocklysystem.

## API Endpoints

Per i prodotti:

- GET /api/product?id={id}: Recupera il singolo articolo in magazzino.
- POST /api/product: Aggiunge un nuovo articolo.
- PUT /api/product: Aggiorna le informazioni di un articolo esistente.
- DELETE /api/product/{id}: Elimina un articolo dal magazzino.

- GET /api/product/getProducts: Recupera la lista di tutti gli articoli in magazzino.

Per le categorie:

- GET /api/category?id={id}: Recupera la singola categoria in magazzino.
- POST /api/category: Aggiunge una nuova categoria.
- PUT /api/category: Aggiorna le informazioni di una categoria esistente.
- DELETE /api/category/{id}: Elimina una categoria dal magazzino.

- GET /api/category/getCategories: Recupera la lista di tutte le categorie in magazzino.

Per visualizzare tutti prodotti con le relative categorie

- GET /api/getCategoryAndProduct: Recupera la lista di tutti i prodotti con le relative categorie

Per l'utente: 

- POST /api/login: Recupera le informazioni dell'utente dal database

## Tecnologie Utilizzate

- Java 8
- Java Servlet
- JDBC per la connessione al database
- MySQL
- Apache Tomcat

## Struttura del Progetto

- src: Contiene tutto il codice sorgente.
- src/main/java/it/academy/servlet: Le servlet principali che gestiscono le richieste HTTP.
- src/main/java/it/academy/model: Classi che rappresentano le entità del sistema (ad esempio, Category, Product).
- src/main/java/it/academy/util: Classi di utility che vengono usate per la connessione al database e le varie interrogazioni ed una classe di Logging che permette di tenere conto delle modifiche fatte ai prodotti ed alle categorie
- src/main/webapp/WEB-INF/logs: Contiene il file di log
- src/main/webapp/WEB-INF/web.xml: Contiene le varie configurazioni
- src/main/webapp/WEB-INF/config.properties: File contenente le informazioni per collegarsi al database
