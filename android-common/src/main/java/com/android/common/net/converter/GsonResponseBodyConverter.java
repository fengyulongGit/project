/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.common.net.converter;

import com.google.gson.TypeAdapter;
import com.android.common.net.base.APIResponse;
import com.android.common.net.exception.ApiException;
import com.android.common.net.exception.TokenInvalidException;
import com.android.common.net.exception.TokenNotExistException;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, Object> {

    private final TypeAdapter<T> adapter;

    GsonResponseBodyConverter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public Object convert(ResponseBody value) throws IOException {
        try {
            APIResponse response = (APIResponse) adapter.fromJson(value.charStream());
            if (response.isTokenNotExist()) {
                throw new TokenNotExistException();
            } else if (response.isTokenInvalid()) {
                throw new TokenInvalidException();
            } else if (!response.isSuccess()) {
                // 特定 API 的错误，在相应的 Subscriber 的 onError 的方法中进行处理
                throw new ApiException();
            } else if (response.isSuccess()) {
                return response.data;
            }
        } finally {
            value.close();
        }
        return null;
    }
}
