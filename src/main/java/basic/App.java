/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: App
 * Author:   feilin
 * Date:     2021/3/29 下午10:28
 * Description:
 */
package basic;

import java.util.HashSet;

public class App {

    public static void main(String[] args) {
        var h = new HashSet<Integer>();
        h.add(4);
        h.add(4);
        h.add(3);

        System.out.println(h);
    }
}
