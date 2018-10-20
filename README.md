# lucene-search-engine 🔍

A [Lucene](https://lucene.apache.org/) implementation of indexing and searching documents from the [Cranfield collection](http://ir.dcs.gla.ac.uk/resources/test_collections/cran/), evaluated using [trec_eval](https://trec.nist.gov/trec_eval/).

## Usage

*(You will need [maven](https://maven.apache.org/), [java](https://www.java.com/), [python](https://www.python.org/) x)*

To download and run the code:
```bash
git clone https://github.com/nating/lucene-search-engine
cd lucene-search-engine
./run.sh
```

You will see the trec_eval results of the Lucene application.

## Output

The output of run.sh should look like this:

|Metric 	|Geoff's |Seamus' |Difference	|
|---|---|---|---|
|num_q  	|225.0   |225.0   |0.0			|
|num_rel 	|218235.0|178376.0|39859.0		|
|um_rel 	|1837.0  |1837.0  |0.0			|
|num_rel_ret|1771.0  |1714.0  |57.0			|
|ap 		|0.425   |0.375   |0.05			|
|...|...|...|...|

These outputs represent: 
* The metrics from trec_eval.
* The results of the application (Geoff's).
* The results given as an example/ground truth in the TCD Computer Science module CS7IS3 (Seamus').
* The difference between the two results for that metric.

## Files

* [**run.sh**](https://github.com/nating/lucene-search-engine/blob/master/run.sh) packages the maven app, indexes the cran files, performs the cran queries across the indexes, runs trec_eval, creates a results file and outputs the results of trec_eval.
* [**IndexFiles.java**](https://github.com/nating/lucene-search-engine/blob/master/luceneapp/src/main/java/com/mycompany/luceneapp/IndexFiles.java) contains the code for indexing the cranfield documents.
* [**CustomAnalyzer.java**](https://github.com/nating/lucene-search-engine/blob/master/luceneapp/src/main/java/com/mycompany/luceneapp/CustomAnalyzer.java) contains the custom analyzer for analyzing document and query content.
* [**SearchFiles.java**](https://github.com/nating/lucene-search-engine/blob/master/luceneapp/src/main/java/com/mycompany/luceneapp/SearchFiles.java) contains the code for running the cranfield queries and creating the results file.


