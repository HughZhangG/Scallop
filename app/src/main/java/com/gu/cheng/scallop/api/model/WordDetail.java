package com.gu.cheng.scallop.api.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gc on 2017/9/9.
 * 单词实体类
 *
 * en_definitions   array     返回英文释义的数组
 en_definition    object    英文释义
 cn_definition    object    中文释义
 id               int       单词的id
 retention        int       单词的熟悉度
 definition       string    中文释义
 target_retention int       用户自定义的目标学习度
 learning_id      long      learing id，如果未返回，在表明用户没学过该单词
 content          string    查询的单词
 pronunciation    string    单词的音标
 */
public class WordDetail implements Serializable {
    private EnDefinitionsBean en_definitions;

    private CnDefinitionBean cn_definition;
    private int id;
    private int retention;
    private String definition;
    private int target_retention;

    private EnDefinitionBean en_definition;
    private long learning_id;
    private String content;
    private String pronunciation;
    private String audio;
    private String uk_audio;
    private String us_audio;

    public EnDefinitionsBean getEn_definitions() {
        return en_definitions;
    }

    public void setEn_definitions(EnDefinitionsBean en_definitions) {
        this.en_definitions = en_definitions;
    }

    public CnDefinitionBean getCn_definition() {
        return cn_definition;
    }

    public void setCn_definition(CnDefinitionBean cn_definition) {
        this.cn_definition = cn_definition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRetention() {
        return retention;
    }

    public void setRetention(int retention) {
        this.retention = retention;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public int getTarget_retention() {
        return target_retention;
    }

    public void setTarget_retention(int target_retention) {
        this.target_retention = target_retention;
    }

    public EnDefinitionBean getEn_definition() {
        return en_definition;
    }

    public void setEn_definition(EnDefinitionBean en_definition) {
        this.en_definition = en_definition;
    }

    public long getLearning_id() {
        return learning_id;
    }

    public void setLearning_id(long learning_id) {
        this.learning_id = learning_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getUk_audio() {
        return uk_audio;
    }

    public void setUk_audio(String uk_audio) {
        this.uk_audio = uk_audio;
    }

    public String getUs_audio() {
        return us_audio;
    }

    public void setUs_audio(String us_audio) {
        this.us_audio = us_audio;
    }

    public static class EnDefinitionsBean implements Serializable{
        private List<String> n;

        public List<String> getN() {
            return n;
        }

        public void setN(List<String> n) {
            this.n = n;
        }
    }

    public static class CnDefinitionBean implements Serializable{
        private String pos;
        private String defn;

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public String getDefn() {
            return defn;
        }

        public void setDefn(String defn) {
            this.defn = defn;
        }
    }

    public static class EnDefinitionBean implements Serializable{
        private String pos;
        private String defn;

        public String getPos() {
            return pos;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public String getDefn() {
            return defn;
        }

        public void setDefn(String defn) {
            this.defn = defn;
        }
    }
}
