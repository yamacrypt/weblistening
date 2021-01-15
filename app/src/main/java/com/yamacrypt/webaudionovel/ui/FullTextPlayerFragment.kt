package com.yamacrypt.webaudionovel.ui

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.yamacrypt.webaudionovel.R
import com.yamacrypt.webaudionovel.TTSController
import kotlinx.android.synthetic.main.fragment_fulltextplayer.*
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
        val model=PlayerViewModel()
        var color=0;
        val numberObserver: Observer<Int> = Observer {
            val num = model.speakingnumber.value
            for(i in 0..textList.size) {
                try {
                    val textView = novelFullTextLinearLayout.get(i) as TextView
                    if (i == num)
                        textView.setTextColor(Color.BLUE)
                    else
                        textView.setTextColor(resources.getColor(R.color.defaultTextColor))
                    num?.let {
                        if(num%20==0)
                        novelFullTextScrollView.scrollTo(
                            0, novelFullTextLinearLayout.get(
                                it
                            ).top)
                    }

                }
                catch (e:java.lang.Exception){}
            }
            //  str.setText(MainActivity.);
        }
        model.speakingnumber.observe(viewLifecycleOwner, numberObserver)
        thread {
            val currentNumber =  model.speakingnumber.value
            try{textList.forEachIndexed{ index, element ->
                var tv = TextView(context)
                tv.text = element
                color=tv.currentTextColor
                try {
                    if(index==currentNumber)
                    tv.setTextColor(Color.BLUE)
                } catch (e: Exception) {
                }
                handler.post(Runnable {
                    try {
                        novelFullTextLinearLayout.addView(tv)
                    } catch (e: Exception) {
                    }
                })
               // novelFullTextLinearLayout.addView(tv)
            }
            }
           catch (e: Exception){}
            handler.post(Runnable {

                try {
                    if (currentNumber != null) {
                        // novelFullTextScrollView.verticalScrollbarPosition=currentNumber
                        novelFullTextScrollView.scrollTo(
                            0, novelFullTextLinearLayout.get(
                                currentNumber
                            ).top
                        )//scrollTo(0,currentNumber)
                    }
                } catch (e: Exception) {
                }
            })
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fulltext, menu);
    }
}