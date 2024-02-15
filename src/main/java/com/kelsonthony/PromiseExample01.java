package com.kelsonthony;

import org.riversun.promise.Promise;

public class PromiseExample01 {
    public static void main(String[] args) {
        Promise.resolve("foo")
                .then(new Promise((action, data) -> {
                    new Thread(() -> {
                        String newData = data + "bar";
                        action.resolve(newData);
                    }).start();
                }))
                .then(new Promise((action, data) -> {
                    System.out.println(data);
                    action.resolve();
                }))
                .start();
        System.out.println("Promise in Java");
    }
}
