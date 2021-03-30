/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: MonadExample
 * Author:   feilin
 * Date:     2021/3/30 下午9:17
 * Description:
 */
package coding.monad;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.Stream;

public class MonadExample {

    @Test
    public void test_monad() {
        var h = Stream.of("Hello", "World")
                .map(x -> x.length())
                .reduce((a,b) -> a+b);

    }

    @Test
    public void test_udef() {
        Optional<Integer> x = Optional.empty();
        var y = x.map(a -> a / 0);
        System.out.println(y);
    }
}
