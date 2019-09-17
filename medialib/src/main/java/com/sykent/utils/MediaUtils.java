package com.sykent.utils;

import android.media.MediaMetadataRetriever;

import com.blankj.utilcode.util.FileUtils;
import com.sykent.media.info.VideoInfo;

import java.util.HashMap;

/**
 * @author Sykent.Lao e-mail:sykent.lao@gmail.com blog:https://sykent.github.io/
 * @version 1.0
 * @since 2019/07/03
 */
public class MediaUtils {
    public static VideoInfo getVideoInfo(String path) {
        if (!FileUtils.isFileExists(path)) {
            return null;
        }

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        String rotationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        String width = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        String height = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        VideoInfo info = new VideoInfo();
        info.setPath(path);
        info.setDuration(NumberUtils.string2Int(duration));
        info.setWidth(NumberUtils.string2Int(width));
        info.setHeight(NumberUtils.string2Int(height));
        mmr.release();

        return info;
    }
}
