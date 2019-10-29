package com.online.J2SE;


import java.net.URL;
import java.net.URLClassLoader;

public class DynamicClassLoader extends URLClassLoader {
    public DynamicClassLoader( URLClassLoader parent) {
        super(parent.getURLs(),parent);
    }

    public Class defineClassFromClassFile(String className, byte[] classFile)
            throws ClassFormatError {
        return defineClass(className, classFile, 0, classFile.length);
    }
}
