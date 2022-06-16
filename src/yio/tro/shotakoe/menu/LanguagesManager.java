package yio.tro.shotakoe.menu;

import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.*;

import org.w3c.dom.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class LanguagesManager {

    private static LanguagesManager instance = null;
    private static final String LANGUAGES_FILE = "languages.xml";
    private static final String DEFAULT_LANGUAGE = "en_UK";
    private HashMap<String, String> languageMap = null;
    private String languageName = null;


    private LanguagesManager() {
        languageMap = new HashMap<>();
        languageName = java.util.Locale.getDefault().toString();
        if (!loadLanguage(languageName)) {
            loadLanguage(DEFAULT_LANGUAGE);
            languageName = DEFAULT_LANGUAGE;
        }
    }


    public static void initialize() {
        instance = null;
    }


    public static LanguagesManager getInstance() {
        if (instance == null) {
            instance = new LanguagesManager();
        }
        return instance;
    }


    public void setLanguage(String langName) {
        loadLanguage(langName);
        languageName = langName;
    }


    public String getString(String key) {
        if (languageMap == null) return key;
        String string = languageMap.get(key);
        if (string != null) return string;
        return key;
    }


    public String getString(String key, Object... args) {
        return String.format(getString(key), args);
    }


    public ArrayList<LanguageChooseItem> getChooseListItems() {
        ArrayList<LanguageChooseItem> result = new ArrayList<>();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            FileHandle fileHandle = Gdx.files.internal(LANGUAGES_FILE);
            Document doc = db.parse(fileHandle.read());

            Element root = doc.getDocumentElement();

            NodeList languages = root.getElementsByTagName("language");
            int numLanguages = languages.getLength();

            for (int i = 0; i < numLanguages; i++) {
                Node language = languages.item(i);

                LanguageChooseItem chooseItem = new LanguageChooseItem();
                chooseItem.name = language.getAttributes().getNamedItem("name").getTextContent();
                chooseItem.title = language.getAttributes().getNamedItem("title").getTextContent();
                chooseItem.author = language.getAttributes().getNamedItem("author").getTextContent();

                result.add(chooseItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public boolean loadLanguage(String lName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            FileHandle fileHandle = Gdx.files.internal(LANGUAGES_FILE);
            Document doc = db.parse(fileHandle.read());

            Element root = doc.getDocumentElement();

            NodeList languages = root.getElementsByTagName("language");
            int numLanguages = languages.getLength();

            for (int i = 0; i < numLanguages; ++i) {
                Node languageNode = languages.item(i);

                if (!isTargetLanguageNode(lName, languageNode)) continue;
                languageMap.clear();
                Element languageElement = (Element) languageNode;
                NodeList strings = languageElement.getElementsByTagName("string");
                int numStrings = strings.getLength();

                for (int j = 0; j < numStrings; ++j) {
                    NamedNodeMap attributes = strings.item(j).getAttributes();
                    String key = attributes.getNamedItem("key").getTextContent();
                    String value = attributes.getNamedItem("value").getTextContent();
//                    System.out.println(value);
                    value = value.replace("<br />", "\n");
                    languageMap.put(key, value);
                }

                return true;
            }
        } catch (Exception e) {
            System.out.println("Error loading languages file " + LANGUAGES_FILE);
            return false;
        }

        return false;
    }


    private boolean isTargetLanguageNode(String langName, Node node) {
        String name = node.getAttributes().getNamedItem("name").getTextContent();
        Node secondNameNode = node.getAttributes().getNamedItem("second_name");
        String secondName = "-";
        if (secondNameNode != null) {
            secondName = secondNameNode.getTextContent();
        }
        return langName.equals(name) || langName.equals(secondName);
    }
}
