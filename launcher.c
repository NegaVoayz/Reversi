#include <stdio.h>
#include <stdlib.h>
int main() {
    char *path;
    if(system("java -jar ./Reversi.jar 2>nul")) {
        puts("Error: Could not run Reversi");
        puts("Please set the java path manually:");
        puts("(path should be like: \"/java/jre7/\")");
        path = malloc(300);
        path[0]='\"';
        fgets(path+1, 256, stdin);
        int i;
        for(i = 1; i < 257; i++) {
            if(path[i] == '\n' || path[i] == '\0') {
                break;
            }
        }
        for(char* p = "bin/java.exe\" -jar ./Reversi.jar 2>nul"; *p != '\0'; p++) {
            path[i++] = *p;
        }
        path[i] = '\0';
        if(system(path)) {
            puts("Error: Could not run Reversi");
        }
        free(path);
    }
    return 0;
}