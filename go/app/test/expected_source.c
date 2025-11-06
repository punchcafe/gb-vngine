
struct GameState {
    char * a_string;
    unsigned char abool;
    int counter;

};

typedef unsigned char bool;
typedef bool (*GameStatePredicate)(struct GameState*);
int main()
{
    return 0;
}