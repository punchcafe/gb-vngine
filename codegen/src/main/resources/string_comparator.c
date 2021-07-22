int is_string_equal(char * string1, char * string2 )
{
    // any non-null string
    char string1LastChar = 'a';
    char string2LastChar = 'a';
    int i = 0;
    while (string1LastChar != '\0' && string2LastChar != '\0')
    {
        if(string1[i] != string2[i])
        {
            return 0;
        }
        string1LastChar = string1[i];
        string2LastChar = string2[i];
        i++;
    }
    // whether both have terminated
    return string1LastChar == string2LastChar;
}