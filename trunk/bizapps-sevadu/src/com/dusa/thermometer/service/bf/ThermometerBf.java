package com.dusa.thermometer.service.bf;

import java.util.ArrayList;
import java.util.List;

import com.dusa.thermometer.service.config.ActivationConfig;
import com.dusa.thermometer.service.to.ActivationTo;
import com.dusa.thermometer.service.to.HeaderTo;
import com.dusa.thermometer.service.to.ThermometerTo;
import com.dusa.thermometer.service.to.TotalTo;
import com.dusa.thermometer.service.transformation.ClientData;
import com.dusa.thermometer.service.transformation.SupervisorData;
import com.dusa.thermometer.service.transformation.ThermometerData;
import com.dusa.thermometer.service.util.HtmlGenerator;
import com.dusa.thermometer.service.util.ThermometerUtil;

public class ThermometerBf {

    public int clientsNumber;
    public String CASCADE_TIME = "300";
    List<TotalTo> totals = new ArrayList<TotalTo>();

    public String generateThermometer(ThermometerTo thermometer) {
        String htmlDom = "";
        ThermometerUtil.initializeTotals(thermometer.getLabels().size() - 8, totals);

        htmlDom += generateLinks();
        htmlDom += generateScripts();

        htmlDom += generateBeginningTableHeaders(thermometer.getLabels());
        htmlDom += composeData(thermometer.getData(), "");
        htmlDom += generateTotals();
        htmlDom += generateEndingTableTags();
        htmlDom += generatePopupScript();

        reset();
        return htmlDom;
    }

    private String generateTotals() {
        String activeAndPlanned = "<tr>" + HtmlGenerator.generateCell(ActivationConfig.TOTAL_ACTIVATED_AND_PLANNED_TEXT,
                ActivationConfig.CSS_TITLE, 3);
        String activeAndNotPlanned = "<tr>" + HtmlGenerator.generateCell(ActivationConfig.TOTAL_ACTIVATED_AND_NOT_PLANNED_TEXT,
                ActivationConfig.CSS_TITLE, 3);
        String planned = "<tr>" + HtmlGenerator.generateCell(ActivationConfig.TOTAL_PLANNED_TEXT,
                ActivationConfig.CSS_TITLE, 3);
        String objective = "<tr>" + HtmlGenerator.generateCell(ActivationConfig.TOTAL_ACTIVATION_OBJECTIVE_TEXT,
                ActivationConfig.CSS_TITLE, 3);
        String activationByLabel = "<tr>" + HtmlGenerator.generateCell(ActivationConfig.TOTAL_ACTIVATION_BY_LABEL,
                ActivationConfig.CSS_TITLE, 3);

        for (TotalTo total : totals) {
            activeAndPlanned += HtmlGenerator.generateCell(String.valueOf(total.getActivatedAndPlanned()),
                    (total.getActivatedAndPlanned() == total.getPlanned() ? ActivationConfig.CSS_LABEL_SUCCESS
                            : ActivationConfig.CSS_LABEL_FAILED));
            activeAndNotPlanned += HtmlGenerator.generateCell(String.valueOf(total.getActivatedAndNotPlanned()),
                    ActivationConfig.CSS_LABEL_NEUTRAL);
            planned += HtmlGenerator.generateCell(String.valueOf(total.getPlanned()), ActivationConfig.CSS_LABEL_TITLE);

            int totalObjective = (total.getPlanned() == 0) ? 100 :
                    total.getPlanned() * 100 / clientsNumber;
            objective += HtmlGenerator.generateCell(String.valueOf(totalObjective) + "%",
                    ActivationConfig.CSS_LABEL_TITLE);

            int totalByLabel = (total.getPlanned() == 0) ? 100 :
                    total.getActivatedAndPlanned() * 100 / total.getPlanned();
            activationByLabel += HtmlGenerator.generateCell(String.valueOf(totalByLabel) + "%",
                    (totalByLabel == 100) ? ActivationConfig.CSS_LABEL_SUCCESS : ActivationConfig.CSS_LABEL_FAILED);
        }
        totals = new ArrayList<TotalTo>();
        return activeAndPlanned + "</tr>\n" + activeAndNotPlanned + "</tr>\n" + planned + "</tr>\n"
                + objective + "</tr>\n" + activationByLabel + "</tr>\n";
    }

