package com.example.yaplacaklarlistesi.View

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.yaplacaklarlistesi.Model.Type
import com.example.yaplacaklarlistesi.R
import com.example.yaplacaklarlistesi.ViewModel.TypeDialogViewModel
import dagger.hilt.android.AndroidEntryPoint
import yuku.ambilwarna.AmbilWarnaDialog

@AndroidEntryPoint
class TypeDialogFragment : DialogFragment() {

    private val viewModel: TypeDialogViewModel by viewModels()
    private val args: TypeDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val type: Type? = args.type
        var choosenColor = "#FF5C5C"

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.fragment_type_dialog)
        val name = dialog.findViewById<EditText>(R.id.row_type_dialog_name)
        val colorLayout = dialog.findViewById<RelativeLayout>(R.id.row_type_dialog_color_layout)
        val colorView = dialog.findViewById<View>(R.id.row_type_dialog_color_view)
        val save = dialog.findViewById<Button>(R.id.row_type_dialog_save)

        type?.let {
            name.setText(it.title)
            colorView.setBackgroundColor(Color.parseColor(it.color))
            choosenColor = it.color
        }


        colorLayout.setOnClickListener {
            val ambilWarnaDialog = AmbilWarnaDialog(requireContext(), Color.parseColor(choosenColor),
                object: AmbilWarnaDialog.OnAmbilWarnaListener {
                    override fun onCancel(dialog: AmbilWarnaDialog?) {

                    }
                    override fun onOk(dialog: AmbilWarnaDialog?, _color: Int) {
                        choosenColor = "#" + Integer.toHexString(_color).substring(2)
                        colorView.setBackgroundColor(Color.parseColor(choosenColor))
                    }

                })
            ambilWarnaDialog.show()
        }
        save.setOnClickListener {
            type?.let {
                if (name.text.isNotEmpty()) {
                    it.title = name.text.toString()
                    it.color = choosenColor
                    viewModel.saveType(it)
                }
            } ?: kotlin.run {
                if (name.text.isNotEmpty()) {
                    viewModel.saveType(Type(name.text.toString(), choosenColor))
                }
            }
            dismiss()
        }

        dialog.create()

        return dialog
    }

}