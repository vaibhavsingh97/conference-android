package com.systers.conference.callback;

import java.util.List;

public interface ListResponseCallback<T> {
    void onSuccess(List<T> response);
}
