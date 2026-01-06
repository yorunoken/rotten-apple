package com.mojang.rubydung;

import com.mojang.rubydung.level.Level;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;

public class BadApple {
    private int startX = 100;
    private int blockY = 43;
    private int startZ = 100;
    private String framesPath = "frames";

    private int videoWidth = 0;
    private int videoHeight = 0;

    private Level level;
    private ArrayList<BitSet> frames;
    private boolean playingAnimation = false;
    private boolean loadingFrames = false;
    private boolean framesLoaded = false;

    public BadApple(Level level) {
        this.level = level;
        this.frames = new ArrayList<>();
    }

    // run in separate thread so we don't have the player waiting.
    public void startLoadingFrames() {
        new Thread(this::loadFrames).start();
    }

    private void loadFrames() {
        this.loadingFrames = true;

        File folder = new File(framesPath);
        File[] files = folder.listFiles();

        if (files == null) {
            System.out.println("No frames found to load.");
            return;
        }
        Arrays.sort(files, Comparator.comparing(File::getName));

        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            try {
                BufferedImage img = ImageIO.read(f);
                BitSet frameData = getImageData(img);
                this.frames.add(frameData);
            } catch (IOException e) {
                System.out.println("Error while getting frame data: " + i + e);
            }

            if (i % 500 == 0) {
                System.out.println("Loaded frame: " + i);
            }
        }

        System.out.println("Loading Finished");
        System.gc();

        this.loadingFrames = false;
        this.framesLoaded = true;
    }

    private BitSet getImageData(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        // set global height and width here
        if (videoWidth == 0) {
            videoHeight = height;
            videoWidth = width;
        }

        BitSet data = new BitSet(width * height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);

                int r = (p >> 16) & 0xFF;
                int g = (p >> 8) & 0xFF;
                int b = p & 0xFF;

                boolean isBlack = (r + g + b) / 3 < 127;
                if (isBlack) {
                    data.set(x + y * width);
                }
            }
        }

        return data;
    }

    public void start() {
        if (this.playingAnimation) {
            System.out.println("Already playing animation.");
            return;
        }

        if (this.loadingFrames && !this.framesLoaded) {
            System.out.println("Frame loading not finished!");
            return;
        }

        this.playingAnimation = true;
        System.out.println("Started animation");
        new Thread(this::animate).start();
    }

    private void animate() {
        long timeBetweenFrames = 1000 / 30; // 30 FPS

        for (BitSet frameValues : frames) {
            long before = System.currentTimeMillis();
            renderFrame(frameValues);

            long elapsed = System.currentTimeMillis() - before;
            long timeLeft = timeBetweenFrames - elapsed;

            if (timeLeft > 0) {
                try {
                    Thread.sleep(timeLeft);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        this.playingAnimation = false;
    }

    private void renderFrame(BitSet frameValues) {
        for (int y = 0; y < videoHeight; y++) {
            for (int x = 0; x < videoWidth; x++) {
                int correctedX = x + startX;
                int correctedZ = y + startZ;

                boolean isBlack = frameValues.get(x + y * videoWidth);
                this.level.setTile(correctedX, blockY, correctedZ, isBlack ? 1 : 0);
            }
        }
    }
}
