package com.sykent.gl.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/04/21
 */
public class GLCoordBuffer {
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTexCoordBuffer;

    private int mVertexCount;
    private int mCoordsPerVertex;
    private int mVertexStride;
    private int mTexCoordStride;

    public GLCoordBuffer(float[] vertexCoord, float[] textureCoord) {
        mVertexBuffer = generateFloatBuffer(vertexCoord);
        mTexCoordBuffer = generateFloatBuffer(textureCoord);

        mCoordsPerVertex = 2; // 一次取多少个
        mVertexStride = mCoordsPerVertex * SIZE_OF_FLOAT; // 顶点坐标一次取多少字节
        mVertexCount = vertexCoord.length / mCoordsPerVertex; // 顶点个数
        mTexCoordStride = mCoordsPerVertex * SIZE_OF_FLOAT; // 纹理坐标一次取多少字节
    }


    public FloatBuffer getVertexBuffer() {
        return mVertexBuffer;
    }

    public FloatBuffer getTexCoordBuffer() {
        return mTexCoordBuffer;
    }


    /**
     * Returns the number of vertices stored in the vertex array.
     */
    public int getVertexCount() {
        return mVertexCount;
    }

    /**
     * Returns the width, in bytes, of the data for each vertex.
     */
    public int getVertexStride() {
        return mVertexStride;
    }

    /**
     * Returns the width, in bytes, of the data for each texture coordinate.
     */
    public int getTexCoordStride() {
        return mTexCoordStride;
    }

    /**
     * Returns the number of position coordinates per vertex.  This will be 2 or 3.
     */
    public int getCoordsPerVertex() {
        return mCoordsPerVertex;
    }

    private FloatBuffer generateFloatBuffer(float[] floatArr) {
        FloatBuffer buffer = ByteBuffer.allocateDirect(floatArr.length * 4) // 创建数据缓存区
                .order(ByteOrder.nativeOrder())                         // 设置字节顺序
                .asFloatBuffer()                                        // 转换为Float 型缓冲
                .put(floatArr);                                        // 缓冲区填入数据
        buffer.position(0);                                           // 设置缓存区起点位置

        return buffer;
    }

    private static final int SIZE_OF_FLOAT = 4; // 四个字节

    // 铺满视口的默认顶点坐标
    public static final float[] DEFAULT_VERTEX_COORDINATE = {
            -1.0f, -1.0f,        // 0 left bottom
            1.0f, -1.0f,         // 1 right bottom
            -1.0f, 1.0f,          // 2 left top
            1.0f, 1.0f            // 3 right top
    };

    // 铺满视口默认的纹理坐标
    public static final float[] DEFAULT_TEXTURE_COORDINATE = {
            0.0f, 0.0f,            // 0 left bottom
            1.0f, 0.0f,            // 1 right bottom
            0.0f, 1.0f,            // 2 left top
            1.0f, 1.0f,            // 3 right top
    };

    // 铺满视口默认上下翻转的纹理坐标
    public static final float[] DEFAULT_FLIP_Y_TEXTURE_COORDINATE = {
            0.0f, 1.0f,            // 0 left top
            1.0f, 1.0f,            // 1 right top
            0.0f, 0.0f,            // 2 left bottom
            1.0f, 0.0f,            // 3 right bottom
    };
}
