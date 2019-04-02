//package org.izolotov.crawler;
//
//import com.google.common.base.Optional;
//import com.integralads.crawler.Seed;
//
//import javax.annotation.Nonnull;
//import java.io.Serializable;
//import java.util.Arrays;
//import java.util.Objects;
//
//public class RawResponse implements Serializable {
//    private final FetchStatus status;
//    private final Seed seed;
//    private final byte[] content;
//    private final Optional<String> contentType;
//
//    public RawResponse(@Nonnull Seed seed, @Nonnull FetchStatus status) {
//        this(seed, status, null, null);
//    }
//
//    public RawResponse(@Nonnull Seed seed, @Nonnull FetchStatus status, byte[] content, String contentType) {
//        this.status = status;
//        this.seed = seed;
//        this.content = content;
//        this.contentType = Optional.fromNullable(contentType);
//    }
//
//    public Seed getSeed() {
//        return seed;
//    }
//
//    public byte[] getContent() {
//        return content;
//    }
//
//    public Optional<String> getContentType() {
//        return contentType;
//    }
//
//    public FetchStatus getStatus() {
//        return status;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        RawResponse that = (RawResponse) o;
//        return Objects.equals(status, that.status) &&
//                Objects.equals(seed, that.seed) &&
//                Arrays.equals(content, that.content) &&
//                Objects.equals(contentType, that.contentType);
//    }
//
//    @Override
//    public int hashCode() {
//        return 31 * Objects.hash(status, seed, contentType) + Arrays.hashCode(content);
//    }
//}
