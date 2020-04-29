package com.example.xyzreader.utils;

import android.content.Context;
import android.os.Environment;

import com.example.xyzreader.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TextDocumentUtils {

    //genereaza un fisier txt unde scriem continutul dorit, numele dorit, salvam fisierul si returnam calea acestuia
    public static String generateTxtFile(Context context, String sFileName, String sBody) {
        boolean isDirectoryCreated;
        try {
            File root = new File(Environment.getExternalStorageDirectory(), context.getString(R.string.Notes));
            if (!root.exists()) {
                isDirectoryCreated = root.mkdirs();
            } else isDirectoryCreated = true;

            if(isDirectoryCreated) {
                File file = new File(root, sFileName);
                FileWriter writer = new FileWriter(file);
                writer.append(sBody);
                writer.flush();
                writer.close();
            }

            return Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.Notes) + "/" + sFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
