/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: WebOutofMem
 * Author:   feilin
 * Date:     2021/6/28 下午9:48
 * Description:
 */
package coding.jvm;

import com.github.javafaker.Faker;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

public class WebOutOfMem {

    public static void main(String[] args) throws IOException {

        var pool = new ForkJoinPool();
        var faker = new Faker();
        var list = new ArrayList<String>();
        try (var serverSocket = new ServerSocket(8080)) {
            while (true) {

                var clientSocket = serverSocket.accept();
                final long start = System.currentTimeMillis();
                pool.submit(() -> {
                    var resp = "HTTP/1.1 200 ok\n\n";
                    try {
                        var ostream = clientSocket.getOutputStream();
                        resp += faker.shakespeare().asYouLikeItQuote();
                        list.add(resp);
                        ostream.write(resp.getBytes(StandardCharsets.UTF_8));
                        ostream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                System.out.println("cost " +(System.currentTimeMillis()-start));
                System.out.println(">>>>>>>>>>>>>>>>>>> "+clientSocket.getLocalAddress());
            }
        }
    }
}
