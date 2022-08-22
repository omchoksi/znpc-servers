package io.github.znetworkw.znpcservers.skin;

public class SkinFetcherBuilder {
    private final SkinFetcherBuilder.SkinServer apiServer;
    private final String name;

    protected SkinFetcherBuilder(SkinFetcherBuilder.SkinServer apiServer, String name) {
        this.apiServer = apiServer;
        this.name = name;
    }

    public SkinFetcherBuilder.SkinServer getAPIServer() {
        return this.apiServer;
    }

    public String getData() {
        return this.name;
    }

    public boolean isUrlType() {
        return this.apiServer == SkinFetcherBuilder.SkinServer.GENERATE_API;
    }

    public boolean isProfileType() {
        return this.apiServer == SkinFetcherBuilder.SkinServer.PROFILE_API;
    }

    public static SkinFetcherBuilder create(SkinFetcherBuilder.SkinServer skinAPIURL, String name) {
        return new SkinFetcherBuilder(skinAPIURL, name);
    }

    public static SkinFetcherBuilder withName(String name) {
        return create(name.startsWith("http") ? SkinFetcherBuilder.SkinServer.GENERATE_API : SkinFetcherBuilder.SkinServer.PROFILE_API, name);
    }

    public SkinFetcher toSkinFetcher() {
        return new SkinFetcher(this);
    }

    public static enum SkinServer {
        PROFILE_API("GET", "https://api.ashcon.app/mojang/v2/user", "textures", "raw"),
        GENERATE_API("POST", "https://api.mineskin.org/generate/url", "data", "texture");

        private final String method;
        private final String url;
        private final String valueKey;
        private final String signatureKey;

        private SkinServer(String method, String url, String valueKey, String signatureKey) {
            this.method = method;
            this.url = url;
            this.valueKey = valueKey;
            this.signatureKey = signatureKey;
        }

        public String getMethod() {
            return this.method;
        }

        public String getURL() {
            return this.url;
        }

        public String getValueKey() {
            return this.valueKey;
        }

        public String getSignatureKey() {
            return this.signatureKey;
        }
    }
}
