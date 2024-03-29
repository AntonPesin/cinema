package com.example.android_coursework_lvl1.ui.fragments.yearPicker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.android_coursework_lvl1.model.enums.Keys
import com.example.android_coursework_lvl1.databinding.YearpickerBinding
import java.util.Calendar

class YearPickerDialog : DialogFragment() {

    private var _binding: YearpickerBinding? = null
    private val binding get() = _binding!!

    private var onYearSetListener: ((year: Int, type: String) -> Unit)? = null

    companion object {
        fun newInstance(yearValue: Int, type: String): YearPickerDialog {
            val args = Bundle()
            args.putInt(Keys.YEAR_VALUE.name, yearValue)
            args.putString(Keys.TYPE.name, type)
            val fragment = YearPickerDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = YearpickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val minYear = 1900
        val yearValue = arguments?.getInt(Keys.YEAR_VALUE.name, 2000) ?: 2000
        val pickerType = arguments?.getString(Keys.PICKER_TYPE.name, "") ?: ""

        binding.yearPicker.minValue = minYear
        binding.yearPicker.maxValue = currentYear
        binding.yearPicker.wrapSelectorWheel = false
        binding.yearPicker.value = yearValue

        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        binding.selectButton.setOnClickListener {
            onYearSetListener?.invoke(binding.yearPicker.value, pickerType)
            dismiss()
        }
    }

    fun setOnYearSetListener(listener: (year: Int, type: String) -> Unit) {
        onYearSetListener = listener
    }

}