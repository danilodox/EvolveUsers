package com.danilo.evolveusers.ui.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.danilo.evolveusers.R
import com.danilo.evolveusers.data.database.DBHelper
import com.danilo.evolveusers.databinding.FragmentUsersBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class UsersFragment : Fragment(R.layout.fragment_users) {

    private val viewModel : UsersViewModel by viewModel()
    private lateinit var binding : FragmentUsersBinding
    private lateinit var db: DBHelper
    companion object {
        fun newInstance() = UsersFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUsersBinding.inflate(inflater, container, false)
        val context = requireContext()
        db = DBHelper(context)

        initUiElements()
       // initObservers()
        getAllUsers()

        return binding.root
    }

    private fun getAllUsers() {

        viewModel.getUsers(db)

    }

    private fun initUiElements() {
        initRecyclerObserver()
    }

    private fun initRecyclerObserver() {


        viewModel.mUsersLiveData.observe(viewLifecycleOwner) {

            it.let { users ->
                with(binding.recyclerListUsers) {

                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)



                    adapter = UsersAdapter(users) { results ->
                        val bundle = Bundle().apply {
                            putString("cpfCnpj", results.cpfCnpj)
                        }
                        findNavController().navigate(
                            R.id.action_usersFragment_to_userDetailsFragment,
                            bundle
                        )



                    }
                }
            }
        }

    }

    private fun initObservers() {
        showLoading()
        showError()
    }

    private fun showError() {
        TODO("Not yet implemented")
    }

    private fun showLoading() {
        TODO("Not yet implemented")
    }



}