package com.czbix.v2ex.model;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.czbix.v2ex.common.exception.FatalException;
import com.czbix.v2ex.network.RequestHelper;
import com.czbix.v2ex.ui.widget.ExArrayAdapter;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.text.Collator;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Node extends Page implements Comparable<Node>, ExArrayAdapter.Filterable, Favable {
    private static final Pattern PATTERN = Pattern.compile("/go/(.+?)(?:\\W|$)");
    private static final Collator COLLATOR = Collator.getInstance(Locale.CHINA);

    private final int mId;
    private final String mTitle;
    private final String mName;
    private final String mTitleAlternative;
    private final int mTopics;
    private final Avatar mAvatar;
    private final boolean mHasInfo;

    public Node(String title, int id, Avatar avatar, String name, String alternative,
                int topics) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(name));
        mId = id;

        mTitle = title;
        mAvatar = avatar;
        mName = name;
        mTitleAlternative = alternative;
        mTopics = topics;

        mHasInfo = !Strings.isNullOrEmpty(title);
    }

    public int getId() {
        return mId;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Nullable
    public Avatar getAvatar() {
        return mAvatar;
    }

    public String getName() {
        return mName;
    }

    public String getTitleAlternative() {
        return mTitleAlternative;
    }

    public int getTopics() {
        return mTopics;
    }

    public boolean hasInfo() {
        return mHasInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return Objects.equal(mId, node.mId) &&
                Objects.equal(mTopics, node.mTopics) &&
                Objects.equal(mName, node.mName) &&
                Objects.equal(mTitleAlternative, node.mTitleAlternative) &&
                Objects.equal(mAvatar, node.mAvatar);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mId, mName, mTitleAlternative, mTopics, mAvatar);
    }

    public static String buildUrlByName(String name) {
        return RequestHelper.BASE_URL + "/go/" + name;
    }

    public static String getNameFromUrl(String url) {
        final Matcher matcher = PATTERN.matcher(url);
        Preconditions.checkState(matcher.find(), "match name for node failed: " + url);
        return matcher.group(1);
    }

    @Override
    public String getUrl() {
        return buildUrlByName(getName());
    }

    @Override
    public boolean filter(String query) {
        if (Strings.isNullOrEmpty(query)) {
            return true;
        }
        if (mName.contains(query) || mTitle.contains(query)) {
            return true;
        }
        if (mTitleAlternative != null && mTitleAlternative.contains(query)) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(@NonNull Node another) {
        return COLLATOR.compare(getTitle(), another.getTitle());
    }

    @Override
    public String getFavUrl(String token) {
        return String.format("%s/favorite/node/%d?once=%s", RequestHelper.BASE_URL, mId, token);
    }

    @Override
    public String getUnFavUrl(String token) {
        return String.format("%s/unfavorite/node/%d?once=%s", RequestHelper.BASE_URL, mId, token);
    }

    public static class Builder {
        private static final Cache<String, Node> CACHE;

        static {
            CACHE = CacheBuilder.newBuilder()
                    .softValues()
                    .initialCapacity(32)
                    .maximumSize(128)
                    .build();
        }

        private int mId;
        private String mTitle;
        private Avatar mAvatar;
        private String mName;
        private String mTitleAlternative;
        private int mTopics;

        public Builder setId(int id) {
            mId = id;
            return this;
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setAvatar(Avatar avatar) {
            mAvatar = avatar;
            return this;
        }

        public Builder setName(String name) {
            mName = name;
            return this;
        }

        public Builder setTitleAlternative(String titleAlternative) {
            mTitleAlternative = titleAlternative;
            return this;
        }

        public Builder setTopics(int topics) {
            mTopics = topics;
            return this;
        }

        public Node createNode() {
            try {
                if (mTitle == null) {
                    return new Node(null, mId, mAvatar, mName, mTitleAlternative, mTopics);
                }
                return CACHE.get(mName, () -> new Node(mTitle, mId, mAvatar, mName, mTitleAlternative, mTopics));
            } catch (ExecutionException e) {
                throw new FatalException(e);
            }
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mTitle);
        dest.writeString(this.mName);
        dest.writeString(this.mTitleAlternative);
        dest.writeInt(this.mTopics);
        dest.writeParcelable(this.mAvatar, 0);
        dest.writeByte(mHasInfo ? (byte) 1 : (byte) 0);
    }

    protected Node(Parcel in) {
        this.mId = in.readInt();
        this.mTitle = in.readString();
        this.mName = in.readString();
        this.mTitleAlternative = in.readString();
        this.mTopics = in.readInt();
        this.mAvatar = in.readParcelable(Avatar.class.getClassLoader());
        this.mHasInfo = in.readByte() != 0;
    }

    public static final Creator<Node> CREATOR = new Creator<Node>() {
        public Node createFromParcel(Parcel source) {
            return new Node(source);
        }

        public Node[] newArray(int size) {
            return new Node[size];
        }
    };
}
