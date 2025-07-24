package com.example.elearningplatform.ui.course

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.elearningplatform.databinding.FragmentPlayerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private val args: PlayerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // ── UI title ──────────────────────────────────────────────────────────────
        requireActivity().title = args.lessonTitle

        // ── YouTubePlayerView setup ───────────────────────────────────────────────
        val ytView = binding.youtubePlayerView
        lifecycle.addObserver(ytView)   // let the player follow fragment lifecycle

        // Extract the video‑ID from any youtu.be or youtube.com URL
        // PlayerFragment.kt, in onViewCreated()
        Log.d("PlayerFragment", "🔥 lessonId raw = '${args.lessonId}'")
        val videoId = args.lessonId.extractYoutubeIdOrNull()
        Log.d("PlayerFragment", "🔥 extracted = $videoId")

        if (videoId == null) {
            Toast.makeText(requireContext(),
                "Invalid YouTube link stored for this lesson.",
                Toast.LENGTH_SHORT).show()
            return
        }

        ytView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                player.loadVideo(videoId, /* startSeconds = */ 0f)
            }
        })
    }

    override fun onDestroyView() {
        binding.youtubePlayerView.release()   // always safe
        _binding = null
        super.onDestroyView()
    }
}

/* -------------------------------------------------------------------------- */
/* Helper extension: pull the 11‑char ID from any common YouTube URL or ID    */
/* -------------------------------------------------------------------------- */
private fun String.extractYoutubeIdOrNull(): String? {
    val idRegex = Regex("^[A-Za-z0-9_-]{11}$")
    val candidate = trim()

    // 1. Already just an ID?
    if (idRegex.matches(candidate)) return candidate

    // 2. Try to parse as a URI
    val uri = try { Uri.parse(candidate) } catch (_: Exception) { return null }

    // 2a. Regular watch URLs: …/watch?v=xxxx
    uri.getQueryParameter("v")?.let { if (idRegex.matches(it)) return it }

    // 2b. Short links, embed, shorts, etc.
    val segments = uri.pathSegments
    if (segments.isNotEmpty()) {
        // youtu.be/xxxx
        if (uri.host?.contains("youtu.be") == true && idRegex.matches(segments[0]))
            return segments[0]

        // …/embed/xxxx  or  …/v/xxxx  or  …/shorts/xxxx
        val keyIdx = segments.indexOfFirst { it in listOf("embed", "v", "vi", "shorts") }
        if (keyIdx != -1 && keyIdx + 1 < segments.size) {
            val possibleId = segments[keyIdx + 1]
            if (idRegex.matches(possibleId)) return possibleId
        }
    }

    return null // nothing matched
}
