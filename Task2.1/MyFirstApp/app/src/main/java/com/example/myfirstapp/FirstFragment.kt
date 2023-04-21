package com.example.myfirstapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myfirstapp.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            buttonFirst_count_fun(view)
        }

        binding.TOAST.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        //为TOAST按钮添加事件
        view.findViewById<Button>(R.id.TOAST).setOnClickListener{
            Toast.makeText(context, "TOAST clicked", Toast.LENGTH_SHORT).show()
        }

        binding.RANDOM.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        //为RANDOM按钮添加事件
        view.findViewById<Button>(R.id.RANDOM).setOnClickListener{
            val (current_count, randomNum) = RANDOM_fun(view)
            //将两个值都传递给 SecondFragment
            val bundle = Bundle()
            bundle.putInt("current_count", current_count)
            bundle.putInt("randomNum", randomNum)

            val secondFragment = SecondFragment()
            secondFragment.arguments = bundle

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container_view, secondFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    private fun buttonFirst_count_fun(view: View) {
        val  showCountTextView = view.findViewById<TextView>(R.id.textview_first)
        val count = (0..100).random()
        showCountTextView.text = count.toString()
    }
    private fun RANDOM_fun(view: View): Pair<Int, Int> {
        val showCountTextView = view.findViewById<TextView>(R.id.textview_first)
        val currentCount = showCountTextView.text.toString().toInt()
        // 由 COUNT 生成的数当做范围
        val randomNum = (0..currentCount).random()
        // 返回包含两个 Int 值的 Pair 对象
        return Pair(currentCount, randomNum)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}