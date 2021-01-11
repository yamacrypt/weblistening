package com.yamacrypt.webaudionovel.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.yamacrypt.webaudionovel.DataStore
import com.yamacrypt.webaudionovel.Database.DBProvider
import com.yamacrypt.webaudionovel.Database.DBTableName
import com.yamacrypt.webaudionovel.Database.StoryIndexDB
import com.yamacrypt.webaudionovel.Database.StoryIndexModel
import com.yamacrypt.webaudionovel.FileController
import com.yamacrypt.webaudionovel.MainActivity.Companion.mInterstitialAd
import com.yamacrypt.webaudionovel.R
import com.yamacrypt.webaudionovel.htmlParser.HTMLFactory
import com.yamacrypt.webaudionovel.htmlParser.Kakuyomu.KakuyomuFactory
import com.yamacrypt.webaudionovel.htmlParser.Narou.MIDFactory
import com.yamacrypt.webaudionovel.htmlParser.Narou.MNLTFactory
import com.yamacrypt.webaudionovel.htmlParser.Narou.NarouFactory
import com.yamacrypt.webaudionovel.htmlParser.Narou.NocFactory
import com.yamacrypt.webaudionovel.htmlParser.Wattpad.WattpadFacotry
import com.yamacrypt.webaudionovel.ui.search.selector.*
import kotlinx.android.synthetic.main.fragment_web.*
import java.io.File
import kotlin.concurrent.thread


