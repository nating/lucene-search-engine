/*
 * CustomAnalyzer.java by Geoffrey Natin 10/10/18
 */

package com.mycompany.luceneapp;

// Misc Lucene imports
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;

// Lucene Tokenizers
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.core.LetterTokenizer;

// Lucene Filters
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;


public class CustomAnalyzer extends Analyzer {

    protected TokenStreamComponents createComponents(String s) {

        //--------------------- Select a tokenizer --------------------

        //StandardTokenizer - “full” “text” “lucene.apache.org”
        //StandardTokenizer tokenizer = new StandardTokenizer();

        //WhitespaceTokenizer - “full-text” “lucene.apache.org”
        //WhitespaceTokenizer tokenizer = new WhitespaceTokenizer();

        //LetterTokenizer - “full” “text” “lucene” “apache” “org”
        LetterTokenizer tokenizer = new LetterTokenizer();

        //--------------------- Create the token stream --------------------

        TokenStream tokenstream = new StandardFilter(tokenizer);

        //------------------ Filter the token stream ------------------

        //LowerCaseFilter - Convert to lowercase
        //tokenstream = new LowerCaseFilter(tokenstream);

        //StopFilter - Remove stopwords
        tokenstream = new StopFilter(tokenstream, StopAnalyzer.ENGLISH_STOP_WORDS_SET);

        //LengthFilter - Remove words that are too long or too short
        tokenstream = new LengthFilter(tokenstream, 2, 20);

        //PorterStemFilter - Implement stemming
        tokenstream = new PorterStemFilter(tokenstream);

        //----------- Return the components of the token stream --------------

        return new TokenStreamComponents(tokenizer, tokenstream);
    }

}
