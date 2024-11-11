package lab02.assignments;

import java.io.IOException;
import java.time.Clock;

public class FileDownloader {
    private static volatile int progress = 0;
    private static final int PROGRESS_MAX = 100;

    private static void doDownload() throws InterruptedException {
        while (progress < PROGRESS_MAX) {
            Thread.sleep(50); // Simulate download.
            progress++;
        }
    }

    private static void refreshUI() {
        // This clears the console.
        System.out.print("\033[H\033[2J");
        System.out.flush();

        if (progress == 100) {
            System.out.println("Download complete.");
        } else {
            System.out.println("Press enter to start downloading.");
        }
        System.out.println("Time: " + Clock.systemDefaultZone().instant().toString());
        System.out.println("Progress: " + progress + " / " + PROGRESS_MAX);
    }

    public static void main(String[] args) {
        try {
            while (true) {
                refreshUI();

                // Check if user entered any input.
                if (System.in.available() == 0) {
                    Thread.sleep(100);
                    continue;
                }
                int c = System.in.read();
                if (c == '\n') {
                    doDownload();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
