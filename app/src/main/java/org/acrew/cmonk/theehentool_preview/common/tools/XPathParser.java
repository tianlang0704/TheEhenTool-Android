package org.acrew.cmonk.theehentool_preview.common.tools;

import android.support.annotation.NonNull;
import android.util.Log;

import com.rx2androidnetworking.Rx2AndroidNetworking;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by CMonk on 2/6/2017.
 */

public class XPathParser {
    static public class ParseConfig {
        @NonNull String m_XPathString;
        @NonNull String m_DefaultValue;
        public ParseConfig(@NonNull String xPathString, @NonNull String defaultValue) {
            m_XPathString = xPathString;
            m_DefaultValue = defaultValue;
        }
    }

    @NonNull private HashMap<String, ParseConfig> m_ParseConfig = new HashMap<>();

    public void AddParseConfig(@NonNull String key, @NonNull ParseConfig config){
        m_ParseConfig.put(key, config);
    }

    public Observable<ArrayList<HashMap<String, String>>> PromiseToParseListInURL(String urlString, final String listXPath) {
        return XPathParser.PromiseToGetString(urlString)
        .flatMap(s -> PromiseToParse(s, listXPath, m_ParseConfig));
    }

    public static Observable<String> PromiseToGetString(final String urlString) {
        return Observable.defer( () -> {
            CookieJar cookieJar = new CookieJar() {

                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    // here you get the cookies from Response
                    cookieStore.put(url.host(), cookies);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    List<Cookie> cookies = cookieStore.get(url.host());
                    return cookies != null ? cookies : new ArrayList<>();
                }
            };

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .cookieJar(cookieJar)
                    .build();

            return Rx2AndroidNetworking.get(urlString)
                    .setOkHttpClient(client)
                    .build()
                    .getStringObservable();
        });
    }

    public static Observable<ArrayList<HashMap<String, String>>> PromiseToParse(
            @NonNull String html,
            @NonNull String listXPath,
            @NonNull Map<String, ParseConfig> config
    ) {
        return Observable.defer(() -> Observable.just(Parse(html, listXPath, config)));
    }

    public static ArrayList<HashMap<String, String>> Parse(
            @NonNull String html,
            @NonNull String listXPath,
            @NonNull Map<String, ParseConfig> config
    ) {
        HtmlCleaner cleaner = new HtmlCleaner();
        TagNode doc = cleaner.clean(html);
        ArrayList<HashMap<String, String>> results = new ArrayList<>();

        try {
            Object[] nodes = doc.evaluateXPath(listXPath);
            for (Object i: nodes) {
                if (!(i instanceof TagNode)) { continue; }
                TagNode node = (TagNode) i;
                HashMap<String, String> resultEntry = new HashMap<>();

                for (Map.Entry<String, ParseConfig> entry: config.entrySet()) {
                    //Check config information
                    String key = entry.getKey();
                    String itemXPath = entry.getValue().m_XPathString;
                    String defaultValue = entry.getValue().m_DefaultValue;
                    if (key == null) { continue; }

                    //Parse the html
                    String result;
                    Object[] resNodes = node.evaluateXPath(itemXPath);
                    if (resNodes.length == 0) { continue; }
                    if (resNodes[0] instanceof TagNode) {
                        TagNode resNode = (TagNode) resNodes[0];
                        result = resNode.toString();
                    }else if (resNodes[0] instanceof  String) {
                        result = (String) resNodes[0];
                    }else{
                        result = defaultValue;
                    }
                    resultEntry.put(key, result);
                }
                if(resultEntry.size() > 0){
                    results.add(resultEntry);
                }
            }
        } catch (XPatherException e) {
            e.printStackTrace();
        }
        Log.wtf("wtf", results.toString());
        return results;
    }

}