    private String composeData(List<ThermometerData> thermoData, String id) {
        if (thermoData == null || thermoData.size() == 0) {
            return "";
        }
        ThermometerData data = thermoData.get(0);

        if (data instanceof ClientData) {
            clientsNumber++;
            totalByLabel(totals, (ClientData) data);
        }

        String lastId = id.split(" ")[id.split(" ").length - 1];
        String id2 = (id.equals("") ? data.getId() : lastId + "-" + data.getId());
        if (thermoData.size() == 1 && (data.getChildren() == null || data.getChildren().size() == 0)) {
            return generateRowTag(id, data, id2) + data.generateHTML(id2) + "</tr>\n";
        } else if(thermoData.size() == 1) {
            return generateRowTag(id, data, id2) + data.generateHTML(id2) + "</tr>\n"
                    + composeData(data.getChildren(), id + ((id.equals("")) ? "" : " ") + id2);
        } else if (data.getChildren() == null || data.getChildren().size() == 0) {
            return generateRowTag(id, data, id2) + data.generateHTML(id2) + "</tr>\n"
                    + composeData(thermoData.subList(1, thermoData.size()), id);
        } else {
            return generateRowTag(id, data, id2) + data.generateHTML(id2) + "</tr>\n"
                    + composeData(data.getChildren(), id + ((id.equals("")) ? "" : " ") + id2)
                    + composeData(thermoData.subList(1, thermoData.size()), id);
        }
    }

    private String generateRowTag(String id, ThermometerData data, String id2) {
        return "<tr id='" + id2 + "' data-collapse=\"" + "false"
                + "\" class='" + (data instanceof SupervisorData ? ActivationConfig.CSS_TOP_ROW : "") + id +"'"
                + (data instanceof SupervisorData ? "" : " style='display: none;'") + ">\n";
    }

    private void totalByLabel(List<TotalTo> totals, ClientData data) {
        ClientData clientData = (ClientData) data;
        for (int i = 0; i < clientData.getActivations().size(); i++) {
            ActivationTo activation = clientData.getActivations().get(i);
            if (activation.isActivated() && activation.isObjective()) {
                totals.get(i).addActivatedAndPlanned();
                totals.get(i).addPlanned();
            } else if (activation.isActivated()) {
                totals.get(i).addActivatedAndNotPlanned();
            } else if (activation.isObjective()) {
                totals.get(i).addPlanned();
            }
        }
    }

    private String generateBeginningTableHeaders(List<HeaderTo> labels) {
        String result = "";
        result += "<table id='thermo' class=\"table table-bordered table-hover custom-table\">\n" +
                  "            <thead class=\"title\">\n";
        for (HeaderTo label : labels) {
            String styleClass = (label.isVertical() ? ActivationConfig.CSS_HEADER_ROTATE
                    + " " + ActivationConfig.CSS_TITLE : ActivationConfig.CSS_TITLE);
            String name = "";
            if (label.isVertical()) {
                name = (label.getName().length() > ActivationConfig.MAX_TABLE_HEADER_STRING_SIZE)
                ? label.getName().substring(0, ActivationConfig.MAX_TABLE_HEADER_STRING_SIZE) : label.getName();
            } else {
                name = label.getName();
            }
            result += HtmlGenerator.generateHeader((label.isVertical()
                    ? HtmlGenerator.generateCustomContent(name) : name), styleClass, label.getStyle());
        }
        result += "</thead>\n" +
                  "<tbody>\n";

        return result;
    }

    private String generateScripts() {
        return "<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js\"></script>\n"
                +  "<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js\"></script>\n"
                +  "<script src=\"http://cdnjs.cloudflare.com/ajax/libs/floatthead/1.2.8/jquery.floatThead.min.js\"></script>"
                +  "<script>\n" +
                "function cascade(name, all) {\n" +
                "  var value = true;\n" +
                "      var x = $(\"#\" + name);\n" +
                "      var y = $(\"#\" + name).data(\"collapse\");\n" +
                "  if (!($(\"#\" + name).data(\"collapse\"))) {\n" +
                "    $(\"#\" + name).data(\"collapse\", true);\n" +
                "    switchImage(name, true);\n" +
                "  } else {\n" +
                "    $(\"#\" + name).data(\"collapse\", false);\n" +
                "    switchImage(name, false);\n" +
                "    value = false;\n" +
                "  }\n" +
                "  toggleRow(name, value, " + CASCADE_TIME + ", false, true);\n" +
                "  \n" +
                "}\n" +
                "function toggleRow(name, value, speed, all, image) {\n" +
                "  $(\".\" + name).each(function() {\n" +
                "    if (value) {\n" +
                "      if (all == null || all == false) {\n" +
                "        var tag1 = $(this).attr('id').split('-');\n" +
                "        var tag2 = name.split('-')\n" +
                "        if (tag1.length == (tag2.length + 1)){\n" +
                "          $(this).show(speed);\n" +
                "          var thisName = $(this).attr('id');\n" +
                "        }\n" +
                "      } else {\n" +
                "        $(this).show(speed);\n" +
                "        var thisName = $(this).attr('id');\n" +
                "        switchImage(thisName, true);\n" +
                "      }\n" +
                "    } else {\n" +
                "      $(this).hide(speed);\n" +
                "      $(this).data(\"collapse\", value);\n" +
                "      var thisName = $(this).attr('id');\n" +
                "      if (image) {\n" +
                "        switchImage(thisName, false);\n" +
                "      }\n" +
                "    }\n" +
                "  });\n" +
                "}\n" +
                "function switchImage(name, collapse) {\n" +
                "  if (collapse) {\n" +
                "    $('#' + name + '-IMG').attr(\"src\",\"img/collapse.png\");\n" +
                "    $('#' + name + '-IMG').data('status', 'collapse');\n" +
                "  } else {\n" +
                "    $('#' + name + '-IMG').attr(\"src\",\"img/expand.png\");\n" +
                "    $('#' + name + '-IMG').data('status', 'expand');\n" +
                "  }\n" +
                "}\n" +
                "</script>\n";
    }

