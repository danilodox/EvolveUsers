package com.danilo.evolveusers.ui.userdetails

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.danilo.evolveusers.R
import com.danilo.evolveusers.data.database.DBHelper
import com.danilo.evolveusers.data.model.User
import com.danilo.evolveusers.databinding.FragmentUserDetailsBinding
import com.danilo.evolveusers.util.convertDate
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.sql.Timestamp
import java.util.Calendar

class UserDetailsFragments : Fragment() {

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: DBHelper
    private val viewModel: UserDetailsViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()
        db = DBHelper(context)

        initUiElements()

        val userModel = User (
            nome = binding.editTextNome.text.toString(),
            username = binding.editTextUsername.text.toString(),
            password = binding.editTextPassword.text.toString(),
            foto = binding.editTextFoto.text.toString(),
            endereco = binding.editTextEndereco.text.toString(),
            email = binding.editTextEmail.text.toString(),
            dataNascimento = Timestamp(System.currentTimeMillis()),
            sexo = binding.editTxtSexo.text.toString(),
            cpfCnpj = ""
        )

        val sexOptions = resources.getStringArray(R.array.sex_options)
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, sexOptions)

        binding.btnDatePicker.setOnClickListener {
            userModel.dataNascimento = showDatePickerDialog(userModel)
        }


        viewModel.userDetailsFormState.observe(viewLifecycleOwner) {userDetailsFormState ->
            if (userDetailsFormState == null) {
                return@observe
            }

            binding.btnUpdate.isEnabled = userDetailsFormState.isDataValid
            userDetailsFormState.emailError?.let {
                binding.editTextEmail.error = getString(it)
            }
            userDetailsFormState.passwordError?.let {
                binding.editTextPassword.error = getString(it)
            }

        }


        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //ignore
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //ignore
            }

            override fun afterTextChanged(s: Editable?) {
                userModel.nome = binding.editTextNome.text.toString()
                userModel.username = binding.editTextUsername.text.toString()
                userModel.password = binding.editTextPassword.text.toString()
                userModel.foto = binding.editTextFoto.text.toString()
                userModel.endereco = binding.editTextEndereco.text.toString()
                userModel.email = binding.editTextEmail.text.toString()
                // userModel.dataNascimento = convertDate.parseStringToTimestamp(binding.dataEditTxt.text.toString()) ?: Timestamp(System.currentTimeMillis())
                //userModel.cpfCnpj = binding.cpfCnpjEditTxt.text.toString()
                viewModel.userDetailsDataChanged(
                    userModel.email,
                    userModel.password
                )
            }
        }


        binding.editTxtSexo.setAdapter(adapter)
        binding.editTxtSexo.setOnItemClickListener { parent, view, position, id ->
            val selectedSex = parent.getItemAtPosition(position).toString()
            binding.editTxtSexo.setText(selectedSex)
        }

        binding.editTextNome.addTextChangedListener(afterTextChangedListener)
        binding.editTextUsername.addTextChangedListener(afterTextChangedListener)
        binding.editTextPassword.addTextChangedListener(afterTextChangedListener)
        binding.editTextFoto.addTextChangedListener(afterTextChangedListener)
        binding.editTextEndereco.addTextChangedListener(afterTextChangedListener)
        binding.editTextEmail.addTextChangedListener(afterTextChangedListener)
        binding.editTxtSexo.addTextChangedListener(afterTextChangedListener)


        binding.editTextPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (viewModel.update(db, userModel, context)) {

                }
            }
            false
        }

        binding.btnUpdate.setOnClickListener {
            if (viewModel.update(db, userModel, context)) {

                Toast.makeText(context, "Usuário atualizado com sucesso!", Toast.LENGTH_LONG).show()
            }
        }


    }

    private fun showDatePickerDialog(userModel: User) : Timestamp {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
            val selectedDate = "${dayOfMonth}/${month + 1}/${year}"
            binding.btnDatePicker.text = selectedDate

            convertDate?.let {
                userModel.dataNascimento = it.parseStringToTimestamp(selectedDate)!!
            }


        },
            year, month, dayOfMonth
        )
        datePickerDialog.show()
        return userModel.dataNascimento
    }


    private fun initUiElements() {
        with (viewModel) {
            mUserDetailsLiveData.observe(viewLifecycleOwner, {
                binding.editTextNome.setText(it.nome)
                binding.editTextUsername.setText(it.username)
                binding.editTextPassword.setText(it.password)
                binding.editTextFoto.setText(it.foto)
                binding.editTextEndereco.setText(it.endereco)
                binding.editTextEmail.setText(it.email)
                binding.btnDatePicker.setText(it.dataNascimento.toString())
                binding.editTxtSexo.setText(it.sexo)
                //binding.editTextCPF.setText(it.cpfCnpj)
            })

            getUser(db,UserDetailsFragmentsArgs.fromBundle(requireArguments()).cpfCnpj)
        }
    }

    private fun showUpdateFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }



}