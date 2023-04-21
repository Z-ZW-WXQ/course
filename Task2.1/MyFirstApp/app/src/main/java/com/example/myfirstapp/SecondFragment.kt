package com.example.myfirstapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myfirstapp.databinding.FragmentSecondBinding
/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
//    val  args: SecondFragmentArgs by navArgs()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        //给PREVIOUS按钮设置点击事件，回到第FirstFragment
        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            activity?.onBackPressed()
        }

        //接收FirstFragment的current_count值
        val current_count = arguments?.getInt("current_count") ?: 0
        val textView = view.findViewById<TextView>(R.id.textView)
        textView.text = getString(R.string.random_number_text, current_count)

        //接收FirstFragment的randomNum值
        val randomNum = arguments?.getInt("randomNum") ?: 0
        val textview_second = view.findViewById<TextView>(R.id.textview_second)
        textview_second.text = randomNum.toString()




    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}