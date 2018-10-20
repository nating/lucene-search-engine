/*
 * IndexFiles.java by Geoffrey Natin 10/10/18
 */

package com.mycompany.luceneapp;

// Input / Output
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.Charset;

// Lucene Analyzers
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

// Lucene index creation methods
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class IndexFiles {

  private IndexFiles() {}

  /** Index all text files under a directory. */
  public static void main(String[] args) {

    //---------------- Set up file paths ----------------

    String indexPath = "index";
    String docsPath = "../cran/cran.all.1400";
    final Path docDir = Paths.get(docsPath);
    if (!Files.isReadable(docDir)) {
      System.out.println("Document directory '" +docDir.toAbsolutePath()+ "' does not exist or is not readable, please check the path");
      System.exit(1);
    }

    try {

      Directory dir = FSDirectory.open(Paths.get(indexPath));

      //---------------- Choose Analyzer ----------------

      //WhitespaceAnalyzer – Splits tokens at whitespace
      //Analyzer analyzer = new WhitespaceAnalyzer();

      //SimpleAnalyzer – Divides text at non letter characters and lowercases
      //Analyzer analyzer = new SimpleAnalyzer();

      //StopAnalyzer – Divides text at non letter characters, lowercases, and removes stop words
      //Analyzer analyzer = new StopAnalyzer();

      //StandardAnalyzer - Tokenizes based on sophisticated grammar that recognizes e-mail addresses, acronyms, etc.; lowercases and removes stop words (optional)
      //Analyzer analyzer = new StandardAnalyzer();

      //EnglishAnalyzer
      //Analyzer analyzer = new EnglishAnalyzer();

      //CustomAnalyzer - Defined in CustomAnalyzer.java
      Analyzer analyzer = new CustomAnalyzer();

      //---------------- Create indexes ----------------

      // Create IndexWriter
      IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
      iwc.setOpenMode(OpenMode.CREATE);
      IndexWriter writer = new IndexWriter(dir, iwc);

      // Index the files
      indexFiles(writer, docDir);

      writer.close();

    } catch (IOException e) {
      System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
    }
  }

  // Returns whether a line of the cranfield collection is a heading or not
  static Boolean isHeading(String line){
    String s = line.substring(0,2);
    return (s.equals(".I") || s.equals(".T") || s.equals(".A") || s.equals(".B") || s.equals(".W"));
  }

  // Creates a document with the fields specified to be written to an index
  static Document createDocument(String id, String title, String author, String bib, String content){
    Document doc = new Document();
    doc.add(new StringField("id", id, Field.Store.YES));
    doc.add(new StringField("path", id, Field.Store.YES));
    doc.add(new TextField("title", title, Field.Store.YES));
    doc.add(new TextField("author", author, Field.Store.YES));
    doc.add(new TextField("bibliography", bib, Field.Store.YES));
    doc.add(new TextField("content", content, Field.Store.YES));
    return doc;
  }

  /** Indexes the cranfield collection */
  static void indexFiles(IndexWriter writer, Path file) throws IOException {
    try (InputStream stream = Files.newInputStream(file)) {

      BufferedReader buffer = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

      String id = "", title = "", author = "", bib = "", content = "", state = "";
      Boolean first = true;
      String line;

      System.out.println("Indexing documents.");

      // Read in lines from the cranfield collection and create indexes for them
      while ((line = buffer.readLine()) != null){
        switch(line.substring(0,2)){
          case ".I":
            if(!first){
              Document d = createDocument(id,title,author,bib,content);
              writer.addDocument(d);
            }
            else{ first=false; }
            title = ""; author = ""; bib = ""; content = "";
            id = line.substring(3,line.length()); break;
          case ".T":
          case ".A":
          case ".B":
          case ".W":
            state = line; break;
          default:
            switch(state){
              case ".T": title += line + " "; break;
              case ".A": author += line + " "; break;
              case ".B": bib += line + " "; break;
              case ".W": content += line + " "; break;
            }
        }
      }
      Document d = createDocument(id,title,author,bib,content);
      writer.addDocument(d);
    }
  }
}
