package cat.hack3.mangrana.utils;

public interface APIInterface {
    enum ProtocolURLMark {
        HTTPS("https://");
        private final String mark;
        ProtocolURLMark(String mark) {
            this.mark = mark;
        }
        public String getMark() {
            return mark;
        }
    }
}