    private String generatePopupScript() {
        return "<script>\n" +
               "$('.popup').each(function() {\n" +
                   "var values = $(this).data('chart').split(';');\n" +
                   "$(this).append('<div style=\"width: 110px;\" class=\"div-to-display\"><table class=\"table table-bordered table-hover\"><tr>'\n" +
                        "+ '<td class=\"tdwidth label-neutral\">' + values[1] + '</td>' \n" +
                        "+ '<td class=\"tdwidth label-title\">' + values[2] + '</td>' \n" +
                        "+ '<td class=\"tdwidth label-title\">' + values[3] + '%</td>'\n" +
                        "+ '<td class=\"tdwidth ' + ((values[4] == 100 || (values[4] == 0 && values[2] == 0)) ? 'label-success' : 'label-failed') + '\">' + values[4] + '%</td>'\n" +
                        "+ '</tr></table></div>');\n" +
               "});\n" +
//               "$('#thermo').floatThead({\n" +
//               "useAbsolutePositioning: true\n" +
//               "});\n" +
               "$(\".cascadable\").click(function() {\n" +
               "  cascade($(this).data(\"click\"));\n" +
               "});" +
               "$(\".expand\").click(function() {\n" +
               "  var image = $(this).children().eq(0);\n" +
               "  if (image.data('status') == 'expand') {\n" +
               "    $(\".\" + $(this).data(\"click\")).data(\"collapse\", true);\n" +
               "    $(\"#\" + $(this).data(\"click\")).data(\"collapse\", true);\n" +
               "    image.data('status', 'collapse');\n" +
               "    image.attr(\"src\",\"img/collapse.png\");\n" +
               "    var name = $(this).data(\"click\")\n" +
               "    $(\".expand\").each(function() {\n" +
               "      if ($(this).data('click').indexOf(name) > -1) {\n" +
               "        $(this).data(\"collapse\", true);\n" +
               "        $(this).children().attr(\"src\",\"img/collapse.png\");\n" +
               "      }\n" +
               "    });\n" +
               "    toggleRow($(this).data(\"click\"), true, " + CASCADE_TIME + ", true, false);\n" +
               "  } else {\n" +
               "    $(\".\" + $(this).data(\"click\")).data(\"collapse\", false);\n" +
               "    $(\"#\" + $(this).data(\"click\")).data(\"collapse\", false);\n" +
               "    image.data('status', 'expand');\n" +
               "    image.attr(\"src\",\"img/expand.png\");\n" +
               "    $(\".expand\").each(function() {\n" +
               "      if ($(this).data('click').indexOf(name) > -1) {\n" +
               "        $(this).data(\"expand\", true);\n" +
               "        $(this).children().attr(\"src\",\"img/expand.png\");\n" +
               "      }\n" +
               "    });\n" +
               "    toggleRow($(this).data(\"click\"), false, " + CASCADE_TIME + ", false, false);\n" +
               "  }\n" +
               "});\n" +
               "</script>";
    }

    private String generateLinks() {
        return "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css\">\n"
                +  "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap-theme.min.css\">\n";
//                +  "<link rel=\"stylesheet\" href=\"/css/thermo.css\">\n";
    }

    private String generateEndingTableTags() {
        return "</tbody>\n" +
                "</table>\n";
    }

    private void reset() {
        totals = new ArrayList<TotalTo>();
        clientsNumber = 0;
    }

}
