../cpuPreBenchmark.sh
sudo update-alternatives --set java /usr/lib/jvm/java-8-oracle/jre/bin/java; 
export JAVA_HOME=/usr/lib/jvm/java-8-oracle
java -jar target/benchmarks.jar -jvmArgs "-Dcsv.dir=/home/aroger/volatile" -f 20 -rf csv -bm sample -tu ms
../cpuPostBenchmark.sh
