# lucene-search-engine

A [Lucene](https://lucene.apache.org/) implementation of indexing and searching documents from the [Cranfield collection](http://ir.dcs.gla.ac.uk/resources/test_collections/cran/), evaluated using [trec_eval](https://trec.nist.gov/trec_eval/).

## Usage

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

These are the metrics from trec_eval, the results of the application (Geoff's), the results given as an example in CS7IS3 (Seamus') and the difference between the two results for that metric.

## Files

* **run.sh** packages the maven app, indexes the cran files, performs the cran queries across the indexes, runs trec_eval, creates a results file and outputs the results of trec_eval.
* **IndexFiles.java** contains the code for indexing the cranfield documents.
* **CustomAnalyzer.java** contains the custom analyzer for analyzing document and query content.
* **SearchFiles.java** contains the code for running the cranfield queries and creating the results file.


