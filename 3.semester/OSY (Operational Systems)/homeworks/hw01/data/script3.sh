#!/usr/bin/env bash
SCRIPT_DIR=$(dirname "$(realpath "$0")")
while read -r CMD ADDR; do
	NUMBER_OF_ARGS=$#
	# echo "$NUMBER_OF_ARGS"
	if [ $NUMBER_OF_ARGS -e 0 ]; then
		if [ "$CMD" = "PATH" ]; then
			echo "$ADDR"
			TMP=$(pwd)
			echo "$TMP"
			GIVENPATH="$SCRIPT_DIR"/"$ADDR"
			if [ -f  "$GIVENPATH" ]; then
				NUMBER_OF_LINES=$(wc -l """$ADDR""")
				HEAD=$(head -1 "$ADDR")
				echo "FILE ""$GIVENPATH" "$NUMBER_OF_LINES" "$HEAD"""
			elif [ -d "$GIVENPATH" ]; then
				echo "DIR $GIVENPATH"
			elif [ -L "$GIVENPATH" ]; then
				LINK=$(readlink "$GIVENPATH")
				echo "LING $GIVENPATH $LINK"
			else 
				echo "ERROR $GIVENPATH" >&2
                exit -1
			fi
		fi
	elif [ "$NUMBER_OF_ARGS" == "1" ]; then
		if [ "$1" = "-h" ]; then
			echo "A simple bash script"
		elif [ "$1" = "-z" ]; then
			echo "Zip"
		fi
	fi
done
