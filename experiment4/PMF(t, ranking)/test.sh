#!/bin/bash

javac *.java

java -Xmx30720m Main -lambda 0.001 -gamma 0.03 -d 20 -fnTrainingData ./ML100K/ML100K-copy1-train -fnTestData ./ML100K/ML100K-copy1-test -fnOutputData ./testResult/0.03_test1_100K.txt -n 943 -m 1682 -num_iterations 30

# 等待用户按Enter键
read -p "Press Enter to continue..."
