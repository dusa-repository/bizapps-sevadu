package com.dusa.thermometer.service.util;

public class HtmlGenerator {

    public static String generateCell(String content, String styleClass, String id, String customData, int colSpan) {
        String result = "<td";
        String val = (content == null) ? "&nbsp;" : content;
        if (colSpan != 0) {
            result += " colspan='" + String.valueOf(colSpan) + "'";
        }
        if (id != null)
            result += " name=\"" + id + "\"";
        if (customData != null) {
            result += " " + customData;
        }
        if (styleClass != null)
            result += " class=\"" + styleClass + "\"";
        result += ">" + val + "</td>\n";
        return result;
    }

    public static String generateDiv(String content, String styleClass) {
        String result = "<div";
        String val = (content == null) ? "&nbsp;" : content;
        if (styleClass != null)
            result += " class=\"" + styleClass + "\"";
        result += ">" + val + "</div>\n";
        return result;
    }

    public static String generateCell(String content, String styleClass, int colSpan) {
        return generateCell(content, styleClass, null, null, colSpan);
    }

    public static String generateCell(String content, String styleClass) {
        return generateCell(content, styleClass, null, null, 1);
    }

    public static String generateCell(String content, String styleClass, String customData) {
        return generateCell(content, styleClass, null, customData, 1);
    }

    public static String generateBlankCells(int amount, String styleClass) {
        String result = "";
        for (int i = 0; i < amount; i++) {
            result += HtmlGenerator.generateCell(null, styleClass);
        }
        return result;
    }
    
    public static String generateClickable(String content, String styleClass, String id) {
        String result = "<td colspan='2' class='description'><a name='click' data-click='" + id
                + "' href='#'";
        String val = (content == null) ? "&nbsp;" : content;
        if (styleClass != null)
            result += " class=\"" + styleClass + "\"";
        result += ">" + val + "</a></td>\n";
        return result;
    }

    public static String generateHeader(String content, String styleClass, String extraStyle) {
        String result = "<th";
        String val = (content == null) ? "&nbsp;" : content;
        if (styleClass != null) {
            result += " class='" + styleClass + "'";
        }
        if (extraStyle != null) {
            result += " style='" + extraStyle + "'";
        }
        result += ">" + "&nbsp;" + val + "</th>\n";
        return result;
    }

    public static String generateTableHeader(String content, String styleClass) {
        return generateHeader(content, styleClass, null);
    }

    public static String generateCustomContent(String content) {
        return (content == null) ? "&nbsp;" : "<div><span>" + content + "</span></div>";
    }

    public static String generateCustomHeadersContent(String content, String styleClass) {
        String result = "<td";
        if (styleClass != null)
            result += " class=\"" + styleClass + "\"";
        result += ">" + generateCustomContent(content) + "</div>\n";
        return result;
    }
}
