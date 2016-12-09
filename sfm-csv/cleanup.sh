sed -i -e 's/:parseCsv[a-zA-Z]*./","/' -e 's/\(parseCsv[a-zA-Z]*\)","sample"/\1","avgt","sample"/' -e 's/"Benchmark","Mode"/"Benchmark","Percentile","Mode"/' -e 's/"org.simpleflatmapper./"/' $1
