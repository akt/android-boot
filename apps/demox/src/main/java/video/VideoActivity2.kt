package video

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ak.demo.R
import kotlinx.android.synthetic.main.activity_video2.*
import cn.jzvd.JZVideoPlayerStandard
import cn.jzvd.JZVideoPlayer





/**
 * @author ak
 * @since 2018/6/11
 */
class VideoActivity2: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video2)

        videoplayer.setUp("http://livesec.mudu.tv/watch/hshys4.m3u8?auth_key=1528794723-425669-0-9527970d582b02cc902132c237847dcb",
                JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL,
                "饺子闭眼睛")
        videoplayer.thumbImageView.setImageURI(Uri.parse("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640"))
    }


    override fun onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        JZVideoPlayer.releaseAllVideos()
    }

}