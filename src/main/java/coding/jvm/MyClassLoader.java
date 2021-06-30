/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: MyClassLoader
 * Author:   feilin
 * Date:     2021/7/1 上午12:21
 * Description:
 */
package coding.jvm;

import javassist.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class MyClassLoader {

    /*
      class Go {
        public void greeting() {
          System.out.println("Hi Greetings!!");
        }
      }
     */
    private static byte[] genClass() throws CannotCompileException, IOException {
        var pool = ClassPool.getDefault();
        var ctClass = pool.getOrNull("greetings.Go");
        if (ctClass != null) {
            ctClass.defrost();
        }
        ctClass = pool.makeClass("greetings.Go");
        var method = new CtMethod(CtClass.voidType, "greetings", new CtClass[]{}, ctClass);
        method.setModifiers(Modifier.PUBLIC);
        method.setBody(" { System.out.println(\"Hi Greetings!!\"); } ");
        ctClass.addMethod(method);
        return ctClass.toBytecode();
    }

    class BinClassLoader extends ClassLoader {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            System.out.println("print class---");

            if (name == "greetings.Go") {
                try {
                    var bytes = genClass();
                    return defineClass(name, genClass(), 0, bytes.length);
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return super.findClass(name);
        }
    }

    class NetClassLoader extends ClassLoader {
        byte[] bytes;
        public NetClassLoader() throws CannotCompileException, IOException {
            this.connect();
        }

        private void connect() throws IOException {
            //bytes = genClass();
            try (var client = new Socket("localhost", 8000)) {
                bytes = client.getInputStream().readAllBytes();
            }
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            if (name == "greetings.Go") {
                return defineClass(name, bytes, 0, bytes.length);
            }
            return super.findClass(name);
        }
    }

    @Test
    public void test_gen() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        var loader = new BinClassLoader();
        var clazz = loader.findClass("greetings.Go");
        //var clazz2 = loader.findClass("greetings.Go");
        var go = clazz.getConstructor().newInstance();
        go.getClass().getMethod("greetings").invoke(go);
    }

    @Test
    public void test_net() throws CannotCompileException, IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        var loader = new NetClassLoader();
        var clazz = loader.findClass("greetings.Go");
        //var clazz2 = loader.findClass("greetings.Go");
        var go = clazz.getConstructor().newInstance();
        go.getClass().getMethod("greetings").invoke(go);
    }

    @Test
    public void server() throws IOException, CannotCompileException {
        var serverSocket = new ServerSocket(8000);
        var bytes = genClass();
        while (true) {
            try (var clientSocket = serverSocket.accept()) {
                System.out.println("receive request... ");
                var out = clientSocket.getOutputStream();
                out.write(bytes);
                out.flush();
            }
        }
    }
}
