/**
 * Copyright (c), 2015-2021, io.eye
 * FileName: Event
 * Author:   feilin
 * Date:     2021/3/30 下午11:15
 * Description:
 */
package coding.monad;

import javax.print.event.PrintEvent;
import javax.xml.crypto.Data;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Event<T> {

    T data;
    public Event(T data) {
        this.data = data;
    }

    static class EventData {
        Integer id;
        String msg;
        public EventData(Integer id, String msg) {
            this.id = id;
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        @Override
        public String toString() {
            return "EventData{" +
                    "id=" + id +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }

    static class Transforms {
        static EventData transform(Integer id) {
            switch (id) {
                case 0:
                    return new EventData(id, "Start");
                case 1:
                    return new EventData(id, "Running");
                case 2:
                    return new EventData(id, "Done");
                case 3:
                    return new EventData(id, "Fail");
                default:
                    return new EventData(id, "Error");
            }
        }
    }

    @FunctionalInterface
    interface FN<A,B> {
        B apply(A a);
    }

    <B> Event<?> map(FN<T, B> f) {
        return new Event<B>(f.apply(this.data));
    }

    public static void main(String[] args) {
        Stream<Event<Integer>> s = Stream.of(
                new Event(1),
                new Event(2),
                new Event(0),
                new Event(4)
        );

        var ss = s.map(e -> e.map(Transforms::transform))
                .map(e -> e.data);
        ss.forEach(e -> {
            System.out.println(e);
        });
    }
}
