package com.danilo.evolveusers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.danilo.evolveusers.databinding.FragmentWelcomeBinding
import androidx.navigation.fragment.findNavController





class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private lateinit var binding : FragmentWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signupBtn.setOnClickListener{

            findNavController().navigate(R.id.action_welcomeFragment_to_signupFragment)

        }
        binding.loginBtn.setOnClickListener{

            findNavController().navigate(R.id.action_welcomeFragment_to_usersFragment)

        }
    }


}