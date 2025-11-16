package org.example.springbootdemo;

import com.alibaba.fastjson.JSON;
import com.mysql.cj.util.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MyTest {
    private List<String> keywords2 = new ArrayList() {{
        add("58同城");
        add("五八");
        add("酒仙");
        add("驼房");
        add("798");
        add("携程");
    }};



    private Map<Integer, List<String>> hanzi = new HashMap<Integer, List<String>>() {{
        put(1, Arrays.asList("昂", "盎", "奥")); // a
        put(2, Arrays.asList("邦", "奔", "彼", "笔", "宾", "秉", "炳", "禀", "帛")); // b
        put(3, Arrays.asList("偲", "粲", "璨", "仓", "彻", "澈", "琛", "澄", "池", "畴", "初", "长", "常", "昌", "曾")); // c
        put(4, Arrays.asList("登", "笛", "典", "殿", "鼎", "顶", "定", "斗", "代", "笃")); // d
        put(5, Arrays.asList("尔", "耳", "迩")); // e
        put(6, Arrays.asList("璠", "凡", "泛", "访", "繁", "帆", "丰", "风", "枫", "封", "抚", "辅", "赋", "傅")); // f
        put(7, Arrays.asList("甘", "感", "皋", "杲", "阁", "舸", "亘", "更", "庚", "耿", "功", "宫", "恭", "贡", "汩", "故", "顾", "关", "观", "纶", "冠", "贯", "归", "果", "恭", "贡", "古")); // g
        put(8, Arrays.asList("函", "涵", "赫", "享", "珩", "厚", "乎", "奂", "涣", "幻", "煌", "互")); // h
        put(10, Arrays.asList("积", "极", "济", "迹", "绩", "加", "甲", "柬", "疆", "匠", "皆", "介", "界", "径", "境", "敬", "炅", "炯", "迥", "究", "久", "九", "居", "举", "具", "均")); // j
        put(11, Arrays.asList("刊", "堪", "侃", "珂", "轲", "可", "刻", "肯", "恳", "宽", "匡", "旷", "况", "琨", "阔")); // k
        put(12, Arrays.asList("岚", "阑", "览", "琅", "朗", "浪", "礼", "利", "联", "敛", "潋", "聆", "领", "令", "留", "珑", "陆", "录", "律", "纶", "仑", "络")); // l
        put(13, Arrays.asList("迈", "脉", "弥", "淼", "渺", "莫", "墨", "目", "慕")); // m
        put(14, Arrays.asList("奈", "南", "念", "凝", "侬", "诺")); // n
        put(15, Arrays.asList("偶")); // o
        put(16, Arrays.asList("畔", "培", "沛", "佩")); // p
        put(17, Arrays.asList("期", "齐", "奇", "洽", "恰", "浅", "乔", "青", "倾", "卿", "晴", "顷", "穹", "秋", "遒", "趋", "曲", "容", "如", "若")); // r
        put(19, Arrays.asList("飒", "森", "商", "舍", "涉", "申", "珅", "升", "时", "识", "实", "史", "始", "士", "示", "世", "仕", "适", "守", "首", "授", "书", "舒", "曙", "双", "爽", "硕", "烁", "司", "斯", "松", "嵩", "诵", "颂", "苏", "诉", "素", "溯", "随", "岁", "邃", "索", "所")); // s
        put(20, Arrays.asList("塔", "太", "泰", "谈", "坦", "堂", "倘", "特", "天", "添", "迢", "听", "霆", "通", "同", "曈", "统", "图", "拓")); // t
        put(23, Arrays.asList("瓦", "万", "往", "望", "微", "为", "卫", "未", "温", "文", "稳", "问", "沃", "幄", "渥", "斡", "乌", "无", "吾", "午", "悟")); // w
        put(24, Arrays.asList("兮", "希", "昔", "息", "惜", "晰", "稀", "溪", "熹", "蹊", "玺", "侠", "遐", "先", "闲", "贤", "弦", "显", "献", "相", "详", "享", "响", "向", "项", "骁", "逍", "潇", "霄", "筱", "笑", "效", "啸", "协", "偕", "心", "昕", "信", "星", "惺", "行", "省", "醒", "休", "修", "朽", "须", "序", "叙", "宣", "绚", "雪", "寻", "循", "迅")); // x
        put(25, Arrays.asList("崖", "涯", "亚", "烟", "延", "严", "言", "沿", "颜", "衍", "彦", "焱", "央", "泱", "扬", "仰", "尧", "遥", "也", "业", "叶", "依", "曳", "仪", "已", "倚", "义", "忆", "亦", "异", "易", "弈", "益", "逸", "意", "因", "音", "殷", "吟", "引", "应", "影", "映", "永", "咏", "悠", "由", "游", "佑", "于", "与", "予", "余", "逾", "瑜", "羽", "雨", "语", "彧", "预", "谕", "遇", "裕", "渊", "元", "远", "愿", "约", "云", "允", "韵")); // y
        put(26, Arrays.asList("载", "暂", "早", "则", "泽", "瞻", "展", "湛", "彰", "章", "仗", "钊", "昭", "召", "哲", "者", "臻", "振", "震", "正", "争", "知", "止", "旨", "祉", "至", "志", "致", "质", "秩", "智", "中", "众", "仲", "舟", "洲", "竹", "逐", "烛", "助", "祝", "专", "转", "传", "缀", "准", "灼", "卓", "琢", "孜", "兹", "资", "自", "宗", "奏", "足", "尊", "佐")); // z
    }};

    private List<String> list2 = Arrays.asList("石宾霄","石宾筱","石宾潇","石宾啸","石宾昕","石宾逸","石宾弈","石宾渊","石宾尧","石宾遥","石宾彦","石宾焱","石宾泱","石宾仰","石宾异","石宾殷","石宾佑","石宾逾","石宾瑜","石宾彧","石宾遇","石宾裕","石宾愿","石宾允","石宾韵","石宾寻","石宾迅","石宾崖","石宾涯","石宾衍","石宾飒","石宾森","石宾嵩","石宾硕","石宾烁","石宾斯","石宾诵","石宾溯","石宾邃","石宾索","石宾塔","石宾泰","石居竹","石居舟","石居洲","石居卓","石居琢","石举昂","石举奥","石举邦","石举奔","石举笔","石举彻","石举澈","石举澄","石举初","石举昌","石举登","石举典","石举鼎","石举顶","石举定","石举斗","石举更","石举耿","石举功","石举恭","石举顾","石举冠","石居自","石居宗","石居奏","石居足","石居尊","石居佐","石举池","石举常","石举代","石举贡","石居祝","石居专","石居转","石居传","石居准","石居助","石举祝","石举专","石举转","石举传","石举准","石举助","石居仲","石为昂","石为邦","石为昌","石为涵","石为朗","石为睿","石为轩","石为扬","石为远","石为诚","石为澄","石为昊","石为恒","石书昂","石书彬","石书涵","石书航","石书浩","石书杰","石书楷","石书朗","石书睿","石书轩","石书扬","石书远","石诵安","石诵彬","石诵涵","石诵航","石诵浩","石诵杰","石诵楷","石诵朗","石诵睿","石诵轩","石诵扬","石诵远","石颂安","石颂彬","石颂涵","石颂航","石颂浩","石颂杰","石颂楷","石颂朗","石颂睿","石颂轩","石颂扬","石颂远","石为恒","石音溪","石音闲","石音弦","石音逍","石音潇","石音霄","石音筱","石音啸","石音昕","石音星","石音修","石音绚","石音雪","石音迅","石音崖","石音涯","石音烟","石音衍","石音彦","石音焱","石音泱","石音尧","石音遥","石音叶","石音异","石音弈","石音逸","石音殷","石音吟","石音影","石音映","石音咏","石音悠","石音游","石音逾","石音瑜","石音羽","石音雨","石音彧","石音渊","石音远","石音云","石音韵","石音早","石音泽","石音湛","石音昭","石音哲","石音臻","石音祉石足渊","石足尧","石足哲","石足臻","石足振","石足志","石足智","石足昭","石足远","石足元","石足扬","石足仰","石足星","石足永","石足咏","石足悠","石足游","石足佑","石足瑜","石足羽","石足雨","石足云","石足韵","石足泽","石足竹","石足卓","石足尊","石尊昂","石尊奥","石尊昌","石尊登","石尊鼎","石尊定","石尊风","石足彦","石足泱","石足延","石足义","石足逸","石足吟","石足迅","石足崖","石足涯","石足寻","石足循","石足绚","石足愿","石足通","石足畅","石足明石也哲","石业哲","石语哲","石彧哲","石心哲","石昕哲","石信哲","石也辰","石业辰","石语辰","石彧辰","石心辰","石昕辰","石信辰","石也轩","石业轩","石语轩","石彧轩","石心轩","石昕轩","石信轩","石也然","石业然","石语然","石彧然","石心然","石昕然","石信然","石也睿","石业睿","石语睿","石彧睿","石心睿","石昕睿","石信睿","石也宸","石业宸","石语宸","石彧宸","石心宸","石昕宸","石信宸","石也恒","石业恒","石语恒","石彧恒","石心恒","石昕恒","石信恒","石也熙","石业熙","石语熙","石彧熙","石心熙","石昕熙","石信熙","石也昊","石业昊","石语昊","石彧昊","石心昊","石昕昊","石信昊","石也霖","石业霖","石语霖","石彧霖","石心霖","石昕霖","石信霖","石也澜","石业澜","石语澜","石彧澜","石心澜","石昕澜","石信澜","石也朗","石业朗","石语朗","石彧朗","石心朗","石昕朗","石信朗","石也睿","石业睿","石语睿","石彧睿","石心睿","石昕睿","石信睿","石也宸","石业宸","石语宸","石彧宸","石心宸","石昕宸","石信宸","石也恒","石业恒","石微尧","石微渊","石微哲","石微臻","石微振","石微侠","石微潇","石微霄","石微啸","石微昕","石微雪","石微迅","石微崖","石微涯","石微烟","石微衍","石微彦","石微泱","石微扬","石微仰","石微遥","石微叶","石微义","石微异","石微弈","石微逸","石微殷","石微吟","石微咏","石微悠","石微佑","石微予","石微逾","石微瑜","石微羽","石微雨","石微远","石微云","石微韵","石微泽","石微瞻","石微湛","石微仗","石微昭","石为昂","石为奥","石为邦","石为秉","石为炳","石为彻","石为澈","石为澄","石为初","石为昌","石为登","石为鼎","石为斗","石为笃","石为枫","石为丰","石为甘","石为亘","石为耿","石为恭","石为涵","石为赫","石为厚","石拓煌","石拓积","石拓极","石拓迹","石拓甲","石拓疆","石拓匠","石拓界","石拓径","石拓境","石拓敬","石拓炯","石拓迥","石拓久","石拓九","石拓堪","石拓侃","石拓珂","石拓恳","石拓宽","石拓匡","石拓旷","石拓琨","石拓阔","石拓岚","石拓琅","石拓朗","石拓浪","石拓守","石微溪","石微弦","石微骁","石微异","石侬辰","石诺辰","石留辰","石珑辰","石举辰","石具辰","石侬昊","石诺昊","石留昊","石珑昊","石举昊","石具昊","石侬轩","石诺轩","石留轩","石珑轩","石举轩","石具轩","石侬哲","石诺哲","石留哲","石珑哲","石举哲","石具哲","石侬远","石诺远","石留远","石珑远","石举远","石具远","石侬浩","石诺浩","石留浩","石珑浩","石举浩","石具浩","石侬青","石诺青","石留青","石珑青","石举青","石具青","石侬清","石诺清","石留清","石珑清","石举清","石具清","石侬然","石诺然","石留然","石珑然","石举然","石具然","石侬穹","石诺穹","石留穹","石珑穹","石举穹","石具穹","石侬嵩","石诺嵩","石留嵩","石珑嵩","石举嵩","石具嵩","石侬硕","石诺硕","石留硕","石珑硕","石举硕","石具硕","石侬烁","石诺烁","石留烁","石珑烁","石举烁","石具烁","石侬商","石诺商","石留商","石珑商","石举商","石具商","石侬舒","石诺舒","石留舒","石珑舒","石举舒","石具舒","石侬爽","石诺爽","石留爽","石珑爽","石举爽","石具爽","石侬司","石诺司","石留司","石珑司","石笛心","石笛昕","石笛星","石笛醒","石笛修","石笛绚","石笛雪","石笛寻","石笛循","石笛迅","石笛崖","石笛涯","石笛延","石笛严","石笛言","石笛沿","石笛衍","石笛彦","石笛央","石笛扬","石笛仰","石笛尧","石笛遥","石笛业","石笛叶","石笛依","石笛仪","石笛义","石笛忆","石笛异","石笛益","石笛逸","石笛音","石笛殷","石笛吟","石笛引","石笛应","石笛映","石笛永","石笛咏","石笛悠","石笛游","石笛佑","石笛予","石笛逾","石笛瑜","石笛羽","石笛雨","石笛语","石笛预","石笛谕","石笛遇","石笛裕","石笛元","石笛远","石笛愿","石笛约","石笛云","石笛允","石笛韵","石笛载","石笛早","石笛则","石笛泽","石笛瞻","石笛展","石笛湛","石笛彰","石笛钊","石笛昭","石笛召","石笛哲","石笛臻","石笛振","石笛正","石笛争","石笛志","石笛秩","石笛智","石笛舟","石笛洲","石笛竹","石笛逐","石笛助","石笛祝","石笛专","石笛转","石笛传","石笛缀","石笛准","石笛卓","石笛琢","石笛孜","石古昌","石古登","石古定","石古鼎","石古丰","石古风","石古枫");

    @Test
    public void func10_6_2() {
        Collections.shuffle(list2);
        for (String str : list2) {
            System.out.print(str + ",");
        }
    }

    /**
     * 名字
     */
    @Test
    public void func10_6() {
        Set<String> nameSet = new LinkedHashSet<>();
        for (Map.Entry<Integer, List<String>> yin : hanzi.entrySet()) { // 一个音
            String letter = "" + (char) ('a' + yin.getKey() - 1);
            String contentStr = "【" + letter + "作为第2个字音】：\n";
            for (String zi : yin.getValue()) {
                for (String zi2 : hanzi.values().stream().flatMap(List::stream).collect(Collectors.toList())) {
                    String name = "石" + zi + zi2;
                    if (nameSet.contains(name)) {
                        continue;
                    }
                    if (zi.equals(zi2)) {
                        continue;
                    }
                    nameSet.add(name);
                    if (letter.compareTo("z") >= 0) {
                        contentStr = contentStr + name + ", ";
                    }

                }
            }
//            if (nameSet.size() >= 50000) {
                System.out.printf("%s\n\n", contentStr);
//            }
        }

        System.out.println("总计 " + nameSet.size() + " 个名字");
    }

//    @Test
//    public void func() throws IOException, InterruptedException {
//        // key time title; value href
//        Map<String, String> result = new HashMap<String, String>();
//        Set<String> mySet = new HashSet<>();
//        mySet.add("无中介费 酒仙桥整租 14号线 将台 电子球场 整租一居室 民用...");
//        mySet.add("朝阳•东城｜王府井整租公寓 地铁站步行200米 楼下王府井步...");
//        mySet.add("朝阳•东城｜无中介费，出租酒仙桥14号线东风北桥，四街坊...");
//        mySet.add("无中介费 酒仙桥整租两居室 14号线 将台 电子球场社区 电梯高...");
//        mySet.add("朝阳•东城｜无中介费，出租酒仙桥14号线望京南。798，360...");
//        mySet.add("海淀•昌平｜学院路 五道口 六道口 学知园地铁站旁 八家嘉...");
//        mySet.add("海淀•昌平｜无中介费，出租酒仙桥14号线东风北桥地铁，南...");
//        mySet.add("无中介费 酒仙桥合租 北窑地次卧 找室友 租金2000 带阳台次卧...");
//
//        List<String> keywords = new ArrayList() {{
//            add("58");
//            add("五八");
//            add("酒仙");
//            add("驼房");
//            add("朝阳");
//            add("798");
//            add("vivo");
//            add("Vivo");
//            add("VIVO");
//            add("携程");
//            add("同程");
//            add("七九八");
//            add("360");
//            add("三六零");
//        }};
//
//
//        // 创建 HttpClient 实例 (Apache HttpClient 4.x)
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//
//        List<String> groupList = new ArrayList<String>() {{
////            add("232413");
////            add("419918");
////            add("516673");
////            add("549574");
////            add("467340");
////            add("596202");
//            add("258919");
////            add("519274");
//        }};
//
//        int index = 0;
//
//
//
//
//        for (String group : groupList) {
//
//            index = 0;
//            if (group.equals("232413")) {
//                index = 18;
//            }
//
//            for (; index < 5000; index++) {
//                Thread.sleep(2500 + randFuncInt());
//                // 构建请求
//                int nums = index * 25;
//                HttpGet request = new HttpGet("https://www.douban.com/group/" + group + "/discussion?start=" + nums + "&type=new");
//
//                // 设置所有请求头（与curl一致）
//                request.setHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
//                request.setHeader("accept-language", "zh-CN,zh;q=0.9");
//                request.setHeader("priority", "u=0, i");
//                request.setHeader("referer", "https://www.douban.com/group/"+ group +"/discussion?start=" + nums +"&type=new");
//                if (randFunc()) {
//                    request.setHeader("sec-ch-ua", "\"Google Chrome\";v=\"137\", \"Chromium\";v=\"137\", \"Not/A)Brand\";v=\"24\"");
//                } else {
//                    request.setHeader("sec-ch-ua", "\"Google Chrome\";v=\"136\", \"Chromium\";v=\"136\", \"Not/A)Brand\";v=\"23\"");
//                }
//                request.setHeader("sec-ch-ua-mobile", "?0");
//                if (randFunc()) {
//                    request.setHeader("sec-ch-ua-platform", "\"macOS\"");
//                } else {
//                    request.setHeader("sec-ch-ua-platform", "\"Win\"");
//                }
//                request.setHeader("sec-fetch-dest", "document");
//                request.setHeader("sec-fetch-mode", "navigate");
//                request.setHeader("sec-fetch-site", "same-origin");
//                request.setHeader("sec-fetch-user", "?1");
//                request.setHeader("upgrade-insecure-requests", "1");
//                if (randFunc()) {
//                    request.setHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36");
//                } else {
//                    request.setHeader("user-agent", "Mozilla/4.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/536.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36");
//                }
//                request.setHeader("Cookie", "frodotk_db=dbcfe62e1fa4bc24936fb28a5eee346c;dbcl2=257426431:gAsHSRtPhY0;ck=7ZW8;bid=8WmknakejM8;_vwo_uuid_v2=D4E04F6B6B819E8D062A7CF3AD1726E58|1d8274f3bfa8bc96f5465d2e33500f06; bid=0rf-AvctyrM");
//
//                // 发送请求
//                try (CloseableHttpResponse response = httpClient.execute(request)) {
//                    // 获取响应状态码
//                    int statusCode = response.getStatusLine().getStatusCode();
//
//                    // 获取响应内容
//                    HttpEntity entity = response.getEntity();
//                    String html = "";
//                    if (entity != null) {
//                        html = EntityUtils.toString(entity, "UTF-8");
//                    }
//
//                    if (html.contains("禁止访问") || html.contains("机器人程序")) {
//                        System.out.println("当前组号：" + group + " 页码：" + (index + 1) + " 出现了 ip 被禁止");
//                        return;
//                    }
//
//
//                    // 解析 html
//                    Document doc = Jsoup.parse(html);
//                    Elements titleElements = doc.selectXpath("//*[@id=\"content\"]/div//table/tbody/tr/td[1]/a");
//                    if (titleElements.isEmpty()) {
//                        // 到了空页，进入下一组
//                        break;
//                    }
//                    int count = titleElements.size();
//                    int temp = 2;
//                    String timeStr = "//*[@id=\"content\"]/div//table/tbody/tr[" + temp + "]/td[4]";
//                    String titleStr = "//*[@id=\"content\"]/div//table/tbody/tr[" + temp + "]/td[1]/a";
//                    String urlStr = "//*[@id=\"content\"]/div//table/tbody/tr[" + temp + "]/td[1]/a";
//                    if (doc.selectXpath(timeStr).isEmpty() || doc.selectXpath(timeStr).get(0) == null || doc.selectXpath(timeStr).get(0).text() == null || doc.selectXpath(timeStr).get(0).text().compareTo("08-01") < 0 ) {
//                        break;
//                    }
//                    // 提取所有讨论标题 (示例)
//                    for (; temp < count + 2; temp++) {
//                        if (doc.selectXpath(timeStr).isEmpty() || doc.selectXpath(timeStr).get(0) == null ||
//                                doc.selectXpath(titleStr).isEmpty() || doc.selectXpath(titleStr).get(0) == null
//                        ) {
//                            continue;
//                        }
//
//                        String time = doc.selectXpath(timeStr).get(0).text();
//                        if (time.compareTo("08-01") < 0) {
//                            return;
//                        }
//
//                        String title = doc.selectXpath(titleStr).get(0).text();
//                        String title2 = doc.selectXpath(titleStr).get(0).attr("title");
//                        if (mySet.contains(title)) {
//                            continue;
//                        }
//                        mySet.add(title);
//
//                        String url = doc.selectXpath(urlStr).get(0).attr("href");
//
//                        boolean flag = false; // 粗略
//                        boolean flag2 = false; // 精细
//                        for (String keyword : keywords) {
//                            if (title.contains(keyword)) {
//                                flag = true;
//                                break;
//                            }
//                            if (title2.contains(keyword)) {
//                                flag = true;
//                                break;
//                            }
//                        }
//                        for (String keyword2 : keywords2) {
//                            if (title.contains(keyword2)) {
//                                flag2 = true;
//                                break;
//                            }
//                            if (title2.contains(keyword2)) {
//                                flag2 = true;
//                                break;
//                            }
//                        }
//                        if (!flag) {
//                            continue;
//                        }
//                        if (!flag2) { // 粗略的，就进入网页内再看
//                            if (!isRight(url, group, nums)) {
//                                continue;
//                            }
//                        }
//
//                        result.put(time + " " + title, url);
//                        System.out.println("组号:" + group + " 时间:" + time + " " + title + " ===========> " + url);
//                    }
//
//                }
//            }
//            System.out.println();
//            System.out.println();
//            System.out.println();
//            System.out.println();
//            System.out.println();
//            System.out.println();
//            System.out.printf("分组 " + group + " 结果直到：%d 页后就符合条件的数据了", index);
//            System.out.println(JSON.toJSONString(result));
//
//        }
//
//
//    }

//    public boolean isRight(String url, String group, int start) throws InterruptedException, IOException {
//        if (StringUtils.isNullOrEmpty(url)) {
//            return false;
//        }
//        if (url.isEmpty()) {
//            return false;
//        }
//        Thread.sleep(2500 + randFuncInt());
//
//        // 创建 HttpClient 实例 (Apache HttpClient 4.x)
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//
//        HttpGet request = new HttpGet(url);
//
//        // 设置所有请求头（与curl一致）
//        request.setHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
//        request.setHeader("accept-language", "zh-CN,zh;q=0.9");
//        request.setHeader("priority", "u=0, i");
//        request.setHeader("referer", "https://www.douban.com/group/"+ group +"/discussion?start="+start+"&type=new");
//        if (randFunc()) {
//            request.setHeader("sec-ch-ua", "\"Google Chrome\";v=\"137\", \"Chromium\";v=\"137\", \"Not/A)Brand\";v=\"24\"");
//        } else {
//            request.setHeader("sec-ch-ua", "\"Google Chrome\";v=\"136\", \"Chromium\";v=\"136\", \"Not/A)Brand\";v=\"23\"");
//        }
//        request.setHeader("sec-ch-ua-mobile", "?0");
//        if (randFunc()) {
//            request.setHeader("sec-ch-ua-platform", "\"macOS\"");
//        } else {
//            request.setHeader("sec-ch-ua-platform", "\"Win\"");
//        }
//        request.setHeader("sec-fetch-dest", "document");
//        request.setHeader("sec-fetch-mode", "navigate");
//        request.setHeader("sec-fetch-site", "same-origin");
//        request.setHeader("sec-fetch-user", "?1");
//        request.setHeader("upgrade-insecure-requests", "1");
//        if (randFunc()) {
//            request.setHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36");
//        } else {
//            request.setHeader("user-agent", "Mozilla/4.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/536.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36");
//        }
//        request.setHeader("Cookie", "frodotk_db=dbcfe62e1fa4bc24936fb28a5eee346c;dbcl2=257426431:gAsHSRtPhY0;ck=7ZW8;bid=8WmknakejM8;_vwo_uuid_v2=D4E04F6B6B819E8D062A7CF3AD1726E58|1d8274f3bfa8bc96f5465d2e33500f06; bid=0rf-AvctyrM");
//
//        // 发送请求
//        try (CloseableHttpResponse response = httpClient.execute(request)) {
//            // 获取响应状态码
//            int statusCode = response.getStatusLine().getStatusCode();
//
//            // 获取响应内容
//            HttpEntity entity = response.getEntity();
//            String html = EntityUtils.toString(entity, "UTF-8");
//
//            Document doc = Jsoup.parse(html);
//            Elements elements = doc.selectXpath("//*[@id=\"link-report\"]/div//p");
//            if (elements.isEmpty()) {
//                return false;
//            }
//            Element element = elements.get(0);
//            if (element == null) {
//                return false;
//            }
//            String neirong = element.text();
//            if (neirong == null) {
//                return false;
//            }
//
//            for (String keywords2 : keywords2) {
//                if (neirong.contains(keywords2)) {
//                    return true;
//                }
//            }
//            return false;
//        }
//    }


//    public boolean randFunc() {
//        Random random = new Random();
//        return random.nextBoolean();
//    }

//    public int randFuncInt() {
//        Random random = new Random();
//        return random.nextInt(500);
//    }


//    @Test
//    public void func_ddd() throws IOException, InterruptedException {
//
//        System.out.println(randFunc());
//        System.out.println(randFunc());
//        System.out.println(randFunc());
//        System.out.println(randFunc());
////        System.out.println(isRight("https://www.douban.com/group/topic/337141765/?_spm_id=MTk3NDY1NDMy&_i=82922878Wmknak"));
//    }
}
