package dev.punchcafe.gbvng.gen;

class HelloWorld {
    public native void print();

    static {
        System.loadLibrary("hello");
    }
}
