package com.example.elearningplatform.ui.course

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.elearningplatform.databinding.FragmentPlayerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerFragment : Fragment() {

    private var _b: FragmentPlayerBinding? = null
    private val b get() = _b!!
    private val args: PlayerFragmentArgs by navArgs()

    /* --------------------------------------------------------------------- */
    /* lifecycle                                                             */
    /* --------------------------------------------------------------------- */

    override fun onCreateView(
        i: LayoutInflater, c: ViewGroup?, s: Bundle?
    ): View {
        _b = FragmentPlayerBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        // 1. Force device to LANDSCAPE immediately
        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

        // 2. Hide the app bar for true full-screen
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        // 3. Video setup
        lifecycle.addObserver(b.youtubePlayerView)

        val raw = args.videoUrl
        Log.d("PlayerFragment", "🔥 videoUrl raw = '$raw'")
        val videoId = raw.extractYoutubeIdOrNull()
        Log.d("PlayerFragment", "🔥 extracted = $videoId")

        if (videoId == null) {
            Toast.makeText(
                requireContext(),
                "Invalid YouTube link stored for this lesson.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        b.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                player.loadVideo(videoId, 0f)
            }
        })
    }

    /** Restore portrait + toolbar once the user leaves the player */
    override fun onDestroyView() {
        requireActivity().requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        b.youtubePlayerView.release()
        _b = null
        super.onDestroyView()
    }
}

/* ---------------------------- helper ------------------------------------ */
private fun String.extractYoutubeIdOrNull(): String? {
    val idRx = Regex("^[A-Za-z0-9_-]{11}$")
    val s = trim()
    if (idRx.matches(s)) return s

    val uri = runCatching { Uri.parse(s) }.getOrNull() ?: return null
    uri.getQueryParameter("v")?.let { if (idRx.matches(it)) return it }

    val seg = uri.pathSegments
    if (seg.isNotEmpty()) {
        if (uri.host?.contains("youtu.be") == true && idRx.matches(seg[0])) return seg[0]
        val idx = seg.indexOfFirst { it in listOf("embed", "v", "vi", "shorts") }
        if (idx != -1 && idx + 1 < seg.size && idRx.matches(seg[idx + 1])) return seg[idx + 1]
    }
    return null
}
