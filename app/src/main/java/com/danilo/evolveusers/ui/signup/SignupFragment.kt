package com.danilo.evolveusers.ui.signup

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.StringRes
import com.danilo.evolveusers.R
import com.danilo.evolveusers.data.database.DBHelper
import com.danilo.evolveusers.data.model.User
import com.danilo.evolveusers.databinding.FragmentSignupBinding
import com.danilo.evolveusers.util.convertDate
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.sql.Timestamp
import java.util.Calendar

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: DBHelper
    private val viewModel: SignupViewModel by viewModel()


       override fun onCreateView(
           inflater: LayoutInflater, container: ViewGroup?,
           savedInstanceState: Bundle?
       ): View? {

           _binding = FragmentSignupBinding.inflate(inflater, container, false)
           return binding.root
       }

       override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
           super.onViewCreated(view, savedInstanceState)
           val context = requireContext()

           db = DBHelper(context)

           val userModel  = User (
               nome = binding.userNameEditTxt.text.toString(),
               username = binding.userNameEditTxt.text.toString(),
               password = binding.passwordEditTxt.text.toString(),
               foto = binding.fotoEditTxt.text.toString(),
               endereco = binding.enderecoEditTxt.text.toString(),
               email = binding.emailEditTxt.text.toString(),
               dataNascimento = Timestamp(System.currentTimeMillis()),
               sexo = binding.sexoEditTxt.text.toString(),
               cpfCnpj = binding.cpfCnpjEditTxt.text.toString()
           )

           val sexOptions = resources.getStringArray(R.array.sex_options)
           val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, sexOptions)



           val signupButton = binding.signupBtn
           val loadingProgressBar = binding.loading

           binding.btnDatePicker.setOnClickListener {
               userModel.dataNascimento = showDatePickerDialog(userModel)
           }





           viewModel.signupFormState.observe(viewLifecycleOwner) {signupFormState ->
               if (signupFormState == null ) {
                   return@observe
               }
               signupButton.isEnabled = signupFormState.isDataValid
               signupFormState.emailError?.let {
                   binding.emailEditTxt.error = getString(it)
               }
               signupFormState.passwordError?.let {
                   binding.passwordEditTxt.error = getString(it)
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
                   userModel.nome = binding.nomeEditTxt.text.toString()
                   userModel.username = binding.userNameEditTxt.text.toString()
                   userModel.password = binding.passwordEditTxt.text.toString()
                   userModel.foto = binding.fotoEditTxt.text.toString()
                   userModel.endereco = binding.enderecoEditTxt.text.toString()
                   userModel.email = binding.emailEditTxt.text.toString()
                  // userModel.dataNascimento = convertDate.parseStringToTimestamp(binding.dataEditTxt.text.toString()) ?: Timestamp(System.currentTimeMillis())
                   userModel.cpfCnpj = binding.cpfCnpjEditTxt.text.toString()
                   viewModel.signupDataChanged(
                       userModel.email,
                       userModel.password
                   )
               }

           }

           binding.sexoEditTxt.setAdapter(adapter)
           binding.sexoEditTxt.setOnItemClickListener { parent, view, position, id ->
               val selectedSex = parent.getItemAtPosition(position).toString()
               binding.sexoEditTxt.setText(selectedSex)
           }


           binding.nomeEditTxt.addTextChangedListener(afterTextChangedListener)
           binding.userNameEditTxt.addTextChangedListener(afterTextChangedListener)
           binding.passwordEditTxt.addTextChangedListener(afterTextChangedListener)
           binding.fotoEditTxt.addTextChangedListener(afterTextChangedListener)
           binding.enderecoEditTxt.addTextChangedListener(afterTextChangedListener)
           binding.emailEditTxt.addTextChangedListener(afterTextChangedListener)
           binding.cpfCnpjEditTxt.addTextChangedListener(afterTextChangedListener)




           binding.passwordEditTxt.setOnEditorActionListener { _, actionId, _ ->
               if (actionId == EditorInfo.IME_ACTION_DONE) {
                   /*if (viewModel.signUp(db, userModel, context)) {
                       clearEditTexts()
                   }*/
               }
               false
           }
           signupButton.setOnClickListener {

               //loadingProgressBar.visibility = View.VISIBLE
               /* (viewModel.signUp(db, userModel, context)) {
                   clearEditTexts()
               }*/

               viewModel.signUp(db, userModel, context) { success ->
                   if (success) {
                       clearEditTexts()
                   } else {
                       // Lógica para tratamento de falha
                   }
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


private fun showSignupFailed(@StringRes errorString: Int) {
    val appContext = context?.applicationContext ?: return
    Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
}
    private fun clearEditTexts() {
        binding.nomeEditTxt.text.clear()
        binding.userNameEditTxt.text.clear()
        binding.passwordEditTxt.text.clear()
        binding.fotoEditTxt.text.clear()
        binding.enderecoEditTxt.text.clear()
        binding.emailEditTxt.text.clear()
        binding.btnDatePicker.text = "Data de Nascimento"
        binding.sexoEditTxt.text.clear()
        binding.cpfCnpjEditTxt.text.clear()

    }

override fun onDestroy() {
    super.onDestroy()
    super.onDestroyView()
    _binding = null
}


}