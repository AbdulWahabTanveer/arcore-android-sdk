package com.google.ar.core.examples.java.augmentedfaces;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import com.google.ar.core.AugmentedFace;
import com.google.ar.core.examples.java.common.rendering.ShaderUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;

/** Renders an AugmentedFace with glasses in OpenGL. */
public class GlassesRenderer {
    private static final String TAG = GlassesRenderer.class.getSimpleName();

    private int modelViewUniform;
    private int modelViewProjectionUniform;
    private int textureUniform;

    private final int[] textureId = new int[1];

    private static final String VERTEX_SHADER_NAME = "shaders/glasses.vert";
    private static final String FRAGMENT_SHADER_NAME = "shaders/glasses.frag";
    private int program;
    private final float[] modelViewProjectionMat = new float[16];
    private final float[] modelViewMat = new float[16];

    private FloatBuffer vertexBuffer;

    private ShortBuffer indexBuffer;

    public GlassesRenderer() {}

    public void createOnGlThread(Context context, String glassesObjAssetName) throws IOException {
        final int vertexShader =
                ShaderUtil.loadGLShader(TAG, context, GLES20.GL_VERTEX_SHADER, VERTEX_SHADER_NAME);
        final int fragmentShader =
                ShaderUtil.loadGLShader(TAG, context, GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_NAME);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);

        modelViewProjectionUniform = GLES20.glGetUniformLocation(program, "u_ModelViewProjection");
        modelViewUniform = GLES20.glGetUniformLocation(program, "u_ModelView");
        textureUniform = GLES20.glGetUniformLocation(program, "u_Texture");

        // Load the glasses .obj file here
        // This is a placeholder, you'll need to integrate an OBJ loader to load the glasses model
        // and bind it to the OpenGL context.

        ObjParser parser = new ObjParser(context, glassesObjAssetName);
        List<float[]> vertices = parser.getVertices();
        List<int[]> indices = parser.getIndices();

        // Convert vertices and indices to buffers for OpenGL rendering
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.size() * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        for (float[] vertex : vertices) {
            vertexBuffer.put(vertex);
        }
        vertexBuffer.position(0);

        ByteBuffer ib = ByteBuffer.allocateDirect(indices.size() * 2);
        ib.order(ByteOrder.nativeOrder());
        indexBuffer = ib.asShortBuffer();
        for (int[] index : indices) {
            indexBuffer.put((short) index[0]);
            indexBuffer.put((short) index[1]);
            indexBuffer.put((short) index[2]);
        }
        indexBuffer.position(0);
    }

    public void draw(float[] projmtx, float[] viewmtx, AugmentedFace face) {
        FloatBuffer vertices = face.getMeshVertices();
        GLES20.glUseProgram(program);

        float[] modelViewProjectionMatTemp = new float[16];
        Matrix.multiplyMM(modelViewProjectionMatTemp, 0, projmtx, 0, viewmtx, 0);
        Matrix.multiplyMM(modelViewProjectionMat, 0, modelViewProjectionMatTemp, 0, modelViewMat, 0);

        GLES20.glUniformMatrix4fv(modelViewUniform, 1, false, modelViewMat, 0);
        GLES20.glUniformMatrix4fv(modelViewProjectionUniform, 1, false, modelViewProjectionMat, 0);

        int attriVertices = 0;
        GLES20.glEnableVertexAttribArray(attriVertices);
        GLES20.glVertexAttribPointer(attriVertices, 3, GLES20.GL_FLOAT, false, 0, vertices);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glUniform1i(textureUniform, 0);

        // Bind the glasses texture and draw the glasses on the face
        // This is a placeholder, you'll need to bind the glasses texture and draw it on the face.

        GLES20.glUseProgram(0);
    }
}
