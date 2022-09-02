#!/bin/bash

# Assignment: https://osy.pages.fel.cvut.cz/docs/cviceni/lab3/ (only in czech)

function printHelp
{
    echo "A simple script, that works with regex. Enter:
    -a to display all pdf files in your current directory,
    -b to display all lines, that starts with a number
    -c to display text divided into sentences
    -d <prefix> to change incuding library name in .c file"
}
getopts "habcd:" opt;
case $opt in
    h)  printHelp
    ;;
    a)  grep -E '\.pdf$|\.PDF|\.pDf' <<< "$(ls -a)"
        ret=$?
        if [ $ret -ne 0 ]; then
            exit 0
        fi
    ;;
    b)  grep -E "^[+\-]?[0-9]+" <&0 | sed -e s/[+-]*[0-9]*[[:space:]]*/''/
    ;;
    c)  tr '\n' ' ' <&0 | grep -Eo "[[:alpha:]][^\.?!]*[\.?!]" 
    ;;
    d)  FILE=$(mktemp)
        cat > "$FILE"
        grep -q "[/]*[[:space:]]*#[[:space:]]*include[[:space:]]*" < "$FILE"
        ret=$?
        if [ $ret -ne 0 ]; then
            cat "$FILE"
            rm "$FILE"
            exit 0            
        fi
        grep -Eo -q '^[/]*[[:space:]]*#include[[:space:]]*<[^>"]*"' < "$FILE"
        ret=$?
        if [ $ret -eq 0 ]; then
            cat "$FILE"
            rm "$FILE"
            exit 0
        fi
        grep -E -q '^[/]*[[:space:]]*#include[[:space:]]*"[^>"]*>' < "$FILE"
        ret=$?
        if [ $ret -eq 0 ]; then
            cat "$FILE"
            rm "$FILE"
            exit 0
        fi
        ARG=${OPTARG/'/'/'\'}
        sed -E s/['<']/"<$ARG/"/ < "$FILE" | sed -E s/['\"']/"\"$ARG/"/
        rm "$FILE"
    ;;
    ?)  echo "Neplatny argument"
        printHelp
        exit 1
    esac
