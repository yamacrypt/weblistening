package com.yamacrypt.webaudionovel.ui.search.selector

import com.yamacrypt.webaudionovel.MainActivity
import com.yamacrypt.webaudionovel.htmlParser.HTMLFactory
import com.yamacrypt.webaudionovel.htmlParser.Kakuyomu.KakuyomuFactory
import com.yamacrypt.webaudionovel.htmlParser.Narou.NarouFactory
import com.yamacrypt.webaudionovel.htmlParser.Wattpad.WattpadFacotry

fun getWebItemList(): MutableList<WebItem> {
   
    var ls= mutableListOf<WebItem>()
    val yomou=WebItem()
    yomou.language=JP
    yomou.mode=YOMOU
    yomou.title="小説を読もう！"
    val noc=WebItem()
    noc.language=JP
    noc.mode=NOC
    noc.title="ノクターンノベルズ"
    val mnlt=WebItem()
    mnlt.language=JP
    mnlt.mode=MNLT
    mnlt.title="ムーンライトノベルズ"
    val mid=WebItem()
    mid.language=JP
    mid.mode=MID
    mid.title="ミッドナイトトノベルズ"
    val kakuyomu=WebItem()
    kakuyomu.language= JP
    kakuyomu.mode= KAKUYOMU
    kakuyomu.title="カクヨム"
    ls.add(yomou)
  /* ls.add(noc)
    ls.add(mnlt)
    ls.add(mid)*/
    ls.add(kakuyomu)
    val wattpad=WebItem()
    wattpad.language= EN
    wattpad.mode=WATTPAD
    wattpad.title="Wattpad"
    ls.add(wattpad)
    return ls
}
val YOMOU=1
val YOMOU_URL="https://yomou.syosetu.com/"
val KAKUYOMU=2
val KAKUYOMU_URL="https://kakuyomu.jp"
val WATTPAD=3
val WATTPAD_URL="https://www.wattpad.com/"
val NOC=4
val MNLT=5
val MID=6
val JP=1
val EN=1
