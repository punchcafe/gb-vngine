#include <jni.h>
#include <stdio.h>

JNIEXPORT void JNICALL
Java_dev_punchcafe_gbvng_gen_HelloWorld_print(JNIEnv *env, jobject obj)
{
printf("Hello From C++ World!\n");
return;
}