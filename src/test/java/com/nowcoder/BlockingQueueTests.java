package com.nowcoder;

import com.nowcoder.community.CommunityApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author byfgg
 * @create 2022-09-13 12:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class BlockingQueueTests {

    public static void main(String[] args) {
        BlockingQueue queue = new ArrayBlockingQueue(10);
        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();
//        new Thread(new Consumer(queue)).start();
//        new Thread(new Consumer(queue)).start();

    }
}

class Producer implements Runnable {

    private BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(20);
                queue.put(i);
                System.out.println(Thread.currentThread().getName() + "生产" + queue.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}


class Consumer implements Runnable {
    private BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(new Random().nextInt(1000));
            queue.take();
            System.out.println(Thread.currentThread().getName() + "消费" + queue.size());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
