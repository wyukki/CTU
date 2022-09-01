CFLAGS += -lm -pedantic -mssse3 -g -O1 -Wall -Werror -std=c99
HW=hw02

$(HW): main.c 
	gcc -mssse3 -g -O1 -Wall -Werror -std=c99 -pedantic main.c -o $(HW) -lm

clean:
	$(RM) -f *.o
	$(RM) -f output.ppm output.txt cachegrind*
	$(RM) -f $(HW)
	$(RM) -f $(HW)-brute.zip

.PHONY: clean zip
