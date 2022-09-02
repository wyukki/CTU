#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(){
	const char * text = "XUY";
	char *string = malloc(strlen(text) * 5 + 5);
	for (int i = 0; i < 5; ++i) {
		sprintf(text, "%d\n", i);
		strcat(string, text);
		if (i != 4) {
			strcat(string, " ");
		} else { 
			strcat(string, "\n");
		}
	}
	printf("%s", string);
	return 0;
}
