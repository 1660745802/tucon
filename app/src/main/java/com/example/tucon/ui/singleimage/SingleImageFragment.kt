package com.example.tucon.ui.singleimage

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import coil.load
import com.example.tucon.R
import com.example.tucon.database.FavoriteImageApplication
import com.example.tucon.databinding.FragmentSingleImageBinding
import com.example.tucon.service.ImageDownloadService
import com.example.tucon.utils.BitmapUtil
import com.example.tucon.utils.ImageUrlUtil
import com.example.tucon.viewmodel.FavoriteImageViewModel
import com.example.tucon.viewmodel.FavoriteImageViewModelFactory


class SingleImageFragment : Fragment() {

    private var _binding: FragmentSingleImageBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SingleImageViewModel by viewModels()

    private val databaseViewModel: FavoriteImageViewModel by activityViewModels {
        FavoriteImageViewModelFactory(
            (activity?.application as FavoriteImageApplication).database.imageFavoritedDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sharedViewModel.setUrl(ImageUrlUtil.changeUrlQuality(it.getString("imgUrl"), 1))
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSingleImageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            singleImageFragment = this@SingleImageFragment
            singleImage.load(sharedViewModel.imgUrl.value!!.toUri().buildUpon().build()) {
                placeholder(R.drawable.loading_img)
                error(R.drawable.ic_broken_image)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.single_image_menu, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun likeImage() {
        databaseViewModel.addNewImage(ImageUrlUtil.getBaseUrl(sharedViewModel.imgUrl.value))
        Toast.makeText(context, "收藏成功!", Toast.LENGTH_SHORT).show()

    }
    fun dislikeImage() {
        databaseViewModel.dislikeImage(ImageUrlUtil.getBaseUrl(sharedViewModel.imgUrl.value))
        Toast.makeText(context, "取消收藏成功!", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save_alt -> {
                downloadImage()
                true
            }
            R.id.action_share -> {
                shareImage()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun downloadImage() {
        Toast.makeText(context, "下载开始...", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, ImageDownloadService::class.java)
        intent.putExtra("imagePath", sharedViewModel.imgUrl.value!!)
        activity?.startService(intent)
    }
    private fun shareImage() {
        Toast.makeText(context, "正在拉起分享...", Toast.LENGTH_SHORT).show()
        val intent = Intent()
        intent.setAction(Intent.ACTION_SEND)
            .setType("image/*")
            .putExtra(Intent.EXTRA_STREAM, BitmapUtil.saveImageByBitmap(
                requireContext(),
                ImageUrlUtil.getUrlName(sharedViewModel.imgUrl.value!!),
                context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString(),
                binding.singleImage.drawable.toBitmap())
            )
        activity?.startActivity(Intent.createChooser(intent, "Tucon Share"))
    }


}