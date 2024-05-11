package com.example.filemanager.ui_logic.images;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Image loading for image preview is the most time expensive operation in app.
 * When user reloads or goes back and forth between the same directories, it is unnecessary to load the same images again
 * every time, so they are stored here at the cost of memory, which isn't as much of a problem.
 */
public class ImageCache {
    private static final HashMap<String, LoadedImage> loadedImages = new HashMap<>();

    private static int currentSize;
    private static final int DEFAULT_LIFE = 10;

    private static final int INCREASE_LIFE = 5;
    private static final int DECREASE_LIFE = 1;
    private static class LoadedImage {
        Image image;
        int life;

        public LoadedImage() {
            this.image = null;
            this.life = DEFAULT_LIFE;
        }
    }

    public static Image getImage(String uri, int size) {
        // if expected image size changed since last `get`, old images are now useless
        if (currentSize != size) {
            currentSize = size;
            loadedImages.clear();
        }

        var data = loadedImages.get(uri);

        if (data == null) {
            data = new LoadedImage();
            // ~15% of whole execution time spent here
            data.image = new javafx.scene.image.Image(uri, size, size, true, true);
            loadedImages.put(uri, data);
        } else {
            data.life += INCREASE_LIFE;
        }

        return data.image;
    }

    /**
     * Removes old unused loaded images. For now unused.
     */
    public static void collectGarbage(){
        var to_remove = new LinkedList<String>();

        for(var key : loadedImages.keySet()){
            var data = loadedImages.get(key);

            data.life -= DECREASE_LIFE;
            if (data.life <= 0){
                to_remove.add(key);
            }
        }

        for(var data : to_remove){
            loadedImages.remove(data);
        }
    }

}
