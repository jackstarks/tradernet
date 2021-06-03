package com.jackstarks.tradernet.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jackstarks.tradernet.R
import com.jackstarks.tradernet.WebServicesProvider
import com.jackstarks.tradernet.databinding.SplashFragmentBinding
import com.jackstarks.tradernet.model.Ticker
import com.jackstarks.tradernet.ui.adapters.PostAdapter

class SplashFragment : Fragment() {
    private lateinit var adapter: PostAdapter

    private lateinit var viewModel: SplashViewModel
    private lateinit var binding: SplashFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.splash_fragment, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(
            this.requireActivity(),
            MyViewModelFactory(MainInteractor(repository = MainRepository(webServicesProvider = WebServicesProvider())))
        ).get(SplashViewModel::class.java)
        binding.viewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner
        setRecyclerView()


    }

    private fun setRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        adapter = PostAdapter(mutableSetOf(), viewModel)
        binding.recyclerView.adapter = adapter
        viewModel.tickers.observe(viewLifecycleOwner, object : Observer<MutableSet<Ticker>> {
            override fun onChanged(list: MutableSet<Ticker>) {
                adapter.updateData(list)
            }
        })

    }


}







