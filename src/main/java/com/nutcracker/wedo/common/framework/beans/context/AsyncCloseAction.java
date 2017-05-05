package com.nutcracker.wedo.common.framework.beans.context;

/**
 * Created by huh on 2017/3/31.
 */
public interface AsyncCloseAction {
    boolean canAsyncClose();

    void asyncClose();
}
