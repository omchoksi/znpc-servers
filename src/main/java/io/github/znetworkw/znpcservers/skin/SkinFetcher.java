package io.github.znetworkw.znpcservers.skin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SkinFetcher {
    private static final String EMPTY_STRING = "";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final ExecutorService SKIN_EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    private static final JsonParser JSON_PARSER = new JsonParser();
    private final SkinFetcherBuilder builder;

    public SkinFetcher(SkinFetcherBuilder builder) {
        this.builder = builder;
    }

    public CompletableFuture<JsonObject> doReadSkin(SkinFetcherResult skinFetcherResult) {
        CompletableFuture<JsonObject> completableFuture = new CompletableFuture();
        SKIN_EXECUTOR_SERVICE.submit(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection)(new URL(this.builder.getAPIServer().getURL() + this.getData())).openConnection();
                connection.setRequestMethod(this.builder.getAPIServer().getMethod());
                if (this.builder.isUrlType()) {
                    connection.setDoOutput(true);
                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());

                    try {
                        outputStream.writeBytes("url=" + URLEncoder.encode(this.builder.getData(), "UTF-8"));
                    } catch (Throwable var17) {
                        try {
                            outputStream.close();
                        } catch (Throwable var14) {
                            var17.addSuppressed(var14);
                        }

                        throw var17;
                    }

                    outputStream.close();
                }

                try {
                    Reader reader = new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8"));

                    try {
                        completableFuture.complete(JSON_PARSER.parse(reader).getAsJsonObject());
                    } catch (Throwable var15) {
                        try {
                            reader.close();
                        } catch (Throwable var13) {
                            var15.addSuppressed(var13);
                        }

                        throw var15;
                    }

                    reader.close();
                } finally {
                    connection.disconnect();
                }
            } catch (Throwable var18) {
                var18.printStackTrace();
                completableFuture.completeExceptionally(var18);
            }

        });
        completableFuture.whenComplete((response, throwable) -> {
            if (completableFuture.isCompletedExceptionally()) {
                skinFetcherResult.onDone((String[])null, throwable);
            } else {
                JsonObject jsonObject = response.getAsJsonObject(this.builder.getAPIServer().getValueKey());
                JsonObject properties = jsonObject.getAsJsonObject(this.builder.getAPIServer().getSignatureKey());
                skinFetcherResult.onDone(new String[]{properties.get("value").getAsString(), properties.get("signature").getAsString()}, (Throwable)null);
            }

        });
        return completableFuture;
    }

    private String getData() {
        return this.builder.isProfileType() ? "/" + this.builder.getData() : "";
    }
}
