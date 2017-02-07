package org.acrew.cmonk.theehentool_preview.common.tools;

/**
 * Created by CMonk on 2/6/2017.
 */

public class EHenConfigHelper {
    public String previewListXPath = "body/div[1]/div[2]/div[2]/div";
    public String previewThumbXPath = "div[2]/a/img/@src";
    public String previewHrefXPath = "div[1]/a/@href";
    public String previewTitleXPath = "div[1]/a";
    public String previewIdXPath = "div[1]/a/@href";
    public String previewIdRegEx = "^http[s]*:\\/\\/.*\\/g\\/(\\d*)\\/.*\\/$";


    public static final EHenConfigHelper singleton = new EHenConfigHelper();
}
