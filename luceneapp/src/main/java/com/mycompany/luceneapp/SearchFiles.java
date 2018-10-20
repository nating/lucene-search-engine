/*
 * SearchFiles.java by Geoffrey Natin 10/10/18
 */

package com.mycompany.luceneapp;

// Input / Output
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

// Lucene analyzers
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

// Lucene scoring
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;

// Lucene queries and search
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

// Other Lucene imports
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

public class SearchFiles {

  private SearchFiles() {}

  // Reads the index at "index" and writes results to "my-results.txt"
  public static void main(String[] args) throws Exception {

    //--------- Create reader, writer & searcher ----------

    String index = "index";
    String results_path = "my-results.txt";
    IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
    PrintWriter writer = new PrintWriter(results_path, "UTF-8");
    IndexSearcher searcher = new IndexSearcher(reader);

    //---------------- Choose Analyzer ----------------

    //WhitespaceAnalyzer – Splits tokens at whitespace
    //Analyzer analyzer = new WhitespaceAnalyzer();

    //SimpleAnalyzer – Divides text at non letter characters and lowercases
    //Analyzer analyzer = new SimpleAnalyzer();

    //StopAnalyzer – Divides text at non letter characters, lowercases, and removes stop words
    //Analyzer analyzer = new StopAnalyzer();

    //StandardAnalyzer - Tokenizes based on sophisticated grammar that recognizes e-mail addresses, acronyms, etc.; lowercases and removes stop words (optional)
    //Analyzer analyzer = new StandardAnalyzer();

    //EnglishAnalyzer - Analyzer with enhancements for stemming English words
    //Analyzer analyzer = new EnglishAnalyzer();

    //CustomAnalyzer - Defined in CustomAnalyzer.java
    Analyzer analyzer = new CustomAnalyzer();

    //---------------- Choose Scoring method ----------------

    //Vector Space Model
    //searcher.setSimilarity(new ClassicSimilarity());

    //BM25
    searcher.setSimilarity(new BM25Similarity());

    //---------------- Read in and parse queries ----------------

    String queriesPath = "../cran/cran.qry";
    BufferedReader buffer = Files.newBufferedReader(Paths.get(queriesPath), StandardCharsets.UTF_8);
    MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[] {"title","author","bibliography","content"}, analyzer);

    String queryString = "";
    Integer queryNumber = 1;
    String line;
    Boolean first = true;

    System.out.println("Reading in queries and creating search results.");

    while ((line = buffer.readLine()) != null){

      if(line.substring(0,2).equals(".I")){
          if(!first){
            Query query = parser.parse(QueryParser.escape(queryString));
            performSearch(searcher,writer,queryNumber,query);
            queryNumber++;
          }
          else{ first=false; }
          queryString = "";
      } else {
          queryString += " " + line;
      }
    }

    Query query = parser.parse(QueryParser.escape(queryString));
    performSearch(searcher,writer,queryNumber,query);

    writer.close();
    reader.close();
  }

  // Performs search and writes results to the writer
  public static void performSearch(IndexSearcher searcher, PrintWriter writer, Integer queryNumber, Query query) throws IOException {
    TopDocs results = searcher.search(query, 1400);
    ScoreDoc[] hits = results.scoreDocs;

    // Write the results for each hit
    for(int i=0;i<hits.length;i++){
      Document doc = searcher.doc(hits[i].doc);
      /*
       * Write the results in the format expected by trec_eval:
       * | Query Number | 0 | Document ID | Rank | Score | "EXP" |
       * (https://stackoverflow.com/questions/4275825/how-to-evaluate-a-search-retrieval-engine-using-trec-eval)
      */
      writer.println(queryNumber + " 0 " + doc.get("id") + " " + i + " " + hits[i].score + " EXP");
    }
  }

}
