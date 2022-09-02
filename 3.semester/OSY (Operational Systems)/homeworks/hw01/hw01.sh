#!/usr/bin/env bash


# Assignment: https://osy.pages.fel.cvut.cz/docs/cviceni/lab2/ (only in czech)


QUOTES="'"
NUMBER_OF_ARGS=$#
if [ $NUMBER_OF_ARGS -ne 0 ]; then 
		if [ "$1" = "-h" ]; then
			echo "A simple bash script, that works with files and directories"
			exit 0
		elif [ "$1" != "-z" ]; then 
			exit 2
		fi
fi
while read -r CMD ADDR; do
	if [ "$CMD" = "PATH" ]; then
		if [ -L "$ADDR" ]; then
			LINK=$(readlink "$ADDR")
			echo "LINK $QUOTES$ADDR$QUOTES $QUOTES$LINK$QUOTES"
		elif [ -d "$ADDR" ]; then
			echo "DIR $QUOTES$ADDR$QUOTES"
		elif [ -f  "$ADDR" ]; then
			NUMBER_OF_LINES=$(wc -l < "$ADDR")
			ret=$?
			if [ $ret -ne 0 ]; then
				exit 2
			fi
			HEAD=$(head -1 "$ADDR")
			FILES_LIST+=("$ADDR")
			if [ "$NUMBER_OF_LINES" -ne 0 ]; then
				echo "FILE $QUOTES$ADDR$QUOTES $NUMBER_OF_LINES $QUOTES$HEAD$QUOTES"
			else 
				echo "FILE $QUOTES$ADDR$QUOTES $NUMBER_OF_LINES"
			fi
		else 
			echo "ERROR $QUOTES$ADDR$QUOTES" >&2
			exit 1
		fi
	fi
done
if [ $NUMBER_OF_ARGS -eq 1 ] && [ "$1" == "-z" ]; then
	tar czf output.tgz "${FILES_LIST[@]}"
	ret=$?
	if [ $ret -ne 0 ]; then
		exit 2
	fi
fi
