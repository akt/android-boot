/*
 *
 * Copyright 2015 TedXiong xiong-wei@hotmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package video.ui;

import java.io.Serializable;

/**
 * 视频对象
 * created by ak
 */
public class CtVideo implements Serializable {
    private String mVideoName;//视频名称 如房源视频、小区视频
    private String mVideoUrl;//视频的地址列表
    private String dispatchUrl;

    public CtVideo() {
    }

    public CtVideo(String mVideoName, String mVideoUrl, String dispatchUrl) {
        this.mVideoName = mVideoName;
        this.mVideoUrl = mVideoUrl;
        this.dispatchUrl = dispatchUrl;
    }

    public String getmVideoName() {
        return mVideoName;
    }

    public void setmVideoName(String mVideoName) {
        this.mVideoName = mVideoName;
    }

    public String getmVideoUrl() {
        return mVideoUrl;
    }

    public void setmVideoUrl(String mVideoUrl) {
        this.mVideoUrl = mVideoUrl;
    }

    public String getDispatchUrl() {
        return dispatchUrl;
    }

    public void setDispatchUrl(String dispatchUrl) {
        this.dispatchUrl = dispatchUrl;
    }
}
