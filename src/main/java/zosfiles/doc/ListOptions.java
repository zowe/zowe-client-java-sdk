package zosfiles.doc;

import zosjobs.input.GetJobParms;

import java.util.Optional;

public class ListOptions extends ZosFilesOptions{
    private Optional<String> volume;
    private Optional<String> attributes;
    private Optional<String> maxLength;
    private Optional<String> start;
    private Optional<String> recall;
    private Optional<String> pattern;

    public ListOptions(ListOptions.Builder builder) {
        if (builder.volume != null)
            this.volume = Optional.ofNullable(builder.volume);
        else this.volume = Optional.empty();

        if (builder.attributes != null)
            this.attributes = Optional.ofNullable(builder.attributes);
        else this.attributes = Optional.empty();

        if (builder.maxLength != null)
            this.maxLength = Optional.ofNullable(builder.maxLength);
        else this.maxLength = Optional.empty();

        if (builder.start != null)
            this.start = Optional.ofNullable(builder.start);
        else this.start = Optional.empty();

        if (builder.recall != null)
            this.recall = Optional.ofNullable(builder.recall);
        else this.recall = Optional.empty();

        if (builder.pattern != null)
            this.pattern = Optional.ofNullable(builder.pattern);
        else this.pattern = Optional.empty();

    }

    public Optional<String> getVolume() {
        return volume;
    }

    public Optional<String> getAttributes() {
        return attributes;
    }

    public Optional<String> getMaxLength() {
        return maxLength;
    }



    public Optional<String> getStart() {
        return start;
    }

    public Optional<String> getRecall() {
        return recall;
    }

    public Optional<String> getPattern() {
        return pattern;
    }


    @Override
    public String toString() {
        return "ListOptions{" +
                "volume=" + volume +
                ", attributes=" + attributes +
                ", maxLength=" + maxLength +
                ", start=" + start +
                ", recall=" + recall +
                ", pattern=" + pattern +
                '}';
    }

    public static class Builder {

        private String volume;
        private String attributes;
        private String maxLength;
        private String start;
        private String recall;
        private String pattern;

        public ListOptions.Builder volume(String volume) {
            this.volume = volume;
            return this;
        }
        public ListOptions.Builder attributes(String attributes) {
            this.attributes = attributes;
            return this;
        }
        public ListOptions.Builder maxLength(String maxLength) {
            this.maxLength = maxLength;
            return this;
        }
        public ListOptions.Builder start(String start) {
            this.start = start;
            return this;
        }
        public ListOptions.Builder recall(String recall) {
            this.recall = recall;
            return this;
        }
        public ListOptions.Builder pattern(String pattern) {
            this.pattern = pattern;
            return this;
        }
        public ListOptions build() {
            return new ListOptions(this);
        }
    }
}
