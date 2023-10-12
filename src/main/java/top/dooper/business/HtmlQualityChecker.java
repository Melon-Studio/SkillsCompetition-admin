package top.dooper.business;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlImage;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlScript;
import org.htmlunit.html.HtmlTitle;
import top.dooper.business.entity.HtmlQualityReport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlQualityChecker {
    public static HtmlQualityReport checkHtml(String url) {
        int score = 100; // 初始得分为100
        List<String> deductions = new ArrayList<>();

        try (final WebClient webClient = new WebClient()) {
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(false);

            HtmlPage page = webClient.getPage(url);
            String htmlCode = page.asXml();

            // 检测小写元素名
            Pattern elementPattern = Pattern.compile("<\\s*([^\\s>/]+)\\s*");
            Matcher elementMatcher = elementPattern.matcher(htmlCode);
            while (elementMatcher.find()) {
                String elementName = elementMatcher.group(1);
                if (!elementName.equals(elementName.toLowerCase())) {
                    score -= 5; 
                    deductions.add("使用大写元素名: <" + elementName + ">");
                }
            }

            // 检测关闭所有 HTML 元素
            Pattern unclosedElementPattern = Pattern.compile("<\\s*([^\\s>/]+)[^>]*>");
            Matcher unclosedElementMatcher = unclosedElementPattern.matcher(htmlCode);
            while (unclosedElementMatcher.find()) {
                String elementName = unclosedElementMatcher.group(1);
                if (htmlCode.indexOf("</" + elementName + ">") == -1) {
                    score -= 10; 
                    deductions.add("未关闭元素: <" + elementName + ">");
                }
            }

            // 检测关闭空的 HTML 元素
            Pattern emptyElementPattern = Pattern.compile("<\\s*([^\\s>/]+)[^>]*\\s*/>");
            Matcher emptyElementMatcher = emptyElementPattern.matcher(htmlCode);
            while (emptyElementMatcher.find()) {
                String elementName = emptyElementMatcher.group(1);
                if (!elementName.equals(elementName.toLowerCase())) {
                    score -= 5; 
                    deductions.add("使用大写元素名的空元素: <" + elementName + "/>");
                }
            }

            // 检测小写属性名
            Pattern attributePattern = Pattern.compile("\\s([^\\s=]+)=");
            Matcher attributeMatcher = attributePattern.matcher(htmlCode);
            while (attributeMatcher.find()) {
                String attributeName = attributeMatcher.group(1);
                if (!attributeName.equals(attributeName.toLowerCase())) {
                    score -= 5; 
                    deductions.add("使用大写属性名: " + attributeName);
                }
            }

            // 检测属性值使用引号
            Pattern attributeValuePattern = Pattern.compile("\\s[\\w-]+=([^\\s>]+)");
            Matcher attributeValueMatcher = attributeValuePattern.matcher(htmlCode);
            while (attributeValueMatcher.find()) {
                String attributeValue = attributeValueMatcher.group(1);
                if (!attributeValue.startsWith("\"") && !attributeValue.startsWith("'")) {
                    score -= 5; 
                    deductions.add("属性值未使用引号: " + attributeValue);
                }
            }

            // 检测图片使用 alt 属性
            List<HtmlImage> images = page.getByXPath("//img");
            for (HtmlImage image : images) {
                if (image.getAttribute("alt").isEmpty()) {
                    score -= 5; 
                    deductions.add("图片缺少 alt 属性: <img src=\"" + image.getSrcAttribute() + "\">");
                }
            }

            // 检测等号前后不能使用空格
            Pattern equalsWithSpacePattern = Pattern.compile("\\s=\\s");
            Matcher equalsWithSpaceMatcher = equalsWithSpacePattern.matcher(htmlCode);
            if (equalsWithSpaceMatcher.find()) {
                score -= 5; 
                deductions.add("属性等号前后存在空格");
            }

            // 避免一行代码过长少于 80 个字符
            String[] lines = htmlCode.split("\n");
            for (String line : lines) {
                if (line.length() > 80) {
                    score -= 5; 
                    deductions.add("一行代码过长: " + line);
                }
            }

            // 不省略 <html> 和 <body> 标签
            if (htmlCode.indexOf("<html>") == -1 || htmlCode.indexOf("<body>") == -1) {
                score -= 10; 
                deductions.add("缺少 <html> 或 <body> 标签");
            }

            // 不能没有<title> 元素
            HtmlTitle title = page.getFirstByXPath("//title");
            if (title == null) {
                score -= 10; 
                deductions.add("缺少 <title> 元素");
            }

            // 不能不声明编码格式
            String charsetMeta = "<meta[^>]*charset\\s*=\\s*['\"]?([^'\">\\s]+)['\"]?";
            Pattern charsetPattern = Pattern.compile(charsetMeta);
            Matcher charsetMatcher = charsetPattern.matcher(htmlCode);
            if (!charsetMatcher.find()) {
                score -= 10; 
                deductions.add("未声明编码格式");
            }

            // JavaScript脚本声明变量禁止使用var
            List<HtmlScript> scripts = page.getByXPath("//script");
            for (HtmlScript script : scripts) {
                String scriptContent = script.getTextContent();
                if (scriptContent.contains("var ")) {
                    score -= 5; 
                    deductions.add("JavaScript脚本使用了var关键字: " + scriptContent);
                }
            }

            // 引用图片等外部资源必须使用小写文件名
            Pattern srcAttributePattern = Pattern.compile("<[^>]+src=\"([^\"]+)\"");
            Matcher srcAttributeMatcher = srcAttributePattern.matcher(htmlCode);
            while (srcAttributeMatcher.find()) {
                String srcAttributeValue = srcAttributeMatcher.group(1);
                String[] pathSegments = srcAttributeValue.split("/");
                String fileName = pathSegments[pathSegments.length - 1];
                if (!fileName.equals(fileName.toLowerCase())) {
                    score -= 5; 
                    deductions.add("引用的资源文件名未使用小写: " + fileName);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HtmlQualityReport(score, deductions);
    }
}
