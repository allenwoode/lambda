/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: Order
 * Author:   feilin
 * Date:     2021/3/29 下午11:29
 * Description:
 */
package coding.proxy;

public class Order implements IOrder {
    @Override
    public void pay() {
        try {
            Thread.sleep(50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        System.out.println("show me the order!");
    }
}
