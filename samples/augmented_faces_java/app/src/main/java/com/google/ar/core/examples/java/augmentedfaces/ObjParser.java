package com.google.ar.core.examples.java.augmentedfaces;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ObjParser {
    private List<float[]> vertices = new ArrayList<>();
    private List<float[]> textures = new ArrayList<>();
    private List<float[]> normals = new ArrayList<>();
    private List<int[]> indices = new ArrayList<>();

    public ObjParser(Context context, String assetName) throws IOException {
        InputStream inputStream = context.getAssets().open(assetName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            switch (parts[0]) {
                case "v":
                    vertices.add(new float[]{Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3])});
                    break;
                case "vt":
                    textures.add(new float[]{Float.parseFloat(parts[1]), Float.parseFloat(parts[2])});
                    break;
                case "vn":
                    normals.add(new float[]{Float.parseFloat(parts[1]), Float.parseFloat(parts[2]), Float.parseFloat(parts[3])});
                    break;
                case "f":
                    int[] vertexIndices = {Integer.parseInt(parts[1].split("/")[0]), Integer.parseInt(parts[2].split("/")[0]), Integer.parseInt(parts[3].split("/")[0])};
                    indices.add(vertexIndices);
                    break;
            }
        }
        reader.close();
    }

    public List<float[]> getVertices() {
        return vertices;
    }

    public List<float[]> getTextures() {
        return textures;
    }

    public List<float[]> getNormals() {
        return normals;
    }

    public List<int[]> getIndices() {
        return indices;
    }
}
