package com.example.yetti.toneplayer.imageLoader.diskCache;


public interface IImageCallbackResult<Result> {
    void onSuccess(Result pResult);
    void onError(Result pResult);
}
