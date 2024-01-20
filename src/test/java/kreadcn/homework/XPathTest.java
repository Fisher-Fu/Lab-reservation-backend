package kreadcn.homework;

import org.junit.jupiter.api.Test;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

public class XPathTest {
    @Test
    public void test() {
        String xml = """
                <li><input id="b1" type="checkbox" value="1" name="b" class="check_list"><dl><dt><a href="JourDetail.jsp?dxNumber=100423578975&amp;d=F55861CAE0EF6214C1EA390BCF774E24" target="_blank">高职<font color="Red">Java</font>程序设计类和对象课堂教学方法探索和实践</a></dt><dd><b>作者：</b>伍德雁&nbsp;&nbsp;<b>文献出处：</b>新教育时代电子杂志(教师版)&nbsp;&nbsp;<b>ISSN：</b>2095-4743&nbsp;&nbsp;<b>年代：</b>2023&nbsp;&nbsp;<b>期号：</b>第26期&nbsp;&nbsp;<b>页码：</b>88-90&nbsp;&nbsp;<b>作者单位：</b>广西机电职业技术学院&nbsp;&nbsp;</dd><dd><b>关键词：</b>三教改革　　教法改革　　<font color="Red">Java</font>　　类和对象</dd><dd><b>摘要：</b>在三教改革的背景下，本文对<font color="Red">Java</font>程序设计类和对象的课堂教学方法改革进行了实践性的探索。讨论了类和对象课堂 教学方法改革的必要性，分析了传统的先理论后实践的课堂教学方法的优缺点，分享了多年一线教学实践中摸索出...</dd>
                						<input type="hidden" id="dxid1" name="f[1].dxid" value="100423578975">
                						<input type="hidden" id="title1" name="f[1].title" value="高职<font color=Red>Java</font>程序设计类和对象课堂教学方法探索和实践">
                						<input type="hidden" id="url1" name="f[1].url" value="http://qk.duxiu.com/JourDetail.jsp?dxNumber=100423578975&amp;d=F55861CAE0EF6214C1EA390BCF774E24">
                						<input type="hidden" id="memo1" name="f[1].memo" value=" <b>作者：</b>伍德雁&nbsp;&nbsp;<b>文献出处：</b>新教育时代电子杂志(教师版)&nbsp;&nbsp;<b>ISSN：</b>2095-4743&nbsp;&nbsp;<b>年代：</b>2023&nbsp;&nbsp;<b>期号：</b>第26期&nbsp;&nbsp;<b>页码：</b>88-90&nbsp;&nbsp;<b>作者单位：</b>广西机电职业技术学院&nbsp;&nbsp;<br><b>关键词：</b>三教改革　　教法改革　　<font color=Red>Java</font>　　类和对象<br><b>摘要：</b>在三教改革的背景下，本文对<font color=Red>Java</font>程序设计类和对象的课堂教学方法改革进行了实践性的探索。讨论了类和对象课堂 教学方法改革的必要性，分析了传统的先理论后实践的课堂教学方法的优缺点，分享了多年一线教学实践中摸索出...<br>">\s
                						</dl></li>                
                """;
        JXDocument document = JXDocument.create(xml);
        JXNode node = document.selNOne("//dd/b[text()='作者：']/../html()");
        System.out.println(node.asString());

    }
}