class WebFragment:Fragment() {
    lateinit var htmlfactory:HTMLFactory
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_web, container, false)
    }

   val webView: WebView? = null
    companion object {
        private const val ARG_PATH: String = "WebFragment.mode"
        /* fun build(block: Builder.() -> Unit) = Builder()
             .apply(block).build()*/
        fun build(block: WebFragment.Builder.() -> Unit) = WebFragment.Builder()
            .apply(block).build()
    }
    class Builder {
        var mode:Int = 0
        fun build(): Bundle {
            val args = Bundle()
            args.putInt(WebFragment.ARG_PATH, mode)
            return args
        }
    }
    var defaulturl:String=""
    //var MODE=0
    val yomou_mode: Int = 1
    val kakuyomu_mode = 2
    val wattpadd_mode = 3
    val noc_mode = 4
    val mnlt_mode = 5
    val mid_mode = 6
    fun Init(){
        val mode=arguments?.getInt(ARG_PATH)
        //htmlfactory=get_Factory(mode)
        if (mode != null) {
            //MODE=mode
        }
        when(mode){
            YOMOU->{
                htmlfactory=NarouFactory()
               // defaulturl=YOMOU_URL
            }
            KAKUYOMU ->{
                htmlfactory= KakuyomuFactory()
            }
            WATTPAD ->{
                htmlfactory=WattpadFacotry()
            }
            NOC->{
                htmlfactory= NocFactory()
            }
            MNLT->{
                htmlfactory= MNLTFactory()
            }
           MID ->{ htmlfactory= MIDFactory()
           }
        }

        defaulturl=htmlfactory.get_defaulturl()
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       Init()
        val webView: WebView =view. findViewById(R.id.search_wevview)
        webView.settings.javaScriptEnabled = true // ★この行を追加★
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                //progressBar?.isEnabled=true
                view?.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                val webUrl = search_wevview?.url
                val check=htmlfactory.downloadable(webUrl)
                menuitem.isVisible=check;
                menuitem.isEnabled=check;
                progressBar?.isEnabled=true
                progressBar?.isVisible=true
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
               progressBar?.isEnabled=false
                progressBar?.isVisible=false
            }
        }

       // val defaulturl=arguments?.getString(ARG_PATH)
        if (defaulturl != null) {
            webView.loadUrl(defaulturl)
        }
        val mToolbar: Toolbar =requireActivity().findViewById(R.id.tool_bar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(mToolbar);
        mToolbar.inflateMenu(R.menu.search_menu);
      mToolbar.setNavigationIcon(R.drawable.ic_dialog_close_dark)
        //mToolbar.setNavigationIcon(R.drawable.abc_vector_)
        mToolbar.setNavigationOnClickListener {
            val navController =
                Navigation.findNavController(requireActivity(),
                    R.id.nav_main_fragment
                )
            navController.navigate(R.id.action_navigation_web_to_navigation_menu);

            //    ~Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //           .setAction("Action", null).show();
        }
      /*  mToolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            if (item.itemId === R.id.getMenu) {
                val webUrl = search_wevview.url
                /*  val check=htmlfactory.downloadable(webUrl)
                      item.isVisible=check;
                  item.isEnabled=check;*/

                // val url="https://ncode.syosetu.com/n9239bj/"
                //getHtmlFromWeb(url+c+"/")
                //  item_Setting()
                // URLcheck(webUrl)
                Toast.makeText(context,getString(R.string.download_start),Toast.LENGTH_SHORT).show()
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                // MainActivity.mInterstitialAd.show();
                SaveNovels(webUrl)
                // do something
            }  else {
                // do something
            }
            false
        })*/
        // val html=getHTML(url)
       //  Log.d("HTML",html)

    }
    lateinit var menuitem:MenuItem
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu,menu)
        menuitem=menu.findItem(R.id.getMenu)
        //if(htmlfactory.downloadable(wenUrl))
        val webUrl = search_wevview.url
       // val check=htmlfactory.downloadable(webUrl)
      //  if(check) {
            menuitem.setOnMenuItemClickListener {

                val webUrl = search_wevview.url
                /*  val check=htmlfactory.downloadable(webUrl)
                item.isVisible=check;
            item.isEnabled=check;*/

                // val url="https://ncode.syosetu.com/n9239bj/"
                //getHtmlFromWeb(url+c+"/")
                //  item_Setting()
                // URLcheck(webUrl)
                Toast.makeText(context, getString(R.string.download_start), Toast.LENGTH_SHORT)
                    .show()
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }

                // MainActivity.mInterstitialAd.show();
                SaveNovels(webUrl)

                //getHtmlFromWeb( webUrl)
                // show(webUrl)
            }
       // }

    }

   /*override fun onCreateOptionsMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu!!, v!!, menuInfo)
        val inflater: MenuInflater = activity?.menuInflater!! //requireActivity().getMenuInflater()
        inflater.inflate(R.menu.search_menu, menu)
       val item=menu.findItem(R.id.getMenu)

       item.setOnMenuItemClickListener {

           val webUrl = search_wevview.url
           // val url="https://ncode.syosetu.com/n9239bj/"
           //getHtmlFromWeb(url+c+"/")
           //  item_Setting()
           // URLcheck(webUrl)
           SaveNovels(webUrl)
           //getHtmlFromWeb( webUrl)
           // show(webUrl)
       }
    }*/


    private fun item_Setting() {
        val webUrl = search_wevview.url
       // URLcheck(webUrl)
        SaveNovels(webUrl)
    }

    val URL_CONTENT=1
    val URL_BOX=2
    val URL_ERROR=0

    val ERROR=0

    //val ignore = HashSet<String>().add("<ruby>")
    private fun SaveNovels(url: String?): Boolean {
       thread {
           val  threadfactory=htmlfactory
           val threadcontext=context
           Filesave(threadfactory,threadcontext,url)
       }
        /*
        Thread(Runnable {
          val  threadfactory=htmlfactory
            val threadcontext=context
            Filesave(threadfactory,threadcontext,url)
        }).start()*/
    return true
    }
    fun Filesave(threadfactory: HTMLFactory,threadcontext: Context?,url: String?){

        threadfactory.parse(url)
        if(threadfactory.BOXcheck){
            val urlls=threadfactory.getChildURLs()
            if (urlls != null) {
                val foldername=threadfactory.getTitle()
                val folder=DataStore.CreateFolder(threadcontext,foldername)
                val db:StoryIndexDB= DBProvider.of(DBTableName.storyindex,requireContext()) as StoryIndexDB
                db.insert_and_update(StoryIndexModel(path=foldername!!,
                    parent_path=DataStore.getShortCutFile(context, "").toString(),
                    index = 0,url=url!!,
                    language =threadfactory.get_language(),
                    novel_name = foldername,
                    link = ""))
                for (childurl in urlls){
                    threadfactory.parse(childurl)
                 //   Filesave(threadfactory,threadcontext,childurl)
                   try {
                        Save(childurl, threadfactory,threadcontext, folder)
                    }
                    catch (e:Exception){break}
                    Thread.sleep(1000)
                }

            }
        }
        else{
            try {
                Save(url, threadfactory,threadcontext)
            }
            catch (e:Exception){}
        }
    }
    fun Save(url:String?,threadfactory: HTMLFactory,threadcontext:Context?,folder: File){
        val title=threadfactory.getTitle()
        val stringList=threadfactory.getTexts()
        val downloader= FileController(threadcontext)
        val convertedurl=DataStore.getConvertedURL(url)
        val novelfile= DataStore.getNovelFile(threadcontext,convertedurl)
        val titlefile= DataStore.getShortCutFile(threadcontext,title,folder)
        val db = DBProvider.of(DBTableName.storyindex, requireContext()) as StoryIndexDB

        downloader.WriteNovel(novelfile,stringList,htmlfactory.get_language())
        //val sc= FileController.ShortCut(title,"",convertedurl,stringList!!.size,htmlfactory.get_language())
        //downloader.WriteShortCut(titlefile,sc)
        db.insert_and_update(
            StoryIndexModel(title!!,folder.toString(),threadfactory.getIndex()!!,url!!,
            language =threadfactory.get_language(),
            novel_name = title,
            link = convertedurl)
        )

    }
    fun Save(url:String?,threadfactory:HTMLFactory,threadcontext: Context?){
        val title=threadfactory.getTitle()
       // val author=threadfactory.getAuthor()
        val stringList=threadfactory.getTexts()
        val downloader= FileController(threadcontext)
        val convertedurl=DataStore.getConvertedURL(url)
        val novelfile= DataStore.getNovelFile(threadcontext,convertedurl)
        val titlefile= DataStore.getShortCutFile(threadcontext,title)
        downloader.WriteNovel(novelfile,stringList,htmlfactory.get_language())
        //val sc= FileController.ShortCut(title,"",convertedurl,stringList!!.size,htmlfactory.get_language())
        //downloader.WriteShortCut(titlefile,sc)
        val db = DBProvider.of(DBTableName.storyindex, requireContext()) as StoryIndexDB
        db.insert_and_update(
            StoryIndexModel(title!!, DataStore.getShortCutFile(context, "").toString(),threadfactory.getIndex()!!,url!!,
                language =threadfactory.get_language(),
                novel_name = title,
                link = convertedurl)
        )
    }


}