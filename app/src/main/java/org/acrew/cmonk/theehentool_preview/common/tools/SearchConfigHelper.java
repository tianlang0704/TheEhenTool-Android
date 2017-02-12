package org.acrew.cmonk.theehentool_preview.common.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by CMonk on 2/11/2017.
 */

public class SearchConfigHelper {
    public enum Options {
        DOUJINSHI, MANGA, ARTISTCG,GAMECG,WESTERN,NONH,IMAGESET,COSPLAY,ASIANPORN,MISC
    }
    static final TreeMap<Options, String> m_OptToQueryString;
    static {
        m_OptToQueryString = new TreeMap<>();
        m_OptToQueryString.put(Options.DOUJINSHI, "f_doujinshi");
        m_OptToQueryString.put(Options.MANGA, "f_manga");
        m_OptToQueryString.put(Options.ARTISTCG, "f_artistcg");
        m_OptToQueryString.put(Options.GAMECG, "f_gamecg");
        m_OptToQueryString.put(Options.WESTERN, "f_western");
        m_OptToQueryString.put(Options.NONH, "f_non-h");
        m_OptToQueryString.put(Options.IMAGESET, "f_imageset");
        m_OptToQueryString.put(Options.COSPLAY, "f_cosplay");
        m_OptToQueryString.put(Options.ASIANPORN, "f_asianporn");
        m_OptToQueryString.put(Options.MISC, "f_misc");
    }

    TreeMap<Options, Boolean> m_OptConfig = new TreeMap<>();
    String m_SearchBaseURL = "https://e-hentai.org/?{filterOptions}&f_search={searchString}&f_apply=Apply+Filter&inline_set=dm_t";

    public void SetConfiguration(Options opt, Boolean setting) {
        m_OptConfig.put(opt, setting);
    }

    public void SetConfiguration(TreeMap<Options, Boolean> options) {
        m_OptConfig.putAll(options);
    }

    public String GetSearchURL(String searchWords) {
        //build option string
        String filterOptionsString = "";
        for (Map.Entry<Options, String> entry: m_OptToQueryString.entrySet()) {
            Options key = entry.getKey();
            String optionValueString = m_OptConfig.containsKey(key) ? (m_OptConfig.get(key) ? "1" : "0") : "0";
            filterOptionsString += entry.getValue() + "=" + optionValueString;
            if (!m_OptToQueryString.isEmpty() && !m_OptToQueryString.lastEntry().equals(entry)) {
                filterOptionsString += "&";
            }
        }

        //build search string
        String escapedSearchWords = "";
        try {
            escapedSearchWords = URLEncoder.encode(searchWords, "ASCII");
        } catch (UnsupportedEncodingException e) {
            //TODO: add desired error action/recovery
            e.printStackTrace();
        }

        return m_SearchBaseURL.replaceFirst("\\{filterOptions\\}", filterOptionsString).replaceFirst("\\{searchString\\}", escapedSearchWords);
    }

    public static final SearchConfigHelper singleton = new SearchConfigHelper();
}
