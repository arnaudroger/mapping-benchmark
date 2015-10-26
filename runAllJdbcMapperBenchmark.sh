#!/bin/bash


function jdbcBenchmarks() {
	echo $*
	java -jar spring/target/benchmarks.jar $3 -rf csv -rff $1/jmh-spring.csv -p db=$2 
	java -jar sql2o/target/benchmarks.jar $3 -rf csv -rff $1/jmh-sql2o.csv -p db=$2
	java -jar querydsl/target/benchmarks.jar $3 -rf csv -rff $1/jmh-querydsl.csv -p db=$2
	java -jar mybatis/target/benchmarks.jar $3 -rf csv -rff $1/jmh-mybatis.csv -p db=$2
	java -jar jpa-hibernate/target/benchmarks.jar $3 -rf csv -rff $1/jmh-jpa-hibernate.csv -p db=$2
	java -jar jpa-eclipselink/target/benchmarks.jar $3 -rf csv -rff $1/jmh-jpa-eclipselink.csv -p db=$2
	java -jar jpa-batoo/target/benchmarks.jar $3 -rf csv -rff $1/jmh-jpa-batoo.csv -p db=$2
	java -jar jooq/target/benchmarks.jar $3 -rf csv -rff $1/jmh-jooq.csv -p db=$2
	java -jar sfm-jdbc/target/benchmarks.jar $3 -rf csv -rff $1/jmh-sfm-jdbc.csv -p db=$2
}

#args="-f 1 -i 1 -wi 1 -p limit=1"
args=""
mkdir -p results/mysql
jdbcBenchmarks results/mysql MYSQL "$args"

mkdir -p results/h2
jdbcBenchmarks results/h2 H2 "$args"

mkdir -p results/datastax
java -jar sfm-datastax/target/benchmarks.jar $args -rf csv -rff results/datastax/jmh-datastax.csv




