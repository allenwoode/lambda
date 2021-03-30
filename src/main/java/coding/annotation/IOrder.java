/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: IOrder
 * Author:   feilin
 * Date:     2021/3/29 下午11:41
 * Description:
 */
package coding.annotation;

public interface IOrder {

    void pay() throws InterruptedException;

    void show();
}
