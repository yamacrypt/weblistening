package com.yamacrypt.webaudionovel.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.yamacrypt.webaudionovel.R
import com.yamacrypt.webaudionovel.TTSController
import kotlinx.android.synthetic.main.fragment_fulltextplayer.*
import java.lang.Exception
import kotlin.concurrent.thread

class FullTextPlayerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return  inflater.inflate(R.layout.fragment_fulltextplayer, container, false);
        //return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mToolbar: Toolbar = requireActivity().findViewById(R.id.tool_bar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(mToolbar)
        mToolbar.setNavigationIcon(R.drawable.ic_group_collapse_13)
        mToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()

        }
        val textList:List<String> = TTSController.getStringList();
        val handler=Handler(Looper.getMainLooper());
        thread {
           try{textList.forEach { element ->
                var tv = TextView(context)
                tv.text = element
                handler.post(Runnable {
                    try {
                        novelFullTextLinearLayout.addView(tv)
                    } catch (e: Exception) {
                    }
                })
               // novelFullTextLinearLayout.addView(tv)
            }
            }
           catch (e:Exception){}
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fulltext,menu);
    }
}