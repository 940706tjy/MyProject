package cn.bulaomeng.fragment.test.delayed;

import java.util.concurrent.LinkedBlockingQueue;

public class Test {
    private int queueSize = 10;
    private LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(queueSize);

    public static void main(String[] args) {
        Test test = new Test();
        Producer producer = test.new Producer();
        Consumer consumer = test.new Consumer();
        producer.start();
        consumer.start();
    }

    class Consumer extends Thread {
        @Override
        public void run() {
            consume();
        }

        private void consume() {
            while (true) {
                try {
                    queue.take();
                    System.out.println(Thread.currentThread().getName() + "从队列取走一个元素，队列剩余" + queue.size() + "个元素");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class Producer extends Thread {
        @Override
        public void run() {
            produce();
        }

        private void produce() {
            while (true) {
                queue.add(1);
                System.out.println(Thread.currentThread().getName() + "向队列取中插入一个元素，队列剩余空间：" + (queueSize - queue.size()));
            }
        }
    }
}