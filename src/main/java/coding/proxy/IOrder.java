/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: IOrder
 * Author:   feilin
 * Date:     2021/3/29 下午11:06
 * Description:
 */
package coding.proxy;

public interface IOrder {

    void pay();

    void show();

    static void print() {
        System.out.println(">>>>>>> "+Thread.currentThread().getName());
    }
}
