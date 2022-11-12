package com.example.tucon.ui.settings

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tucon.databinding.FragmentSettingPageBinding
import com.example.tucon.service.UpdateService
import com.example.tucon.utils.AlertDialogBuilder
import com.example.tucon.utils.CacheManager


class SettingPageFragment: Fragment() {

    private var _binding: FragmentSettingPageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AppSettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingPageBinding.inflate(inflater, container, false)
        viewModel.latestVersion.observe(viewLifecycleOwner) {
            binding.latestVersionText.text = "最新版本: ".plus(it)
        }
        viewModel.cacheSize.observe(viewLifecycleOwner) {
            binding.cacheSize.text = "缓存占用: $it"
        }
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getVersion()
        viewModel.setCacheSize(CacheManager.getTotalCacheSize(context))
        binding.cacheClear.setOnClickListener {
            AlertDialogBuilder.build(requireContext(), "确定清除缓存?", "是的", "不") { dialog, _ ->
                context?.let { CacheManager.clearAllCache(it) }
                viewModel.setCacheSize(CacheManager.getTotalCacheSize(context))
                Toast.makeText(context, "清除成功!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        binding.apkDown.setOnClickListener {
            if (viewModel.currentVersion == viewModel.latestVersion.value) {
                Toast.makeText(context, "已经是最新版了", Toast.LENGTH_SHORT).show()
            } else {
                AlertDialogBuilder.build(requireContext(), "确定要下载最新版安装包吗?", "是的", "不") { dialog, _ ->
                    activity?.startService(Intent(context, UpdateService::class.java))
                    dialog.dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getVersion() {
        val packageManager: PackageManager? = context?.packageManager
        val packageInfo = context?.packageName?.let { packageManager?.getPackageInfo(it, 0) }
        viewModel.currentVersion = packageInfo?.versionName.toString()
        viewModel.updateLatestVersion()
    }

}