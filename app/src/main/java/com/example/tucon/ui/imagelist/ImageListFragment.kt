package com.example.tucon.ui.imagelist

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tucon.R
import com.example.tucon.data.SettingsDataStore
import com.example.tucon.databinding.FragmentImageListBinding
import kotlinx.coroutines.launch


class ImageListFragment : Fragment() {

    private val viewModel: ImageListViewModel by viewModels()
    private var _binding: FragmentImageListBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingsDataStore: SettingsDataStore
    private var isLinearLayoutManager = false
    private lateinit var recyclerView: RecyclerView

    private lateinit var refreshButton: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImageListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        recyclerView = binding.photosGrid
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener { view, _, _, _, _ ->
                if (!view.canScrollVertically(1)) viewModel.getPhotosAgain(2, false)
            }
        }
        chooseLayout()
        binding.photosGrid.adapter = PhotoGridAdapter(viewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsDataStore = SettingsDataStore(requireContext())
        settingsDataStore.preferenceFlow.asLiveData().observe(viewLifecycleOwner) {
            isLinearLayoutManager = it
            chooseLayout()
            activity?.invalidateOptionsMenu()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun chooseLayout() {
        if (isLinearLayoutManager) {
            recyclerView.layoutManager = LinearLayoutManager(context)
        } else {
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.layout_menu, menu)
        val layoutButton = menu.findItem(R.id.action_switch_layout)
        refreshButton = menu.findItem(R.id.action_refresh)
        chooseRefreshIcon(viewModel.searchMode)
        setIcon(layoutButton)
    }

    private fun setIcon(menuItem: MenuItem?) {
        if (menuItem == null)
            return
        menuItem.icon =
            if (isLinearLayoutManager)
                ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_grid_layout)
            else ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_linear_layout)
    }
    private fun chooseRefreshIcon(searchMode: Int) {
        refreshButton.icon = when (searchMode) {
            0 -> ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_baseline_walk)
            1 -> ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_baseline_run)
            2 -> ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_baseline_bike)
            else -> null
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                isLinearLayoutManager = !isLinearLayoutManager
                chooseLayout()
                setIcon(item)
                lifecycleScope.launch {
                    settingsDataStore.saveLayoutToPreferencesStore(isLinearLayoutManager, requireContext())
                }
                true
            }
            R.id.action_refresh -> {
                viewModel.changeSearchMode()
                chooseRefreshIcon(viewModel.searchMode)
                viewModel.getPhotosAgain()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}