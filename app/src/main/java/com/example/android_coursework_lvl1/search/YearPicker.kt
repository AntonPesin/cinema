package com.example.android_coursework_lvl1.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.android_coursework_lvl1.databinding.YearpickerBinding
import java.util.Calendar

class YearPickerDialog : DialogFragment() {

    private var _binding: YearpickerBinding? = null
    private val binding get() = _binding!!

    private var onYearSetListener: ((year: Int, type: String) -> Unit)? = null

    companion object {
        fun newInstance(yearValue: Int, type: String): YearPickerDialog {
            val args = Bundle()
            args.putInt("year_value", yearValue)
            args.putString("picker_type", type)
            val fragment = YearPickerDialog()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = YearpickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val minYear = 1900
        val yearValue = arguments?.getInt("year_value", 2000) ?: 2000
        val pickerType = arguments?.getString("picker_type", "") ?: ""

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