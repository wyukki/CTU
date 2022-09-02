#!/usr/bin/bash
# A simple script

A=0
# if [ $A -ne 0]; then
if [ -f "./test.txt" ]; then
	echo Hello, World!
else 
	echo "else"
fi

while read INPUT; do
	echo "$INPUT"
done

