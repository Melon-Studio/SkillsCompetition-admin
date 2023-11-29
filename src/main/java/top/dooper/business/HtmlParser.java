package top.dooper.business;

import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class HtmlParser {
    File htmlFile;
    public HtmlParser(File htmlFile){
        this.htmlFile = htmlFile;
    }

    @Nullable
    private Elements parse(){
        try {
            Document doc = Jsoup.parse(htmlFile, "UTF-8", "");
            Elements elements = doc.getAllElements();
            return elements;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public Map<String, Object> specificationChecker() {
        Map<String, Object> result = new HashMap<>();
        List<String> deductions = new ArrayList<>();
        Elements elements = parse();
        double totalScore = 100;

        // 元素小写检查
        boolean allLowerCase = true;
        for (Element element : elements) {
            if (!element.tagName().equals(element.tagName().toLowerCase())) {
                allLowerCase = false;
                break;
            }
        }

        // 关闭所有HTML元素检查
        boolean allClosed = true;
        List<String> selfClosedTags = new ArrayList<>();
        Collections.addAll(selfClosedTags, "br", "hr", "img", "input", "link", "meta", "area", "base", "col", "command", "embed", "keygen", "source", "track", "wbr", "param", "object", "applet");
        for (Element element : elements) {
            String tagName = element.tagName();

            boolean isSelfClosing = element.children().isEmpty();
        }
        if (!elements.html().equals(elements.outerHtml())){
            allClosed = false;
        }

        // HTML元素属性小写检查
        boolean hasUpperCaseAttributes = true;
        for (Element element : elements) {
            Attributes attributes = element.attributes();

            for (org.jsoup.nodes.Attribute attribute : attributes) {
                String attributeName = attribute.getKey();
                if (attributeName.matches(".*[A-Z].*")) {
                    hasUpperCaseAttributes = false;
                    break;
                }
            }
        }

        // 图片必须包含 alt 属性检查
        boolean allImagesHaveAlt = true;
        for (Element imgElement : elements.select("img")) {
            if (!imgElement.hasAttr("alt")) {
                allImagesHaveAlt = false;
                break;
            }
        }

        // 属性值是否被引号包裹
        boolean allAttributesQuoted = true;
        for (Element element : elements) {
            Attributes attributes = element.attributes();

            for (org.jsoup.nodes.Attribute attribute : attributes) {
                String attributeValue = attribute.getValue();

                if (attributeValue != null && !attributeValue.isEmpty()) {
                    char firstChar = attributeValue.charAt(0);
                    char lastChar = attributeValue.charAt(attributeValue.length() - 1);

                    if ((firstChar != '\"' && firstChar != '\'') || (lastChar != '\"' && lastChar != '\'')) {
                        allAttributesQuoted = false;
                        break;
                    }
                }
            }
        }

        // 属性值等号空格检查
        boolean allAttributesCorrect = true;
        for (Element element : elements) {
            Attributes attributes = element.attributes();

            for (org.jsoup.nodes.Attribute attribute : attributes) {
                String attributeName = attribute.getKey();
                String attributeValue = attribute.getValue();

                if (attributeName.contains(" ") || (attributeValue != null && (attributeValue.startsWith(" ") || attributeValue.endsWith(" ")))) {
                    allAttributesCorrect = false;
                    break;
                }
            }
        }

        // var声明检查
        boolean isNotUseVar = true;
        for (Element scriptElement : elements.select("script")) {
            String scriptCode = scriptElement.html();

            if (!scriptCode.matches(".*\\b\\w+\\s*=\\s*[^v].*")) {
                isNotUseVar = false;
                break;
            }
        }


        if (!allLowerCase) {
            totalScore -= 1.43; 
            deductions.add("标签(元素)名称使用了大写字母");
        }

        if (!allClosed) {
            totalScore -= 2.43; 
            deductions.add("标签(元素)没有被正确关闭");
        }

        if (!hasUpperCaseAttributes) {
            totalScore -= 1.43; 
            deductions.add("标签(元素)的属性使用了大写字母");
        }

        if (!allImagesHaveAlt) {
            totalScore -= 1.43; 
            deductions.add("图片标签(元素)没有 alt 属性");
        }

        if (!allAttributesQuoted) {
            totalScore -= 2.43;
            deductions.add("标签(元素)的属性值没有使用双引号");
        }

        if (!allAttributesCorrect) {
            totalScore -= 1.43; 
            deductions.add("标签(元素)的属性值等号前或后使用了空格");
        }

        if (!isNotUseVar) {
            totalScore -= 3.43;
            deductions.add("内联 JavaScript 中使用了 var 来声明变量");
        }

        BigDecimal decimal = new BigDecimal(totalScore);
        BigDecimal score = decimal.setScale(2, RoundingMode.HALF_UP);
        result.put("Score", score);
        result.put("Deductions", deductions);
        return result;
    }
}
