package it.academy.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletContext;

import it.academy.model.Categoria;
import it.academy.model.Prodotto;

public class Logging {

    private String fileLog = "";

    public Logging(ServletContext context) {
        // Prendo il riferimento al file e lo apro in scrittura
        this.fileLog = context.getRealPath("/WEB-INF/logs/log.txt");
        ensureLogFileExists();        
    }

    private void ensureLogFileExists() {
        try {
            Path logPath = Paths.get(fileLog);
            
            // Verifica se il file di log esiste, altrimenti lo crea
            if (Files.notExists(logPath)) {
                // Crea la cartella logs se non esiste
                Files.createDirectories(logPath.getParent());
                // Crea il file se non esiste
                Files.createFile(logPath);
                // Imposta i permessi di lettura e scrittura
                logPath.toFile().setReadable(true, false);
                logPath.toFile().setWritable(true, false);
                System.out.println("File di log creato: " + fileLog);
            } else {
                System.out.println("Il file di log esiste già: " + fileLog);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore durante la creazione del file di log.");
        }
    }

    public void logToFile(String operation, Prodotto old, Prodotto newer) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String log;
        
        if (operation.equals("UPDATE")) {
            log = String.format(
                    "[%s] - OPERATION: %s, Old Product: %s, New Product: %s",
                    timestamp, operation, old.toString(), newer.toString());
        } else {
            log = String.format("[%s] - OPERATION: %s, Product: %s", timestamp, operation, old.toString());
        }

        writeLogToFile(log);
    }

    public void logToFile(String operation, Categoria old, Categoria newer) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String log;
        
        if (operation.equals("UPDATE")) {
            log = String.format(
                    "[%s] - OPERATION: %s, Old Category: %s, New Category: %s",
                    timestamp, operation, old.toString(), newer.toString());
        } else {
            log = String.format("[%s] - OPERATION: %s, Category: %s", timestamp, operation, old.toString());
        }

        writeLogToFile(log);
    }

    private void writeLogToFile(String log) {
        // Scrive il log nel file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileLog, true))) {
            writer.write(log);
            writer.newLine(); // Aggiunge una nuova riga dopo ogni log
            writer.flush();   // Assicura che il log venga scritto nel file
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore durante la scrittura del log nel file.");
        }
    }
    
    private void readFileLog() {
    	try (BufferedReader reader = new BufferedReader(new FileReader(fileLog))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);  // Stampa ogni riga del file
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore durante la lettura del file di log.");
        }
    }
}
