package com.example.filemanager.logic;

import com.example.filemanager.logic.sort_strategy.NameStrategy;
import com.example.filemanager.logic.sort_strategy.SortStrategy;

import java.io.File;


public class Logic {
    public static Logic instance;
    private SortStrategy sortStrategy;


    //region constructors
    private Logic() {
        sortStrategy = new NameStrategy();
    }

    public Logic getInstance(){
        if (instance == null){
            instance = new Logic();
        }

        return instance;
    }
    //endregion

    //region setters
    public static void setSortStrategy(SortStrategy sortStrategy) {
        instance.sortStrategy = sortStrategy;
    }
    //endregion

    public static File[] getFiles(String path){
        File file = new File(path);

        File[] files = file.listFiles();

        instance.sortStrategy.sort(files);


        return files;
    }
}
