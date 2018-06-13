package com.codetutor.androidrestwebserviceintegration.restbean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by anildeshpande on 2/21/18.
 */

public class Author implements Serializable {

    @SerializedName("authorId")
    private long authorId;

    @SerializedName("authorName")
    private String authorName;

    @SerializedName("authorEmailId")
    private String authorEmailId;

    @SerializedName("authorPassword")
    private String authorPassword;

    public Author() {
        super();
    }

    public Author(Author author) {
        this(author.authorId, author.authorName, author.authorEmailId, author.authorPassword);
    }

    public Author(long authorId, String authorName, String authorEmailId, String authorPassword) {
        super();
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorEmailId = authorEmailId;
        this.authorPassword = authorPassword;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public String getAuthorEmailId() {
        return authorEmailId;
    }
    public void setAuthorEmailId(String authorEmailId) {
        this.authorEmailId = authorEmailId;
    }

    public void setAuthorPassword(String authorPassword) {
        this.authorPassword = authorPassword;
    }

    public String getAuthorPassword() {
        return authorPassword;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(obj instanceof Author) {
            Author author=(Author) obj;
            if(this.authorEmailId.equals(author.authorEmailId) && this.authorPassword.equals(author.authorPassword) &&
                    this.authorId==author.authorId && this.authorName.equals(author.authorName)) {
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "("+this.authorEmailId+", "+this.authorPassword+", "+this.authorId+", "+this.authorName+")";
    }
}
