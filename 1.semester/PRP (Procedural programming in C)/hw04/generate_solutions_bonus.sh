#!/bin/sh

HW=04b
PROGRAM=./b0b36prp-hw$HW-genref

if [ "$1" = "-osx" ]
then
	echo "Build for osx has been selected."
	PROGRAM=./b0b36prp-hw$HW-genref-osx
fi

mkdir -p files
for i in `seq 1 10`
do
   PROBLEM=files/hw$HW-$i
   echo "Generate random input '$PROBLEM.in'"
   #$PROGRAM -generate > $PROBLEM.in 2>/dev/null
   $PROGRAM -generate > $PROBLEM.in 
   echo "Solve '$PROBLEM.in' and store the reference solution to '$PROBLEM.out'"
   $PROGRAM < $PROBLEM.in > $PROBLEM.out 2>$PROBLEM.err
done

return 0
