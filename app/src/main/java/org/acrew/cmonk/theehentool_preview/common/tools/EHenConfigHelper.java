package org.acrew.cmonk.theehentool_preview.common.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by CMonk on 2/6/2017.
 */


public class EHenConfigHelper {
    public SearchConfigHelper searchConfig = new SearchConfigHelper();

    public String m_PreviewListXPath = "body/div[1]/div[2]/div[2]/div";
    public String m_PreviewThumbXPath = "div[2]/a/img/@src";
    public String m_PreviewHrefXPath = "div[1]/a/@href";
    public String m_PreviewTitleXPath = "div[1]/a";
    public String m_PreviewIdXPath = "div[1]/a/@href";
    public String m_PreviewIdRegEx = "^http[s]*:\\/\\/.*\\/g\\/(\\d*)\\/.*\\/$";


    public static final EHenConfigHelper singleton = new EHenConfigHelper();
}
