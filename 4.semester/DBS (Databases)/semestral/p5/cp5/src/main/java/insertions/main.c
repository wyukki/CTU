#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
    FILE *fin = fopen("input.txt", "r");
    FILE *fout = fopen("output.txt", "w");
    int r;
    int counter, doctorPass, donorPass;
    while ((r = fscanf(fin, "%d,%d,%d\n", &counter, &donorPass, &doctorPass)) != EOF) {
        fprintf(fout, "INSERT INTO public.donation(artkey, donorpass, doctorpass) VALUES (%d,%d,%d);\n", counter, donorPass, doctorPass);
    }    
    fclose(fin);
    fclose(fout);
    return 0;
}
