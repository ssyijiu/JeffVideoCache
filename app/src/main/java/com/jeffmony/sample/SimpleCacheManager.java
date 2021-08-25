package com.jeffmony.sample;

import com.jeffmony.videocache.VideoProxyCacheManager;
import com.jeffmony.videocache.listener.IVideoCacheListener;
import com.jeffmony.videocache.model.VideoCacheInfo;
import com.jeffmony.videocache.utils.LogUtils;
import com.jeffmony.videocache.utils.ProxyCacheUtils;

import java.util.Map;

/**
 * Created by ssyijiu on 2021/8/22.
 * 简单的缓存管理类，与播放器无关
 */
public class SimpleCacheManager {

    private static final String TAG = "RangeCacheManager";

    private final String mVideoUrl;

    private final IVideoCacheListener mCacheListener = new IVideoCacheListener() {
        @Override
        public void onCacheStart(VideoCacheInfo cacheInfo) {
        }

        @Override
        public void onCacheProgress(VideoCacheInfo cacheInfo) {
            LogUtils.i(TAG, "onCacheProgress: cachedSize = " + cacheInfo.getCachedSize() + ", percent = " + cacheInfo.getPercent());
        }

        @Override
        public void onCacheError(VideoCacheInfo cacheInfo, int errorCode) {
            LogUtils.i(TAG, "onCacheError: cachedSize = " + cacheInfo.getCachedSize() + ", errorCode = " + errorCode);
        }

        @Override
        public void onCacheForbidden(VideoCacheInfo cacheInfo) {
            LogUtils.i(TAG, "onCacheForbidden: cachedSize = " + cacheInfo.getCachedSize());
        }

        @Override
        public void onCacheFinished(VideoCacheInfo cacheInfo) {
            LogUtils.i(TAG, "onCacheFinished: cachedSize = " + cacheInfo.getCachedSize());
        }
    };

    public SimpleCacheManager(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    public void startCache(Map<String, String> headers, Map<String, Object> extraParams) {
        VideoProxyCacheManager.getInstance().addCacheListener(mVideoUrl, mCacheListener);
        VideoProxyCacheManager.getInstance().setPlayingUrlMd5(ProxyCacheUtils.computeMD5(mVideoUrl));
        VideoProxyCacheManager.getInstance().startRequestVideoInfo(mVideoUrl, headers, extraParams);
    }

    public void pauseCache() {
        LogUtils.i(TAG, "pauseCache");
        VideoProxyCacheManager.getInstance().pauseCacheTask(mVideoUrl);
    }

    public void resumeCache() {
        LogUtils.i(TAG, "resumeCache");
        VideoProxyCacheManager.getInstance().resumeCacheTask(mVideoUrl);
    }

    public void seekToCachePosition(float percent) {
        LogUtils.i(TAG, "seekToCachePosition: percent = " + percent);
        VideoProxyCacheManager.getInstance().seekToCacheTaskFromClient(mVideoUrl, percent);
    }

    public void releaseCache() {
        LogUtils.i(TAG, "releaseCache");
        VideoProxyCacheManager.getInstance().stopCacheTask(mVideoUrl);
        VideoProxyCacheManager.getInstance().releaseProxyReleases(mVideoUrl);
    }
}
