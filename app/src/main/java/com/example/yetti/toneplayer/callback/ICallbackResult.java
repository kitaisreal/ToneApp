package com.example.yetti.toneplayer.callback;


public interface ICallbackResult<Result> {
    void onSuccess(Result result);
    void onError(Exception e);
}
